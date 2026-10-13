#!/usr/bin/env bash
# sync-and-restart.sh: Sync db.properties, rebuild WAR, deploy, and restart Tomcat
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TOMCAT_DIR="/Users/dakshduhlani/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp/apache-tomcat-11.0.25"
export JAVA_HOME="/opt/homebrew/opt/openjdk"
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

echo "=================================================="
echo "  ShopSphere - Sync DB Config & Restart Tomcat"
echo "=================================================="

# 1. Sync db.properties from root to src/main/resources
if [ -f "$DIR/db.properties" ]; then
    echo "[1/5] Syncing db.properties..."
    cp "$DIR/db.properties" "$DIR/src/main/resources/db.properties"
    
    # Extract db.user and db.password if set
    USER=$(grep "^db.user=" "$DIR/db.properties" | cut -d'=' -f2-)
    PASS=$(grep "^db.password=" "$DIR/db.properties" | cut -d'=' -f2-)
    if [ -n "$PASS" ]; then
        echo "Found configured DB credentials in db.properties."
        # Update Tomcat setenv.sh
        cat <<EOF > "$TOMCAT_DIR/bin/setenv.sh"
#!/bin/sh
export JAVA_HOME="/opt/homebrew/opt/openjdk"
export PATH="\$JAVA_HOME/bin:\$PATH"
export DB_USER="${USER:-shopsphere_user}"
export DB_PASSWORD="$PASS"
EOF
        chmod +x "$TOMCAT_DIR/bin/setenv.sh"
        echo "Updated Tomcat bin/setenv.sh with DB_USER and DB_PASSWORD."
    fi
fi

# 2. Rebuild WAR package
echo "[2/5] Building shopsphere.war..."
cd "$DIR"
mvn package -DskipTests -q

# 3. Stop Tomcat
echo "[3/5] Restarting Tomcat 11..."
"$TOMCAT_DIR/bin/shutdown.sh" 2>/dev/null || true
sleep 2

# Force kill any lingering Tomcat java process if needed
pkill -f "apache-tomcat-11.0.25" 2>/dev/null || true
sleep 1

# 4. Deploy fresh WAR
echo "[4/5] Deploying target/shopsphere.war to Tomcat webapps..."
rm -rf "$TOMCAT_DIR/webapps/shopsphere"
cp "$DIR/target/shopsphere.war" "$TOMCAT_DIR/webapps/shopsphere.war"

# 5. Start Tomcat
echo "[5/5] Starting Tomcat..."
"$TOMCAT_DIR/bin/startup.sh"
sleep 3

echo "=================================================="
echo "  Tomcat restarted! ShopSphere is live at:"
echo "  http://localhost:8080/shopsphere/"
echo "=================================================="
