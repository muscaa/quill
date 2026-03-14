@echo off
setlocal EnableDelayedExpansion

call "%~dp0\..\dist\bin\qinit.cmd"

call "%Q_DIST_BIN%\qpre.cmd"
call "%Q_DIST_BIN%\qcore.cmd" run packages/default/muscaa@quill bin.quillx %*

set "POST_COMMAND=%errorlevel%"
call "%Q_DIST_BIN%\qpost.cmd"
