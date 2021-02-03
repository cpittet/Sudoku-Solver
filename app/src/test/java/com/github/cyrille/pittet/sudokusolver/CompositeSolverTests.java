package com.github.cyrille.pittet.sudokusolver;

import com.github.cyrille.pittet.sudokusolver.solvers.BacktrackingSolver;
import com.github.cyrille.pittet.sudokusolver.solvers.CompositeSolver;
import com.github.cyrille.pittet.sudokusolver.solvers.RuleSolver;
import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompositeSolverTests {

    @Test
    public void compositeSolverOnExampleSudoku() {
        int[][] basis = new int[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE];
        basis[0][1] = 7;
        basis[0][3] = 6;
        basis[0][6] = 3;
        basis[1][2] = 4;
        basis[1][4] = 1;
        basis[1][8] = 5;
        basis[2][0] = 5;
        basis[2][2] = 6;
        basis[2][4] = 4;
        basis[2][6] = 9;
        basis[2][7] = 7;
        basis[3][3] = 4;
        basis[3][5] = 3;
        basis[3][8] = 2;
        basis[4][1] = 4;
        basis[4][2] = 9;
        basis[4][6] = 7;
        basis[4][7] = 8;
        basis[5][0] = 7;
        basis[5][3] = 8;
        basis[5][5] = 6;
        basis[6][1] = 9;
        basis[6][2] = 7;
        basis[6][4] = 8;
        basis[6][6] = 6;
        basis[6][8] = 3;
        basis[7][0] = 4;
        basis[7][4] = 3;
        basis[7][6] = 8;
        basis[8][2] = 3;
        basis[8][5] = 4;
        basis[8][7] = 2;

        List<Integer> trueSolution = Arrays.asList(2, 7, 8, 6, 5, 9, 3, 1, 4,
                9, 3, 4, 7, 1, 8, 2, 6, 5,
                5, 1, 6, 3, 4, 2, 9, 7, 8,
                6, 8, 1, 4, 7, 3, 5, 9, 2,
                3, 4, 9, 5, 2, 1, 7, 8, 6,
                7, 2, 5, 8, 9, 6, 4, 3, 1,
                1, 9, 7, 2, 8, 5, 6, 4, 3,
                4, 6, 2, 1, 3, 7, 8, 5, 9,
                8, 5, 3, 9, 6, 4, 1, 2, 7);

        CompositeSolver solver = new CompositeSolver(new RuleSolver(null), new BacktrackingSolver(null));

        Sudoku solution = solver.solve(new Sudoku(basis));
        int[][] gridSolution = solution.toArray();
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            int curRow = i * Sudoku.GRID_SIZE;
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                assertThat(gridSolution[i][j], is(trueSolution.get(curRow + j)));
            }
        }
    }
}
