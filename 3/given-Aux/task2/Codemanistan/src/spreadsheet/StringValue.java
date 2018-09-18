package spreadsheet;

public class StringValue implements Value {
  private String s;

  StringValue(String s) {
    this.s = s;
  }

  @Override
  public String getValue() {
    return s;
  }
}
