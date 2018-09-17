
import org.junit.Assert;
import org.junit.Test;

public class TestClass {
    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("TestClass");
    }

    Spreadsheet testSheet;
    void initialize() {
        this.testSheet = new Spreadsheet(3, 3);

    }

    @Test
    //tests that a sheet is initialized with empty cells correctly
    public void testPrintBlankSheet() throws Exception {
        initialize();
        String actualOut = this.testSheet.printSpreadsheet();
        String expectedOut = "X \t X \t X \t \n X \t X \t X \t \n X \t X \t X \t \n ";
        Assert.assertEquals(expectedOut, actualOut);

    }

    @Test
    //tests that adding a value to the sheet works
    public void testAddValueConstant() throws Exception {
        initialize();

        //adding the constant 4 to position (1,0) in the spreadsheet
        Constant constant = new Constant(4);
        ValueCell cell = new ValueCell(1, 0, constant);
        this.testSheet.addCell(cell, 1, 0);

        String actualOut = this.testSheet.printSpreadsheet();

        String expectedOut = "X \t X \t X \t \n 4 \t X \t X \t \n X \t X \t X \t \n ";
        Assert.assertEquals(expectedOut, actualOut);

    }

    @Test
    //tests that adding a cell reference computes correctly
    public void testAddValueCellReference() throws Exception {
        initialize();

        //adding the constant 4 to position (1,0) in the spreadsheet
        Constant constant = new Constant(4);
        ValueCell cell1 = new ValueCell(1, 0, constant);
        this.testSheet.addCell(cell1, 1, 0);

        CellReference ref = new CellReference(1, 0, testSheet);
        ValueCell cell2 = new ValueCell(0, 1, ref);
        this.testSheet.addCell(cell2, 0, 1);
        String actualOut = this.testSheet.printSpreadsheet();

        String expectedOut = "X \t 4 \t X \t \n 4 \t X \t X \t \n X \t X \t X \t \n ";
        Assert.assertEquals(expectedOut, actualOut);

    }

    @Test
    //tests that adding a multiply function cell computes correctly
    public void testAddValueMultiply() throws Exception {
        initialize();

        //adding the constant 4 to position (1,0) in the spreadsheet
        Constant constant1 = new Constant(4);
        ValueCell cell1 = new ValueCell(1, 0, constant1);
        this.testSheet.addCell(cell1, 1, 0);

        Constant constant2 = new Constant(3);
        ValueCell cell2 = new ValueCell(2, 0, constant2);
        this.testSheet.addCell(cell2, 2, 0);

        CellReference ref = new CellReference(1, 0, testSheet);
        ValueCell cell3 = new ValueCell(0, 1, ref);
        this.testSheet.addCell(cell3, 0, 1);

        Multiply function = new Multiply(constant2, ref);
        ValueCell cell4 = new ValueCell(2, 2, function);
        this.testSheet.addCell(cell4, 2,2);

        String actualOut = this.testSheet.printSpreadsheet();

        String expectedOut = "X \t 4 \t X \t \n 4 \t X \t X \t \n 3 \t X \t 12 \t \n ";
        Assert.assertEquals(expectedOut, actualOut);

    }

    @Test
    //tests that adding a multiply function cell computes correctly
    public void testMultFunction() throws Exception {
        initialize();

        //adding the constant 4 to position (1,0) in the spreadsheet
        Constant constant1 = new Constant(4);
        ValueCell cell1 = new ValueCell(1, 0, constant1);
        this.testSheet.addCell(cell1, 1, 0);

        Constant constant2 = new Constant(3);
        ValueCell cell2 = new ValueCell(2, 0, constant2);
        this.testSheet.addCell(cell2, 2, 0);

        Multiply mult = new Multiply();

        Assert.assertEquals(mult.apply(cell1, cell2), 12);

    }

    @Test
    //tests that adding a multiply function cell computes correctly
    public void testAddWithCellReference() throws Exception {
        initialize();

        //adding the constant 4 to position (1,0) in the spreadsheet
        Constant constant1 = new Constant(4);
        ValueCell cell1 = new ValueCell(1, 0, constant1);
        this.testSheet.addCell(cell1, 1, 0);


        CellReference ref = new CellReference(1, 0, testSheet);
        ValueCell cell2 = new ValueCell(0, 1, ref);
        this.testSheet.addCell(cell2, 0, 1);

        Add add = new Add();

        Assert.assertEquals(add.apply(cell2, cell1), 8);

    }


}

