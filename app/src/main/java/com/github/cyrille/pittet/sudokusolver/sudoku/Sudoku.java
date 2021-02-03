package com.github.cyrille.pittet.sudokusolver.sudoku;

import android.util.Log;

import com.github.cyrille.pittet.sudokusolver.SudokuInputActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing a sudoku 9x9.
 */
public class Sudoku {

    public static final int GRID_SIZE = 9;

    private SolutionStatus status;

    private final int[][] grid;

    public Sudoku(int[][] grid) {
        boolean isNotValid = false;
        for (int[] ints : grid) {
            if (ints.length != GRID_SIZE) {
                isNotValid = true;
                break;
            }
        }
        if(isNotValid || grid.length != GRID_SIZE) {
            throw new IllegalArgumentException("The size of the grid is not " + GRID_SIZE + "x" +
                    GRID_SIZE + ".");
        }

        this.grid = grid;
        status = SolutionStatus.INCOMPLETE;
    }

    /**
     *
     * @param i index of the row
     * @return the ith row as a set
     */
    public Set<Integer> getRow(int i) {
        if(indexIsNotValid(i))
            throw new IllegalArgumentException("The index is not between 0 and 8.");

        Set<Integer> row = new HashSet<>(9);
        for(int a = 0; a < GRID_SIZE; a++) {
            if(grid[i][a] != 0) {
                row.add(grid[i][a]);
            }
        }
        return row;
    }

    /**
     *
     * @param j index of the column
     * @return the jth column as a set
     */
    public Set<Integer> getCol(int j) {
        if(indexIsNotValid(j))
            throw new IllegalArgumentException("The index is not between 0 and 8.");

        Set<Integer> col = new HashSet<>(9);
        for(int a = 0; a < GRID_SIZE; a++) {
            if(grid[a][j] != 0) {
                col.add(grid[a][j]);
            }
        }
        return col;
    }

    /**
     *
     * @param i index of the row
     * @param j index of the column
     * @return the elements present in the 3x3 square the cell i,j is
     * belonging to.
     */
    public Set<Integer> getSquare(int i, int j) {
        if(indexIsNotValid(i) || indexIsNotValid(j))
            throw new IllegalArgumentException("The index is not between 0 and 8.");

        int rowIdx = i / 3;
        int colIdx = j / 3;
        Set<Integer> result = new HashSet<>(GRID_SIZE);
        for(int a = 0; a < 3; a++) {
            int row = 3 * rowIdx + a;
            for(int b = 0; b < 3; b++) {
                int col = 3 * colIdx + b;
                if(grid[row][col] != 0) {
                    result.add(grid[row][col]);
                }
            }
        }
        return result;
    }

    /**
     * Compute the set of indexes of empty cells in the current sudoku
     * @return the set of indexes empty cells in the current sudoku
     */
    public List<int[]> getEmptyCells() {
        List<int[]> result = new ArrayList<>();
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                if(grid[i][j] == 0) {
                    result.add(new int[]{i, j});
                }
            }
        }
        return result;
    }

    /**
     * Set the value of a cell in the sudoku.
     * @param i row index of the cell
     * @param j column index of the cell
     * @param value the value of the cell
     */
    public void setValue(int i, int j, int value) {
        if(indexIsNotValid(i) || indexIsNotValid(j))
            throw new IllegalArgumentException("The index is not between 0 and 8.");
        if(indexIsNotValid(value))
            throw new IllegalArgumentException("The value of a cell must be between 1 and 9.");

        this.grid[i][j] = value;
    }

    /**
     * Set the status of this sudoku.
     * @param status the new status of this sudoku
     */
    public void setStatus(SolutionStatus status) {
        this.status = status;
    }

    public SolutionStatus getStatus() {
        return this.status;
    }

    /**
     * Convert this sudoku to a 2D int array.
     * @return this sudoku as a int[][]
     */
    public int[][] toArray(){
        return this.grid.clone();
    }

    private boolean indexIsNotValid(int i) {
        return i < 0 || i >= GRID_SIZE;
    }
}
