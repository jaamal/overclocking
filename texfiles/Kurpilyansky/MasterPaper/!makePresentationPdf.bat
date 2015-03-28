@echo off
del presentation.pdf
if errorlevel 1 exit /b
rem pdflatex presentation.tex
rem if errorlevel 1 exit /b
pdflatex presentation.tex
if errorlevel 1 exit /b
rem del *.aux *.dvi *.ps *.log *.toc
presentation.pdf