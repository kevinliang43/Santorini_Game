import java.util.ArrayList;

/**
 * RuleChecker handles verifying specific rules of the Santorini game. These rules are the following:
 *   - if a place action is legal
 *   - if a move action is legal
 *   - if a build action is legal
 *   - if the game is in a GameOver state
 * RuleChecker does not care specifically about whose turn it is or which part of the turn the game
 * is in; these are to be checked in the component calling the RuleChecker.
 */
public class RuleChecker{

  private static final int WINNING_HEIGHT = 3;

  /**
   * Checks if a new Worker can be placed at (x,y). This requires that:
   *   - the (x,y) position is on the Board
   *   - the (x,y) position is not already occupied
   *
   * @param b Board that the new Worker will be placed on
   * @param x x Position the new Worker will be placed on
   * @param y y Position the new Worker will be placed on
   * @return True if the place is legal, False if it is not
   */
  public static boolean isPlaceLegal(Board b, int x, int y) {
    //todo # of all workers is less than 4
    return b.isXYOnBoard(x, y) && !b.isOccupied(x, y);
  }

  /**
   * Checks if this worker can make this move at this time A Move Action is legal if:
   *  - (x,y) is any of the up to eight neighboring fields, ({(x-1) to (x+1)}, {(y-1) to (y+1)},
   *      such that x and y are still between 0 and 5 inclusive)
   * - There is no other Worker on the target field
   * - A Worker is jumping down any number of floors
   * - The building on the target field is at most one floor taller than the one where the Worker
   *    is currently located
   *
   * @param b        Board that the Worker is moving on
   * @param workerID Worker that is Moving
   * @param x        x Position the Worker is moving to
   * @param y        y Position the Worker is moving to
   * @return True if the move is legal, False if it is not
   */
  public static boolean isMoveLegal(Board b, int workerID, int x, int y) {
    if(b.isXYOnBoard(x, y) && b.getWorkerIDs().contains(workerID)) {

      int workerX = b.getWorkerSquare(workerID).getX();
      int workerY = b.getWorkerSquare(workerID).getY();
      int workerHeight = b.getFloorHeight(workerX, workerY);
      int destinationHeight = b.getFloorHeight(x, y);

      return b.neighboring(workerX, workerY, x, y)
              && destinationHeight < Square.MAX_BUILDING_HEIGHT
              && !b.isOccupied(x, y)
              && destinationHeight <=  workerHeight + 1;
    }

    return false;
  }

