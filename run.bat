@echo off
echo Starting SORS Project Services...

:: Start main backend (Port 8080)
echo [1/3] Starting parent backend on port 8080...
start "SORS Parent Backend (8080)" cmd /k "cd backend && .\mvnw spring-boot:run"

:: Start organizations backend (Port 8082)
echo [2/3] Starting organizations backend on port 8082...
start "SORS Organizations (8082)" cmd /k "cd backend\organizations && .\mvnw spring-boot:run"

:: Start frontend server (Port 3000)
echo [3/3] Starting frontend server on port 3000...
:: Attempt to use python, fallback to npx http-server if python fails
where python >nul 2>nul
if %errorlevel% equ 0 (
    start "SORS Frontend (3000)" cmd /k "python -m http.server 3000"
) else (
    start "SORS Frontend (3000)" cmd /k "npx http-server -p 3000"
)

echo ===================================================
echo All services have been launched in separate windows!
echo - Main Backend:      http://localhost:8080
echo - Organizations API: http://localhost:8082
echo - Frontend App:      http://localhost:3000/login.html
echo ===================================================
echo To stop them, close the spawned windows or run stop.bat
