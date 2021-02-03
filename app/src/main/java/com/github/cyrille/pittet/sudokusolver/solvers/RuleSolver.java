package com.github.cyrille.pittet.sudokusolver.solvers;

import android.util.Log;
import android.widget.ProgressBar;

import com.github.cyrille.pittet.sudokusolver.SudokuInputActivity;
import com.github.cyrille.pittet.sudokusolver.sudoku.SolutionStatus;
import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A naive sudoku solver using the rules of sudoku and search to solve the sudoku.
 * Inspiration from : https://medium.com/datadriveninvestor/solving-sudoku-in-seconds-or-less-with-python-1c21f10117d6
 */
public class RuleSolver implements Solver {

    private final Set<Integer> possibilities;
    private Sudoku sudoku;

    private final ProgressBar progressBar;

    public RuleSolver(ProgressBar progressBar) {
        Set<Integer> tmp = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        possibilities = Collections.unmodifiableSet(tmp);

        this.progressBar = progressBar;
    }

    @Override
    public Sudoku solve(Sudoku sudoku) {
        this.sudoku = sudoku;
        List<int[]> emptyCells = this.sudoku.getEmptyCells();

        progressBar.setProgress(Sudoku.GRID_SIZE * Sudoku.GRID_SIZE - emptyCells.size());

        Set<Integer> possibleValues;
        List<int[]> emptyCellsToVisit;
        int oldSizeEmptyCells = 0;

        while(!emptyCells.isEmpty()) {
            oldSizeEmptyCells = emptyCells.size();
            emptyCellsToVisit = new ArrayList<>(emptyCells);

            for(int[] cell : emptyCellsToVisit) {
                possibleValues = getPossibilities(cell[0], cell[1]);

                if(possibleValues.isEmpty()) {
                    // The sudoku seems to not have a solution.
                    this.sudoku.setStatus(SolutionStatus.NO_SOLUTION);
                    return this.sudoku;
                } else if(possibleValues.size() == 1) {
                    int value = possibleValues.iterator().next();
                    this.sudoku.setValue(cell[0], cell[1],value );
                    emptyCells.remove(cell);

                    progressBar.incrementProgressBy(1);
                }
            }

            if(oldSizeEmptyCells == emptyCells.size()) {
                // Went through all the remaining empty cells
                // without being able to fill one.
                this.sudoku.setStatus(SolutionStatus.NOT_ABLE_TO_FIND_SOLUTION);
                return this.sudoku;
            }
        }

        this.sudoku.setStatus(SolutionStatus.COMPLETE);
        return this.sudoku;
    }

    /**
     * Compute the remaining possibilities for the ith row.
     * @param i the index of the row
     * @return the set of possibilities
     */
    private Set<Integer> getRowPossibilities(int i) {
        Set<Integer> row = sudoku.getRow(i);
        Set<Integer> result = new HashSet<>(possibilities);
        result.removeAll(row);
        return result;
    }

    /**
     * Compute the remaining possibilities for the jth column.
     * @param j the index of the column
     * @return the set of possibilities
     */
    private Set<Integer> getColPossibilities(int j) {
        Set<Integer> row = sudoku.getCol(j);
        Set<Integer> result = new HashSet<>(possibilities);
        result.removeAll(row);
        return result;
    }

    /**
     * Compute the remaining possibilities for the square.
     * @param i the index of the row
     * @param j the index of the col
     * @return the set of possibilities
     */
    private Set<Integer> getSquarePossibilities(int i, int j) {
        Set<Integer> row = sudoku.getSquare(i, j);
        Set<Integer> result = new HashSet<>(possibilities);
        result.removeAll(row);
        return result;
    }

    /**
     * Compute the possibilities for a given cell.
     * @param i row index of the cell
     * @param j column index of the cell
     * @return the set of possible values for this cell
     */
    private Set<Integer> getPossibilities(int i, int j) {
        Set<Integer> result = new HashSet<>(possibilities);
        result.retainAll(getRowPossibilities(i));
        result.retainAll(getColPossibilities(j));
        result.retainAll(getSquarePossibilities(i, j));
        return result;
    }
}
