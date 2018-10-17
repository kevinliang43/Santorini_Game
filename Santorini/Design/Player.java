import java.util.List;

/**
 * Represents a Player
 *
 * A Player has:
 *  - A name (String)
 *  - A list of Worker's IDs
 *
 * A Player can:
 *  - Get a copy of the Board
 *  - Check if it is their turn to play
 *  - Check if the Game is over
 *  - Check if a Place Action is legal
 *  - Check if a Move Action is legal
 *  - Check if a Build Action is legal
 *
 *  - Get a list of the Workers it controls
 *
 *  - Place a Worker if it is a legal Action, and receive confirmation that the Place Action happened
 *  - Move a Worker if it is a legal Action, and receive confirmation that the Move Action happened
 *  - Build a Floor if it is a legal Action, and receive confirmation that the Build Action happened
 *
 */
public interface Player {

  /**
   * Get a read-only version of the Board
   * @return    A two dimensional array of ImmutableSquares representing the current Board state
   */
  ImmutableSquare[][] getBoard();

  /**
   * Find out if it is this Player's turn to move
   * @return    True if it is this Player's turn, false if it is their opponent's turn or the game is over
   */
  boolean myTurn();

  /**
   * Find out if the Game is over
   * @return    True if the Game is over, false if it is not.
   */
  boolean isGameOver();

  /**
   * Ask the Rule Checker if it is legal to Place a Worker on the Square (x,y)
   * @param x         x Position of the Square a Worker would be placed on
   * @param y         y Position of the Square a Worker would be placed on
   * @return          True if the Place is legal, False if it is not
   */
  boolean checkPlace(int x, int y);

  /**
   * Ask the Rule Checker if it is legal to Move a Worker to the Square (x,y)
   * @param workerID  ID of the Worker that would be Moved
   * @param x         x Position of the Square a Worker would be moved to
   * @param y         y Position of the Square a Worker would be moved to
   * @return          True if the Move is legal, False if it is not
   */
  boolean checkMove(int workerID, int x, int y);

  /**
   * Ask the Rule Checker if it is legal to Build a Floor on the Square (x,y)
   * @param workerID  ID of the Worker that would Build the Floor
   * @param x         x Position of the Square that would be built on
   * @param y         y Position of the Square that would be built on
   * @return          True if the Build is legal, False if it is not
   */
  boolean checkBuild(int workerID, int x, int y);

  /**
   * Ask for the list of WorkerIDs that this Player can use
   * @return  A List of at most 2 Integers, each unique and representing a different worker
   */
  List<Integer> getWorkers();

  /**
   * Place a new Worker on the Board at (x,y) if it is a legal Action
   * @param x         x Position to Place a Worker on the Board
   * @param y         y Position to Place a Worker on the Board
   * @return          True if the Place is legal and successful, False if it is not
   */
  boolean placeWorkerIfLegal(int x, int y);

  /**
   * Move a Worker on the Board to (x,y) if it is a legal Action
   * @param workerID  ID of the Worker that is being moved
   * @param x         x Position to Move a Worker to
   * @param y         y Position to Move a Worker to
   * @return          True if the Move is legal and successful, False if it is not
   */
  boolean moveWorkerIfLegal(int workerID, int x, int y);

  /**
   * Build a Floor on the Board to (x,y) if it is a legal Action
   * @param workerID  ID of the Worker that is building
   * @param x         x Position of the Square that is getting built on
   * @param y         y Position of the Square that is getting built on
   * @return          True if the Build is legal and successful, False if it is not
   */
  boolean buildFloorIfLegal(int workerID, int x, int y);

  /**
   * TODO
   * @return
   */
  Turn id();

}
