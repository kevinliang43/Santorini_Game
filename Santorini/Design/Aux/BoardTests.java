import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class BoardTests {
  Board b;

  // Basic Board setup before each test
  @Before
  public void initBoard() {
    b = new BoardImpl();
    b.initBoard();
  }

  // Check that each square starts at height 0
  @Test
  public void getFloorHeightTest() {
    for (int x = 0; x < 6; x++) {
      for (int y = 0; y < 6; y++) {
        assertTrue(b.getFloorHeight(x, y) == 0);
      }
    }
  }

  // Test exception when x is negative in getFloorHeight
  @Test(expected = IllegalArgumentException.class)
  public void getFloorNegXTest() {
    b.getFloorHeight(-1, 0);
  }

  // Test exception when y is negative in getFloorHeight
  @Test(expected = IllegalArgumentException.class)
  public void getFloorNegYTest() {
    b.getFloorHeight(0, -1);
  }

  // Test exception when x is greater than board bounds in getFloorHeight
  @Test(expected = IllegalArgumentException.class)
  public void getFloorXPosBoundTest() {
    b.getFloorHeight(6, 0);
  }

  // Test exception when y is greater than board bounds in getFloorHeight
  @Test(expected = IllegalArgumentException.class)
  public void getFloorYPosBoundTest() {
    b.getFloorHeight(0, 6);
  }

  // Test building a single floor
  @Test
  public void buildOneFloorTest() {
    assertTrue(b.getFloorHeight(0, 0) == 0);
    assertTrue(b.getFloorHeight(1, 1) == 0);
    b.buildFloor(1, 1);

    // Simple check for at least one other floor not getting height added
    assertTrue(b.getFloorHeight(0, 0) == 0);

    // Check that (1, 1) increased floor height
    assertTrue(b.getFloorHeight(1, 1) == 1);
  }

  // Test building up to four floors on the same square
  @Test
  public void buildFourFloorTest() {
    assertTrue(b.getFloorHeight(3, 3) == 0);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 1);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 2);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 3);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 4);
  }

  // Test building more than four floors
  @Test(expected = IllegalArgumentException.class)
  public void buildFiveFloorTest() {
    assertTrue(b.getFloorHeight(3, 3) == 0);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 1);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 2);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 3);
    b.buildFloor(3, 3);
    assertTrue(b.getFloorHeight(3, 3) == 4);
    b.buildFloor(3, 3);
  }

  // Test exception when x is negative in buildFloor
  @Test(expected = IllegalArgumentException.class)
  public void buildFloorXNegTest() {
    b.buildFloor(-1, 0);
  }

  // Test exception when x is greater than board bounds in buildFloor
  @Test(expected = IllegalArgumentException.class)
  public void buildFloorXPosTest() {
    b.buildFloor(6, 0);
  }

  // Test exception when y is negative in buildFloor
  @Test(expected = IllegalArgumentException.class)
  public void buildFloorYNegTest() {
    b.buildFloor(0, -1);
  }

  // Test exception when y is greater than board bounds in buildFloor
  @Test(expected = IllegalArgumentException.class)
  public void buildFloorYPosTest() {
    b.buildFloor(0, 6);
  }

  // Test a valid worker position
  @Test
  public void getWorkerPositionValidTest() {
    int workerID = b.placeWorker(0 ,0);
    ImmutableSquare s = b.getWorkerPosition(workerID);
    assertTrue(s.getX() == 0 && s.getY() == 0);
  }

  // Test two workers with getWorkerPosition
  @Test
  public void getWorkerPositionTwoWorkersTest() {
    int worker1ID = b.placeWorker(1, 2);
    int worker2ID = b.placeWorker(3, 5);
    ImmutableSquare s1 = b.getWorkerPosition(worker1ID);
    ImmutableSquare s2 = b.getWorkerPosition(worker2ID);

    assertTrue(s1.getX() == 1 && s1.getY() == 2);
    assertTrue(s2.getX() == 3 && s2.getY() == 5);
  }

  // Test exception when x in negative in placeWorker
  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerXNegBoundsTest() {
    b.placeWorker(-1, 0);
  }

  // Test exception when x is greater than board bounds in placeWorker
  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerXPosBoundsTest() {
    b.placeWorker(6, 0);
  }

  // Test exception when y in negative in placeWorker
  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerYNegBoundsTest() {
    b.placeWorker(0, -1);
  }

  // Test exception when y is greater than board bounds in placeWorker
  @Test(expected = IllegalArgumentException.class)
  public void placeWorkerYPosBoundsTest() {
    b.placeWorker(0, 6);
  }

  // Test getWorkerIDs with no workers on the board
  @Test
  public void getWorkerIDsEmptyTest() {
    ArrayList<Integer> ids = b.getWorkerIDs();
    assertTrue(ids.size() == 0);
  }

  // Test getWorkerIDs when at two workers are on the board
  @Test
  public void getWorkerIDsTest() {
    b.placeWorker(2, 2);
    b.placeWorker(4, 4);
    ArrayList<Integer> ids = b.getWorkerIDs();
    assertTrue(ids.get(0) == 1 && ids.get(1) == 2);
  }

  // Test valid (x,y) value for moveWorker
  @Test
  public void moveWorkerValidTest() {
    int workerID = b.placeWorker(1, 3);
    ImmutableSquare s = b.getWorkerPosition(workerID);
    assertTrue(s.getX() == 1 && s.getY() == 3);
    b.moveWorker(workerID, 4, 2);
    assertTrue(s.getX() == 4 && s.getY() == 2);
  }

  // Test exception when x is negative in moveWorker
  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerXNegTest() {
    int workerID = b.placeWorker(1, 3);
    b.moveWorker(workerID, -1, 3);
  }

  // Test exception when x is greater than board bounds in moveWorker
  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerXPosTest() {
    int workerID = b.placeWorker(1, 3);
    b.moveWorker(workerID, 6, 3);
  }

  // Test exception when y is negative in moveWorker
  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerYNegTest() {
    int workerID = b.placeWorker(1, 3);
    b.moveWorker(workerID, 1, -1);
  }

  // Test exception when y is greater than board bounds in moveWorker
  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerYPosTest() {
    int workerID = b.placeWorker(1, 3);
    b.moveWorker(workerID, 1, 6);
  }

  // Test invalid worker ID in moveWorker
  @Test(expected = IllegalArgumentException.class)
  public void moveWorkerInvalidWorkerIDTest() {
    b.placeWorker(2, 2);
    b.moveWorker(3, 1, 1);
  }
}
