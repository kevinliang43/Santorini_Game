import java.util.ArrayList;
import java.util.List;

/** strategy class that decides on next turns based on whether or not the player calling it can
 * stay alive with this strategy's number of turns to look ahead.
**/
public class StayAliveStrategy implements Strategy {
  private static final boolean DEBUG = false;
  private boolean useStrat1;
  private int numTurns;

  public StayAliveStrategy(boolean useStrat1, int numTurns) {
    this.useStrat1 = useStrat1;
    this.numTurns = numTurns;
  }

  /**
   * Create a new Action based on the current state of the Board.
   *
   * @param workerIDs The integer values corresponding to the Workers this strategy is allowed to
   *                  manipulate. This should be the current Player's list of workerIDs. If a
   *                  Strategy creates an Action using a Worker that is not owned by the Player it
   *                  will be interpreted as an invalid Action.
   * @param acc       @return The next Action to perform.
   */



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
    if (b.getStatus() == Status.PLACE) {
      return placeHere(b.getBoard(), b.filterWorkers(workerIDs), this.useStrat1);
    }
    //(b.getStatus() == Status.MOVEBUILD)
    else {
      return canStayAlive(b.getBoard(), workerIDs, b.filterWorkers(workerIDs), this.numTurns);
    }
  }

  //strat1

  /**
   * Determines the next placement of a worker for the player that calls this strategy
   * @param board copy of the official game board representing the current game state
   * @param opponentWorkers list of workerID integers that represents the workers of the opposing player
   * @param strat1 boolean representing if we are using diagonal placement strategy (true) or
   *               furthest placement strategy (false)
   * @return Action representing a worker placement on the board
   */
  private Action placeHere(Board board, ArrayList<Integer> opponentWorkers, boolean strat1) {
    //placement strat 1 (diagonal)
    if(strat1) {
      for(int i = 0; i < Board.BOARD_X; i++) {
        if(!board.isOccupied(i, i)) {
          return new Action(Status.PLACE, Board.INVALID_WORKER_ID, i, i, null);
        }
      }
    }

    else {
      // placement strat 2 (farthest)
      int furthestX = -1;
      int furthestY = -1;
      float maxDistance = -1;
      for (int i = 0; i < Board.BOARD_X; i++) {
        for (int j = 0; j < Board.BOARD_Y; j++) {
          int dist = 0;
          for (int op : opponentWorkers) {
            dist += Math.pow(board.getWorkerSquare(op).getX() - i, 2) + Math.pow(board.getWorkerSquare(op).getY() - j, 2);
          }
          if (dist > maxDistance && !board.isOccupied(i, j)) {
            maxDistance = dist;
            furthestX = i;
            furthestY = j;
          }
        }
      }
      return new Action(Status.PLACE, Board.INVALID_WORKER_ID, furthestX, furthestY, null);
    }

    // Return an invalid placement
    return new Action(Status.PLACE, Board.INVALID_WORKER_ID, -1, -1, null);
  }

  //can the player calling this method stay alive

  /**
   * Decides on a next move for the current player that keeps them alive for numTurns moves
   * @param board copy of the official game board, representing current game state
   * @param myWorkers list of worker IDs for the player calling this strategy
   * @param opponentWorkers list of worker IDs for the opposing player
   * @param acc numTurns to look ahead for
   * @return if there is a valid MoveBuild that keeps this player alive in acc turns, returns the first MoveBuild seen that
   *         keeps it alive. else, returns null for the player to check.
   */
  public MoveBuild canStayAlive(Board board, ArrayList<Integer> myWorkers, ArrayList<Integer> opponentWorkers,
                              int acc) {
    //If the game is over because the player can't move, they lose, return false
    if (RuleChecker.isGameOver(board, myWorkers) == GameOverStatus.NO_MOVE_BUILD) {
      if(DEBUG) System.err.println("You lose, can't move or build");
      return null;
    }
    //If the game is over because the opponent has a worker on the winning floor, lose, return false
    if (RuleChecker.isGameOver(board, opponentWorkers) == GameOverStatus.WINNING_FLOOR) {
      if(DEBUG) System.err.println("You lose, Opponent is on third floor");
      return null;
    }

    List<MoveBuild> legalMoves = RuleChecker.listOfLegalMoves(board, myWorkers);

    //If we've reached the depth without the game being over, the player stayed alive, return true
    if(acc == 0 && legalMoves.size() > 0) {
      if(DEBUG) System.err.print("Pd");
      return legalMoves.get(0);
    } else {  //acc =/= 0
      //for every legal move,
      for (MoveBuild mb : legalMoves) {
        //go to the hypothetical board performed by that move,
        Board newBoard = RuleChecker.performAction(board, mb);
        //and see if that game is over with the player winning. If so, return true
        if(RuleChecker.isGameOver(newBoard, myWorkers) == GameOverStatus.WINNING_FLOOR) {
          if(DEBUG) System.err.println("You win, reached third floor with " + mb);
          MoveBuild mb2 = new MoveBuild(mb.getMove(), null);
          return mb2;
        }// else, see if the opponent can win from that position. If they can, continue to search
        if(canOpWin(newBoard, opponentWorkers, myWorkers, acc - 1)) {
          if(DEBUG) System.err.println("You lose, opponent can win after your " + mb);
          continue;
        }// else, the opponent can't win from that position, so the player has a valid move.
        // The player can stay alive if they play mb, return true
        if(DEBUG) System.err.println("You win, opponent can't win after your " + mb);
        return mb;
      }

      // The opponent has a winning move for every legal move the player can make.
      // The player loses, return false
      if(DEBUG) System.err.println("You lose, no winning moves");
      return null;
    }
  }

  //can the opponent of the player calling this method win from this board state

  /**
   * determines if the opposing player is able to win with the given number of turns
   * @param board copy of the official game board representing the current game state
   * @param myWorkers list of integer worker IDs of the player calling this strategy
   * @param opponentWorkers list of integer worker IDs of the opponent player
   * @param acc int representing number of turns to look ahead into
   * @return boolean representing whether or not the opponent can win or not
   */
  public boolean canOpWin(Board board, ArrayList<Integer> myWorkers, ArrayList<Integer> opponentWorkers, int acc) {
    //if the game is over because the opponent can't move, they lose, return false
    if(RuleChecker.isGameOver(board, myWorkers) == GameOverStatus.NO_MOVE_BUILD) {
      if(DEBUG) System.err.println("Opponent loses, can't move or build");
      return false;
    }
    //if the game is over because the player has won, the opponent loses, return false
    if (RuleChecker.isGameOver(board, opponentWorkers) == GameOverStatus.WINNING_FLOOR) {
      if(DEBUG) System.err.println("Opponent loses, player is on third floor");
      return false;
    }

    //if we've reached the depth without the game being over, the opponent didn't win, return false
    if (acc == 0) {
      if(DEBUG) System.err.print("Od");
      return false;
    } else {    //acc =/= 0
      //for every legal move,
      for(MoveBuild mb : RuleChecker.listOfLegalMoves(board, myWorkers)) {
        //go to the hypothetical board performed by that move,
        Board newBoard = RuleChecker.performAction(board, mb);
        //and see if that game is over with the opponent winning. If so, return true
        if(RuleChecker.isGameOver(newBoard, myWorkers) == GameOverStatus.WINNING_FLOOR) {
          if(DEBUG) System.err.println("Opponent wins, reached third floor with " + mb);
          return true;
        }
        //else, see if the player can stay alive from that position. If they can, continue to search
        if(canStayAlive(newBoard, opponentWorkers, myWorkers, acc - 1) != null) {
          if(DEBUG) System.err.println("Opponent loses, player can stay alive");
          continue;
        } // else, the player can't stay alive from that position, so the opponent has a valid move.
        // The opponent can win by playing mb, return true
        if(DEBUG) System.err.println("Opponent wins, player can't stay alive after " + mb);
        return true;
      }
      // the player has a way to stay alive after every legal move,
      // the opponent loses. return false.
      if(DEBUG) System.err.println("Opponent loses, no winning moves");
      return false;
    }
  }

  @Override
  public void sendMessage(String message) {

  }
}
