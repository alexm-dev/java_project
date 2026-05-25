#!/usr/bin/env bash
# Generates JavaDoc HTML for the project.
#
# Usage:
#   ./scripts/build-docs.sh             (generate to target/reports/apidocs/)
#   ./scripts/build-docs.sh --open      (also opens index.html in the default browser)

set -euo pipefail

project_root="$(cd "$(dirname "$0")/.." && pwd)"
index_path="$project_root/target/reports/apidocs/index.html"
open_browser=0

if [[ "${1:-}" == "--open" ]]; then
    open_browser=1
fi

echo "==> Generating JavaDocs"
mvn -f "$project_root/pom.xml" javadoc:javadoc

if [[ ! -f "$index_path" ]]; then
    echo "Expected index.html at $index_path but it was not generated." >&2
    exit 1
fi

echo ""
echo "JavaDocs generated:"
echo "  $index_path"

if [[ "$open_browser" -eq 1 ]]; then
    if   command -v xdg-open >/dev/null; then xdg-open "$index_path"
    elif command -v open     >/dev/null; then open "$index_path"
    else echo "(no opener found - install xdg-open or open manually)"; fi
fi
