package com.github.cyrille.pittet.sudokusolver.solvers;

import android.util.Log;
import android.widget.ProgressBar;

import com.github.cyrille.pittet.sudokusolver.SudokuInputActivity;
import com.github.cyrille.pittet.sudokusolver.sudoku.SolutionStatus;
import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

        if(progressBar != null) {
            // Set the maximal size if try all inputs
            progressBar.setMax(sizeSearchSpace);
            progressBar.setProgress(0);

        }

        int[][] grid = input.toArray();
        boolean isSolution = solveSubProblem(grid);
        Sudoku sudoku = new Sudoku(grid);

        if(isSolution) {
            sudoku.setStatus(SolutionStatus.COMPLETE);
        } else {
            sudoku.setStatus(SolutionStatus.NO_SOLUTION);
        }

        if(progressBar != null)
            progressBar.setProgress(sizeSearchSpace);

        return sudoku;
    }


    private boolean solveSubProblem(int[][] grid) {
        int[] emptyCellIdx = findEmptyCell(grid);

        if(emptyCellIdx == null) {
            return true;
        }

        for(int value = 1; value <= Sudoku.GRID_SIZE; value++) {
            grid[emptyCellIdx[0]][emptyCellIdx[1]] = value;

            if(!subSudokuIsValid(grid, emptyCellIdx[0], emptyCellIdx[1])) {
                if(progressBar != null)
                    progressBar.incrementProgressBy(1);

                grid[emptyCellIdx[0]][emptyCellIdx[1]] = 0;
            } else {
                boolean subSolution = this.solveSubProblem(grid);
                if(subSolution) {
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
     * @param grid the grid to search in
     * @return the coordinates of an empty cell or null if the grid is full
     */
    private int[] findEmptyCell(int[][] grid) {
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                if(grid[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Check whether the newly added cell keeps the sudoku valid or not.
     * @param grid the new grid
     * @param i row index of the newly filled cell
     * @param j column index of the newly filled cell
     * @return true if it is still valid, false otherwise
     */
    private boolean subSudokuIsValid(int[][] grid, int i, int j) {
        return rowIsValid(grid, i) && colIsValid(grid, j) && squareIsValid(grid, i, j);
    }

    /**
     * Check whether the ith row of the grid does not contain duplicates
     * @param grid the grid to check
     * @param i row index
     * @return true if there is no duplicates, false otherwise
     */
    private boolean rowIsValid(int[][] grid, int i) {
        Set<Integer> present = new HashSet<>(Sudoku.GRID_SIZE);

        for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
            if(grid[i][j] != 0) {
                if(present.contains(grid[i][j])) {
                    return false;
                }
                present.add(grid[i][j]);
            }
        }
        return true;
    }

    /**
     * Check whether the jth col of the grid does not contain duplicates
     * @param grid the grid to check
     * @param j column index
     * @return true if there is no duplicates, false otherwise
     */
    private boolean colIsValid(int[][] grid, int j) {
        Set<Integer> present = new HashSet<>(Sudoku.GRID_SIZE);

        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            if(grid[i][j] != 0) {
                if(present.contains(grid[i][j])) {
                    return false;
                }
                present.add(grid[i][j]);
            }
        }
        return true;
    }

    /**
     * Check whether the square of the grid does not contain duplicates
     * @param grid the grid to check
     * @param i row index
     * @param j column index
     * @return true if there is no duplicates, false otherwise
     */
    private boolean squareIsValid(int[][] grid, int i, int j) {
        Set<Integer> present = new HashSet<>(Sudoku.GRID_SIZE);
        int rowIdx = 3 * (i / 3);
        int colIdx = 3 * (j / 3);

        int curRow = 0;
        int curCol = 0;
        for(int a = 0; a < 3; a++) {
            for(int b = 0; b < 3; b++) {
                curRow = rowIdx + a;
                curCol = colIdx + b;
                if(grid[curRow][curCol] != 0) {
                    if(present.contains(grid[curRow][curCol])) {
                        return false;
                    }
                    present.add(grid[curRow][curCol]);
                }
            }
        }
        return true;
    }

    private String cellIndexToString(int[] cellIdx) {
        return cellIdx[0] + "-" + cellIdx[1];
    }
}
