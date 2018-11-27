import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Translator Class to convert Board states into JSON.
 * Utils Static Class
 */
public class Translator {

  /**
   * Converts a list of kicked Players and a SORTED list of GameResult Objects into their respective
   * JSON Arrays
   * @param removedPlayers List of Removed Players
   * @param results List of GameResults in SORTED order (p1 v rest, p2 v rest...etc)
   * @return JSON String consisting of an array of two arrays
   *          First Sub Array [STRINGNAME, STRINGNAME, ...] Each name represents the name of a kicked
   *          player
   *          Second Sub Array [[StringName1, StringName2], [StringName1, StringName3] ...]
   *          Each subarray of the second subarray represents the names of the players of the
   *          specific game in the form of [WINNER, LOSER]
   *
   */
  public static String tournamentResultsAsJSON(ArrayList<Player> removedPlayers, ArrayList<GameResult> results) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultNode = mapper.createArrayNode();
    ArrayNode removedPlayersNode = mapper.createArrayNode();
    ArrayNode resultsNode = mapper.createArrayNode();

    // Removed Players
    for (int i = 0; i < removedPlayers.size(); i++) {
      removedPlayersNode.add(removedPlayers.get(i).getName());
    }

    // Sorted Results
    for (int i = 0; i < results.size(); i++) {
      ArrayNode singleResult = mapper.createArrayNode();
      singleResult.add(results.get(i).getWinner().getName());
      singleResult.add(results.get(i).getLoser().getName());
      if (results.get(i).isIrregular()) {
        singleResult.add("irregular");
      }
      resultsNode.add(singleResult);
    }
    resultNode.add(removedPlayersNode);
    resultNode.add(resultsNode);


