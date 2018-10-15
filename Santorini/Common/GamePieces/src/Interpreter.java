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
  public static String executeInitialRequests(Board board, ArrayList<ArrayNode> requests, Appendable log) {
    if (requests.size() == 0) {
      throw new IllegalArgumentException("Cannot execute an empty list of requests on the given board.");
    }
    if (!isBoardRequest(requests.get(0))) {
      throw new IllegalArgumentException("Initialization Requests must start with a Board Specification.");
    }

    return executeRequests(board, requests, log);

  }

  /**
   * Executes all given requests, one by one, onto the given Board.
   * @param board Board to be updated with the given requests.
   * @param requests List of requests to be executed on the given Board.
   */
  public static String executeRequests(Board board, ArrayList<ArrayNode> requests, Appendable log) {
    String lastWorker = null;
    for (ArrayNode request : requests) {
      lastWorker = execute(board, request, log, lastWorker);
    }

    return lastWorker;
  }

  public static String execute(Board board, ArrayNode request, Appendable log, String lastWorker) {

    if (isBoardRequest(request)) {
      executeBoardRequest(board, request);
      return null;
    }

    else if (isPlusRequest(request)) {
      String type = request.get(0).toString();
      ArrayNode direction = (ArrayNode)request.get(1);
      int ewDir = JSONParse.parseDirection(direction.get(0).asText());
      int nsDir = JSONParse.parseDirection(direction.get(1).asText());

      switch(type) {
        case "\"+build\"":
          executeBuildRequest(board, lastWorker, ewDir, nsDir, log);
          break;
      }

      return lastWorker;

    }

    else {
      //assuming all requests are valid, we know the first index in the request is the string
      //representing which request is being called
      String type = request.get(0).toString();
      String workerName = request.get(1).asText();
      ArrayNode direction = (ArrayNode)request.get(2);
      int ewDir = JSONParse.parseDirection(direction.get(0).asText());
      int nsDir = JSONParse.parseDirection(direction.get(1).asText());

      switch(type) {
        case "\"move\"":
          executeMoveRequest(board, workerName, ewDir, nsDir, log);
          break;
        case "\"build\"":
          executeBuildRequest(board, workerName, ewDir, nsDir, log);
          break;
        case "\"neighbors\"":
          executeNeighborsRequest(board, workerName, ewDir, nsDir, log);
          break;
        case "\"height\"":
          executeHeightRequest(board, workerName, ewDir, nsDir, log);
          break;
        case "\"occupied?\"":
          executeOccupiedRequest(board, workerName, ewDir, nsDir, log);
          break;
      }

      return workerName;

    }

  }

  private static boolean isPlusRequest(ArrayNode request) {
    return request.get(0).asText().charAt(0) == '+';
  }

  /**
   * Executes the given move request on the given board.
   * @param board the Board to be modified.
   * @param workerName name of the worker to be moved
   * @param ewDir Number of spaces moved on the EAST WEST plane
   * @param nsDir Number of spaces moved on the NORTH SOUTH plane
   * @param log Log to keep track of executed commands and their output.
   */
  private static void executeMoveRequest(Board board, String workerName, int ewDir, int nsDir, Appendable log) {
    board.move(workerName, ewDir, nsDir);
    try {
      log.append("[]\n");
    } catch (IOException e) {

    }
  }

  /**
   * Executes the given build request on the given board.
   * @param board the Board to be modified.
   * @param workerName name of the worker that is building
   * @param ewDir Number of spaces on the EAST WEST plane of the target cell to be built on
   * @param nsDir Number of spaces on the NORTH SOUTH plane of the target cell to be built on
   * @param log Log to keep track of executed commands and their output.
   */
  private static void executeBuildRequest(Board board, String workerName, int ewDir, int nsDir, Appendable log) {
    board.build(workerName, ewDir, nsDir);
    try {
      log.append("[]\n");
    } catch (IOException e) {

    }
  }

  /**
   * Executes a Neighbor request on the given board.
   * @param board the Board to execute the request on.
   * @param workerName name of the worker that executing the request
   * @param ewDir Number of spaces on the EAST WEST plane of the target cell to be queried
   * @param nsDir Number of spaces on the NORTH SOUTH plane of the target cell to be queried
   * @param log Log to keep track of executed commands and their output.
   */
  private static void executeNeighborsRequest(Board board, String workerName, int ewDir, int nsDir, Appendable log) {
    boolean response = board.neighborQuery(workerName, ewDir, nsDir);
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
   * Executes a Height request on the given board.
   * @param board the Board to execute the request on.
   * @param workerName name of the worker that executing the request
   * @param ewDir Number of spaces on the EAST WEST plane of the target cell to be queried
   * @param nsDir Number of spaces on the NORTH SOUTH plane of the target cell to be queried
   * @param log Log to keep track of executed commands and their output.
   */
  private static void executeHeightRequest(Board board, String workerName, int ewDir, int nsDir, Appendable log) {
    int response = board.heightQuery(workerName, ewDir, nsDir);
    // Append log message
    try {
      log.append(response + "\n");
    } catch (IOException e) {

    }
  }

  /**
   * Executes a Occupied request on the given board.
   * @param board the Board to execute the request on.
   * @param workerName name of the worker that executing the request
   * @param ewDir Number of spaces on the EAST WEST plane of the target cell to be queried
   * @param nsDir Number of spaces on the NORTH SOUTH plane of the target cell to be queried
   * @param log Log to keep track of executed commands and their output.
   */
  private static void executeOccupiedRequest(Board board, String workerName, int ewDir, int nsDir, Appendable log) {

    boolean response = board.occupyQuery(workerName, ewDir, nsDir);
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
  public static boolean isBoardRequest(ArrayNode request) {
    return request.get(0).isArray();
  }

}
