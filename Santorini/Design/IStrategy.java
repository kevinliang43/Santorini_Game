import java.util.ArrayList;

/**
 * A Strategy object is responsible for generating an Action given
 * the state of the Board, the list of Workers it can manipulate,
 * and the Status of the turn (PLACE, MOVE, or BUILD).
 */
public interface IStrategy {

  /**
   * Create a new Action based on the current state of the Board.
   * @param b The current state of the Board and the current turn Status.
   * @param workerIDs The integer values corresponding to the Workers this
   *                  strategy is allowed to manipulate. This should be the
   *                  current Player's list of workerIDs. If a Strategy
   *                  creates an Action using a Worker that is not owned by
   *                  the Player it will be interpreted as an invalid Action.
   * @return The next Action to perform.
   */
  Action getNextAction(BoardStatus b, ArrayList<Integer> workerIDs);
}
