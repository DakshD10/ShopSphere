#!/usr/bin/env bash
# run.sh: One-step build, deploy to Tomcat 11, and display live URLs
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
"$DIR/sync-and-restart.sh" "$@"