    return resultNode.toString();


  }


  public static String convertStringListToJSONArray(ArrayList<String> toConvert) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultNode = mapper.createArrayNode();

    for (int i = 0; i < toConvert.size(); i++) {
      resultNode.add(toConvert.get(i));
    }
    return resultNode.toString();
  }

  /**
   * Converts a Movebuild Action into its respective JSON String
   * @param board Current Board State
   * @param action action being made
   * @return JSON String representing the corresponding moveBuild.
   */
  public static String moveBuildAsJSON(Board board, MoveBuild action) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultNode = mapper.createArrayNode();

    resultNode.add(action.getWorkerName());
    Square initialSquare = board.getWorkerSquare(action.getWorkerID());
    int relMoveX = action.getxMove() - initialSquare.getX();
    int relMoveY = action.getyMove() - initialSquare.getY();

    resultNode.add(getWEDirection(relMoveY));
    resultNode.add(getNSDirection(relMoveX));

    if (action.getBuild() != null) {
      int relBuildX = action.getxBuild() - action.getxMove();
      int relBuildY = action.getyBuild() - action.getyMove();
      resultNode.add(getWEDirection(relBuildY));
      resultNode.add(getNSDirection(relBuildX));
    }

    return resultNode.toString();

  }

  /**
   * Convert a West-East Direction into a String direction
   * @param y relative direction of y axis
   * @return String representation of WE Direction
   */
  private static String getWEDirection(int y) {
    String retString = "";
    switch (y) {
      case -1:
        retString += "WEST";
        break;
      case 0:
        retString += "PUT";
        break;
      case 1:
        retString += "EAST";
        break;
    }
    return retString;
  }

  /**
   * Convert a NorthSouth Direction into a String direction
   * @param x relative direction of x axis
   * @return String representation of NS Direction
   */
  private static String getNSDirection(int x) {
    String retString = "";

    switch (x) {
      case -1:
        retString += "NORTH";
        break;
      case 0:
        retString += "PUT";
        break;
      case 1:
        retString += "SOUTH";
        break;

    }
    return retString;
  }

  private static int convertDirToNum(String dir) {
    if (dir.equals("NORTH") || dir.equals("WEST")) {
      return -1;
    }
    else if (dir.equals("SOUTH") || dir.equals("EAST")) {
      return 1;
    }
    else {
      return 0;
    }

  }

  public static String placeActionAsJSON(Action placeAction) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultNode = mapper.createArrayNode();
    resultNode.add(placeAction.getX());
    resultNode.add(placeAction.getY());
    return resultNode.toString();
  }

  public static IAction convertJSONToAction(Square workerSquare, String json) {
    int x = workerSquare.getX();
    int y = workerSquare.getY();
    int workerID = workerSquare.getWorkerID();
    String workerName = workerSquare.getWorkerName();

    try {
      JsonNode node = ConfigReader.parse(json).get(0);
      int mx = x + convertDirToNum(node.get(2).asText());
      int my = y + convertDirToNum(node.get(1).asText());
      Action move = new Action(Status.MOVE, workerID, mx, my, workerName);

      if (node.size() == 5) {
        int bx = mx + convertDirToNum(node.get(4).asText());
        int by = my + convertDirToNum(node.get(3).asText());
        Action build = new Action(Status.BUILD, workerID, bx, by, workerName);
        MoveBuild moveBuild = new MoveBuild(move, build);
        return moveBuild;
      }
      else {
        return move;
      }

    } catch (IOException e) {
    }


    return null;
  }

  public static Board convertJSONToBoard(String playerName, ArrayNode boardNode, ArrayList<Integer> workerIds) {
    if (boardNode.size() != 6 || boardNode.get(0).size() != 6) {
      throw new IllegalArgumentException("Cannot Construct Board from this.");
    }

    // Setup new board and available IDs
    Board b = new Board();
    ArrayList<Integer> availableIds = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      if (!workerIds.contains(i)){
        availableIds.add(i);
      }
    }

    // Iterate through and generate board
    for (int row = 0; row < boardNode.size(); row++) {
      for (int col = 0; col < boardNode.get(0).size(); col++) {
        JsonNode square = boardNode.get(row).get(col);
        // Cell is a Height
        if (square.isInt()) {
          b.setFloor(row, col, square.asInt());
        }
        else {

          // Parse args
          String JsonString = square.asText();
          int cellHeight = Integer.parseInt(JsonString.substring(0, 1));
          int workerNum = Integer.parseInt(JsonString.substring(JsonString.length()-1));
          String workerName = JsonString.substring(1,JsonString.length()-1);
          int workerID = 0;
          // if its one of the player's workers
          if (workerName.equals(playerName)) {
            if (workerNum == 1) {
              workerID = Math.min(workerIds.get(0), workerIds.get(1));
            } else {
              workerID = Math.max(workerIds.get(0), workerIds.get(1));
            }
          }
          else {
            workerID = availableIds.get(0);
            availableIds.remove(0);
          }

          b.setFloor(row, col, cellHeight);
          b.placeWorker(row, col, workerName+workerNum, workerID);

        }
      }
    }

    return b;

  }

  /**
   * Checks to see if a JsonNode representing a Config File in JSON Format contains
   * the correct given fields.
   * @param configNode JsonNode to be checked for proper fields.
   * @param fields Fields to be checked for.
   * @return boolean representing if JsonNode has correctFields.
   */
  public static boolean checkFields(JsonNode configNode, ArrayList<String> fields) {
    Iterator<String> fieldNamesIt = configNode.fieldNames();
    ArrayList<String> actualFields = new ArrayList<>();
    while (fieldNamesIt.hasNext()) {
      actualFields.add(fieldNamesIt.next());
    }

    for (int i = 0; i < fields.size(); i++) {
      if (!actualFields.contains(fields.get(i))){
        return false;
      }
    }
    for (int i = 0; i < actualFields.size(); i++) {
      if (!fields.contains(actualFields.get(i))) {
        return false;
      }
    }

    return true;
  }

  public static String placementMessage(ArrayList<Square> workerSquares) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode responseNode = mapper.createArrayNode();
    for (Square square : workerSquares) {
      ArrayNode workerPlace = mapper.createArrayNode();
      workerPlace.add(square.getWorkerName());
      workerPlace.add(square.getX());
      workerPlace.add(square.getY());
      responseNode.add(workerPlace);
    }
    return responseNode.toString();


  }

  /**
   * Checks to see if the given string is a valid JSON Object
   * in string format
   * @param JSONString String to be checked
   * @return boolean representing whether or not the given String
   * is a valid JSON Object
   */
  public static boolean isValidJSON(String JSONString) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(JSONString);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   *
   * Logic that Handles and determines the type of message that
   * a given JsonNode is (JsonNode represents some type of JsonObject)
   *
   * Currently, there are five different types of messages that this
   * Proxy will encounter FROM the server.
   * 1. Playing-As (["playing-as" Name])
   * 2. other (JSON String of next opponent.)
   * 3. Placement ([[Worker,Coordinate,Coordinate],...])
   * 4. Take Turn (Board)
   * 5. Informing Players (JSON array of EncounterOutcomes)
   *    EncounterOutcome is one of the following:
   *        [String, String], which is the name of the winner followed by the loser;
   *        [String, String, "irregular"], which is like the first alternative but signals that the losing player misbehaved.
   *
   *
   * If the JSON Node is not one of the above, then it will return null.
   *
   * @param node Node to be Checked for MessageType
   * @return One of MessageTypes (See MessageType enum for more info.)
   */
  public static MessageType messageType(JsonNode node) {
    if (node.isTextual()) {
      return MessageType.OPP_NAME;
    }
    else if (node.isArray()){
      ArrayNode arrayNode = (ArrayNode)node;
      // Playing As
      if (arrayNode.size() == 2 && arrayNode.get(0).isTextual() && arrayNode.get(1).isTextual()) {
        return MessageType.PLAYING_AS;
      }
      // Board
      else if (arrayNode.size() == 6 && arrayNode.get(0).size() == 6) {
        return MessageType.TAKE_TURN;
      }
      // Placement
      else if (arrayNode.size() == 0 || arrayNode.get(0).get(1).isInt()) {
        return MessageType.PLACEMENT;
      }
      // Inform Players
      else {
        return MessageType.INFORM_PLAYERS;
      }
    }
    return null;
  }

  public static String encountersAsJSON(ArrayList<GameResult> results) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultsNode = mapper.createArrayNode();

    // Sorted Results
    for (int i = 0; i < results.size(); i++) {
      ArrayNode singleResult = mapper.createArrayNode();
      singleResult.add(results.get(i).getWinner().getName());
      singleResult.add(results.get(i).getLoser().getName());
      if (results.get(i).isIrregular()) {
        singleResult.add("irregular");
      }
      resultsNode.add(singleResult);
    }
    return resultsNode.toString();
  }




}