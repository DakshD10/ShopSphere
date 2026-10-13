#!/usr/bin/env bash
# run.sh: Compile and launch Experiment 2 (Swing MVC Calculator)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

cd "$SCRIPT_DIR"
mkdir -p out
echo "[1/2] Compiling Swing MVC Calculator..."
javac -d out src/*.java

echo "[2/2] Launching Calculator GUI..."
java -cp out Main "$@"
