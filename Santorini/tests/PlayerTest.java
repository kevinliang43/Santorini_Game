import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PlayerTest {
  private class MockStrategy implements Strategy {
    @Override
    public Action getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {
      return null;
    }
    @Override
    public void sendMessage(String message) {
    }
  }

  Player p1;
  Player p2;
  Board board;


  public PlayerTest() {

  }

  public void init() {
    //player that uses strat1 (diagonal)
    this.p1 = new Player("one", 0, new StayAliveStrategy(true, 2));
    //player that uses strat2 (furthest)
    this.p2 = new Player("two", 0, new StayAliveStrategy(false, 2));

    this.p1.addWorkerID(0);
    this.p1.addWorkerID(2);
    this.p2.addWorkerID(1);
    this.p2.addWorkerID(3);
    this.board = new Board();
  }
  @Test
  public void addWorkerIDTest() {
    Player p = new Player("player_name", 0, new MockStrategy());
    assertEquals(0, p.getWorkerIDs().size());
    p.addWorkerID(2);
    assertEquals(1, p.getWorkerIDs().size());
    int id = p.getWorkerIDs().get(0);
    assertEquals(2, id);
    p.addWorkerID(4);
    assertEquals(2, p.getWorkerIDs().size());
    id = p.getWorkerIDs().get(1);
    assertEquals(4, id);
    id = p.getWorkerIDs().get(0);
    assertEquals(2, id);
  }

  @Test //tests the get worker IDs function
  public void getWorkerIDTest() {
    init();
    ArrayList<Integer> p1Workers = new ArrayList<>();
    p1Workers.add(0);
    p1Workers.add(2);

    ArrayList<Integer> p2Workers = new ArrayList<>();
    p2Workers.add(1);
    p2Workers.add(3);

    assertEquals(p1Workers, p1.getWorkerIDs());
    assertEquals(p2Workers, p2.getWorkerIDs());

  }

  @Test // tests if the first players place worker function returns an Action placing a worker
        // in the top left corner, (0,0)
  public void checkPlaceWorkerRequestDiagonal1() {
    init();
    IAction action = p1.getNextAction(board, Status.PLACE);
    assertEquals(action instanceof Action, true);
    if (action instanceof  Action) {
      Action place = (Action)action;
      assertEquals(place.getX(), 0);
      assertEquals(place.getY(),  0);
      assertEquals(place.getWorkerID(), -1);
    }
  }

  @Test // tests if the second players place worker function returns an Action placing a worker
        // in the bottom right corner (5,5) if there is already a worker in the top (0,0)
  public void checkPlaceWorkerRequestFurthest2() {
    init();
    board.placeWorker(0,0, "one", 0);
    IAction action = p2.getNextAction(board, Status.PLACE);
    assertEquals(action instanceof Action, true);
    if (action instanceof  Action) {
      Action place = (Action)action;
      assertEquals(place.getX(), 5);
      assertEquals(place.getY(),  5);
      assertEquals(place.getWorkerID(), -1);
    }
  }

  @Test // tests if the first players place worker function returns an Action placing a worker
  // in (1,1) if there is already a worker in the top (0,0) and (5,5)
  public void checkPlaceWorkerRequestDiagonal3() {
    init();
    board.placeWorker(0,0, "one", 0);
    board.placeWorker(5,5, "two", 1);
    IAction action = p1.getNextAction(board, Status.PLACE);
    assertEquals(action instanceof Action, true);
    if (action instanceof  Action) {
      Action place = (Action)action;
      assertEquals(place.getX(), 1);
      assertEquals(place.getY(),  1);
      assertEquals(place.getWorkerID(), -1);
    }
  }

  @Test // tests if the second players place worker function returns an Action placing a worker
  // in (4,5) if there is already a worker in the top (0,0), (1,1) and (5,5)
  public void checkPlaceWorkerRequestDiagonal34() {
    init();
    board.placeWorker(0,0, "one", 0);
    board.placeWorker(5,5, "two", 1);
    board.placeWorker(1,1, "three", 2);
    IAction action = p2.getNextAction(board, Status.PLACE);
    assertEquals(action instanceof Action, true);
    if (action instanceof  Action) {
      Action place = (Action)action;
      assertEquals(place.getX(), 4);
      assertEquals(place.getY(),  5);
      assertEquals(place.getWorkerID(), -1);
    }
  }

  @Test //tests the getName function
  public void testGetName() {
    init();
    assertEquals(p1.getName(), "one");
    assertEquals(p2.getName(), "two");

  }


}

