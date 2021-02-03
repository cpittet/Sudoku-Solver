package com.github.cyrille.pittet.sudokusolver.solvers;

import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

/**
 * A sudoku solver.
 */
public interface Solver {

    /**
     * Solve the sudoku.
     * @param input : The input incomplete sudoku.
     * @return the completed sudoku or set status to NO_SOLUTION
     * if it has no solution.
     */
    Sudoku solve(Sudoku input);
}
