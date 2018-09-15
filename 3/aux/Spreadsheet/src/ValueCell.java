public class ValueCell implements ICell {

    //represents the row the cell is in
    int x;
    //represents the column the cell is in
    int y;
    //the value of this cell, null if no data
    IFormula formula;

    //constructor for a cell with a given formula value
    ValueCell(int x, int y, IFormula formula) {
        this.x = x;
        this.y = y;
        this.formula = formula;
    }

    public int evaluate() {
        return this.formula.evaluate();
    }

    @Override
    public boolean emptyCell() {
        return false;
    }
}
