@echo off

for %%i in ("!%1!") do set "RESOLVED=%%~fi"

set "%1=%RESOLVED%"
set "RESOLVED="
