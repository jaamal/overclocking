@echo off
set name=Abstract
del %name%.pdf
if errorlevel 1 exit /b
latex %name%.tex
if errorlevel 1 exit /b
latex %name%.tex
if errorlevel 1 exit /b
dvips %name%.dvi
if errorlevel 1 exit /b
gsprint -sDEVICE=pdfwrite -sOutputFile=%name%.pdf %name%.ps
if errorlevel 1 exit /b
rem del *.aux *.dvi *.ps *.log *.toc
%name%.pdf