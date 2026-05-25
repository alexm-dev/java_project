#!/usr/bin/env bash
# Runs the shaded ShareSpace JAR normally.
# Usage:  ./scripts/run.sh

set -euo pipefail

project_root="$(cd "$(dirname "$0")/.." && pwd)"
jar="$(find "$project_root/target" -maxdepth 1 -name '*.jar' ! -name 'original-*' 2>/dev/null | head -n 1)"

if [[ -z "$jar" ]]; then
    echo "No JAR found - building first..."
    mvn -f "$project_root/pom.xml" package -q
    jar="$(find "$project_root/target" -maxdepth 1 -name '*.jar' ! -name 'original-*' | head -n 1)"
fi

exec java --enable-native-access=ALL-UNNAMED -jar "$jar"
