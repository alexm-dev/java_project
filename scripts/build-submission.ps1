# Builds a self-contained submission package for ShareSpace.
# Usage:   .\scripts\build-submission.ps1
# Output:  submission/ShareSpace-submission.zip

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$stage       = Join-Path $projectRoot "submission\ShareSpace"
$outZip      = Join-Path $projectRoot "submission\ShareSpace-submission.zip"

Write-Host "==> Cleaning previous builds"
& mvn -f "$projectRoot\pom.xml" clean -q

Write-Host "==> Compiling + packaging (creates fat JAR)"
& mvn -f "$projectRoot\pom.xml" package -q

Write-Host "==> Generating JavaDocs"
& mvn -f "$projectRoot\pom.xml" javadoc:javadoc -q

Write-Host "==> Staging submission folder"
if (Test-Path "$projectRoot\submission") { Remove-Item "$projectRoot\submission" -Recurse -Force }
New-Item -ItemType Directory -Path $stage | Out-Null

Copy-Item -Path "$projectRoot\src"      -Destination $stage -Recurse
Copy-Item -Path "$projectRoot\pom.xml"  -Destination $stage
Copy-Item -Path "$projectRoot\README.md" -Destination $stage -ErrorAction SilentlyContinue
Copy-Item -Path "$projectRoot\LICENSE"   -Destination $stage -ErrorAction SilentlyContinue

Copy-Item -Path "$projectRoot\target\reports\apidocs" -Destination "$stage\javadoc" -Recurse

# Pick the shaded fat JAR (skip the maven-jar-plugin "original-*.jar")
$shadedJar = Get-ChildItem "$projectRoot\target\*.jar" | Where-Object { $_.Name -notlike "original-*" } | Select-Object -First 1
if (-not $shadedJar) { throw "No shaded JAR found in target/" }
Copy-Item $shadedJar.FullName -Destination $stage

Copy-Item -Path "$projectRoot\doc\sharespace_doc.pdf" -Destination $stage -ErrorAction SilentlyContinue

Write-Host "==> Creating zip"
Compress-Archive -Path "$stage\*" -DestinationPath $outZip -Force

Write-Host ""
Write-Host "Done.  Submission package at:" -ForegroundColor Green
Write-Host "  $outZip"
Write-Host "JAR included: $($shadedJar.Name) ($([math]::Round($shadedJar.Length/1MB,2)) MB)" -ForegroundColor Cyan
