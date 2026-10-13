#!/bin/bash
# One-step build, deploy, and launch script for Experiment 6 (HTML to JSP Conversion)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TOMCAT_DIR="$SCRIPT_DIR/../03-tomcat-basic-webapp/apache-tomcat-11.0.25"

export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

echo "=========================================================="
echo " Experiment 6: HTML to JSP Conversion (Tomcat 11)"
echo " Exact Reference: HTML_to_JSP_Conversion.pdf"
echo "=========================================================="

echo "[1/3] Packaging WAR with Maven..."
cd "$SCRIPT_DIR"
mvn clean package -q

echo "[2/3] Deploying WAR to Tomcat 11 webapps..."
cp "$SCRIPT_DIR/target/html-to-jsp.war" "$TOMCAT_DIR/webapps/html-to-jsp.war"

echo "[3/3] Checking Tomcat status..."
if ! lsof -i :8080 >/dev/null 2>&1; then
    echo "Starting Apache Tomcat 11..."
    "$TOMCAT_DIR/bin/startup.sh"
    sleep 3
else
    echo "Tomcat 11 is already running on port 8080."
fi

echo ""
echo "=========================================================="
echo " Experiment 6 is running live!"
echo " Student Form: http://localhost:8080/html-to-jsp/student.jsp"
echo " Portal URL:   http://localhost:8080/html-to-jsp/"
echo " Controller:   http://localhost:8080/html-to-jsp/hello"
echo "=========================================================="
