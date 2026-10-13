#!/usr/bin/env bash
# run-client.sh: Launch TCP Client
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

cd "$SCRIPT_DIR"
if [ ! -d "out" ] || [ ! -f "out/Client.class" ]; then
    mkdir -p out
    javac -d out src/Server.java src/Client.java
fi

echo "Connecting to TCP Server on Port 5000..."
java -cp out Client
