import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Rule Checker Class is used to verify actions within a game.
 */
public class RuleChecker {

  private int MAX_HEIGHT; // Maximum height a building can be
  private int MAX_MOVE_RANGE; // Maximum amount of squares a Worker can move
  private int MAX_BUILD_RANGE; // Maximum squares away a Worker can build from

  /**
   * Cosntructor for customized maximum height, moving range, and building range
   *
   * @param max_height      int maximum height a builing can be
   * @param max_move_range  int maximum amount of squares a Worker can move
   * @param max_build_range int maximum squares away a Worker can build from
   */
  RuleChecker(int max_height, int max_move_range, int max_build_range) {
    if (max_height < 1) {
      throw new IllegalArgumentException("Cannot have Maximum Building Height of less than 1.");
    }
    if (max_move_range < 1) {
      throw new IllegalArgumentException("Cannot have Maximum Worker Move Range of less than 1.");
    }
    if (max_build_range < 1) {
      throw new IllegalArgumentException("Cannot have Maximum Worker Build Range of less than 1.");
    }
    this.MAX_HEIGHT = max_height;
    this.MAX_MOVE_RANGE = max_move_range;
    this.MAX_BUILD_RANGE = max_build_range;
  }

  /**
   * Default Constructor for a Rule Checker with Default Rules:
   * Max Height of Building: 4
   * Maximum Amount of Squares moved per turn per worker: 1
   * Maximum Amount of Squares away a Worker can build: 1
   */
  RuleChecker() {
    this.MAX_HEIGHT = 4;
    this.MAX_MOVE_RANGE = 1;
    this.MAX_BUILD_RANGE = 1;
  }

  /**
   * @param requestingPlayer Player that is making the request
   * @param playerTurn       Player who's turn it is
   */
  public boolean checkRequests(Board board, Player requestingPlayer, Player playerTurn,
                               ArrayList<ArrayNode> requests, Appendable log) {
    Board boardCopy = new Board(board);
    StringBuilder executeLog = new StringBuilder();

    if (!requestingPlayer.equals(playerTurn)) {
      appendRuleCheckerLog(false, log);
      return false;
    }

    String lastWorker = "";

    // Check
    for (ArrayNode request : requests) {
      if (checkRequest(boardCopy, request, lastWorker)) {
        lastWorker = Interpreter.execute(boardCopy, request, executeLog, lastWorker);
      } else {
        appendRuleCheckerLog(false, log);
        return false;
      }

    }

    appendRuleCheckerLog(true, log);
    return true;


  }

  /**
   * Checks to see if a given request is Valid. A valid request is defined to be: 1) the player
   * making the request matches the player whose turn it is 2) the target Cell is within the bounds
   * of the given board 3) Worker Exists 4) Subconditions of Specific Requests are also met. See
   * Methods below for further subconditions.
   *
   * @param board   Game Board
   * @param request ArrayNode representation of a request
   * @return Boolean representing whether or not a given request is valid.
   */
  public boolean checkRequest(Board board, ArrayNode request, String lastWorker) {
    String type = request.get(0).toString();

    boolean valid = false;
    ArrayNode direction;
    int ewDir;
    int nsDir;
    Worker worker;
    Cell targetCell;

    switch (type) {

      case "\"move\"":

        String workerName = request.get(1).asText();
        direction = (ArrayNode) request.get(2);
        ewDir = JSONParse.parseDirection(direction.get(0).asText());
        nsDir = JSONParse.parseDirection(direction.get(1).asText());

        try {
          worker = board.getWorker(workerName);
        } catch (Exception e) {
          return false;
        }

        // action is within bounds of the board
        if (checkInBounds(board, ewDir, nsDir, worker)) {
          targetCell = board.getNextCell(worker, ewDir, nsDir);
        } else {
          return false;
        }

        valid = isValidMove(targetCell, worker, ewDir, nsDir);
        break;

      case "\"+build\"":
        direction = (ArrayNode) request.get(1);
        ewDir = JSONParse.parseDirection(direction.get(0).asText());
        nsDir = JSONParse.parseDirection(direction.get(1).asText());

        try {
          worker = board.getWorker(lastWorker);
        } catch (Exception e) {
          return false;
        }

        // action is within bounds of the board
        if (checkInBounds(board, ewDir, nsDir, worker)) {
          targetCell = board.getNextCell(worker, ewDir, nsDir);
        } else {
          return false;
        }

        valid = isValidBuild(targetCell, worker, ewDir, nsDir);
        break;
    }

    return valid;

  }


  /**
   * Appends resultant message from calling checking methods to a given log.
   *
   * @param result Result of the check methods.
   * @param log    Log to append to.
   */
  private void appendRuleCheckerLog(boolean result, Appendable log) {
    String message = "\"no\"\n";
    if (result) {
      message = "\"yes\"\n";
    }
    try {
      log.append(message);
    } catch (IOException e) {
    }
  }

