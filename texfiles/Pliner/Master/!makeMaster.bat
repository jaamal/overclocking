@echo off
del document.pdf
if errorlevel 1 exit /b
latex Master.tex
if errorlevel 1 exit /b
latex Master.tex
if errorlevel 1 exit /b
dvips Master.dvi
if errorlevel 1 exit /b
gsprint -sDEVICE=pdfwrite -sOutputFile=Master.pdf Master.ps
if errorlevel 1 exit /b
rem del *.aux *.dvi *.ps *.log *.toc
Master.pdf