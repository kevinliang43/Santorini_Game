import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
  public void executeInitialRequests(Board board, ArrayList<ArrayNode> requests) {
    if (requests.size() == 0) {
      throw new IllegalArgumentException("Cannot execute an empty list of requests on the given board.");
    }
    if (!isBoardRequest(requests.get(0))) {
      throw new IllegalArgumentException("Initialization Requests must start with a Board Specification.");
    }

    executeRequests(board, requests);

  }

  /**
   * Executes all given requests, one by one, onto the given Board.
   * @param board Board to be updated with the given requests.
   * @param requests List of requests to be executed on the given Board.
   */
  public void executeRequests(Board board, ArrayList<ArrayNode> requests) {
    for (ArrayNode request : requests) {
      execute(board, request);
    }

  }

  public void execute(Board board, ArrayNode request) {

    if (isBoardRequest(request)) {
      executeBoardRequest(board, request);
    }

    else {

    }

  }


  /**
   * Executes a Board Request with a given Board Specifications. Modifies a given Board to match
   * the given Specification.
   * @param board The Board to be modified.
   * @param boardRequest the Board Specification
   */
  private void executeBoardRequest(Board board, ArrayNode boardRequest) {

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
          Worker worker = new Worker(name, currentCell);
          currentCell.addWorker(worker);
        }
      }
    }
  }




  /**
   * Checks a given request to see if it is a Board Specification.
   * @param request the Request to be Checked
   * @return A boolean representing whether or not the given request is a Board Specification.
   */
  private boolean isBoardRequest(ArrayNode request) {
    return request.get(0).isArray();
  }

}
