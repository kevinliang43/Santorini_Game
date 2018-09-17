import javafx.scene.control.Cell;
import sun.awt.SunHints;

import java.util.ArrayList;

public class Spreadsheet implements ISpreadsheet {

    ArrayList<ArrayList<ICell>> sheet;
    //arbitrary number of rows chosen to initialize the grid
    int numRows = 50;
    //arbitrary number of columns chosen to initialize the grid
    int numCols = 50;

    //constructor for a spreadsheet that uses the default 50x50 grid
    Spreadsheet() {
        this.sheet = new ArrayList<>();
        //initializing the grid with empty cells without formula values
        //for loop iterating through rows
        this.initialize();
    }

    //constructor
    Spreadsheet(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.initialize();
    }

    // initializes the spreadsheet to be filled with empty cells
    void initialize() {
        this.sheet = new ArrayList<>(numRows);
        for (int col = 0; col < numCols; col++) {
            ArrayList<ICell> initList = new ArrayList<>(numCols);
            this.sheet.add(initList);
        }
        for (int r = 0; r < numRows; r++){
            //for loop iterating through columns
            for(int c = 0; c < numCols; c++) {
                sheet.get(r).add(c, new EmptyCell(r, c));
            }
        }
    }

    // evaluates the cell in the given position
    public int evaluate(int row, int col) throws Exception {
        //if the sheet doesn't have a cell in the given location, throw a null pointer exception
        if (sheet.get(row).get(col).emptyCell()) {
            throw new NullPointerException("This cell is empty, there is no formula.");
        }
        else {
            //checked that this is not an empty cell so we evaluate it
            return ((ValueCell)sheet.get(row).get(col)).evaluate();
        }
    }

    //takes in a row and a value
    //applies the functions with the value to every cell that has a value (has a formula value)
    public void applyToRow(IFunction func, int row, int value) throws Exception {
        for (int c = 0; c < numCols; c++) {
            ICell cell = this.sheet.get(row).get(c);
            //checks if it's an empty cell
            if (!cell.emptyCell()) {
                //applies the formula the the value in this cell and the given value
                ((ValueCell)this.sheet.get(row).get(c)).formula = new Constant(func.apply(cell, value));
            }
        }
    }

    //adds the given cell into the spreadsheet if given a cell
    public void addCell (ICell cell, int row, int col) {
        //adds rows and columns to the sheet until there are enough represented to input this cell
        while (row >= numRows) {
            this.addRow();
        }
        while (col >= numCols) {
            this.addCol();
        }
        this.sheet.get(row).set(col, cell);
    }

    //adds the given cell into the spreadsheet if given a function object
    public void addCell (IFunction function, int row, int col) {
        //adds rows and columns to the sheet until there are enough represented to input this cell
        while (row >= numRows) {
            this.addRow();
        }
        while (col >= numCols) {
            this.addCol();
        }
        this.sheet.get(row).set(col, new ValueCell(row, col, function));
    }

    //adds a row of empty cells to the bottom of the sheet
    private void addRow() {
        //create a new Array of cells representing the last row
        ArrayList<ICell> newRow = new ArrayList<>(numCols);
        for (int c = 0; c < numCols; c++) {
            newRow.add(new EmptyCell(numRows + 1, c));
        }
        this.sheet.add(newRow);
        numRows++;
    }

    //adds a column of empty cells to the right of the sheet
    private void addCol() {
        for (int r = 0; r < numRows; r++) {
            this.sheet.get(r).add(new EmptyCell(r, numCols + 1));
        }
        numCols++;

    }

    //Prints the spreadsheet for testing purposes
    String printSpreadsheet() throws Exception {
        String s = "";
        for (int r = 0; r < numRows; r++){
            for (int c = 0; c < numCols; c++) {
                ICell cell = this.sheet.get(r).get(c);
                if (cell.emptyCell()) {
                    s += "X \t ";
                }
                else {
                    s += (evaluate(r, c) + " \t ");
                }

            }
            s += "\n ";

        }
        System.out.println(s);
        return s;
    }
}
