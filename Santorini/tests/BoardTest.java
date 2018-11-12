import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BoardTest {

  private class Posn {
    public int x, y;
    public Posn(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  Board b;
  ArrayList<Posn> ignoreSquare;

  public BoardTest() {

  }

  @Before
  public void setupTest() {
    b = new Board();
    ignoreSquare = new ArrayList<>();
  }

  // Ensure board has all heights initialized to 0
  @Test
  public void initHeightTest() {
    heightsEqualZero();
  }

  @Test(expected = IllegalArgumentException.class)
  public void getWorkerSquareInvalidID() {
    b.getWorkerSquare(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getWorkerSquareWithWorkerInvalidID() {
    b.placeWorker(0, 0, "one", 0);
    b.getWorkerSquare(Board.INVALID_WORKER_ID);
  }

  // Placing worker basic tests
  @Test
  public void placeWorkerTest() {
    int workerID1 = b.placeWorker(0, 0, "one", 0);
    checkWorkerPosition(workerID1, 0, 0);
    heightsEqualZero();

    int workerID2 = b.placeWorker(2, 4, "two", 1);
    checkWorkerPosition(workerID2, 2, 4);
    checkWorkerPosition(workerID1, 0, 0);
    heightsEqualZero();
  }

  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerNegX() {
    b.placeWorker(-1, 0, "one", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerNegY() {
    b.placeWorker(0, -1, "two", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerBigX() {
    b.placeWorker(Board.BOARD_X, 0, "one", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerBigY() {
    b.placeWorker(0, Board.BOARD_Y, "one", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerSameSquare() {
    b.placeWorker(0, 0, "one", 0);
    b.placeWorker(0, 0, "two", 1);
  }

  // Verify the board IDs and returned IDs from placing workers match-up
  @Test
  public void getWorkerIDs() {
    ArrayList<Integer> workerIDs = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      String name = "one" + i;
      int id = b.placeWorker(i, 0, name, i);
      assertEquals(false, workerIDs.contains(id));
      workerIDs.add(id);
    }

    ArrayList<Integer> boardWorkerIDs = b.getWorkerIDs();
    assertEquals(workerIDs.size(), boardWorkerIDs.size());

    for (int id : workerIDs) {
      assertEquals(true, boardWorkerIDs.contains(id));
    }
  }

  // Moving worker basic tests
  @Test
  public void moveWorkerTest() {
    int workerID1 = b.placeWorker(1, 3, "one", 0);
    checkWorkerPosition(workerID1, 1, 3);

    int workerID2 = b.placeWorker(5, 2, "two", 1);
    checkWorkerPosition(workerID2, 5, 2);

    b.moveWorker(workerID1, 4, 3);
    checkWorkerPosition(workerID1, 4, 3);
    checkWorkerPosition(workerID2, 5, 2);
    heightsEqualZero();
  }

  // Test Square worker came from is no longer occupied
  @Test
  public void moveWorkerOccupiedTest() {
    int workerID1 = b.placeWorker(1, 3, "one", 0);
    checkWorkerPosition(workerID1, 1, 3);
    b.moveWorker(workerID1, 1, 4);
    assertEquals(false, b.isOccupied(1, 3));
    assertEquals(true, b.isOccupied(1, 4));
    b.moveWorker(workerID1, 0, 3);
    assertEquals(false, b.isOccupied(1, 4));
    assertEquals(true, b.isOccupied(0, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerNegX() {
    int workerID = b.placeWorker(0, 0, "one", 1);
    b.moveWorker(workerID, -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerNegY() {
    int workerID = b.placeWorker(0, 0, "one", 0);
    b.moveWorker(workerID, 0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerBigX() {
    int workerID = b.placeWorker(0, 0, "one", 1);
    b.moveWorker(workerID, Board.BOARD_X, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerBigY() {
    int workerID = b.placeWorker(0, 0, "two", 1);
    b.moveWorker(workerID, 0, Board.BOARD_Y);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerInvalidID() {
    b.moveWorker(0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerConstInvalidID() {
    b.placeWorker(0, 0, "two", 0);
    b.moveWorker(Board.INVALID_WORKER_ID, 0, 0);
  }

  // Building floor basic tests
  @Test
  public void buildFloorTest() {
    heightsEqualZero();
    b.buildFloor(0, 0);
    assertEquals(1, b.getFloorHeight(0, 0));

    // Verify all other heights are 0
    ignoreSquare.add(new Posn(0, 0));
    heightsEqualZero();

    b.buildFloor(0, 0);
    b.buildFloor(2, 5);
    assertEquals(2, b.getFloorHeight(0, 0));
    assertEquals(1, b.getFloorHeight(2, 5));

    ignoreSquare.add(new Posn(2, 5));
    heightsEqualZero();
  }

  @Test
  public void buildFloorValidHeightTest() {
    b.buildFloor(0, 0);
    assertEquals(1, b.getFloorHeight(0, 0));
    b.buildFloor(0, 0);
    assertEquals(2, b.getFloorHeight(0, 0));
    b.buildFloor(0, 0);
    assertEquals(3, b.getFloorHeight(0, 0));
    b.buildFloor(0, 0);
    assertEquals(4, b.getFloorHeight(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildFloorNegX() {
    b.buildFloor(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildFloorNegY() {
    b.buildFloor(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildFloorBigX() {
    b.buildFloor(Board.BOARD_X, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildFloorBigY() {
    b.buildFloor(0, Board.BOARD_Y);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildFloorMaxHeight() {
    b.buildFloor(0, 0);
    b.buildFloor(0, 0);
    b.buildFloor(0, 0);
    b.buildFloor(0, 0);
    b.buildFloor(0, 0);
  }

  @Test
  public void setFloorTest() {
    heightsEqualZero();
    b.setFloor(0, 0, 2);
    assertEquals(2, b.getFloorHeight(0, 0));
    ignoreSquare.add(new Posn(0, 0));
    heightsEqualZero();

    b.setFloor(3, 4, Square.MAX_BUILDING_HEIGHT - 1);
    assertEquals(Square.MAX_BUILDING_HEIGHT - 1, b.getFloorHeight(3, 4));
    assertEquals(2, b.getFloorHeight(0, 0));
    ignoreSquare.add(new Posn(3, 4));
    heightsEqualZero();
  }

  @Test(expected = IllegalArgumentException.class)
  public void setFloorBeyondMaxHeight() {
    b.setFloor(0, 0, Square.MAX_BUILDING_HEIGHT + 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setFloorNegX() {
    b.setFloor(-1, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setFloorNegY() {
    b.setFloor(0, -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setFloorBigX() {
    b.setFloor(Board.BOARD_X, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setFloorBigY() {
    b.setFloor(0, Board.BOARD_Y, 0);
  }

  @Test
  public void isOccupiedTest() {
    assertEquals(false, b.isOccupied(2, 3));
    b.placeWorker(2, 3, "two", 0);
    assertEquals(true, b.isOccupied(2, 3));
    assertEquals(false, b.isOccupied(2, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isOccupiedNegX() {
    b.isOccupied(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void isOccupiedNegY() {
    b.isOccupied(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void isOccupiedBigX() {
    b.isOccupied(Board.BOARD_X, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void isOccupiedBigY() {
    b.isOccupied(0, Board.BOARD_Y);
  }

  @Test
  public void neighborsTest() {
    assertEquals(true, b.neighboring(2, 3, 3, 4));
    assertEquals(true, b.neighboring(2, 3, 2, 4));
    assertEquals(true, b.neighboring(2, 3, 2, 2));
    assertEquals(true, b.neighboring(2, 3, 3, 3));
    assertEquals(true, b.neighboring(2, 3, 1, 3));
    assertEquals(true, b.neighboring(2, 3, 1, 2));
    assertEquals(true, b.neighboring(2, 3, 1, 4));
    assertEquals(true, b.neighboring(2, 3, 3, 2));
    assertEquals(false, b.neighboring(2, 3, 2, 3));
    assertEquals(false, b.neighboring(2, 3, 0, 3));
    assertEquals(false, b.neighboring(2, 3, 1, 5));
    assertEquals(false, b.neighboring(2, 3, 3, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestNegX1() {
    b.neighboring(-1, 0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestNegY1() {
    b.neighboring(0, -1, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestNegX2() {
    b.neighboring(0, 0, -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestNegY2() {
    b.neighboring(0, 0, 0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestBigX1() {
    b.neighboring(Board.BOARD_X, 0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestBigY1() {
    b.neighboring(0, Board.BOARD_Y, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestBigX2() {
    b.neighboring(0, 0, Board.BOARD_X, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void neighborsTestBigY2() {
    b.neighboring(0, 0, 0, Board.BOARD_Y);
  }

  @Test
  public void testAsJSONArray() {
    Board b = new Board();
    b.placeWorker(2, 3, "two1", 1);
    b.placeWorker(0, 0, "one1", 0);
    String expected = "[[\"0one1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,\"0two1\",0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]";
    assertEquals(b.asJSONArray(), expected);
  }



  // Verifies the Board has only heights of 0. Ignores positions in ignoreSquare.
  private void heightsEqualZero() {
    for (int x = 0; x < Board.BOARD_X; x++) {
      for (int y = 0; y < Board.BOARD_Y; y++) {

        // Ignore values in ignoreSquare
        boolean ignore = false;
        for (Posn p : ignoreSquare) {
          if (p.x == x && p.y == y) {
            ignore = true;
          }
        }
        if (ignore) continue;

        assertEquals(0, b.getFloorHeight(x, y));
      }
    }
  }

  // Verifies a workers position on the Board
  private void checkWorkerPosition(int workerID, int expX, int expY) {
    Square s = b.getWorkerSquare(workerID);
    assertEquals(expX, s.getX());
    assertEquals(expY, s.getY());
  }




}
