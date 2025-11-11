@echo off
echo ==========================================
echo 🚀 Launching Chrome with persistent profile and remote debugging
echo ==========================================

:: --- CONFIG ---
set "CHROME_PATH=C:\Program Files\Google\Chrome\Application\chrome.exe"
set "CUSTOM_PROFILE_DIR=C:\ChromeDebugProfile"
set "AMAZON_URL=https://hiring.amazon.ca/job-opportunities/warehouse-jobs"
set "DEBUG_PORT=9222"

:: --- CLEANUP OLD INSTANCES ---
echo 🔄 Closing any existing Chrome sessions...
taskkill /F /IM chrome.exe >nul 2>&1

:: --- LAUNCH CHROME ---
echo 🟢 Starting Chrome on port %DEBUG_PORT%...
start "" "%CHROME_PATH%" ^
  --remote-debugging-port=%DEBUG_PORT% ^
  --user-data-dir="%CUSTOM_PROFILE_DIR%" ^
  --profile-directory=Default ^
  --start-maximized ^
  --no-first-run ^
  --no-default-browser-check ^
  --disable-popup-blocking ^
  --disable-background-timer-throttling ^
  --disable-backgrounding-occluded-windows ^
  --disable-renderer-backgrounding ^
  --disable-notifications ^
  "%AMAZON_URL%"

:: --- WAIT AND VERIFY ---
timeout /t 4 >nul
echo 🔍 Checking if debugging port %DEBUG_PORT% is active...
netstat -ano | find "%DEBUG_PORT%" || (
    echo ❌ Chrome debugging port not found. Something went wrong.
    pause
    exit /b
)

echo ✅ Chrome launched successfully on port %DEBUG_PORT%.
echo You can now run your Selenium automation.
echo ------------------------------------------
echo Press any key to exit this launcher window...
pause >nul
exit /b
