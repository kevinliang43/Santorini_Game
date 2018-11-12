import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testRuleChecker {

//  RuleChecker r = new RuleChecker();

  public testRuleChecker() {

  }

  @Test //test that a worker can be placed on an empty board,
        // that a worker can't be placed on top of another worker,
        // and that there can only be 4 workers on a board at a time
  public void testPlaceLegal() {
                  //board, x, y
    //empty board
    assertTrue(RuleChecker.isPlaceLegal(new Board(), 0, 0));
    assertTrue(RuleChecker.isPlaceLegal(new Board(), 5, 5));
    assertFalse(RuleChecker.isPlaceLegal(new Board(), 3, 6));
    assertFalse(RuleChecker.isPlaceLegal(new Board(), -1, 4));

    Board b = new Board();
    assertTrue(RuleChecker.isPlaceLegal(b, 0, 0));
    b.placeWorker(0, 0, "one1", 0);
    assertFalse(RuleChecker.isPlaceLegal(b, 0, 0));
    assertTrue(RuleChecker.isPlaceLegal(b, 1, 1));
    b.placeWorker(1, 1, "two1", 1);
    assertTrue(RuleChecker.isPlaceLegal(b, 2, 2));
    b.placeWorker(2, 2, "one2", 2);
    assertFalse(RuleChecker.isPlaceLegal(b, -1, -1));
    assertTrue(RuleChecker.isPlaceLegal(b, 3, 3));
    b.placeWorker(3, 3, "two2", 3);
    assertFalse(RuleChecker.isPlaceLegal(b, 3, 3));
    assertTrue(RuleChecker.isPlaceLegal(b, 4, 4));

    //restart board
    b.initBoard();
    //can place workers on fresh board
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        assertTrue(RuleChecker.isPlaceLegal(b, i, j));
      }
    }

  }

  @Test // test that a move is legal when it is to a neighboring square,
        // it is to an unoccupied square,
        // and a worker never moves up more than one floor
  public void testMoveLegal() {
    Board b = new Board();
    int id = b.placeWorker(0, 1, "one", 0);
                  // Board, workerID, x, y
    assertTrue(RuleChecker.isMoveLegal(b, id, 0, 0));
    //can't move to its own square
    assertFalse(RuleChecker.isMoveLegal(b, id, 0, 1));
    //can't move off the board
    assertFalse(RuleChecker.isMoveLegal(b, id, -1, 1));
    assertFalse(RuleChecker.isMoveLegal(b, id, -1, 0));
    assertFalse(RuleChecker.isMoveLegal(b, id, -1, 2));
    //can't move far away
    assertFalse(RuleChecker.isMoveLegal(b, id, 5, 5));

    assertTrue(RuleChecker.isMoveLegal(b, id, 1, 2));
    int id2 = b.placeWorker(1, 2, "two", 1);
    //can't move to occupied square
    assertFalse(RuleChecker.isMoveLegal(b, id, 1, 2));

    //Can move up one floor
    b.setFloor(1, 1, 1);
    assertTrue(RuleChecker.isMoveLegal(b, id, 1, 1));
    b.setFloor(1, 1, 2);
    assertFalse(RuleChecker.isMoveLegal(b, id, 1, 1));
    b.setFloor(1, 1, 3);
    assertFalse(RuleChecker.isMoveLegal(b, id, 1, 1));

    b.setFloor(2, 1, 1);
    assertTrue(RuleChecker.isMoveLegal(b, id2, 2, 1));
    b.moveWorker(id2, 2, 1);
    assertFalse(RuleChecker.isMoveLegal(b, id2, 1, 1));
    b.setFloor(1, 1, 2);
    assertTrue(RuleChecker.isMoveLegal(b, id2, 1, 1));
    b.moveWorker(id2, 1, 1);
    b.setFloor(2,2, 3);
    assertTrue(RuleChecker.isMoveLegal(b, id2, 2, 2));

  }

  @Test //test that a build is legal when it is performed by the same worker that just moved,
        // the worker is neighboring the building,
        // and the building doesn't have 4 floors already
  public void testBuildLegal() {

    //Need to do some refactoring for the first point

  }

  @Test
  public void testLegalMoves() {

    Board board = new Board();
    int worker11 = board.placeWorker(3, 3, "one", 0);
    ArrayList<Integer> workers = new ArrayList<>();
    workers.add(worker11);

    assertEquals(RuleChecker.listOfLegalMoves(board, workers).size(), 64);

    for(int x = 0; x < 6; x++) {
      for(int y = 0; y < 6; y++) {
        board.setFloor(x, y, 4);
      }
    }
    board.setFloor(3, 3, 2);

    assertEquals(RuleChecker.listOfLegalMoves(board, workers).size(), 0);

    board.setFloor(3, 4, 2);

    assertEquals(RuleChecker.listOfLegalMoves(board, workers).size(), 1);

  }

  @Test
  public void testLegalMoves2Workers() {

    Board board = new Board();
    int worker11 = board.placeWorker(2, 2, "one1", 0);
    int worker12 = board.placeWorker(3, 3, "one2", 2);
    ArrayList<Integer> workers = new ArrayList<>();
    workers.add(worker11);
    workers.add(worker12);

    ArrayList<MoveBuild> legals = RuleChecker.listOfLegalMoves(board, workers);
    assertEquals(legals.size(), 108);
    for(int i = 0; i < legals.size(); i++){
      if(i < legals.size()/2) {
        assertEquals(legals.get(i).getWorkerID(), worker11);
      } else {
        assertEquals(legals.get(i).getWorkerID(), worker12);
      }
    }

    for(int x = 0; x < 6; x++) {
      for(int y = 0; y < 6; y++) {
        board.setFloor(x, y, 4);
      }
    }

  }


  @Test //test that a game is over only when a worker has made it to the third floor,
        //a player's worker can't jump to a two story or shorter building, or
        //a player has no legal moves
  public void testGameOver() {
    //test build legal first

    //win case
    Board board = new Board();

    int worker11 = board.placeWorker(2, 3, "one1", 0);
    int worker21 = board.placeWorker(4, 5, "two1", 1);
    int worker12 = board.placeWorker(0,5, "one2", 2);
    int worker22 = board.placeWorker(5,0, "two2", 3);

    ArrayList<Integer> p1Workers = new ArrayList<>();
    p1Workers.add(worker11);
    p1Workers.add(worker12);

    ArrayList<Integer> p2Workers = new ArrayList<>();
    p2Workers.add(worker21);
    p2Workers.add(worker22);

//    ArrayList<Integer>

//    Action move = new Action(Status.MOVE, worker11, 3,3);
//    Action build = new Action(Status.BUILD, worker11, 4, 4);
//
//    MoveBuild moveBuild = new MoveBuild(move, build);
    assertEquals(GameOverStatus.NOT_OVER, RuleChecker.isGameOver(board, p2Workers));

    board.setFloor(5, 0, 3);
//    board.setFloor(5, 1, 2);
//    board.setFloor(5, 2, 3);

    assertEquals(GameOverStatus.WINNING_FLOOR, RuleChecker.isGameOver(board, p2Workers));
  }


}
