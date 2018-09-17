//represents an empty cell
public class EmptyCell implements ICell {
    //represents the row the cell is in
    int x;
    //represents the column the cell is in
    int y;

    //constructor for an empty cell
    EmptyCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //checks if this is an empty cell, returns true
    public boolean emptyCell() {
        return true;
    }
}
