#!/bin/sh

pdflatex main.tex
pdflatex main.tex

if [ $1 = "mrproper" ] || [ $2 = "mrproper" ]; then
    rm -f *.ps *.aux *.dvi *.toc *.log
fi

mv main.pdf rapport.pdf

if [ $1 = "o" ] || [ $1 = "O" ] || [ $2 = "o" ] || [ $2 = "O" ]; then
    okular rapport.pdf &
fi
