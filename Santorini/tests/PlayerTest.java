import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PlayerTest {
  private class MockStrategy implements Strategy {
    @Override
    public Action getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {
      return null;
    }
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
}
