#!/usr/bin/env bash
# run-network.sh: Compile and test the ShopSphere Java TCP Network Socket Module
set -e
export JAVA_HOME="/opt/homebrew/opt/openjdk"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MYSQL_JAR="$HOME/.m2/repository/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar"
cd "$DIR/network"

CP="bin"
if [ -f "$MYSQL_JAR" ]; then
    CP="$CP:$MYSQL_JAR"
fi

echo "[1/3] Compiling Network Module..."
mkdir -p bin
javac -cp "$CP" -d bin src/model/NetworkProduct.java src/server/InventoryServer.java src/client/InventoryClient.java

echo "[2/3] Starting InventoryServer in background (Port 6060)..."
java -cp "$CP" server.InventoryServer 6060 &
SERVER_PID=$!

sleep 1

echo "[3/3] Running InventoryClient Automated Demo..."
java -cp bin client.InventoryClient localhost 6060 --demo

echo "[*] Shutting down background InventoryServer (PID: $SERVER_PID)..."
kill $SERVER_PID 2>/dev/null || true
echo "Network module test completed successfully!"
