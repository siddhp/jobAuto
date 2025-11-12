@echo off

:: Get the path of the folder where this .bat file is located
set "REPO_PATH=%~dp0"

:: Go to that folder
cd /d "%REPO_PATH%"

echo.
echo ==========================================================
echo [STEP 3] Running Maven tests
echo ==========================================================
mvn test -Dfile=run.xml

echo.
echo ==========================================================
echo [DONE] Completed at %date% %time%.
echo ==========================================================

pause