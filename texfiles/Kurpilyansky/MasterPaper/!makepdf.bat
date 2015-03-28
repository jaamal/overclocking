@echo off
del MasterPaper.pdf
if errorlevel 1 exit /b
latex MasterPaper.tex
if errorlevel 1 exit /b
latex MasterPaper.tex
if errorlevel 1 exit /b
dvips MasterPaper.dvi
if errorlevel 1 exit /b
gsprint -sDEVICE=pdfwrite -sOutputFile=MasterPaper.pdf MasterPaper.ps
if errorlevel 1 exit /b
rem del *.aux *.dvi *.ps *.log *.toc
MasterPaper.pdf