# Sudoku-Solver

![Java CI with Gradle](https://github.com/cpittet/Sudoku-Solver/workflows/Java%20CI%20with%20Gradle/badge.svg)

Minimalist Android app that solves 9x9 sudokus.

For now the sudoku solver uses first a search based on the rules of sudoku to decrease the size of the search space for the second solver. The second solver uses backtracking (brute force) to find the final solution.
