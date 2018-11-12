/**
 * class contiaining 2 acitons
 */
public class MoveBuild implements IAction{

  private final Action move;
  private final Action build;

  private final Status actionType = Status.MOVEBUILD;
  private final int xMove, yMove, xBuild, yBuild;
  private final int workerID;
  private final String workerName;

  MoveBuild(Action move, Action build) {

    this.move = move;
    this.build = build;

//    if(move.workerID != build.workerID) {
//      throw new IllegalStateException("Move and Build must be performed on the same Worker.");
//    }
    workerID = move.getWorkerID();
    workerName = move.getWorkerName();
    xMove = move.getX();
    yMove = move.getY();
    if (build != null) {
      xBuild = build.getX();
      yBuild = build.getY();
    }
    else {
      xBuild = -1;
      yBuild = -1;
    }

  }

  /**
   * Getter for the Move
   * @return Move Action
   */
  public Action getMove() {
    return move;
  }

  /**
   * Getter for the Build
   * @return Build Action
   */
  public Action getBuild() {
    return build;
  }

  /**
   * converts this moveBuild into an easy to read string format
   * @return String representation of this moveBuild
   */
  public String toString() {
    return this.move.toString() + " " + this.build.toString();
  }

  /**
   * Getter for ActionType
   * @return Action Type
   */
  public Status getActionType() {
    return actionType;
  }

  /**
   * Getter for X value for build
   * @return X value for build
   */
  public int getxBuild() {
    return this.xBuild;
  }

  /**
   * Getter for Y Value For build
   * @return Y value for build
   */
  public int getyBuild() {
    return this.yBuild;
  }

  /**
   * Getter for X value for Move
   * @return X value for Move
   */
  public int getxMove() {
    return this.xMove;
  }

  /**
   * Getter for Y Value for Move
   * @return Y value for Move
   */
  public int getyMove() {
    return this.yMove;
  }

  /**
   * Getter for the WorkerID
   * @return workerID
   */
  public int getWorkerID() {
    return workerID;
  }

  /**
   * Getter for Worker Name
   * @return WorkerName
   */
  public String getWorkerName() {
    return workerName;
  }

}
