#!/usr/bin/env bash
# Builds a self-contained submission package for ShareSpace.
# Usage:   ./scripts/build-submission.sh
# Output:  submission/ShareSpace-submission.zip

set -euo pipefail

project_root="$(cd "$(dirname "$0")/.." && pwd)"
stage="$project_root/submission/ShareSpace"
out_zip="$project_root/submission/ShareSpace-submission.zip"

echo "==> Cleaning previous builds"
mvn -f "$project_root/pom.xml" clean -q

echo "==> Compiling + packaging (creates fat JAR)"
mvn -f "$project_root/pom.xml" package -q -DskipTests

echo "==> Generating JavaDocs"
mvn -f "$project_root/pom.xml" javadoc:javadoc -q

echo "==> Staging submission folder"
rm -rf "$project_root/submission"
mkdir -p "$stage"

cp -r "$project_root/src/main" "$stage/"
cp "$project_root/pom.xml" "$stage/"
cp "$project_root/README.md" "$stage/" 2>/dev/null || true
cp "$project_root/LICENSE" "$stage/" 2>/dev/null || true

cp -r "$project_root/target/reports/apidocs" "$stage/javadoc"

# Pick the shaded fat JAR (skip the maven-jar-plugin "original-*.jar")
shaded_jar="$(find "$project_root/target" -maxdepth 1 -name '*.jar' ! -name 'original-*' | head -n 1)"
if [[ -z "$shaded_jar" ]]; then
    echo "Error: no shaded JAR found in target/" >&2
    exit 1
fi
cp "$shaded_jar" "$stage/"

cp "$project_root/doc/sharespace_doc.pdf" "$stage/" 2>/dev/null || true

echo "==> Creating zip"
( cd "$stage/.." && zip -qr "$out_zip" "$(basename "$stage")"/* )

jar_size_mb=$(du -m "$shaded_jar" | cut -f1)
echo ""
echo "Done.  Submission package at:"
echo "  $out_zip"
echo "JAR included: $(basename "$shaded_jar") (${jar_size_mb} MB)"
