#!/bin/bash
# Helper script to stop Apache Tomcat
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

echo "Stopping Apache Tomcat 11.0.25..."
"$SCRIPT_DIR/apache-tomcat-11.0.25/bin/shutdown.sh"
