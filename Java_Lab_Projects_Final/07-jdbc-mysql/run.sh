#!/usr/bin/env bash
# run.sh: Build and run Experiment 7 (JDBC with MySQL)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk}"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

MYSQL_JAR="$SCRIPT_DIR/lib/mysql-connector-j.jar"
if [ ! -f "$MYSQL_JAR" ]; then
    MYSQL_JAR="$HOME/.m2/repository/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar"
fi

cd "$SCRIPT_DIR"
mkdir -p out
echo "[1/2] Compiling JDBC Experiment..."
javac -cp "out:$MYSQL_JAR" -d out src/*.java

echo "[2/2] Running JdbcDemo (Professor's Step-by-Step Lab Demo)..."
java -cp "out:$MYSQL_JAR" JdbcDemo "$@"
