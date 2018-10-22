/**
 * Created by KevinLiang on 10/22/18.
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

  @Override
  public void run() {
    IAction nextAction = p.getNextAction(this.board, this.status);
    this.action = nextAction;
  }

  public IAction getAction() {
    return this.action;
  }

}
