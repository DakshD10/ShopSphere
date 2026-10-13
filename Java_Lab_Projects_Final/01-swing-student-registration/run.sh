#!/usr/bin/env bash
# run.sh: Compile and launch Experiment 1 (Swing Student Registration Form)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:$PATH"

cd "$SCRIPT_DIR"
mkdir -p out
echo "[1/2] Compiling StudentRegistration..."
javac -d out src/StudentRegistration.java

echo "[2/2] Launching Student Registration UI..."
java -cp out StudentRegistration "$@"
