#!/usr/bin/env bash
# Runs the shaded ShareSpace JAR with the JDWP debug agent attached.
# Suspends on startup until a debugger connects on port 5005.
# Usage:  ./scripts/run-debug.sh
#
# Attach with:
#   - nvim-dap: { type = "java", request = "attach", hostName = "127.0.0.1", port = 5005 }
#   - IntelliJ: Run > Edit Configurations > + > Remote JVM Debug, port 5005
#   - Eclipse:  Run > Debug Configurations > Remote Java Application, port 5005

set -euo pipefail

project_root="$(cd "$(dirname "$0")/.." && pwd)"
jar="$(find "$project_root/target" -maxdepth 1 -name '*.jar' ! -name 'original-*' 2>/dev/null | head -n 1)"

if [[ -z "$jar" ]]; then
    echo "No JAR found - building first..."
    mvn -f "$project_root/pom.xml" package -q
    jar="$(find "$project_root/target" -maxdepth 1 -name '*.jar' ! -name 'original-*' | head -n 1)"
fi

# suspend=y : app halts at start until debugger attaches
# server=y  : JVM listens for the debugger (vs initiating the connection)
jdwp="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"

echo "Listening for debugger on port 5005..."
echo "Attach your IDE/nvim-dap to continue."

exec java --enable-native-access=ALL-UNNAMED "$jdwp" -jar "$jar"
