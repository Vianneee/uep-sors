#!/bin/bash
echo "Starting SORS Project Services..."

# Load environment variables from .env if it exists
if [ -f .env ]; then
    echo "Loading environment variables from .env..."
    export $(grep -v '^#' .env | xargs)
fi

# Ensure Maven Wrapper is executable
if [ -f "backend/mvnw" ]; then
    chmod +x backend/mvnw
fi


# Start main backend (Port 8080)
echo "[1/2] Starting backend on port 8080..."
cd backend
./mvnw spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
cd ..

# Start frontend server (Port 3000)
echo "[2/2] Starting frontend server on port 3000..."
# Attempt to use python3, python, or fallback to npx http-server
if command -v python3 >/dev/null 2>&1; then
    python3 -m http.server 3000 > frontend.log 2>&1 &
    FRONTEND_PID=$!
elif command -v python >/dev/null 2>&1; then
    python -m http.server 3000 > frontend.log 2>&1 &
    FRONTEND_PID=$!
else
    npx -y http-server -p 3000 > frontend.log 2>&1 &
    FRONTEND_PID=$!
fi

echo "==================================================="
echo "All services have been launched in the background!"
echo "- Backend API:       http://localhost:8080"
echo "- Health Check:      http://localhost:8080/api/health"
echo "- Organizations API: http://localhost:8080/api/organizations"
echo "- Frontend App:      http://localhost:3000/login.html"
echo "==================================================="
echo "Backend log is at: backend/backend.log"
echo "Frontend log is at: frontend.log"
echo "To stop them, run ./stop.sh"
