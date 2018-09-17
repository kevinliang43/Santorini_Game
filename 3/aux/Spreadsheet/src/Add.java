public class Add implements IFunction {

    IFormula formula1;
    IFormula formula2;

    //Constructor with two formula values
    Add(IFormula formula1, IFormula formula2) {
        this.formula1 = formula1;
        this.formula2 = formula2;
    }

    //Empty constructor as per the soecification details
    Add() {

    }

    //apply function asked for in the specification
    public int apply(ICell cellOne, ICell cellTwo) throws Exception {
        if(cellOne.emptyCell() || cellTwo.emptyCell()) {
            throw new Exception("Cannot apply formula on an empty cell");
        }
        return ((ValueCell)cellOne).evaluate() + ((ValueCell)cellTwo).evaluate();
    }

    //apply to add a cell value with a non-cell value
    public int apply(ICell cell, int value) throws Exception {
        if(cell.emptyCell()) {
            throw new Exception("Cannot apply formula on an empty cell");
        }
        return ((ValueCell)cell).evaluate() + value;
    }

    //to evaluate the two formulas
    public int evaluate() throws Exception{
        return formula1.evaluate() + formula2.evaluate();
    }

    //checks if this formula is a cell reference, returns false
    public boolean cellReference() {
        return false;
    }
}
