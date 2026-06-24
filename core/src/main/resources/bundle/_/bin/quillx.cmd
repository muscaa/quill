@echo off
setlocal EnableDelayedExpansion

call "%~dp0\..\dist\core\qinit.cmd"

call "%Q_DIST_CORE%\qcore.cmd" run packages/default/muscaa@quill bin.quillx %*
