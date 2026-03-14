@echo off

set "Q_HOME=%~dp0\..\.."
call "%~dp0\qresolve.cmd" Q_HOME

set "Q_BIN=%Q_HOME%\bin"

set "Q_DIST=%Q_HOME%\dist"
set "Q_DIST_BIN=%Q_DIST%\bin"

set "Q_CONFIG=%Q_HOME%\config"
if not exist "%Q_CONFIG%" mkdir "%Q_CONFIG%"

set "Q_TEMP=%Q_HOME%\temp"
if not exist "%Q_TEMP%" mkdir "%Q_TEMP%"
