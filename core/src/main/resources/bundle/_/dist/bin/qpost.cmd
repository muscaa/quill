@echo off

if not defined POST_COMMAND set "POST_COMMAND=%1"
if not defined POST_COMMAND_ARGS set "POST_COMMAND_ARGS=%*"

:loop
set "_COMMAND=%POST_COMMAND%"
set "_COMMAND_ARGS=%POST_COMMAND_ARGS%"
set "POST_COMMAND="
set "POST_COMMAND_ARGS="

if defined _COMMAND if exist "%POST_RUN_DIR%\%_COMMAND%.cmd" (
    call "%POST_RUN_DIR%\%_COMMAND%.cmd" %_COMMAND_ARGS%

    if not defined POST_COMMAND set "POST_COMMAND=!errorlevel!"
    if not defined POST_COMMAND_ARGS set "POST_COMMAND_ARGS=%*"

    goto :loop
)

rmdir /s /q "%POST_RUN_DIR%"
