/**
 * Translator Class to convert Board states into JSON.
 * Utils Static Class
 */
public class Translator {

  /**
   * Converts a Movebuild Action into its respective JSON String
   * @param board Current Board State
   * @param action action being made
   * @return JSON String representing the corresponding moveBuild.
   */
  public static String moveBuildAsJSON(Board board, MoveBuild action) {
    String result = "[\"";
    result += action.workerName + "\", ";

    Square initialSquare = board.getWorkerSquare(action.workerID);
    int relMoveX = action.xMove - initialSquare.getX();
    int relMoveY = action.yMove - initialSquare.getY();

    result += getDirection(relMoveX, relMoveY);

    if (action.build != null) {
      int relBuildX = action.xBuild - action.xMove;
      int relBuildY = action.yBuild - action.yMove;
      result += ", " + getDirection(relBuildX, relBuildY);
    }

    result += "]";
    return result;

  }

  /**
   * Convert a Direction into a JSON String direction
   * @param x relative direction of x axis
   * @param y relative direction of y axis
   * @return
   */
  private static String getDirection(int x, int y) {
    String retString = "";
    switch (y) {
      case -1:
        retString += "\"WEST\", ";
        break;
      case 0:
        retString += "\"PUT\", ";
        break;
      case 1:
        retString += "\"EAST\", ";
        break;
    }
    switch (x) {
      case -1:
        retString += "\"NORTH\"";
        break;
      case 0:
        retString += "\"PUT\"";
        break;
      case 1:
        retString += "\"SOUTH\"";
        break;

    }
    return retString;
  }

}