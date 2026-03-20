@echo off

::
:: FILES
::
set "Q_HOME=%~dp0\..\.."
for %%i in ("%Q_HOME%") do set "Q_HOME=%%~fi"

set "Q_BIN=%Q_HOME%\bin"

set "Q_DIST=%Q_HOME%\dist"
set "Q_DIST_CORE=%Q_DIST%\bin"

set "Q_CONFIG=%Q_HOME%\config"
if not exist "%Q_CONFIG%" mkdir "%Q_CONFIG%"

set "Q_TEMP=%Q_HOME%\temp"
if not exist "%Q_TEMP%" mkdir "%Q_TEMP%"

::
:: OTHER
::
call "%Q_DIST_CORE%\qpid.cmd"
set "QPID=%errorlevel%"
