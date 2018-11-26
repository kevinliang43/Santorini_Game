import java.util.ArrayList;

/**
 * Infinite Strategy will perform places, but will go into an infinite loop
 * when it is time to return a move build.
 */
public class InfiniteStrategy implements Strategy{


  /**
   * Create a new Action based on the current state of the Board.
   *
   * @param b         The current state of the Board and the current turn Status.
   * @param workerIDs The integer values corresponding to the Workers this strategy is allowed to
   *                  manipulate. This should be the current Player's list of workerIDs. If a
   *                  Strategy creates an Action using a Worker that is not owned by the Player it
   *                  will be interpreted as an invalid Action.
   * @return The next Action to perform.
   */
  @Override
  public IAction getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {

    IAction retAction = null;
    if (b.getStatus() == Status.PLACE) {
      // Place worker
      for (int i = 0; i < Board.BOARD_X; i++) {
        if (!b.getBoard().isOccupied(i, i)) {
          retAction = new Action(Status.PLACE, Board.INVALID_WORKER_ID, i, i, null);
          break;
        }
      }

    } else {
      // Go into an infinite loop
      while (true) {

      }
    }

    return retAction;
  }

  @Override
  public void sendMessage(String message) {

  }
}
