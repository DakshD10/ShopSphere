#!/usr/bin/env bash
# run-rmi.sh: Compile and test the ShopSphere Java RMI Module
set -e
export JAVA_HOME="/opt/homebrew/opt/openjdk"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MYSQL_JAR="$HOME/.m2/repository/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar"
cd "$DIR/rmi"

CP="bin"
if [ -f "$MYSQL_JAR" ]; then
    CP="$CP:$MYSQL_JAR"
fi

echo "[1/3] Compiling RMI Remote Interfaces, Server, and Client..."
mkdir -p bin
javac -cp "$CP" -d bin src/remote/ProductDTO.java src/remote/InventoryService.java src/server/InventoryServiceImpl.java src/server/RmiServer.java src/client/RmiClient.java

echo "[2/3] Starting RmiServer in background..."
java -cp "$CP" server.RmiServer &
SERVER_PID=$!

sleep 2

echo "[3/3] Running RmiClient invocation..."
java -cp bin client.RmiClient localhost 1099

echo "[*] Terminating background RmiServer (PID: $SERVER_PID)..."
kill $SERVER_PID 2>/dev/null || true
echo "RMI Module demonstration completed successfully!"
