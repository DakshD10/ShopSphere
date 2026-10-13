#!/usr/bin/env bash
# run-swing-admin.sh: Compile and launch the ShopSphere Swing Desktop Admin App
set -e
export JAVA_HOME="/opt/homebrew/opt/openjdk"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MYSQL_JAR="$HOME/.m2/repository/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar"

cd "$DIR/swing-admin"

echo "[1/2] Compiling Swing Admin Module..."
mkdir -p bin
CP="bin"
if [ -f "$MYSQL_JAR" ]; then
    CP="$CP:$MYSQL_JAR"
fi

javac -cp "$CP" -d bin $(find src -name "*.java")

echo "[2/2] Launching ShopSphere Desktop Admin Application..."
java -cp "$CP" Main "$@"
