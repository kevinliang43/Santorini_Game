//represents a cell with a value in it in the form of an IFormula
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

    //evaluates the formula in this cell
    public int evaluate() throws Exception{
        return this.formula.evaluate();
    }

    //checks if this is an empty cell, returns false
    public boolean emptyCell() {
        return false;
    }
}
