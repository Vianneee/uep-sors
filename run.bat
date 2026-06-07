@echo off
echo Starting SORS Project Services...

:: Load environment variables from .env if it exists
if exist .env (
    echo Loading environment variables from .env...
    for /f "usebackq tokens=* delims=" %%i in (".env") do (
        echo %%i | findstr /r "^#" >nul
        if errorlevel 1 (
            set %%i
        )
    )
)

:: Start main backend (Port 8080)
echo [1/2] Starting backend on port 8080...
start "SORS Backend (8080)" cmd /k "cd backend && .\mvnw spring-boot:run"


:: Start frontend server (Port 3000)
echo [2/2] Starting frontend server on port 3000...
:: Attempt to use python, fallback to npx http-server if python fails
where python >nul 2>nul
if %errorlevel% equ 0 (
    start "SORS Frontend (3000)" cmd /k "python -m http.server 3000"
) else (
    start "SORS Frontend (3000)" cmd /k "npx http-server -p 3000"
)

echo ===================================================
echo All services have been launched in separate windows!
echo - Backend API:       http://localhost:8080
echo - Health Check:      http://localhost:8080/api/health
echo - Organizations API: http://localhost:8080/api/organizations
echo - Frontend App:      http://localhost:3000/login.html
echo ===================================================
echo To stop them, close the spawned windows or run stop.bat
