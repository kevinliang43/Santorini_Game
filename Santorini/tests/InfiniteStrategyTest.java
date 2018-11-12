import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class InfiniteStrategyTest {

    Board board;
    ArrayList<Integer> p1Workers;
    ArrayList<Integer> p2Workers;


    InfiniteStrategy strat;

    @Before
    public void setup() {
        board = new Board();

        p1Workers = new ArrayList<>();

        p2Workers = new ArrayList<>();

        strat = new InfiniteStrategy();
    }

    @Test
    //tests the placement of the first worker, should be (0,0)
    public void testPlacement() {
        setup();
        IAction place = strat.getNextAction(new BoardStatus(board, Status.PLACE), p1Workers);
        Action placeAction = (Action) place;
        assertEquals(placeAction.getX(), 0);
        assertEquals(placeAction.getY(), 0);
    }

    @Test
    //tests the placement of the second worker, should be (1,1)
    public void testSecondPlacement() {
        setup();
        this.board.placeWorker(0,0,"one1", 0);
        IAction place = strat.getNextAction(new BoardStatus(board, Status.PLACE), p2Workers);
        Action placeAction = (Action) place;
        assertEquals(placeAction.getX(), 1);
        assertEquals(placeAction.getY(), 1);
    }

}

