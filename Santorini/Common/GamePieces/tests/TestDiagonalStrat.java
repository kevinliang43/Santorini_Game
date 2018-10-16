import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Test class to test the Diagonal Strategy Placement
public class TestDiagonalStrat {
    DiagonalStrat strat;
    Board board;
    String p1;
    String p2;
    String w1;
    String w2;
    String w3;
    String w4;
    HashMap<String, List<String>> playerWorkers;



    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("TestDiagonalStrat");

    }

    public void init() {
        //initializes an empty board
        this.board = new Board(6,6);
        //initializes 2 player string IDs, that's all the strategy will need
        this.p1 = "one";
        this.p2 = "two";
        //initializes 4 worker IDs, 2 for each player
        this.w1 = "one1";
        this.w2 = "one2";
        this.w3 = "two1";
        this.w4 = "two2";

        List<String> oneWorkers = new ArrayList<>();
        List<String> twoWorkers = new ArrayList<>();

        oneWorkers.add(this.w1);
        oneWorkers.add(this.w2);
        twoWorkers.add(this.w3);
        twoWorkers.add(this.w4);

        //adds the player id with the list of their worker id onto the map
        this.playerWorkers = new HashMap<>();
        this.playerWorkers.put(this.p1, oneWorkers);
        this.playerWorkers.put(this.p2, twoWorkers);

        this.strat = new DiagonalStrat(5);
    }

    //Test to check that w1 is not instantiated on the board yet
    @Test(expected = IllegalArgumentException.class)
    public void testInitialization1() {
        init();
        board.getWorker(this.w1);
    }
    //Test to check that w2 is not instantiated on the board yet
    @Test(expected = IllegalArgumentException.class)
    public void testInitialization2() {
        init();
        board.getWorker(this.w2);
    }
    //Test to check that w3 is not instantiated on the board yet
    @Test(expected = IllegalArgumentException.class)
    public void testInitialization3() {
        init();
        board.getWorker(this.w3);
    }
    //Test to check that w4 is not instantiated on the board yet
    @Test(expected = IllegalArgumentException.class)
    public void testInitialization4() {
        init();
        board.getWorker(this.w4);
    }

    //Tests adding the first worker onto the board, should be in top left corner
    @Test
    public void testPlaceFirstWorker() {
        init();
        String expected = "[\"add\", 0, 0]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }

    //Tests adding the second worker onto the board if (0,0) is taken, should be in (1,1)
    @Test
    public void testPlaceSecondWorker() {
        init();
        this.board.addWorker(0,0, this.w1);
        String expected = "[\"add\", 1, 1]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }

    //Tests adding the second worker onto the board if (0,0) is NOT taken, should be in (1,1)
    @Test
    public void testPlaceSecondWorker2() {
        init();
        this.board.addWorker(1,0, this.w1);
        String expected = "[\"add\", 0, 0]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }

    //Tests adding the second worker onto the board if (0,0) and (1,1) are taken, should be in (2,2)
    @Test
    public void testPlaceThirdWorker() {
        init();
        this.board.addWorker(0,0, this.w1);
        this.board.addWorker(1,1, this.w3);
        String expected = "[\"add\", 2, 2]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }

    //Tests adding the second worker onto the board if (0,0) is taken but (1,1) is NOT, should be in (1,1)
    @Test
    public void testPlaceThirdWorker2() {
        init();
        this.board.addWorker(0,0, this.w1);
        this.board.addWorker(1,2, this.w3);
        String expected = "[\"add\", 1, 1]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }

    //Tests adding the second worker onto the board if (0,0), (1,1), and (2,2) are NOT taken, should be in (3,3)
    @Test
    public void testPlaceLastWorker() {
        init();
        this.board.addWorker(0,0, this.w1);
        this.board.addWorker(1,1, this.w3);
        this.board.addWorker(2,2, this.w2);
        String expected = "[\"add\", 3, 3]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }

    //Tests adding the second worker onto the board if (0,0), (1,1) are taken but (2,2) is NOT taken, should be in (2,2)
    @Test
    public void testPlaceLastWorker2() {
        init();
        this.board.addWorker(0,0, this.w1);
        this.board.addWorker(1,1, this.w3);
        this.board.addWorker(3,2, this.w2);
        String expected = "[\"add\", 2, 2]";
        String request = strat.placeWorker(this.board, playerWorkers);
        Assert.assertEquals(request, expected);
    }
}

