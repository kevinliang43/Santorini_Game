import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BoardStatusTest {

  public BoardStatusTest() {

  }

  @Test
  public void filterWorkerTest() {
    Board b = new Board();
    ArrayList<Integer> toFilter = new ArrayList<>();
    ArrayList<Integer> expectedFilter = new ArrayList<>();
    toFilter.add(b.placeWorker(0, 0, "one", 1));
    toFilter.add(b.placeWorker(0, 1, "two", 2));
    expectedFilter.add(b.placeWorker(1, 0, "three", 3));

    BoardStatus boardStatus = new BoardStatus(b, Status.PLACE);
    ArrayList<Integer> actualFilter = boardStatus.filterWorkers(toFilter);

    assertEquals(expectedFilter.size(), actualFilter.size());
    for (int i = 0; i < expectedFilter.size(); i++) {
      assertEquals(expectedFilter.get(i), actualFilter.get(i));
    }
  }
}
