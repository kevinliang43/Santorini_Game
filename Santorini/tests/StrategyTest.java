import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StrategyTest {

  Board board;
  ArrayList<Integer> p1Workers;
  ArrayList<Integer> p2Workers;

  int worker11;
  int worker12;
  int worker21;
  int worker22;

  StayAliveStrategy strat;

  public StrategyTest() {

  }

  @Before
  public void setup() {
    board = new Board();

    worker11 = board.placeWorker(2, 3, "one1", 0);
    worker21 = board.placeWorker(4, 5, "two1", 1);
    worker12 = board.placeWorker(0,5, "one2", 2);
    worker22 = board.placeWorker(5,0, "two2", 3);

    p1Workers = new ArrayList<>();
    p1Workers.add(worker11);
    p1Workers.add(worker12);

    p2Workers = new ArrayList<>();
    p2Workers.add(worker21);
    p2Workers.add(worker22);

    strat = new StayAliveStrategy(true, 1);
  }


  // Test strategy works for up to 3 look aheads
  @Test
  public void testStrategyStayAliveUpToThree(){

    Action move = new Action(Status.MOVE, worker11, 3,3, "one1");
    Action build = new Action(Status.BUILD, worker11, 4, 4, "one1");

    MoveBuild moveBuild = new MoveBuild(move, build);
    Board actB = RuleChecker.performAction(board, moveBuild);

    assertTrue(strat.canStayAlive(actB, p1Workers, p2Workers, 0) != null);
    assertTrue(strat.canStayAlive(actB, p1Workers, p2Workers, 1) != null);
    assertTrue(strat.canStayAlive(actB, p1Workers, p2Workers, 2) != null);
    assertTrue(strat.canStayAlive(actB, p1Workers, p2Workers, 3) != null);

  }

  // Test case where we cannot stay alive
  @Test
  public void testFailure() {
    assertTrue(strat.canStayAlive(board, p1Workers, p2Workers, 2) != null);

    board.setFloor(5, 0, 2);
    board.setFloor(5, 1, 3);

    assertTrue(strat.canOpWin(board, p2Workers, p1Workers, 1));
    assertTrue(strat.canStayAlive(board, p1Workers, p2Workers, 2) == null);

  }
//
//  // Test placements with no workers already on the diagonal
//  @Test
//  public void testPlacementDiag() {
//    Board newBoard = new Board();
//    Strategy diag = new StayAliveStrategy(true);
//    for (int i = 0; i < 4; i++) {
//      IAction place = diag.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//      assertEquals(i, place.x);
//      assertEquals(i, place.y);
//      newBoard.placeWorker(place.x, place.y);
//    }
//  }

//  // Test placements with workers already on the diagonal
//  @Test
//  public void testPlacementDiagObstacle() {
//    Board newBoard = new Board();
//    ArrayList<Integer> placedObs = new ArrayList<>();
//    placedObs.add(0);
//    placedObs.add(2);
//    newBoard.placeWorker(placedObs.get(0), placedObs.get(0));
//    newBoard.placeWorker(placedObs.get(1), placedObs.get(1));
//    Strategy diag = new StayAliveStrategy(true);
//    for (int i = 0; i < 4; i++) {
//      Action place = diag.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//      int actualPos = i;
//      while (placedObs.contains(actualPos)) {
//        actualPos++;
//      }
//      assertEquals(actualPos, place.x);
//      assertEquals(actualPos, place.y);
//      newBoard.placeWorker(place.x, place.y);
//      placedObs.add(actualPos);
//    }
//  }
//
//  // Basic test for furthest placement strategy
//  @Test
//  public void testPlacementFurthest() {
//    Board newBoard = new Board();
//    ArrayList<Integer> placedIDs = new ArrayList<>();
//    Strategy furthest = new StayAliveStrategy(false);
//    Action place = furthest.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(0, 0, place);
//    placedIDs.add(newBoard.placeWorker(place.x, place.y));
//
//    place = furthest.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(5, 5, place);
//    placedIDs.add(newBoard.placeWorker(place.x, place.y));
//
//    place = furthest.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(0, 5, place);
//    placedIDs.add(newBoard.placeWorker(place.x, place.y));
//
//    place = furthest.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(5, 0, place);
//    placedIDs.add(newBoard.placeWorker(place.x, place.y));
//
//    place = furthest.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(0, 1, place);
//    placedIDs.add(newBoard.placeWorker(place.x, place.y));
//  }
//
//  // Expect invalid x,y placement on diagonal placement
//  @Test
//  public void noValidPlacementDiag() {
//    Board newBoard = new Board();
//    for (int x = 0; x < Board.BOARD_X; x++) {
//      for (int y = 0; y < Board.BOARD_Y; y++) {
//        newBoard.placeWorker(x, y);
//      }
//    }
//
//    Strategy diag = new StayAliveStrategy(true);
//    Action place = diag.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(-1, -1, place);
//  }
//
//  // Expect invalid x,y if diagonal is filled
//  @Test
//  public void noValidPlacementDiagJustDiag() {
//    Board newBoard = new Board();
//    for (int x = 0; x < Board.BOARD_X; x++) {
//        newBoard.placeWorker(x, x);
//    }
//
//    Strategy diag = new StayAliveStrategy(true);
//    Action place = diag.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(-1, -1, place);
//  }
//
//  // Expect invalid x,y placement on diagonal placement
//  @Test
//  public void noValidPlacementFurthest() {
//    Board newBoard = new Board();
//    for (int x = 0; x < Board.BOARD_X; x++) {
//      for (int y = 0; y < Board.BOARD_Y; y++) {
//        newBoard.placeWorker(x, y);
//      }
//    }
//
//    Strategy diag = new StayAliveStrategy(false);
//    Action place = diag.getNextAction(new BoardStatus(newBoard, Status.PLACE), new ArrayList<>());
//    assertPlaceEquals(-1, -1, place);
//  }

}
