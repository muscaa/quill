@echo off

set "POST_RUN_DIR=%Q_TEMP%\post-%QPID%"

if exist "%POST_RUN_DIR%\" rmdir /s /q "%POST_RUN_DIR%"
mkdir "%POST_RUN_DIR%"