  /**
   * Check if this Worker can build this floor at this time
   *
   * A Build Action is legal if:
   * - The Worker is neighboring the Square
   * - The Square at (x,y) doesn't already have four floors
   * - The Square is not occupied by a Worker
   *
   * @param b        Board that the Worker is building on
   * @param workerID Worker that is building
   * @param x        x Position the Worker is building on
   * @param y        y Position the Worker is building on
   * @return True if the build is legal, False if it is not
   */
  public static boolean isBuildLegal(Board b, int workerID, int x, int y) {

    int workerX = b.getWorkerSquare(workerID).getX();
    int workerY = b.getWorkerSquare(workerID).getY();

    try {
      return b.neighboring(workerX, workerY, x, y)
              && b.isXYOnBoard(x, y)
              && b.getFloorHeight(x, y) < Square.MAX_BUILDING_HEIGHT
              && !b.isOccupied(x, y);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Check if the game is over The game is over when:
   * - a Player’s Worker reaches a building with a
   * floor height of 3
   * - a Player can’t move a Worker to a two-story (or shorter) building
   * - a Player can move a Worker but can't add a floor to a building after the move.
   *
   * @param b Board of the game that might be over
   * @return True if the game is over, False if it is not
   */
  public static GameOverStatus isGameOver(Board b, ArrayList<Integer> playerWorkerIDs) {

    if (isWorkerOnWinningFloor(b, playerWorkerIDs))
      return GameOverStatus.WINNING_FLOOR;
    else if (!canMoveWorkerMoveAndBuild(b, playerWorkerIDs))
      return GameOverStatus.NO_MOVE_BUILD;
    return GameOverStatus.NOT_OVER;
  }

  /**
   * Checks first GameOver condition: Worker on a winning floor.
   * @param b Board to check against
   * @param playerWorkerIDs The Worker IDs to check if they are on winning floors
   * @return True if any of the worker IDs in playerWorkerIDs belongs to a Worker on a building
   *         that is WINNING_HEIGHT tall
   */
  private static boolean isWorkerOnWinningFloor(Board b, ArrayList<Integer> playerWorkerIDs) {
    for (int i : playerWorkerIDs) {
      if (b.getWorkerSquare(i).getHeight() == WINNING_HEIGHT) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks second and third GameOver conditions:
   *   - a Player cannot move a Worker to a 2 story or smaller building
   *   - a Player can move a Worker, but cannot build once moved
   * @param b The Board to check against
   * @param playerWorkerIDs The IDs of the Workers to check
   * @return True if any of the worker IDs contained in playerWorkerIDs belong to a Worker
   *         that can move and then build
   */
  private static boolean canMoveWorkerMoveAndBuild(Board b, ArrayList<Integer> playerWorkerIDs) {
    for (int id : playerWorkerIDs) {
      for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
          Square s = b.getWorkerSquare(id);
          // Skip same position
          if (x == 0 && y == 0) continue;

          // Check if Worker has move to a Square with a height of less than 3
          if (b.isXYOnBoard(s.getX() + x, s.getY() + y)
                  && b.getFloorHeight(s.getX() + x, s.getY()) < 3) {

            // Check if a Worker can build if moved to the new valid position
            if (canBuildFrom(b, id, s.getX() + x, s.getY() + y)) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  /**
   * Checks if a Worker can build from a given position. This ignores the current Worker position
   * and instead uses fromX and fromY as theoretical positions.
   * @param b Board to check against
   * @param workerID Worker ID that would theoretically move
   * @param fromX Actual x position to use
   * @param fromY Actual y position to use
   * @return True is the Worker would be able to do any build if moved to (fromX,fromY)
   */
  private static boolean canBuildFrom(Board b, int workerID, int fromX, int fromY) {
    Square workerSquare = b.getWorkerSquare(workerID);
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        if (x == 0 && y == 0) continue;
        if (!b.isXYOnBoard(fromX + x, fromY + y)) continue;
        if (b.getFloorHeight(fromX + x, fromY + y) < Square.MAX_BUILDING_HEIGHT
                && (!b.isOccupied(fromX + x, fromY + y)
                    || (workerSquare.getX() == fromX + x && workerSquare.getY() == fromY + y))) {
          return true;
        }
      }
    }

    return false;
  }


  /**
   * returns a list of legal MoveBuild actions to take from the current workers in the current game state
   * @param board copy of the official game board to represent the current game state
   * @param workerIDs list of int worker IDs of all workers on the board
   * @return list of possible moves
   */
  public static ArrayList<MoveBuild> listOfLegalMoves(Board board, ArrayList<Integer> workerIDs) {
    ArrayList<MoveBuild> result = new ArrayList<>();

    for (int w : workerIDs) {
      for(int iMove = -1; iMove <= 1; iMove++) {
        for(int jMove = -1; jMove<= 1; jMove++) {
          if(iMove == 0 && jMove == 0) {
            continue;
          }
          int x = board.getWorkerSquare(w).getX();
          int y = board.getWorkerSquare(w).getY();
          String name = board.getWorkerSquare(w).getWorkerName();

          if(isMoveLegal(board, w,   x + iMove,y + jMove)) {

            Action moveAction = new Action(Status.MOVE, w, x + iMove, y + jMove, name);

            for(int iBuild = -1; iBuild <= 1; iBuild++) {
              for (int jBuild = -1; jBuild <= 1; jBuild++) {

                if(isBuildLegal(performAction(board, moveAction), w, x + iMove + iBuild, y + jMove + jBuild)) {
                  Action buildAction = new Action(Status.BUILD, w, x + iMove + iBuild, y + jMove + jBuild, name);
                  result.add(new MoveBuild(moveAction, buildAction));

                }
              }
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * returns a new board based after the given action has been executed
   * @param board board to perform action on
   * @param action action to perform on board
   * @return new Board with the given action taken on it
   */
  public static Board performAction(Board board, Action action) {
    Board result = new Board(board);

    if(action.getActionType() == Status.MOVE) {
      result.moveWorker(action.getWorkerID(), action.getX(), action.getY());
    }
    if(action.getActionType() == Status.BUILD) {
      result.buildFloor(action.getX(), action.getY());
    }

    return result;
  }

  /**
   * returns a new board based after the given moveBuild has been executed
   * @param board board to perform moveBuild on
   * @param moveBuild action to perform on board
   * @return new Board with the given moveBuild taken on it
   */
  public static Board performAction(Board board, MoveBuild moveBuild) {
    Board result = new Board(board);

    result.moveWorker(moveBuild.getWorkerID(), moveBuild.getxMove(), moveBuild.getyMove());

    result.buildFloor(moveBuild.getxBuild(), moveBuild.getyBuild());

    return result;
  }

}
