@echo off

if exist graphs.sty del graphs.sty

for %%i in (*.sty) do (
	if "%%i" neq "graphs.sty" (
		echo %%%%%% %%i >> graphs.sty
		echo. >> graphs.sty
		type %%i >> graphs.sty
		echo. >> graphs.sty
	)
)