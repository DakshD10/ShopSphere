#!/usr/bin/env bash
# run-server.sh: Compile and launch TCP Server (Port 5000)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

cd "$SCRIPT_DIR"
mkdir -p out
javac -d out src/Server.java src/Client.java
echo "Starting TCP Server on Port 5000..."
java -cp out Server
