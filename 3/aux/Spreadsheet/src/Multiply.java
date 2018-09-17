//represents the IFunction multiply
public class Multiply implements IFunction {

    //updated to hold two formulas so that this can be evaluated
    IFormula formula1;
    IFormula formula2;

    //constructor to hold two formulas
    Multiply(IFormula formula1, IFormula formula2) {
        this.formula1 = formula1;
        this.formula2 = formula2;
    }

    //constructor to create an empty multiply function
    //as per the specification
    Multiply() {

    }

    //apply function asked for in the specification
    public int apply(ICell cellOne, ICell cellTwo) throws Exception {
        if(cellOne.emptyCell() || cellTwo.emptyCell()) {
            throw new Exception("Cannot apply formula on an empty cell");
        }
        return ((ValueCell)cellOne).evaluate() * ((ValueCell)cellTwo).evaluate();
    }

    //apply function with a cell and a specification
    public int apply(ICell cell, int value) throws Exception {
        if(cell.emptyCell()) {
            throw new Exception("Cannot apply formula on an empty cell");
        }
        return ((ValueCell)cell).evaluate() * value;
    }

    //evaluate the two formulas
    public int evaluate() throws Exception {
        return formula1.evaluate() * formula2.evaluate();
    }

    //checks if this formula is a cell reference, returns false
    public boolean cellReference() {
        return false;
    }
}
