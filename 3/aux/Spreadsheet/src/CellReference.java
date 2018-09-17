//represents and IFormula reference to another cell
public class CellReference implements IFormula {

    int x;
    int y;
    //spreadsheet so we can evaluate the reference
    Spreadsheet sheet;

    CellReference(int x, int y, Spreadsheet sheet) {
        this.x = x;
        this.y = y;
        this.sheet = sheet;
    }

    //checks to see if this formula is a cell reference, returns true
    public boolean cellReference() {
        return true;
    }
    //evaluates the cell at the given reference
    public int evaluate() throws Exception{
        return sheet.evaluate(x, y);
    }
}
