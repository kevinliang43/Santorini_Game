//represents an IFormula function
public interface IFunction extends IFormula{

    // used to apply a function onto two cells
    int apply(ICell cellOne, ICell cellTwo) throws Exception;

    // used to apply a function onto a cell and an integer
    int apply(ICell cell, int value) throws Exception;

}
