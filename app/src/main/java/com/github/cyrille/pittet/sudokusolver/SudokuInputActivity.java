package com.github.cyrille.pittet.sudokusolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.cyrille.pittet.sudokusolver.solvers.BacktrackingSolver;
import com.github.cyrille.pittet.sudokusolver.solvers.CompositeSolver;
import com.github.cyrille.pittet.sudokusolver.solvers.RuleSolver;
import com.github.cyrille.pittet.sudokusolver.solvers.Solver;
import com.github.cyrille.pittet.sudokusolver.sudoku.SolutionStatus;
import com.github.cyrille.pittet.sudokusolver.sudoku.Sudoku;

import java.util.HashMap;
import java.util.Map;

public class SudokuInputActivity extends AppCompatActivity {

    public static final String LOG_TAG = "DEBUGGING";

    private static final String NO_SOLUTION_TXT = "This sudoku seems to not have a solution !";
    private static final String NOT_ABLE_TO_FIND_SOLUTION = "The algorithm was not able to find a solution.";

    private ProgressBar progressBar;
    private TextView result_txt_solving;
    private TableLayout sudokuGrid;

    private EditText[][] text_cells;

    private Solver solver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_input);

        progressBar = findViewById(R.id.progress_bar_solving);
        result_txt_solving = findViewById(R.id.result_txt_solving);
        sudokuGrid = findViewById(R.id.grid_sudoku_input);

        text_cells = new EditText[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE];

        setupGridView();

        solver = new CompositeSolver(new RuleSolver(progressBar), new BacktrackingSolver(progressBar));

        if(savedInstanceState != null) {
            for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
                for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                    text_cells[i][j].setText(savedInstanceState.getString("" + i + j));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                outState.putString("" + i + j, text_cells[i][j].getText().toString());
            }
        }
    }

    private void setupGridView() {
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                View cell = getLayoutInflater().inflate(R.layout.cell_layout, null);

                row.addView(cell);
                text_cells[i][j] = (EditText) cell;
            }
            sudokuGrid.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private int[][] gatherInput() {
        int[][] input = new int[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE];
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                String cellValue = text_cells[i][j].getText().toString();
                if(!cellValue.equals("")) {
                    input[i][j] = Integer.parseInt(cellValue);
                }
            }
        }
        return input;
    }

    private void writeSolution(int[][] solution) {
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                if(solution[i][j] != 0) {
                    (text_cells[i][j]).setText(String.valueOf(solution[i][j]));
                }
            }
        }
    }

    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void solveSudoku(View view) {
        progressBar.setVisibility(View.VISIBLE);

        int[][] input = gatherInput();
        Sudoku sudoku = new Sudoku(input);

        Sudoku solution = solver.solve(sudoku);
        SolutionStatus solutionStatus = solution.getStatus();

        if(solutionStatus == SolutionStatus.COMPLETE) {
            writeSolution(solution.toArray());
            result_txt_solving.setText(R.string.result_txt_solution_found);
        } else if(solutionStatus == SolutionStatus.NOT_ABLE_TO_FIND_SOLUTION) {
            showMessage(NOT_ABLE_TO_FIND_SOLUTION);

            // Write partial solution
            writeSolution(solution.toArray());
            result_txt_solving.setText(R.string.result_txt_not_able_find_solution);
        } else {
            showMessage(NO_SOLUTION_TXT);
            result_txt_solving.setText(R.string.result_txt_no_solution);
        }
        result_txt_solving.setVisibility(View.VISIBLE);
    }
}