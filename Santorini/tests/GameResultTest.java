import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for GameResultObject
 */
public class GameResultTest {

  Player p1;
  Player p2;
  GameResult result;

  public void init() {
    //player that uses strat1 (diagonal)
    this.p1 = new Player("one", 0, new StayAliveStrategy(true, 2));
    //player that uses strat2 (furthest)
    this.p2 = new Player("two", 0, new StayAliveStrategy(false, 2));
    this.result = new GameResult(p1,p2,false);
  }

  //testing the getter methods in the GameResult class
  @Test
  public void getTheStuffTests(){
    init();
    assertEquals(this.result.getLoser(), this.p2);
    assertEquals(this.result.getWinner(), this.p1);
  }

  //testing the setter methods in the GameResult class
  @Test
  public void setTheStuffTests() {
    init();
    assertEquals(this.result.getLoser(), this.p2);
    assertEquals(this.result.getWinner(), this.p1);
    this.result.setLoser(this.p1);
    this.result.setWinner(this.p2);
    assertEquals(this.result.getLoser(), this.p1);
    assertEquals(this.result.getWinner(), this.p2);

  }

  //testing that the json is formatted correctly
  @Test
  public void JSONStringTest(){
    init();
    String expected = "[\"one\",\"two\"]";
    assertEquals(this.result.asJSONString(), expected);

  }

}
