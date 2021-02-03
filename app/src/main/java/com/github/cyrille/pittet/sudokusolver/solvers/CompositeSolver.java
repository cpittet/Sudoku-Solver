package com.github.cyrille.pittet.sudokusolver.solvers;

import com.github.cyrille.pittet.sudokusolver.sudoku.SolutionStatus;
import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

/**
 * A composite sudoku solver using first a rule solver to decrease
 * the search space for the second solver which is a brute force backtracking solver.
 */
public class CompositeSolver implements Solver {

    private RuleSolver ruleSolver;
    private BacktrackingSolver backtrackingSolver;

    public CompositeSolver(RuleSolver ruleSolver, BacktrackingSolver backtrackingSolver) {
        this.ruleSolver = ruleSolver;
        this.backtrackingSolver = backtrackingSolver;
    }

    @Override
    public Sudoku solve(Sudoku input) {
        Sudoku subSolution = ruleSolver.solve(input);

        if(subSolution.getStatus().equals(SolutionStatus.COMPLETE)) {
            return subSolution;
        }
        return backtrackingSolver.solve(subSolution);
    }
}
