import java.awt.*;

class Spreadsheet {
    Spreadsheet(int r, int c){

    }

    void addCell(Cell cell) {

    }

    String getValueAt(Point point){
        return "";
    }
}

interface IFormula {

}

class Cell {
    Cell(int r, int c, IFormula formula) {

    }
}

class Constant implements IFormula {
    Constant(int number) {

    }
}

class cellReference implements IFormula {
    cellReference(Point point) {

    }
}

class formula implements IFormula {
    formula(IFormula f1, IFormula f2, boolean addition){

    }
}


