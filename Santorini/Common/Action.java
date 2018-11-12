/**
 * Immutable representation of an action to perform on the Board. It contains
 * the action type it is representing. This is one of the following Status enum values:
 *   - PLACE
 *   - MOVE
 *   - BUILD
 * GAMEOVER is an invalid action type Status. The workerID is used to represent
 * the Worker performing the action. It is ignored in the PLACE case. x and y are
 * the position to perform the Action on.
 *   - PLACE: place a Worker at (x,y). workerID is ignored.
 *   - MOVE: move specified Worker from current location to (x,y).
 *   - BUILD: build a floor at the location (x,y) using the Worker with workerID.
 * Any Action should adhere to rules defined in RuleChecker (ex Worker can only build
 * on a neighboring building).
 */
public class Action implements IAction{
  private final Status actionType;
  private final int x, y;
  private final int workerID;
  private final String workerName;

  /**
   * Construct an Action for one of the three action types defined in Status:
   *   - PLACE
   *   - MOVE
   *   - BUILD
   * @param actionType The type of Action this is. GAMEOVER is not a valid Action
   * @param workerID The ID of the Worker to work on. In the case of PLACE, workerID
   *                 is ignored.
   * @param x        The x position to perform this Action on. Refer to class
   *                 description for details.
   * @param y        The y position to perform this Action on. Refer to class
   *                 description for details.
   * @throws IllegalArgumentException if passed Status is GAMEOVER.
   */
  public Action(Status actionType, int workerID, int x, int y, String workerName) {
    if (actionType == Status.GAMEOVER) {
      throw new IllegalArgumentException("GAMEOVER is not a valid Action Type");
    } else if (actionType == null) {
      throw new IllegalArgumentException("actionType cannot be null");
    }
    this.workerName = workerName;
    this.actionType = actionType;
    this.workerID = workerID;
    this.x = x;
    this.y = y;
  }


  /**
   * conerts this action into an easy to read string
   * @return String format of this action
   */
  public String toString() {
    String action = "";
    if(actionType == Status.MOVE) {
      action = "move ";
    }
    if(actionType == Status.BUILD) {
      action = "build ";
    }
    if(actionType == Status.PLACE) {
      action = "place ";
    }

    return action + workerID + " to " + x + "," + y;
  }

  /**
   * Getter for ActionType
   * @return Action Type
   */
  public Status getActionType() {
    return actionType;
  }

  /**
   * Getter for X value
   * @return X value
   */
  public int getX() {
    return x;
  }

  /**
   * Getter for Y Value
   * @return Y value
   */
  public int getY() {
    return y;
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