import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Test class to test the Diagonal Strategy Placement
public class TestFurthestStrat {
    FurthestStrat p1Strat;
    FurthestStrat p2Strat;
    Board board;
    String p1;
    String p2;
    String w1;
    String w2;
    String w3;
    String w4;
    HashMap<String, List<String>> playerWorkers;



    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("TestFurthestStrat");

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

//        List<String> oneWorkers = new ArrayList<>();
//        List<String> twoWorkers = new ArrayList<>();
//
//        oneWorkers.add(this.w1);
//        oneWorkers.add(this.w2);
//        twoWorkers.add(this.w3);
//        twoWorkers.add(this.w4);

        //adds the player id with the list of their worker id onto the map
        this.playerWorkers = new HashMap<>();
//        this.playerWorkers.put(this.p1, oneWorkers);
//        this.playerWorkers.put(this.p2, twoWorkers);

        this.p1Strat = new FurthestStrat(this.p1, 3);
        this.p2Strat = new FurthestStrat(this.p2, 3);
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
        String request = p1Strat.placeWorker(this.board, this.playerWorkers);
        Assert.assertEquals(expected, request);
    }

    //Tests adding the second worker onto the board when first worker is at (0,0), should be in bottom right corner
    @Test
    public void testPlaceSecondWorker() {
        init();
        this.board.addWorker(0,0,this.w1);
        List<String> p1Workers = new ArrayList<>();
        p1Workers.add(this.w1);
        this.playerWorkers.put(this.p1, p1Workers);
        String expected = "[\"add\", 5, 5]";
        String request = p2Strat.placeWorker(this.board, this.playerWorkers);
        Assert.assertEquals(expected, request);
    }

    //Tests adding the third (p1) worker onto the board, should be in bottom left corner
    //when p2 has a worker in the top right corner
    @Test
    public void testPlaceThirdWorker() {
        init();
        this.board.addWorker(0,0,this.w1);
        this.board.addWorker(0, 5, this.w3);
        List<String> p1Workers = new ArrayList<>();
        p1Workers.add(this.w1);
        this.playerWorkers.put(this.p1, p1Workers);
        List<String> p2Workers = new ArrayList<>();
        p2Workers.add(this.w3);
        this.playerWorkers.put(this.p2, p2Workers);
        String expected = "[\"add\", 5, 0]";
        String request = p1Strat.placeWorker(this.board, this.playerWorkers);
        Assert.assertEquals(expected, request);
    }

    //Tests adding the third (p1) worker onto the board, should be top left corner
    //when p2 has a worker in (3,3) in the middle of the board
    @Test
    public void testPlaceThirdWorker2() {
        init();
        this.board.addWorker(0,0,this.w1);
        this.board.addWorker(3, 3, this.w3);
        List<String> p1Workers = new ArrayList<>();
        p1Workers.add(this.w1);
        this.playerWorkers.put(this.p1, p1Workers);
        List<String> p2Workers = new ArrayList<>();
        p2Workers.add(this.w3);
        this.playerWorkers.put(this.p2, p2Workers);
        String expected = "[\"add\", 0, 0]";
        String request = p1Strat.placeWorker(this.board, this.playerWorkers);
        Assert.assertEquals(expected, request);
    }

    //Tests adding the fourth (p2) worker onto the board, should be in (5,2)
    //when p1 has workers in the top two corners
    @Test
    public void testPlaceFourthWorker() {
        init();
        this.board.addWorker(0,0,this.w1);
        this.board.addWorker(5, 5, this.w3);
        this.board.addWorker(0, 5, this.w2);
        List<String> p1Workers = new ArrayList<>();
        p1Workers.add(this.w1);
        p1Workers.add(this.w2);
        this.playerWorkers.put(this.p1, p1Workers);
        List<String> p2Workers = new ArrayList<>();
        p2Workers.add(this.w3);
        this.playerWorkers.put(this.p2, p2Workers);
        String expected = "[\"add\", 5, 2]";
        String request = p2Strat.placeWorker(this.board, this.playerWorkers);
        Assert.assertEquals(expected, request);
    }

    //Tests adding the fourth (p2) worker onto the board, should be in (5,2)
    //when p1 has workers in the top two corners
    @Test
    public void testPlaceFourthWorker2() {
        init();
        this.board.addWorker(2,2,this.w1);
        this.board.addWorker(0, 5, this.w3);
        this.board.addWorker(4,1, this.w2);
        List<String> p1Workers = new ArrayList<>();
        p1Workers.add(this.w1);
        p1Workers.add(this.w2);
        this.playerWorkers.put(this.p1, p1Workers);
        List<String> p2Workers = new ArrayList<>();
        p2Workers.add(this.w3);
        this.playerWorkers.put(this.p2, p2Workers);
        String expected = "[\"add\", 5, 5]";
        String request = p2Strat.placeWorker(this.board, this.playerWorkers);
        Assert.assertEquals(expected, request);
    }

}

