@echo off
echo Stopping SORS Project Services...

:: Find and kill process on port 8080 (backend)
echo Stopping process on port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr /r "LISTENING" ^| findstr ":8080"') do (
    taskkill /f /pid %%a >nul 2>nul
)

:: Find and kill process on port 3000 (frontend)
echo Stopping process on port 3000...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr /r "LISTENING" ^| findstr ":3000"') do (
    taskkill /f /pid %%a >nul 2>nul
)

echo Services stopped!
