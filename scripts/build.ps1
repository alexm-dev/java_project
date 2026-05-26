# Build script for ShareSpace.
# Usage:
#   .\scripts\build.ps1 DEBUG    - compile and run integration tests
#   .\scripts\build.ps1 RELEASE  - clean build of release JAR, no tests

param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("DEBUG", "RELEASE")]
    [string]$Mode
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot

switch ($Mode) {

    "DEBUG" {
        Write-Host "==> [DEBUG] Compiling and running integration tests"
        & mvn -f "$projectRoot\pom.xml" test
    }

    "RELEASE" {
        Write-Host "==> [RELEASE] Clean build, skipping tests"
        & mvn -f "$projectRoot\pom.xml" clean package -DskipTests -q

        $jar = Get-ChildItem "$projectRoot\target\*.jar" `
            | Where-Object { $_.Name -notlike "original-*" } `
            | Select-Object -First 1

        if (-not $jar) { throw "No shaded JAR found in target/" }

        Write-Host ""
        Write-Host "Build complete." -ForegroundColor Green
        Write-Host "  JAR: $($jar.Name)  ($([math]::Round($jar.Length / 1MB, 2)) MB)" -ForegroundColor Cyan
        Write-Host "  Run: java -jar target\$($jar.Name)"
    }
}
