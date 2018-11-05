import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BreakerStrategyTest {

    Board board;
    ArrayList<Integer> p1Workers;
    ArrayList<Integer> p2Workers;


    BreakerStrategy strat;

    @Before
    public void setup() {
        board = new Board();

        p1Workers = new ArrayList<>();

        p2Workers = new ArrayList<>();

        strat = new BreakerStrategy();
    }

    @Test
    //tests the placement of the first worker, should be (0,0)
    public void testPlacement() {
        setup();
        IAction place = strat.getNextAction(new BoardStatus(board, Status.PLACE), p1Workers);
        Action placeAction = (Action) place;
        assertEquals(placeAction.x, 0);
        assertEquals(placeAction.y, 0);
    }

    @Test
    //tests the placement of the second worker, should be (1,1)
    public void testSecondPlacement() {
        setup();
        this.board.placeWorker(0,0,"one1", 0);
        IAction place = strat.getNextAction(new BoardStatus(board, Status.PLACE), p2Workers);
        Action placeAction = (Action) place;
        assertEquals(placeAction.x, 1);
        assertEquals(placeAction.y, 1);
    }

    @Test
    //tests the placement of the second worker, should be (1,1)
    public void testMoveBuildPlacement() {
        setup();
        this.board.placeWorker(0,0,"one1", 0);
        this.board.placeWorker(1,1,"two1", 1);
        this.board.placeWorker(2,2,"one2", 2);
        this.board.placeWorker(3,3,"two1", 3);
        p1Workers.add(0);
        p1Workers.add(2);
        IAction place = strat.getNextAction(new BoardStatus(board, Status.MOVEBUILD), p1Workers);
        MoveBuild moveBuild = (MoveBuild) place;
        assertEquals(moveBuild.xMove, -1);
        assertEquals(moveBuild.yMove, -1);
        assertEquals(moveBuild.xBuild, -1);
        assertEquals(moveBuild.yBuild, -1);
    }

}