  /**
   * Checks to see if the given move is a valid move.
   * 1) the movement is within the range of the maximum moving range specified by this rule checker
   * 2) the move is not stationary
   * 3) the target Cell has a height less than the maximum height specified by this rule checker
   * 4) the target Cell is at most 1 height higher than the current Cell
   * 5) the given worker has not yet moved yet
   * 6) the target Cell does not contain a worker
   * 7) TODO the player has not moved yet
   *
   * @param targetCell Cell that the Worker is being moved to.
   * @param worker     the Worker representing the worker being moved.
   * @param ewDir      Integer representing number of Horizontal steps in terms of Cells.
   * @param nsDir      Integer representing number of Vertical steps in terms of Cells.
   * @return Boolean representing whether or not the move is valid.
   */
  public boolean isValidMove(Cell targetCell, Worker worker, int ewDir, int nsDir) {

    return checkMoveRange(ewDir, nsDir) && // Moving less than or equal to the allowed amount
            checkTargetCellConditions(targetCell) &&
            worker.getCell().getHeight() >= targetCell.getHeight() - 1 && // height of building is atmost 1 more than current
            !worker.hasMoved(); // this worker has not yet moved.
    //TODO : HAS THIS PLAYER MOVED?
  }


  /**
   * Checks to see if the given build is a valid build.
   * A build is valid when:
   * 1) the build is within the range of the maximum build range specified by this rule checker
   * 2) the target Cell does not contain a worker
   * 3) the target Cell has a height less than the maximum height specified by this rule checker
   * 4) the worker performing the build has moved in this turn
   * 5) TODO the player has not built yet
   *
   * @param targetCell Cell that is being built on by the Worker.
   * @param worker     the Worker representing the worker being moved.
   * @param ewDir      Integer representing number of Horizontal steps in terms of Cells.
   * @param nsDir      Integer representing number of Vertical steps in terms of Cells.
   * @return Boolean representing whether or not the build is valid.
   */
  public boolean isValidBuild(Cell targetCell, Worker worker, int ewDir, int nsDir) {
    return checkBuildRange(ewDir, nsDir) && // Building less than or equal to the allowed amount and not stationary
            checkTargetCellConditions(targetCell) && // Check conditions of target Cell
            worker.hasMoved(); // this worker has moved.
    //TODO : HAS THIS PLAYER MOVED?

  }

  /**
   * Checks to see if the target Cell's conditions are fit to have an action performed on it.
   * Conditions are fit when:
   * Target Cell does not have a worker
   * Target Cell's height is less than the maximum height allowed by the rule checker
   *
   * @param targetCell Cell to be checked
   * @return Boolean representing whether or not the target Cell is fit to have an action performed
   * on it.
   */
  private boolean checkTargetCellConditions(Cell targetCell) {
    return targetCell.getHeight() < MAX_HEIGHT && // targetCell's building is less than max height
            !targetCell.containsWorker(); // targetCell does not contain a worker
  }

  /**
   * Checks to see if a given action is within the maximum range specified by this Rule Checker
   * and if the move is moving off of the current Cell (is not stationary)
   *
   * @param ewDir Integer representing number of Horizontal steps in terms of Cells
   * @param nsDir Integer representing number of Vertical steps in terms of Cells
   * @return Boolean representing whether or not the given action is within the maximum range and is
   * not a stationary move
   */
  private boolean checkMoveRange(int ewDir, int nsDir) {
    return Math.abs(ewDir) <= MAX_MOVE_RANGE &&
            Math.abs(nsDir) <= MAX_MOVE_RANGE &&
            (ewDir != 0 || nsDir != 0);

  }

  /**
   * Checks to see if a given build action is within the maximum range specified by this Rule
   * Checker and if the build is not on top of the worker.
   *
   * @param ewDir Integer representing number of Horizontal steps in terms of Cells
   * @param nsDir Integer representing number of Vertical steps in terms of Cells
   * @return Boolean representing whether or not the build action is within the maximum range and is
   * not a stationary build
   */
  private boolean checkBuildRange(int ewDir, int nsDir) {
    return Math.abs(ewDir) <= MAX_BUILD_RANGE &&
            Math.abs(nsDir) <= MAX_BUILD_RANGE &&
            (ewDir != 0 || nsDir != 0);

  }

  /**
   * Checks to see if the target Cell of given action is within bounds of the game
   *
   * @param board  The Game Board
   * @param ewDir  Integer representing number of Horizontal steps in terms of Cells
   * @param nsDir  Integer representing number of Vertical steps in terms of Cells
   * @param worker Worker performing the action
   * @return Boolean representing whether or not the target Cell is within bounds
   */
  private boolean checkInBounds(Board board, int ewDir, int nsDir, Worker worker) {
    return worker.getRow() + nsDir < board.getNumRows() &&
            worker.getRow() + nsDir >= 0 &&
            worker.getColumn() + ewDir < board.getNumColumns() &&
            worker.getColumn() + ewDir >= 0;
  }


}
