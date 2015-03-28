@echo off
del BachelorPaper.pdf
if errorlevel 1 exit /b
latex BachelorPaper.tex
if errorlevel 1 exit /b
latex BachelorPaper.tex
if errorlevel 1 exit /b
dvips BachelorPaper.dvi
if errorlevel 1 exit /b
gsprint -sDEVICE=pdfwrite -sOutputFile=BachelorPaper.pdf BachelorPaper.ps
if errorlevel 1 exit /b
rem del *.aux *.dvi *.ps *.log *.toc
BachelorPaper.pdf