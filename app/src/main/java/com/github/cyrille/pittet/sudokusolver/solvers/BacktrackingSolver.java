package com.github.cyrille.pittet.sudokusolver.solvers;

import android.widget.ProgressBar;

import com.github.cyrille.pittet.sudokusolver.sudoku.SolutionStatus;
import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

import java.util.List;

/**
 * A brute force sudoku solver using backtracking (depth-first search).
 * Inspiration for the implementation : https://www.geeksforgeeks.org/sudoku-backtracking-7/
 */
public class BacktrackingSolver implements Solver {

    private final ProgressBar progressBar;

    public BacktrackingSolver(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public Sudoku solve(Sudoku input) {
        List<int[]> emptyCells = input.getEmptyCells();
        int sizeEmptyCells = emptyCells.size();
        int sizeSearchSpace = (int) Math.pow(sizeEmptyCells, 9);

        if (progressBar != null) {
            // Set the maximal size if try all inputs
            progressBar.setMax(sizeSearchSpace);
            progressBar.setProgress(0);

        }

        int[][] grid = input.toArray();
        boolean isSolution = solveSubProblem(grid);
        Sudoku sudoku = new Sudoku(grid);

        if (isSolution) {
            sudoku.setStatus(SolutionStatus.COMPLETE);
        } else {
            sudoku.setStatus(SolutionStatus.NO_SOLUTION);
        }

        if (progressBar != null)
            progressBar.setProgress(sizeSearchSpace);

        return sudoku;
    }


    private boolean solveSubProblem(int[][] grid) {
        int[] emptyCellIdx = findEmptyCell(grid);

        if (emptyCellIdx == null) {
            return true;
        }

        for (int value = 1; value <= Sudoku.GRID_SIZE; value++) {
            grid[emptyCellIdx[0]][emptyCellIdx[1]] = value;

            if (!(new Sudoku(grid)).sudokuIsValid(emptyCellIdx[0], emptyCellIdx[1])) {
                if (progressBar != null)
                    progressBar.incrementProgressBy(1);

                grid[emptyCellIdx[0]][emptyCellIdx[1]] = 0;
            } else {
                boolean subSolution = this.solveSubProblem(grid);
                if (subSolution) {
                    return true;
                } else {
                    grid[emptyCellIdx[0]][emptyCellIdx[1]] = 0;
                }
            }
        }
        return false;
    }

    /**
     * Find an empty cell to be filled or return null if the grid is full
     *
     * @param grid the grid to search in
     * @return the coordinates of an empty cell or null if the grid is full
     */
    private int[] findEmptyCell(int[][] grid) {
        for (int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for (int j = 0; j < Sudoku.GRID_SIZE; j++) {
                if (grid[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private String cellIndexToString(int[] cellIdx) {
        return cellIdx[0] + "-" + cellIdx[1];
    }
}
