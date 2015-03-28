@echo off
del document.pdf
if errorlevel 1 exit /b
latex document.tex
if errorlevel 1 exit /b
latex document.tex
if errorlevel 1 exit /b
dvips document.dvi
if errorlevel 1 exit /b
gsprint -sDEVICE=pdfwrite -sOutputFile=document.pdf document.ps
if errorlevel 1 exit /b
rem del *.aux *.dvi *.ps *.log *.toc
document.pdf