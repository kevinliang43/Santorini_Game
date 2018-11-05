import java.util.ArrayList;

/**
 * Strategy Class that breaks the rules on the movebuild.
 */
public class BreakerStrategy implements Strategy {
  @Override
  public IAction getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {

    IAction retAction = null;
    if (b.getStatus() == Status.PLACE) {
      // Place worker
      for(int i = 0; i < Board.BOARD_X; i++) {
        if(!b.getBoard().isOccupied(i, i)) {
          retAction = new Action(Status.PLACE, Board.INVALID_WORKER_ID, i, i, null);
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
}
