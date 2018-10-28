/**
 * class contiaining 2 acitons
 */
public class MoveBuild implements IAction{

  public final Action move;
  public final Action build;

  public final Status actionType = Status.MOVEBUILD;
  public final int xMove, yMove, xBuild, yBuild;
  public final int workerID;
  public final String workerName;

  MoveBuild(Action move, Action build) {

    this.move = move;
    this.build = build;

//    if(move.workerID != build.workerID) {
//      throw new IllegalStateException("Move and Build must be performed on the same Worker.");
//    }
    workerID = move.workerID;
    workerName = move.workerName;
    xMove = move.x;
    yMove = move.y;
    if (build != null) {
      xBuild = build.x;
      yBuild = build.y;
    }
    else {
      xBuild = -1;
      yBuild = -1;
    }

  }

  /**
   * converts this moveBuild into an easy to read string format
   * @return String representation of this moveBuild
   */
  public String toString() {
    return this.move.toString() + " " + this.build.toString();
  }

}
