import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
  BreakerStrategy breakerStrat;

  Player p1;
  Player p2;
  Player p3;


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
    breakerStrat = new BreakerStrategy();

    p1 = new Player("Marina", 0, breakerStrat);
    p2 = new Player("Kevin", 1, strat);
    p3 = new Player("Bob", 2, breakerStrat);


  }

  //testing writing a movebuild as JSON
  @Test
  public void testMoveBuild() {
    setup();

    Action move = new Action(Status.MOVE, worker11, 3,3, "one1");
    Action build = new Action(Status.BUILD, worker11, 4, 4, "one1");

    MoveBuild moveBuild = new MoveBuild(move, build);

    String expected = "[\"one1\",\"PUT\",\"SOUTH\",\"EAST\",\"SOUTH\"]";
    assertEquals(Translator.moveBuildAsJSON(board, moveBuild), expected);
    move = new Action(Status.MOVE, worker11, 3, 4, "one1");
    build = new Action(Status.BUILD, worker11, 2, 3, "one1");
    moveBuild = new MoveBuild(move, build);
    expected = "[\"one1\",\"EAST\",\"SOUTH\",\"WEST\",\"NORTH\"]";
    assertEquals(Translator.moveBuildAsJSON(board, moveBuild), expected);


  }

  //testing converStringListToJSONArray helper function in the Translater class
  @Test
  public void testConvert() {
    ArrayList<String> los = new ArrayList<>();
    los.add("one");
    los.add("two");
    los.add("three");

    String expected = "[\"one\",\"two\",\"three\"]";
    assertEquals(Translator.convertStringListToJSONArray(los), expected);
  }

  //testing tournamentResultsAsJSON method in Translator class
  @Test
  public void testTournamentResult() {
    setup();
    ArrayList<Player> removedPlayers = new ArrayList<>();
    removedPlayers.add(p1);
    ArrayList<GameResult> results = new ArrayList<>();
    GameResult result1 = new GameResult(this.p1, this.p2);
    GameResult result2 = new GameResult(this.p3, this.p2);
    GameResult result3 = new GameResult(this.p1, this.p2);
    results.add(result1);
    results.add(result2);
    results.add(result3);

    String expected = "[[\"Marina\"],[[\"Marina\",\"Kevin\"],[\"Bob\",\"Kevin\"],[\"Marina\",\"Kevin\"]]]";
    assertEquals(Translator.tournamentResultsAsJSON(removedPlayers,results), expected);
  }

  @Test
  public void testCheckFields() {
    ArrayList<String> correctFields = new ArrayList<>(Arrays.asList("min players", "port", "waiting for", "repeat"));
    String test = "{ \"min players\" : 3, \"port\"        : 56789, \"waiting for\" : 10, \"repeat\"      : 0}";
    try {
      JsonNode testNode = ConfigReader.parse(test).get(0);
      boolean result = Translator.checkFields(testNode, correctFields);
      assertEquals(result, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testCheckFieldsTooManyFields() {
    ArrayList<String> correctFields = new ArrayList<>(Arrays.asList("min players", "port", "waiting for", "repeat"));
    String test = "{ \"min players\" : 3, \"port\"        : 56789, \"waiting for\" : 10, \"repeat\"      : 0, \"lol\" : 3}";
    try {
      JsonNode testNode = ConfigReader.parse(test).get(0);
      boolean result = Translator.checkFields(testNode, correctFields);
      assertEquals(result, false);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testCheckFieldsTooFewFields() {
    ArrayList<String> correctFields = new ArrayList<>(Arrays.asList("min players", "port", "waiting for", "repeat"));
    String test = "{ \"min players\" : 3, \"port\"        : 56789, \"waiting for\" : 10}";
    try {
      JsonNode testNode = ConfigReader.parse(test).get(0);
      boolean result = Translator.checkFields(testNode, correctFields);
      assertEquals(result, false);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testCheckFieldsIncorrectFieldName() {
    ArrayList<String> correctFields = new ArrayList<>(Arrays.asList("min_players", "port", "waiting for", "repeat"));
    String test = "{ \"min players\" : 3, \"port\"        : 56789, \"waiting for\" : 10, \"repeat\"      : 0}";
    try {
      JsonNode testNode = ConfigReader.parse(test).get(0);
      boolean result = Translator.checkFields(testNode, correctFields);
      assertEquals(result, false);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
