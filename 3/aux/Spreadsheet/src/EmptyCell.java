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

    @Override
    public boolean emptyCell() {
        return true;
    }
}
