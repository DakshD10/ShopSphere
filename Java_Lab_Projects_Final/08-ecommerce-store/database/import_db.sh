#!/bin/bash
# =====================================================================
# ShopSphere - MySQL Database Schema Importer
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_FILE="$SCRIPT_DIR/shopsphere.sql"

DB_USER="${DB_USER:-root}"
MYSQL_BIN="$(which mysql 2>/dev/null || echo "/opt/homebrew/bin/mysql")"

if [ ! -x "$MYSQL_BIN" ]; then
    echo "[Error] MySQL client not found. Ensure MySQL is installed and in PATH."
    exit 1
fi

echo "=================================================="
echo "  ShopSphere Database Importer"
echo "=================================================="
echo "Target SQL: $SQL_FILE"
echo "MySQL User: $DB_USER"

if [ -n "$DB_PASSWORD" ]; then
    echo "Using DB_PASSWORD from environment variable..."
    "$MYSQL_BIN" -u "$DB_USER" -p"$DB_PASSWORD" < "$SQL_FILE"
else
    echo "Please enter MySQL password for user '$DB_USER' (or press Enter if blank):"
    "$MYSQL_BIN" -u "$DB_USER" -p < "$SQL_FILE"
fi

if [ $? -eq 0 ]; then
    echo ""
    echo "[SUCCESS] ShopSphere database 'shopsphere' created and seeded successfully!"
else
    echo ""
    echo "[FAIL] Could not import schema. Check MySQL server status and password."
    exit 1
fi
