import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Translator Class
 */
public class TranslatorTest {
  Board board;
  ArrayList<Integer> p1Workers;
  ArrayList<Integer> p2Workers;

  int worker11;
  int worker12;
  int worker21;
  int worker22;

  StayAliveStrategy strat;

  @Before
  public void setup() {
    board = new Board();

    worker11 = board.placeWorker(2, 3, "one1");
    worker21 = board.placeWorker(4, 5, "two1");
    worker12 = board.placeWorker(0,5, "one2");
    worker22 = board.placeWorker(5,0, "two2");

    p1Workers = new ArrayList<>();
    p1Workers.add(worker11);
    p1Workers.add(worker12);

    p2Workers = new ArrayList<>();
    p2Workers.add(worker21);
    p2Workers.add(worker22);

    strat = new StayAliveStrategy(true, 1);
  }

  @Test
  public void testMoveBuild() {
    setup();

    Action move = new Action(Status.MOVE, worker11, 3,3, "one1");
    Action build = new Action(Status.BUILD, worker11, 4, 4, "one1");

    MoveBuild moveBuild = new MoveBuild(move, build);

    String expected = "[\"one1\", \"PUT\", \"SOUTH\", \"EAST\", \"SOUTH\"]";
    assertEquals(Translator.moveBuildAsJSON(board, moveBuild), expected);
    move = new Action(Status.MOVE, worker11, 3, 4, "one1");
    build = new Action(Status.BUILD, worker11, 2, 3, "one1");
    moveBuild = new MoveBuild(move, build);
    expected = "[\"one1\", \"EAST\", \"SOUTH\", \"WEST\", \"NORTH\"]";
    assertEquals(Translator.moveBuildAsJSON(board, moveBuild), expected);


  }
}
