import java.util.ArrayList;

/**
 * Infinite Strategy will perform places, but will go into an infinite loop
 * when it is time to return a move build.
 */
public class InfiniteStrategy implements Strategy{
  @Override
  public IAction getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {

    IAction retAction = null;
    if (b.getStatus() == Status.PLACE) {
      // Place worker
      for (int i = 0; i < Board.BOARD_X; i++) {
        if (!b.getBoard().isOccupied(i, i)) {
          retAction = new Action(Status.PLACE, Board.INVALID_WORKER_ID, i, i, null);
        }
      }

    } else {
      // Go into an infinite loop
      while (true) {

      }
    }

    return retAction;
  }
}
