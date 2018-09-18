package spreadsheet;

public class Cell {

  private Point point;
  private Value value;

  Cell(Value v) {
    this.value = v;
  }

  public void setPoint(Point p) {
    this.point = p;
  }

  public String getValue() {
    return value.getValue();
  }
}
