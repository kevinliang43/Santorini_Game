// an IFormula is oneOf: IFunction, Cell, Integer
//represents the value of a cell

//for the formulas, we added references to two other formulas so that we can evaluate the formula in each cell
public interface IFormula {
    int evaluate() throws Exception;
    boolean cellReference();
}
