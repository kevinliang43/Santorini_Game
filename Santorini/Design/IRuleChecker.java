/**
 * Rule Checker
 *
 * The Rule Checker is accessible to both the Administrator and the Players,
 * for checking the basic rules of the game. A Player or an Administrator can use the
 * Rule Checker to check the validity of an Action at any time without mutating the state of the board.
 *
 * An Action is one of:
 *  - Place
 *  - Move
 *  - Build
 *
 * Santorini is governed by the following basic rules.
 *
 * On a Player's turn:
 *  - The Player must move one of their own Workers
 *  - If the Worker has been moved to a third floor, they win and the game is over.
 *  - Otherwise, the Player must use this Worker to add a floor to a neighboring building
 *
 * A Rule Checker can:
 *  - Check if a Place action is legal
 *  - Check if a Move action is legal
 *  - Check if a Build action is legal
 *  - Check if the game is over
 *
 */
public interface IRuleChecker {

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
  boolean isPlaceLegal(Board b, int x, int y);

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
  boolean isMoveLegal(Board b, int workerID, int x, int y);

  /**
   * Check if this Worker can build this floor at this time
   * @param b         Board that the Worker is building on
   * @param workerID  Worker that is building
   * @param t         Turn status indicating who's turn it is
   * @param s         Status of Turn (i.e. Place, Build, Move, Game Over)
   * @param x         x Position the Worker is building on
   * @param y         y Position the Worker is building on
   * @return          True if the build is legal, False if it is not
   */
  boolean isBuildLegal(Board b, Player p, Turn t, Status s, int workerID, int x, int y);


  /**
   * Check if the game is over
   *    The game is over when:
   *      - a Player’s Worker reaches a building with a floor height of 3
   *      - a Player can’t move a Worker to a two-story (or shorter) building
   *      - a Player can move a Worker but can't add a floor to a building after the move.
   *
   * @param b         Board of the game that might be over
   * @param t         Turn status indicating who's turn it is
   * @param s         Status of Turn (i.e. Place, Build, Move, Game Over)
   * @return          True if the game is over, False if it is not
   */
  boolean isGameOver(Board b, Turn t, Status s);

}
