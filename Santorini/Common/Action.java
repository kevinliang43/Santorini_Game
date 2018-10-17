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
public class Action {
  public final Status actionType;
  public final int x, y;
  public final int workerID;

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
  public Action(Status actionType, int workerID, int x, int y) {
    if (actionType == Status.GAMEOVER) {
      throw new IllegalArgumentException("GAMEOVER is not a valid Action Type");
    } else if (actionType == null) {
      throw new IllegalArgumentException("actionType cannot be null");
    }
    this.actionType = actionType;
    this.workerID = workerID;
    this.x = x;
    this.y = y;
  }


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
}