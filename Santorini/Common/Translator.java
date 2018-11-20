import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
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


}