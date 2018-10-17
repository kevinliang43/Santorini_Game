/**
 * class contiaining 2 acitons
 */
public class MoveBuild {

  public final Action move;
  public final Action build;

  public final Status actionType = Status.MOVEBUILD;
  public final int xMove, yMove, xBuild, yBuild;
  public final int workerID;

  MoveBuild(Action move, Action build) {

    this.move = move;
    this.build = build;

    if(move.workerID != build.workerID) {
      throw new IllegalStateException("Move and Build must be performed on the same Worker.");
    }
    workerID = move.workerID;
    xMove = move.x;
    yMove = move.y;
    xBuild = build.x;
    yBuild = build.y;

  }

  public String toString() {
    return this.move.toString() + " " + this.build.toString();
  }

}
