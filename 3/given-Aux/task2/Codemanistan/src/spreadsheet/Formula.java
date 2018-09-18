package spreadsheet;

public class Formula implements Value{

  private boolean operation; //false means there's only a constant, true means there's an operation
  private boolean plus; //true is addition, false is multiplication

  private Formula formula1;
  private Formula formula2;

  private Double value;

  public Formula(Double value) {
    operation = false;
    this.value = value;
  }

  public Formula(Formula f1, Formula f2, boolean plus) {
    this.operation = true;

    this.plus = plus;

    this.formula1 = f1;
    this.formula2 = f2;

    this.value = evaluate();

  }

  private Double evaluate() {
    if (operation) {
      if(plus) {
        this.value = formula1.evaluate() + formula2.evaluate();
      } else {
        this.value = formula1.evaluate() * formula2.evaluate();
      }
    }
    return value;
  }

  @Override
  public String getValue() {
    return this.evaluate().toString();
  }

}
