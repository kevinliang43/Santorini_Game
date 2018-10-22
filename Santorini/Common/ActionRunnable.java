/**
 * Action Runnable created to run on separate threads to check for time outs
 */
public class ActionRunnable implements Runnable {

  Player p;
  Board board;
  Status status;
  IAction action;

  /**
   * Constructor for an Action Runnable
   * @param p Player to get command from
   */
  ActionRunnable(Player p, Board board, Status status) {
    this.p = p;
    this.board = board;
    this.status = status;
    this.action = null;

  }

  /**
   * Runs the getNextAction function in the player class
   */
  @Override
  public void run() {
    IAction nextAction = p.getNextAction(this.board, this.status);
    this.action = nextAction;
  }

  /**
   * gets the action gotten by the getNextAction call within the run function
   * @return Action next move
   */
  public IAction getAction() {
    return this.action;
  }

}
