#!/bin/bash
# Automated end-to-end test script for Experiment 6 (HTML to JSP Conversion)
set -e

BASE_URL="http://localhost:8080/html-to-jsp"
PASSED=0
FAILED=0

assert_contains() {
    local test_name="$1"
    local expected="$2"
    local actual="$3"
    if echo "$actual" | grep -Fq "$expected"; then
        echo "  [PASS] $test_name"
        PASSED=$((PASSED + 1))
    else
        echo "  [FAIL] $test_name (Expected to find '$expected')"
        FAILED=$((FAILED + 1))
    fi
}

assert_status() {
    local test_name="$1"
    local expected_code="$2"
    local actual_code="$3"
    if [ "$actual_code" = "$expected_code" ]; then
        echo "  [PASS] $test_name (HTTP $actual_code)"
        PASSED=$((PASSED + 1))
    else
        echo "  [FAIL] $test_name (Expected HTTP $expected_code, got $actual_code)"
        FAILED=$((FAILED + 1))
    fi
}

echo "=========================================================="
echo " Running End-to-End Tests for 06-html-to-jsp"
echo " Target: $BASE_URL"
echo "=========================================================="
echo ""

# 1. Root Context Access (index.jsp forwarding to student.jsp)
echo "Test 1: Webapp Root Index (/)"
CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/")
RESP=$(curl -s "$BASE_URL/")
assert_status "Root URL status" "200" "$CODE"
assert_contains "Root renders Student Information" "Student Information" "$RESP"

# 2. Direct student.jsp Access
echo ""
echo "Test 2: Student Form (student.jsp)"
CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/student.jsp")
RESP=$(curl -s "$BASE_URL/student.jsp")
assert_status "student.jsp status" "200" "$CODE"
assert_contains "Form action points to hello" 'action="hello"' "$RESP"
assert_contains "Method is post" 'method="post"' "$RESP"
assert_contains "Name input present" 'name="name"' "$RESP"
assert_contains "Email input present" 'name="email"' "$RESP"
assert_contains "Course input present" 'name="course"' "$RESP"
assert_contains "Age input present" 'name="age"' "$RESP"
assert_contains "Register button present" 'value="Register"' "$RESP"

# 3. Form Submission POST to HelloServlet (/hello)
echo ""
echo "Test 3: POST /hello -> HelloServlet -> RequestDispatcher.forward -> result.jsp"
RESP=$(curl -s -X POST \
  -d "name=Daksh Duhlani" \
  -d "email=daksh@example.com" \
  -d "course=Computer Science" \
  -d "age=21" \
  "$BASE_URL/hello")

assert_contains "Registration header displayed" "Registration Successful" "$RESP"
assert_contains "Student Details section" "Student Details" "$RESP"
assert_contains "EL \${name} evaluated" "Daksh Duhlani" "$RESP"
assert_contains "EL \${email} evaluated" "daksh@example.com" "$RESP"
assert_contains "EL \${course} evaluated" "Computer Science" "$RESP"
assert_contains "EL \${age} evaluated" "21" "$RESP"

# 4. Form Submission with UTF-8 Special Characters
echo ""
echo "Test 4: POST /hello with UTF-8 encoded characters"
RESP=$(curl -s -X POST \
  --data-urlencode "name=René Descartes" \
  --data-urlencode "email=rene@philosophy.edu" \
  --data-urlencode "course=Mathematics & Logic" \
  --data-urlencode "age=35" \
  "$BASE_URL/hello")

assert_contains "UTF-8 name rendered" "René Descartes" "$RESP"
assert_contains "Course with ampersand rendered" "Mathematics & Logic" "$RESP"

# 5. GET /hello (Direct access redirect check)
echo ""
echo "Test 5: GET /hello redirects back to student.jsp"
CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/hello")
LOC=$(curl -s -I "$BASE_URL/hello" | grep -i "Location:" | tr -d '\r')
assert_status "GET /hello redirect status" "302" "$CODE"
assert_contains "Redirects to student.jsp" "student.jsp" "$LOC"

echo ""
echo "=========================================================="
echo " Test Results: $PASSED Passed, $FAILED Failed"
echo "=========================================================="

if [ "$FAILED" -eq 0 ]; then
    exit 0
else
    exit 1
fi
