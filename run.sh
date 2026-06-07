#!/bin/bash
echo "Starting SORS Project Services..."

# Load environment variables from .env if it exists
if [ -f .env ]; then
    echo "Loading environment variables from .env..."
    while IFS= read -r line || [ -n "$line" ]; do
        line="${line%$'\r'}"                         # strip Windows carriage return
        [[ -z "$line" || "$line" == \#* ]] && continue  # skip blanks and comments
        [[ "$line" =~ ^([^=]+)=(.*)$ ]] || continue
        key="${BASH_REMATCH[1]}"
        val="${BASH_REMATCH[2]}"
        val="${val#[\"\']}" ; val="${val%[\"\']}"    # strip surrounding quotes
        export "$key=$val"
    done < .env
fi

# Ensure Maven Wrapper is executable
[ -f "backend/mvnw" ] && chmod +x backend/mvnw

# Start main backend (Port 8080)
echo "[1/2] Starting backend on port 8080..."
(cd backend && ./mvnw spring-boot:run) &

# Start frontend server (Port 3000)
echo "[2/2] Starting frontend server on port 3000..."
if command -v python3 >/dev/null 2>&1; then
    python3 -m http.server 3000 &
elif command -v python >/dev/null 2>&1; then
    python -m http.server 3000 &
else
    npx -y http-server -p 3000 &
fi

echo "==================================================="
echo "All services have been launched in the background!"
echo "- Backend API:       http://localhost:8080"
echo "- Health Check:      http://localhost:8080/api/health"
echo "- Organizations API: http://localhost:8080/api/organizations"
echo "- Frontend App:      http://localhost:3000/login.html"
echo "==================================================="
echo "To stop them, run ./stop.sh"
