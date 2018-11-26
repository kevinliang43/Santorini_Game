import java.util.ArrayList;

/**
 * Strategy Class that breaks the rules on the MoveBuild
 */
public class BreakerStrategy implements Strategy {

  //Constructor
  BreakerStrategy() {}

  /**
   * get Next action for a Breaker Strategy will move to an invalid location eventually.
   * @param b The current state of the Board and the current turn Status.
   * @param workerIDs The integer values corresponding to the Workers this
   *                  strategy is allowed to manipulate. This should be the
   *                  current Player's list of workerIDs. If a Strategy
   *                  creates an Action using a Worker that is not owned by
   *                  the Player it will be interpreted as an invalid Action.
   * @return MoveBuild
   */
  @Override
  public IAction getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {

    IAction retAction = null;
    if (b.getStatus() == Status.PLACE) {
      // Place worker
      for(int i = 0; i < Board.BOARD_X; i++) {
        if(!b.getBoard().isOccupied(i, i)) {
          retAction = new Action(Status.PLACE, Board.INVALID_WORKER_ID, i, i, null);
          break;
        }
      }

    }
    else {
      // Return a movebuild that breaks the rules
      String workerName = b.getWorkerSquare(workerIDs.get(0)).getWorkerName();
      Action action = new Action(Status.MOVE, workerIDs.get(0), -1, -1, workerName);
      Action build = new Action(Status.BUILD, workerIDs.get(0), -1, -1, workerName);
      retAction = new MoveBuild(action, build);
    }

    return retAction;
  }

  @Override
  public void sendMessage(String message) {

  }
}
