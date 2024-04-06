package ca.csfoy.tp2_ac_ltn.mc;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

import org.junit.Before;
import org.junit.Test;

import ca.csfoy.tp2_ac_ltn.R;
import ca.csfoy.tp2_ac_ltn.views.GameActivity;

public class GoModelControllerTest {
    private GoModelController controller;
    private final int SIZE = 9;
 /*
    @Before
    public void setUp(){
        ImageButton[][] board = new ImageButton[SIZE][SIZE];
        ImageButton[][] previousBoard = new ImageButton[SIZE][SIZE];
        controller = new GoModelController(board, previousBoard);
    }
   @Test
    public void testOnClickCancelTurn() {
        if (controller == null) {

        }
        controller.getLastPlayedY();

        controller.onClickCancelTurn();
        assertEquals(2, GoModelController.getTokenState());
    }
    @Test
    public void testInitTable() {
       controller.initTable();

        // Add assertions to check if the initialization is done correctly
        // You can check specific cells based on their expected image resource and tag
        assertEquals(R.drawable.ul, (int) controller.getBoard()[0][0].getTag());
        assertEquals(R.drawable.dl, (int) controller.getBoard()[0][controller.getSIZE() - 1].getTag());
        assertEquals(R.drawable.ur, (int) controller.getBoard()[controller.getSIZE() - 1][0].getTag());
        assertEquals(R.drawable.dr, (int) controller.getBoard()[controller.getSIZE() - 1][controller.getSIZE() - 1].getTag());
        // Add more assertions for other cells based on your expected behavior

        // You can also check if the image resources are set correctly
        assertEquals(R.drawable.ul, getImageResourceForCell(controller.getBoard(), 0, 0));
        assertEquals(R.drawable.dl, getImageResourceForCell(controller.getBoard(), 0, controller.getSIZE() - 1));
        assertEquals(R.drawable.ur, getImageResourceForCell(controller.getBoard(), controller.getSIZE() - 1, 0));
        assertEquals(R.drawable.dr, getImageResourceForCell(controller.getBoard(), controller.getSIZE() - 1, controller.getSIZE() - 1));
    }
    @Test
    public void testDrawDot() {
        // Call the method you want to test
        int result = controller.drawDot(1, 1);

        // Add assertions to check if the method returns the expected result
        assertEquals(R.drawable.blackdot, result);
        assertEquals(R.drawable.blackdotsquare, (int) controller.getBoard()[1][1].getTag());
        // Add more assertions based on your expected behavior
    }
    private int getImageResourceForCell(ImageButton[][] board, int x, int y) {
        return (int) board[x][y].getTag();
    }

    @Test
    public void testSetTokenState() {
    }
    @Test
    public void testPlayTurn() {
    }
    @Test
    public void testOnClickBoard() {
    }*/
}
