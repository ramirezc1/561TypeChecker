# CS 561 Typechecker
Lexer, parser,AST and TypeChecker build by Sam Gerendasy & Cristian Ramirez. 
Starter code created by Michal Young.

# To Build
./CompileAndRun [input filename]

'make clean' removes sym table, lexer, parser, and all .class files.

'make dust' removes all .class files only.


# To Run
./run [input fileName]


# Notes
Cup doesn't get location of tokens correctly. Examples of use of locations from  
http://www2.cs.tum.edu/projekte/cup/examples.php
returns location (-1, -1) for all tokens. 