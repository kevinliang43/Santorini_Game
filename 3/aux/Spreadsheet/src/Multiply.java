public class Multiply implements IFunction {

    IFormula formula1;
    IFormula formula2;

    Multiply(IFormula formula1, IFormula formula2) {
        this.formula1 = formula1;
        this.formula2 = formula2;
    }

    public int apply(ICell cellOne, ICell cellTwo) throws Exception {
        if(cellOne.emptyCell() || cellTwo.emptyCell()) {
            throw new Exception("Cannot apply formula on an empty cell");
        }
        return ((ValueCell)cellOne).evaluate() * ((ValueCell)cellTwo).evaluate();
    }

    public int apply(ICell cell, int value) throws Exception {
        if(cell.emptyCell()) {
            throw new Exception("Cannot apply formula on an empty cell");
        }
        return ((ValueCell)cell).evaluate() * value;
    }

    public int evaluate() {
        return formula1.evaluate() * formula2.evaluate();
    }
}
