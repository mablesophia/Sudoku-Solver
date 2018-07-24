# Sudoku-Solver
AI Sudoku Solver

# (1) Compiling
 
This is simple. From the folder with the "Makefile", execute the command: make
A bin folder should have appeared with the compiled product in there. I recommend everyone to make sure they can compile their code before they start coding.
# (2) Operating
 
Once you have compiled your program, you can test to see that it is working. The BackTracking Solver will have some basic backtracking logic already implemented at the start. This logic should suffice to solve P = 3, Q = 3, matrices (More on P and Q below).
To run your progam just execute the binaries in the bin folder:
C++: bin/Sudoku
Java: java -jar bin/Sudoku.jar
Python: python3 bin/Main.pyc
By default, a random 3x3 matrix will be generated and displayed. The solver will attempt to solve it, and the solution will be displayed once found. If no solution is found, a text message description appears. The number of assignments and backtracks made will also be displayed; this can be used to test your heuristic implementations.
You add tokens on the command line to make your program run in different ways. For example, if you would like to use MRV to select a variable and LCV to select a value you would execute:

C++: bin/Sudoku MRV LCV
Java: java -jar bin/Sudoku.jar MRV LCV
Python: python3 bin/Main.pyc MRV LCV
The token order doesn't matter. The following tokens are valid:
MRV: Minimum Remaining Value Variable Selector
DEG: Degree Heuristic Variable Selector
MAD: MRV and DEG tie breaker
LCV: Least Constraining Value Value Selector
FC: Forward Checking Constraint Propagation
NOR: Norvig's Sudoku Constraint Propagation
TOURN: Custom Heuristic for tournament
You can also specify a path to a Sudoku file or folder containing many Sudoku files. Sudoku files are outputs of the board generator also included in the student repository (More on that below).
This is an example of something I will execute when grading your projects for part 1:

C++: bin/Sudoku MRV LCV FC path/to/board/files
Java: java -jar bin/Sudoku.jar MRV LCV FC path/to/board/files
Python: python3 bin/Main.pyc MRV LCV FC path/to/board/files
 
# (2.1) Board files, understanding, and generation
You will have to generate and use Sudoku Board files throughout this project. This is made easy with the Board Generator. This should be found in the shell root folder. Look for "Sudoku_Generator" In there you can excute the make command to simply generate a set of custom boards. You can also use the board generator by the following synopsis:
python3 board_generator.py <File Prefix> <# of boards> <P> <Q> <M>
This will generate your desired boards. The file format is very simple, so you can custom the boards easily. The file format is:
P Q
# # # ...
# # # ...
# # # ...
.
.
.
Where each # represents the value at that place on the board. If this is confusing, generate a file, and look at it. I'm sure it is self-explanatory. If you are still confused, you can always email me or post on Piazza.

# (2.2) P Q and M
N = the length of one side of the NxN grid, also the number of distinct tokens
P = the number of rows in each block (Norvig's box is a synonym for block as used here)
Q = the number of columns in each block
M = the number of filled-in values at the start
Each block is a rectangle, P rows X Q columns.  The set of blocks that align horizontally are called a block row (= a row of blocks).  Similarly, the set of blocks that align vertically are called a block column (= a column of blocks).

N = P*Q, so P = N/Q and Q = N/P.  Thus, there are P block columns and Q block rows.  Please distinguish between rows/columns per block and block rows/block columns per grid. You can experiment by generating different board configurations with these parameters to see how they work.
 
M = 0 is an empty sudoku board, and M = 81 is a sudoku board with 81 values filled in.  Note that higher values of M result in longer board generation times.
