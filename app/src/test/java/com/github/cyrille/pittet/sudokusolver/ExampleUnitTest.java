package com.github.cyrille.pittet.sudokusolver;

import com.github.cyrille.pittet.sudokusolver.solvers.BacktrackingSolver;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void removeValueWithObjectIsOk() {
        BacktrackingSolver.CellState state = new BacktrackingSolver.CellState();
        state.removeValue(8);
        for(int i = 0; i < state.valuesToTest.size(); i++) {
            System.out.print(state.valuesToTest.get(i));
        }
    }
}