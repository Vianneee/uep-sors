#!/bin/bash
echo "Stopping SORS Project Services..."

kill_port() {
    local port=$1
    echo "Stopping process on port $port..."

    local pid
    if command -v lsof >/dev/null 2>&1; then
        pid=$(lsof -t -i:$port 2>/dev/null)
    elif command -v ss >/dev/null 2>&1; then
        pid=$(ss -lptn "sport = :$port" 2>/dev/null | grep -oP 'pid=\K\d+' | head -n 1)
    fi

    if [ -n "$pid" ]; then
        kill -9 $pid 2>/dev/null
    elif command -v fuser >/dev/null 2>&1; then
        fuser -k -n tcp $port >/dev/null 2>&1
    fi
}

kill_port 8080
kill_port 3000

echo "Services stopped!"
