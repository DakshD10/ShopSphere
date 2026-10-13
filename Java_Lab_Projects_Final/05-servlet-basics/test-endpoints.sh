#!/bin/bash
# End-to-end automated test script for Experiment 5 (Servlet Basics)
# Tests all endpoints and assertions against the running Tomcat 11 instance.

BASE_URL="http://localhost:8080/servlet-basics"
COOKIE_JAR="/tmp/servlet_test_cookies.txt"
rm -f "$COOKIE_JAR"

PASSED=0
FAILED=0

assert_contains() {
    local test_name="$1"
    local expected="$2"
    local actual="$3"
    if echo "$actual" | grep -Fq "$expected"; then
        echo "  [PASS] $test_name"
        ((PASSED++))
    else
        echo "  [FAIL] $test_name (Expected to find '$expected')"
        ((FAILED++))
    fi
}

assert_status() {
    local test_name="$1"
    local expected_code="$2"
    local actual_code="$3"
    if [ "$actual_code" = "$expected_code" ]; then
        echo "  [PASS] $test_name (HTTP $actual_code)"
        ((PASSED++))
    else
        echo "  [FAIL] $test_name (Expected HTTP $expected_code, got $actual_code)"
        ((FAILED++))
    fi
}

echo "=========================================================="
echo " Running End-to-End Tests for 05-servlet-basics"
echo " Target: $BASE_URL"
echo "=========================================================="
echo ""

# 1. Portal Home
echo "Test 1: Lab Portal (index.html)"
CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/")
RESP=$(curl -s "$BASE_URL/")
assert_status "Portal response code" "200" "$CODE"
assert_contains "Portal content check" "Java Servlet Basics" "$RESP"

# 2. HelloServlet doGet
echo ""
echo "Test 2: HelloServlet doGet (/hello)"
RESP=$(curl -s "$BASE_URL/hello")
assert_contains "HelloServlet heading" "Hello Students!" "$RESP"
assert_contains "HelloServlet welcome" "Welcome to Servlet Programming" "$RESP"

# 3. HelloServlet doGet with parameter
echo ""
echo "Test 3: HelloServlet doGet with query parameter (/hello?name=Daksh)"
RESP=$(curl -s "$BASE_URL/hello?name=Daksh")
assert_contains "HelloServlet parameterized greeting" "Welcome Daksh!" "$RESP"

# 4. HelloServlet doPost
echo ""
echo "Test 4: HelloServlet doPost (/hello)"
RESP=$(curl -s -X POST -d "name=Daksh" "$BASE_URL/hello")
assert_contains "HelloServlet POST greeting" "Welcome Daksh" "$RESP"
assert_contains "HelloServlet POST note" "Processed through <code>doPost()</code>" "$RESP"

# 5. StudentServlet POST (name, email, course)
echo ""
echo "Test 5: Student Form submission POST (/student)"
RESP=$(curl -s -X POST -d "name=Daksh Duhlani&email=daksh@example.com&course=Computer Science" "$BASE_URL/student")
assert_contains "Student name displayed" "Daksh Duhlani" "$RESP"
assert_contains "Student email displayed" "daksh@example.com" "$RESP"
assert_contains "Student course displayed" "Computer Science" "$RESP"
assert_contains "Session tracking message" "HttpSession Tracking" "$RESP"

# 6. AuthFilter blocking unauthenticated access
echo ""
echo "Test 6: AuthFilter unauthorized check (/home)"
CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/home")
LOC=$(curl -s -I "$BASE_URL/home" | grep -i "Location:" | tr -d '\r')
assert_status "AuthFilter blocks with 302" "302" "$CODE"
assert_contains "Redirects to login.html" "login.html" "$LOC"

# 7. LoginServlet (credential check & session creation)
echo ""
echo "Test 7: LoginServlet (/login)"
RESP=$(curl -s -c "$COOKIE_JAR" -X POST -d "username=admin&password=1234" "$BASE_URL/login")
assert_contains "Login successful" "Login Successful" "$RESP"
assert_contains "Session attribute stored" "session.setAttribute" "$RESP"

# 8. AuthFilter passing authenticated session to HomeServlet
echo ""
echo "Test 8: Protected HomeServlet access (/home) with active session"
RESP=$(curl -s -b "$COOKIE_JAR" "$BASE_URL/home")
assert_contains "HomeServlet accessed" "Student Home" "$RESP"
assert_contains "Welcome message for admin" "Welcome admin" "$RESP"

# 9. LogoutServlet (session.invalidate)
echo ""
echo "Test 9: LogoutServlet (/logout)"
RESP=$(curl -s -b "$COOKIE_JAR" "$BASE_URL/logout")
assert_contains "Logout executed" "Logged Out Successfully" "$RESP"
assert_contains "session.invalidate confirmed" "session.invalidate()" "$RESP"

# 10. Post-logout protection check
echo ""
echo "Test 10: AuthFilter re-blocking /home after logout"
CODE=$(curl -s -b "$COOKIE_JAR" -o /dev/null -w "%{http_code}" "$BASE_URL/home")
assert_status "AuthFilter blocks post-logout with 302" "302" "$CODE"

# 11. LifeCycleServlet
echo ""
echo "Test 11: Servlet Life Cycle demo (/lifecycle)"
RESP=$(curl -s "$BASE_URL/lifecycle")
assert_contains "Lifecycle init timestamp" "init() invocation timestamp" "$RESP"
assert_contains "Lifecycle service counter" "service() execution count" "$RESP"

# 12. ConfigContextServlet
echo ""
echo "Test 12: ServletConfig vs ServletContext demo (/config-context)"
RESP=$(curl -s "$BASE_URL/config-context")
assert_contains "ConfigContext heading" "ServletConfig vs ServletContext" "$RESP"
assert_contains "ServletConfig param check" "servletSpecificEmail" "$RESP"
assert_contains "ServletContext param check" "appTitle" "$RESP"

# 13. CookieServlet
echo ""
echo "Test 13: Cookies demo (/cookies)"
RESP=$(curl -s "$BASE_URL/cookies?action=set&val=Jahanvi")
assert_contains "Cookie created message" "Cookie created and added to response" "$RESP"
assert_contains "Cookie vs Session comparison" "Cookie vs HttpSession" "$RESP"

echo ""
echo "=========================================================="
echo " Test Results: $PASSED Passed, $FAILED Failed"
echo "=========================================================="

rm -f "$COOKIE_JAR"
if [ "$FAILED" -eq 0 ]; then
    exit 0
else
    exit 1
fi
