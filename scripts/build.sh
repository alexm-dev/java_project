#!/usr/bin/env bash
# Build script for ShareSpace.
# Usage:
#   ./scripts/build.sh DEBUG    - compile and run integration tests
#   ./scripts/build.sh RELEASE  - clean build of release JAR, no tests

set -euo pipefail

MODE="${1:-}"
project_root="$(cd "$(dirname "$0")/.." && pwd)"

case "$MODE" in
    DEBUG)
        echo "==> [DEBUG] Compiling and running integration tests"
        mvn -f "$project_root/pom.xml" test
        ;;

    RELEASE)
        echo "==> [RELEASE] Clean build, skipping tests"
        mvn -f "$project_root/pom.xml" clean package -DskipTests -q

        jar="$(find "$project_root/target" -maxdepth 1 -name '*.jar' ! -name 'original-*' | head -n 1)"
        if [[ -z "$jar" ]]; then
            echo "Error: no shaded JAR found in target/" >&2
            exit 1
        fi

        size_mb=$(du -m "$jar" | cut -f1)
        echo ""
        echo "Build complete."
        echo "  JAR: $(basename "$jar")  (${size_mb} MB)"
        echo "  Run: java -jar $jar"
        ;;

    *)
        echo "Usage: $0 DEBUG|RELEASE" >&2
        exit 1
        ;;
esac
