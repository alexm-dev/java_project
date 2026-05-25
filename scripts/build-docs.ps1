# Generates JavaDoc HTML for the project.
#
# Usage:
#   .\scripts\build-docs.ps1            (generate to target/reports/apidocs/)
#   .\scripts\build-docs.ps1 -Open      (also opens index.html in the default browser)

param(
    [switch]$Open
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$indexPath   = Join-Path $projectRoot "target\reports\apidocs\index.html"

Write-Host "==> Generating JavaDocs" -ForegroundColor Cyan
& mvn -f "$projectRoot\pom.xml" javadoc:javadoc

if ($LASTEXITCODE -ne 0) {
    Write-Host "JavaDoc generation failed (exit $LASTEXITCODE)" -ForegroundColor Red
    exit $LASTEXITCODE
}

if (-not (Test-Path $indexPath)) {
    Write-Host "Expected index.html at $indexPath but it was not generated." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "JavaDocs generated:" -ForegroundColor Green
Write-Host "  $indexPath"

if ($Open) {
    Start-Process $indexPath
}
