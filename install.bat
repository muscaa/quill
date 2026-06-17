@echo off
setlocal

set "TMP_ZIP=%TEMP%\quill.%RANDOM%%RANDOM%.zip"

curl -fL -o "%TMP_ZIP%" https://github.com/muscaa/quill/releases/latest/download/quill-bundle.zip
if errorlevel 1 (
    del /f /q "%TMP_ZIP%" 2>nul
    endlocal
    (goto) 2>nul & del "%~f0"
    exit /b 1
)

python3 "%TMP_ZIP%"

del /f /q "%TMP_ZIP%" 2>nul

endlocal
(goto) 2>nul & del "%~f0"
