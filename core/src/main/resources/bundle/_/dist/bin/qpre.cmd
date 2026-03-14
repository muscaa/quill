@echo off

call "%Q_DIST_BIN%\qpid.cmd"
set "PID=%errorlevel%"

@REM set "POST_RUN_DIR=%Q_TEMP%\post-%PID%"
set "POST_RUN_DIR=%Q_TEMP%\post-0"

if exist "%POST_RUN_DIR%\" (
    rmdir /s /q "%POST_RUN_DIR%"
)
mkdir "%POST_RUN_DIR%"
