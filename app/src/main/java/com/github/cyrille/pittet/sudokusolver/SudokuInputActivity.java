package com.github.cyrille.pittet.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.GestureDetector;
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

public class SudokuInputActivity extends AppCompatActivity {

    public static final String LOG_TAG = "DEBUGGING";

    private static final String NO_SOLUTION_TXT = "This sudoku seems to not have a solution !";
    private static final String NOT_ABLE_TO_FIND_SOLUTION = "The algorithm was not able to find a solution.";

    private ProgressBar progressBar;
    private TextView resultTxtSolving;
    private TableLayout sudokuGrid;
    private Button resetButton;

    private EditText[][] textCells;

    private Solver solver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_input);

        progressBar = findViewById(R.id.progress_bar_solving);
        resultTxtSolving = findViewById(R.id.result_txt_solving);
        sudokuGrid = findViewById(R.id.grid_sudoku_input);
        resetButton = findViewById(R.id.button_reset_sudoku);

        textCells = new EditText[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE];

        setupGridView();

        solver = new CompositeSolver(new RuleSolver(progressBar), new BacktrackingSolver(progressBar));
    }

    private void setupGridView() {
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                View cell = getLayoutInflater().inflate(R.layout.cell_layout, null);

                row.addView(cell);
                textCells[i][j] = (EditText) cell;
            }
            sudokuGrid.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private int[][] gatherInput() {
        int[][] input = new int[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE];
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                String cellValue = textCells[i][j].getText().toString();
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
                    textCells[i][j].setText(String.valueOf(solution[i][j]));
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
            resultTxtSolving.setText(R.string.result_txt_solution_found);
        } else if(solutionStatus == SolutionStatus.NOT_ABLE_TO_FIND_SOLUTION) {
            showMessage(NOT_ABLE_TO_FIND_SOLUTION);

            // Write partial solution
            writeSolution(solution.toArray());
            resultTxtSolving.setText(R.string.result_txt_not_able_find_solution);
        } else {
            showMessage(NO_SOLUTION_TXT);
            resultTxtSolving.setText(R.string.result_txt_no_solution);
        }
        resultTxtSolving.setVisibility(View.VISIBLE);
    }

    public void resestSudoku(View view) {
        for(int i = 0; i < Sudoku.GRID_SIZE; i++) {
            for(int j = 0; j < Sudoku.GRID_SIZE; j++) {
                textCells[i][j].setText("");
            }
        }
    }
}