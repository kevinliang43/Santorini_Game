import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The Interpreter Class is a Utils class that executes a list of requests onto a given game Board.
 */
public class Interpreter {

  /**
   * Executes a List of Requests during the beginning of a Game. This Specification means that the
   * First request of the given list of requests must be a Board Request.
   * @param board The Board that the list of Request is acting on.
   * @param requests The list of requests to be executed.
   */
  public static void executeInitialRequests(Board board, ArrayList<ArrayNode> requests, Appendable log) {
    if (requests.size() == 0) {
      throw new IllegalArgumentException("Cannot execute an empty list of requests on the given board.");
    }
    if (!isBoardRequest(requests.get(0))) {
      throw new IllegalArgumentException("Initialization Requests must start with a Board Specification.");
    }

    executeRequests(board, requests, log);

  }

  /**
   * Executes all given requests, one by one, onto the given Board.
   * @param board Board to be updated with the given requests.
   * @param requests List of requests to be executed on the given Board.
   */
  public static void executeRequests(Board board, ArrayList<ArrayNode> requests, Appendable log) {
    for (ArrayNode request : requests) {
      execute(board, request, log);
    }
  }

  public static void execute(Board board, ArrayNode request, Appendable log) {

    if (isBoardRequest(request)) {
      executeBoardRequest(board, request);
    }

    else {
      //assuming all requests are valid, we know the first index in the request is the string
      //representing which request is being called
      String type = request.get(0).toString();
      switch(type) {
        case "\"move\"":
          executeMoveRequest(board, request, log);
          break;
        case "\"build\"":
          executeBuildRequest(board, request, log);
          break;
        case "\"neighbors\"":
          executeNeighborsRequest(board, request, log);
          break;
        case "\"height\"":
          executeHeightRequest(board, request, log);
          break;
        case "\"occupied?\"":
          executeOccupiedRequest(board, request, log);
          break;
      }

    }

  }

  /**
   * Executes the given move request on the given board.
   * @param board the Board to be modified.
   * @param moveRequest the move request to be executed.
   */
  private static void executeMoveRequest(Board board, ArrayNode moveRequest, Appendable log) {
    String workerName = moveRequest.get(1).asText();
    ArrayNode direction = (ArrayNode)moveRequest.get(2);
    int rDir = parseDirection(direction.get(0).asText());
    int cDir = parseDirection(direction.get(1).asText());
    board.move(workerName, rDir, cDir);
    try {
      log.append("[]\n");
    } catch (IOException e) {

    }
  }

  /**
   * Executes the given build request on the given board
   * @param board the Board to be modified.
   * @param buildRequest the build request to be executed
   */
  private static void executeBuildRequest(Board board, ArrayNode buildRequest, Appendable log) {
    String workerName = buildRequest.get(1).asText();
    ArrayNode direction = (ArrayNode)buildRequest.get(2);
    int rDir = parseDirection(direction.get(0).asText());
    int cDir = parseDirection(direction.get(1).asText());
    board.build(workerName, rDir, cDir);
    try {
      log.append("[]\n");
    } catch (IOException e) {

    }
  }

  /**
   * Executes the given neighbor query on the given board, prints the outcome of the query to the console
   * @param board the Board to check.
   * @param neighborsRequest the neighbors query to execute.
   */
  private static void executeNeighborsRequest(Board board, ArrayNode neighborsRequest, Appendable log) {
    String workerName = neighborsRequest.get(1).asText();
    ArrayNode direction = (ArrayNode)neighborsRequest.get(2);
    int rDir = parseDirection(direction.get(0).asText());
    int cDir = parseDirection(direction.get(1).asText());

    boolean response = board.neighborQuery(workerName, rDir, cDir);

    // Append log message
    try {
      if (response) {
        log.append("\"yes\"\n");
      }
      else {
        log.append("\"no\"\n");
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  /**
   * Executes the given height query request on the given board, prints the outcome of the query to the console.
   * @param board the Board the check.
   * @param heightRequest the height query to execute.
   */
  private static void executeHeightRequest(Board board, ArrayNode heightRequest, Appendable log) {
    String workerName = heightRequest.get(1).asText();
    ArrayNode direction = (ArrayNode)heightRequest.get(2);
    int rDir = parseDirection(direction.get(0).asText());
    int cDir = parseDirection(direction.get(1).asText());
    int response = board.heightQuery(workerName, rDir, cDir);

    // Append log message
    try {
      log.append(response + "\n");
    } catch (IOException e) {

    }
  }

  /**
   * Executes the given occupied query request on the given board, prints the outcome of the query to the console.
   * @param board the Board to check.
   * @param occupiedRequest the occupied query to execute.
   */
  private static void executeOccupiedRequest(Board board, ArrayNode occupiedRequest, Appendable log) {
    String workerName = occupiedRequest.get(1).asText();
    ArrayNode direction = (ArrayNode)occupiedRequest.get(2);
    int rDir = parseDirection(direction.get(0).asText());
    int cDir = parseDirection(direction.get(1).asText());
    boolean response = board.occupyQuery(workerName, rDir, cDir);
    // Append log message
    try {
      if (response) {
        log.append("\"yes\"\n");
      }
      else {
        log.append("\"no\"\n");
      }
    } catch (IOException e){

    }

  }

  /**
   * Determines how to handle the given string direction
   * @param direction is one of EAST, SOUTH, PUT, NORTH, or WEST representing the direction we're moving in
   * @return 1, 0, or -1 representing the direction to move in
   */
  private static int parseDirection(String direction) {
    if(direction.equals("EAST") || direction.equals("SOUTH")) {
      return 1;
    }
    else if(direction.equals("PUT")) {
      return 0;
    }
    else if(direction.equals("WEST") || direction.equals("NORTH")) {
      return -1;
    }
    else {
      throw new IllegalArgumentException(direction + " is not a valid direction");
    }
  }

  /**
   * Executes a Board Request with a given Board Specifications. Modifies a given Board to match
   * the given Specification.
   * @param board The Board to be modified.
   * @param boardRequest the Board Specification
   */
  private static void executeBoardRequest(Board board, ArrayNode boardRequest) {

    // Iterate through the given Cell Specifications in the board Spec.
    for (int row = 0; row < boardRequest.size(); row++) {
      ArrayNode currentRow = (ArrayNode)boardRequest.get(row); // Get the row of Cell Specs
      for (int col = 0; col < currentRow.size(); col++) {
        JsonNode currentCellSpec = currentRow.get(col); // Cell Spec
        Cell currentCell = board.getCell(row, col);     // Actual Board Cell

        // If the current Cell is a Height
        if (currentCellSpec.isInt()) {
          currentCell.setHeight(currentCellSpec.asInt());
        }

        // If the current Cell is a BuildingWorker
        else {
          String buildingWorker = currentCellSpec.asText();
          String height = "";
          int indx = 0;

          // Build the height from the string, until it hits a character.
          while (Character.isDigit(buildingWorker.charAt(indx))) {
            height += buildingWorker.charAt(indx);
            indx++;
          }

          // Set the height First
          currentCell.setHeight(Integer.parseInt(height));
          String name = buildingWorker.substring(indx);
          //creating a new worker with the given ID
          board.addWorker(row, col, name);
          /*Worker worker = new Worker(name, currentCell);
          currentCell.addWorker(worker);*/
        }
      }
    }
  }




  /**
   * Checks a given request to see if it is a Board Specification.
   * @param request the Request to be Checked
   * @return A boolean representing whether or not the given request is a Board Specification.
   */
  private static boolean isBoardRequest(ArrayNode request) {
    return request.get(0).isArray();
  }

}
