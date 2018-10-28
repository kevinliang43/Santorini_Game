/**
 * Created by KevinLiang on 10/28/18.
 */
public class Translator {

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