import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for GameResultObject
 */
public class GameResultTest {

  Player p1;
  Player p2;

  public void init() {
    //player that uses strat1 (diagonal)
    this.p1 = new Player("one", 0, new StayAliveStrategy(true, 2));
    //player that uses strat2 (furthest)
    this.p2 = new Player("two", 0, new StayAliveStrategy(false, 2));
  }

  @Test
  public void JSONStringTest(){
    init();
    GameResult result = new GameResult(p1, p2);
    String expected = "[\"one\",\"two\"]";

    assertEquals(result.asJSONString(), expected);

  }

}
