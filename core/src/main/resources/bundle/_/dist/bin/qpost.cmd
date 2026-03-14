@echo off

if not defined POST_COMMAND set "POST_COMMAND=%1"
if not defined POST_COMMAND_ARGS set "POST_COMMAND_ARGS=%*"

:loop
set "COMMAND=%POST_COMMAND%"
set "COMMAND_ARGS=%POST_COMMAND_ARGS%"
set "POST_COMMAND="
set "POST_COMMAND_ARGS="

if defined COMMAND if exist "%POST_RUN_DIR%\%COMMAND%.cmd" (
    call "%POST_RUN_DIR%\%COMMAND%.cmd" %COMMAND_ARGS%

    if not defined POST_COMMAND set "POST_COMMAND=!errorlevel!"
    if not defined POST_COMMAND_ARGS set "POST_COMMAND_ARGS=%*"

    goto :loop
)

rmdir /s /q "%POST_RUN_DIR%"
