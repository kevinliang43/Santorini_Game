import java.util.ArrayList;

/**
 * Strategy that goes into an infinite loop during the place phase.
 */
public class InfinitePlaceStrategy implements Strategy {

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
      while (true) {

      }

    } else {

    }

    return retAction;
  }

  @Override
  public void sendMessage(String message) {

  }
}

