# Runs the shaded ShareSpace JAR with the JDWP debug agent attached.
# Suspends on startup until a debugger connects on port 5005.
# Usage:  .\scripts\run-debug.ps1
#
# Attach with:
#   - nvim-dap: { type = "java", request = "attach", hostName = "127.0.0.1", port = 5005 }
#   - IntelliJ: Run > Edit Configurations > + > Remote JVM Debug, port 5005

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$jar = Get-ChildItem "$projectRoot\target\*.jar" | Where-Object { $_.Name -notlike "original-*" } | Select-Object -First 1

if (-not $jar) {
    Write-Host "No JAR found - building first..." -ForegroundColor Yellow
    & mvn -f "$projectRoot\pom.xml" package -q
    $jar = Get-ChildItem "$projectRoot\target\*.jar" | Where-Object { $_.Name -notlike "original-*" } | Select-Object -First 1
}

# suspend=y : app halts at start until debugger attaches
# server=y  : JVM listens for the debugger (vs initiating the connection)
$jdwp = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"

Write-Host "Listening for debugger on port 5005..." -ForegroundColor Cyan
Write-Host "Attach your IDE/nvim-dap to continue."  -ForegroundColor Cyan

& java --enable-native-access=ALL-UNNAMED $jdwp -jar $jar.FullName
