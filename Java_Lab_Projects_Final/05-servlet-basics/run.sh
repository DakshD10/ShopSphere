#!/bin/bash
# One-step build, deploy, and launch script for Experiment 5 (Servlet Basics)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TOMCAT_DIR="$SCRIPT_DIR/../03-tomcat-basic-webapp/apache-tomcat-11.0.25"

export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

echo "=========================================================="
echo " Experiment 5: Java Servlet Basics (Tomcat 11)"
echo "=========================================================="

echo "[1/3] Packaging WAR with Maven..."
cd "$SCRIPT_DIR"
mvn clean package -q

echo "[2/3] Deploying WAR to Tomcat 11 webapps..."
cp "$SCRIPT_DIR/target/servlet-basics.war" "$TOMCAT_DIR/webapps/servlet-basics.war"
cp "$SCRIPT_DIR/target/servlet-basics.war" "$TOMCAT_DIR/webapps/servlet.war"

echo "[3/3] Checking Tomcat status..."
if ! lsof -i :8080 >/dev/null 2>&1; then
    echo "Starting Apache Tomcat 11..."
    "$TOMCAT_DIR/bin/startup.sh"
    sleep 2
else
    echo "Tomcat 11 is already running on port 8080."
fi

echo ""
echo "=========================================================="
echo " Experiment 5 is running live!"
echo " Portal URL:  http://localhost:8080/servlet-basics/"
echo " Alternative: http://localhost:8080/servlet/"
echo "=========================================================="
