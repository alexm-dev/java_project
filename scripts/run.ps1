# Runs the shaded ShareSpace JAR normally.
# Usage:  .\scripts\run.ps1

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$jar = Get-ChildItem "$projectRoot\target\*.jar" | Where-Object { $_.Name -notlike "original-*" } | Select-Object -First 1

if (-not $jar) {
    Write-Host "No JAR found - building first..." -ForegroundColor Yellow
    & mvn -f "$projectRoot\pom.xml" package -q
    $jar = Get-ChildItem "$projectRoot\target\*.jar" | Where-Object { $_.Name -notlike "original-*" } | Select-Object -First 1
}

& java --enable-native-access=ALL-UNNAMED -jar $jar.FullName
