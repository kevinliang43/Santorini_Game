package spreadsheet;

public class Point implements Value {

  int x, y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String getValue() {
    return Sheet.cellArray[x][y].getValue();
  }
}
