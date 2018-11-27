import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.TRANSACTION_MODE;

import java.io.IOException;
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
    GameResult result1 = new GameResult(this.p1, this.p2, false);
    GameResult result2 = new GameResult(this.p3, this.p2, false);
    GameResult result3 = new GameResult(this.p1, this.p2, false);
    results.add(result1);
    results.add(result2);
    results.add(result3);

    String expected = "[[\"Marina\"],[[\"Marina\",\"Kevin\"],[\"Bob\",\"Kevin\"],[\"Marina\",\"Kevin\"]]]";
    assertEquals(Translator.tournamentResultsAsJSON(removedPlayers,results), expected);
  }

  //testing that the check fields sees correct configuration files as true
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

  //testing that the check fields sees incorrect configuration files as false with extra fields
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

  //testing that the check fields sees incorrect configuration files when too few fields
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

  //testing that check fields sees incorrect configuration files when incorrect names
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

  //test that placementMessage returns expected JSON list back
  @Test
  public void testPlacementMessage() {
    Square sq0 = new Square(2, 3, 1, true, 1, "kevin1");
    Square sq1 = new Square(0, 1, 1, true, 0, "kevin2");
    ArrayList<Square> list = new ArrayList<>();
    list.add(sq0);
    list.add(sq1);

    String actual = Translator.placementMessage(list);
    String expected = "[[\"kevin1\",2,3],[\"kevin2\",0,1]]";

    assertEquals(expected, actual);
  }

  //tests for messageType
  @Test
  public void testMessageType() {
    String oppName = "\"Kevin\"";
    String playingAs = "[\"playing-as\", \"kevin\"]";
    String board = "[[\"0one1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,\"0two1\",0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]";
    String emptyPlace = "[]";
    String nonEmptyPlace = "[[\"kevin\", 1, 1]]";
    String inform1 = "[[\"Kevin\", \"Marina\"]]";
    String inform2 = "[[\"Kevin\", \"Marina\"], [\"Kevin\", \"Marina\"]]";
    String inform3 = "[[\"Kevin\", \"Marina\", \"irregular\"], [\"Kevin\", \"Marina\"]]";
    ObjectMapper mapper = new ObjectMapper();
    try {
      JsonNode messageNode = mapper.readTree(oppName);
      assertEquals(Translator.messageType(messageNode), MessageType.OPP_NAME);
      messageNode = mapper.readTree(board);
      assertEquals(Translator.messageType(messageNode), MessageType.TAKE_TURN);
      messageNode = mapper.readTree(playingAs);
      assertEquals(Translator.messageType(messageNode), MessageType.PLAYING_AS);
      messageNode = mapper.readTree(emptyPlace);
      assertEquals(Translator.messageType(messageNode), MessageType.PLACEMENT);
      messageNode = mapper.readTree(nonEmptyPlace);
      assertEquals(Translator.messageType(messageNode), MessageType.PLACEMENT);
      messageNode = mapper.readTree(inform1);
      assertEquals(Translator.messageType(messageNode), MessageType.INFORM_PLAYERS);
      messageNode = mapper.readTree(inform2);
      assertEquals(Translator.messageType(messageNode), MessageType.INFORM_PLAYERS);
      messageNode = mapper.readTree(inform3);
      assertEquals(Translator.messageType(messageNode), MessageType.INFORM_PLAYERS);
      messageNode = mapper.readTree("5");
      assertEquals(Translator.messageType(messageNode), null);
    } catch (IOException e) {

    }
  }

  //test that placeActions are written correctly as JSON
  @Test
  public void testPlaceActionAsJSON() {
    Action place = new Action(Status.PLACE, 1, 2, 3, "KEIVN1");
    String placeJSON = Translator.placeActionAsJSON(place);
    String expected = "[2,3]";
    assertEquals(expected, placeJSON);

    place = new Action(Status.PLACE, 1, 4, 5, "KEIVN1");
    placeJSON = Translator.placeActionAsJSON(place);
    expected = "[4,5]";
    assertEquals(expected, placeJSON);
  }

  //tests that we can correctly generate a Board from a JSON specification
  @Test
  public void testConvertJSONToBoard() {

    String testBoard = "[[\"2one1\", 3, 0, 0, 0, 0], [0, \"0one2\", \"1two1\", 0, 0, 0], [0, 0, 1, 0, 0, 0], [0, 0, 0, 1, 0, 0], [0, 0, 0, 0, 0, \"0two2\"], [0, 0, 0, 0, 0, 0]]";
    String name = "one";
    ArrayList ids = new ArrayList(Arrays.asList(1, 3));
    ObjectMapper mapper = new ObjectMapper();
    try {
      JsonNode testNode = mapper.readTree(testBoard);
      Board b = Translator.convertJSONToBoard(name, (ArrayNode)testNode, ids);
      assertEquals(testNode.toString(), b.asJSONArray());
    } catch (IOException e) {

    }
  }

  //tests that converting a JSON action correctly makes a movebuild
  @Test
  public void testConvertJSONToActionMB() {
    Square sq = new Square(3, 3);
    sq.setOccupied(true, 1, "kevin");
    String json = "[\"kevin\", \"EAST\", \"SOUTH\", \"WEST\", \"NORTH\"]";
    IAction mb = Translator.convertJSONToAction(sq, json);
    assertEquals(mb.toString(), "move 1 to 4,4 build 1 to 3,3" );
  }

  //tests that converting a JSON action correctly makes a move
  @Test
  public void testConvertJSONToActionM() {
    Square sq = new Square(3, 3);
    sq.setOccupied(true, 1, "kevin");
    String json = "[\"kevin\", \"EAST\", \"SOUTH\"]";
    IAction mb = Translator.convertJSONToAction(sq, json);
    assertEquals(mb.toString(), "move 1 to 4,4" );
  }

  //testing isValidJSON on different strings
  @Test
  public void testIsValidJSON() {
    String json = "5";
    assertEquals(Translator.isValidJSON(json), true);
    json = "\"hello\"";
    assertEquals(Translator.isValidJSON(json), true);
    json = "\"hello\"";
    assertEquals(Translator.isValidJSON(json), true);
    json = "[\"hello\", 5]";
    assertEquals(Translator.isValidJSON(json), true);
    json = "[\"hello\"";
    assertEquals(Translator.isValidJSON(json), false);
    json = "[[\"hello\"],[5]]";
    assertEquals(Translator.isValidJSON(json), true);
    json = "hello\"";
    assertEquals(Translator.isValidJSON(json), false);
    json = "[\"hello\"";
    assertEquals(Translator.isValidJSON(json), false);

  }
}
