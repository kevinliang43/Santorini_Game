// representation of a spreadsheet
// for the purposes of this code, rows are 0-indexed
public interface ISpreadsheet {

    //locates the information of a cell in the given position
    //returns the evaluation of the formula in the Cell
    int evaluate(int row, int col) throws Exception;

    //applying a function to a row
    //func: function being applied
    //row: int row being manipulated
    //value: int being added or multiplied to each Cell
    void applyToRow(IFunction func, int row, int value) throws Exception;


}
