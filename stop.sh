#!/bin/bash
echo "Stopping SORS Project Services..."

kill_port() {
    local port=$1
    echo "Stopping process on port $port..."
    
    # Try using lsof
    if command -v lsof >/dev/null 2>&1; then
        local pid=$(lsof -t -i:$port 2>/dev/null)
        if [ ! -z "$pid" ]; then
            kill -9 $pid 2>/dev/null
            echo "Killed process $pid on port $port using lsof."
            return 0
        fi
    fi
    
    # Try using fuser as fallback
    if command -v fuser >/dev/null 2>&1; then
        fuser -k -n tcp $port >/dev/null 2>&1
        echo "Killed process on port $port using fuser."
        return 0
    fi
    
    # Try using ss / netstat to find pid and kill
    local pid_ss=$(ss -lptn "sport = :$port" 2>/dev/null | grep -oP 'pid=\K\d+')
    if [ ! -z "$pid_ss" ]; then
        kill -9 $pid_ss 2>/dev/null
        echo "Killed process $pid_ss on port $port using ss."
        return 0
    fi
    
    echo "No process detected on port $port (or insufficient permissions)."
}

kill_port 8080
kill_port 3000

echo "Services stopped!"
