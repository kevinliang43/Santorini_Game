

/**
 * Created by KevinLiang on 9/30/18.
 */
public class Board {

  private static final int MAX_ROWS = 6;
  private static final int MAX_COLUMNS = 6;
  int numRows;
  int numColumns;
  Cell[][] gameBoard;

  Board(int numRows, int numColumns) {
    if (numRows <= 0 || numRows > MAX_ROWS) {
      throw new IllegalArgumentException("Cannot Create a Board with less than 1 row " +
              "OR more than " + MAX_ROWS + " rows.");
    }
    if (numColumns <= 0 || numColumns > MAX_COLUMNS) {
      throw new IllegalArgumentException("Cannot Create a Board with less than 1 column +" +
              "OR more than " + MAX_COLUMNS + " columns.");
    }

    this.numColumns = numColumns;
    this.numRows = numRows;
    initializeBoard();
  }



  /**
   * Determines if a Cell exists in the direction faced by a given Worker
   * @param worker Represents the Worker that this query is being called from
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @return Boolean representing whether or not the neighbor Cell exists
   * @throws IllegalArgumentException thrown when trying to determine neighbor status of a Cell that
   *                                   is not adjacent to the given Worker.
   */
  public boolean neighborQuery(Worker worker, int rDir, int cDir) throws IllegalArgumentException{
    if (rDir > 1 || rDir < -1 || cDir > 1 || cDir < -1) {
      throw new IllegalArgumentException("Worker can only perform actions on adjacent Cells.");
    }
    return !(worker.getColumn() + cDir >= MAX_COLUMNS ||
            worker.getColumn() + cDir < 0 ||
            worker.getRow() + rDir >= MAX_ROWS ||
            worker.getRow() + rDir < 0);

  }

  /**
   * Moves a given Worker to a Cell that the given Worker is facing.
   * @param worker Represents the Worker that is building a Floor
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @throws IllegalStateException Thrown in one of 4 Cases:
   *                                1. Worker has already moved.
   *                                2. Cell being moved to is already occupied.
   *                                3. Cell being moved to is not adjacent.
   *                                4. Cell being moved to is out of bounds.
   *                                5. Worker cannot move to Cell containing Worker.
   */
  public void move(Worker worker, int rDir, int cDir) throws IllegalStateException {
    worker.move(getNextCell(worker, rDir, cDir));
  }

  /**
   * Builds another Floor onto the Cell that the given Worker is facing.
   * @param worker Represents the Worker that is being moved
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @throws IllegalStateException Thrown in one of 5 Cases:
   *                                1. Worker has NOT moved
   *                                2. Worker cannot build on Cell with height 4
   *                                3. Cell being moved to is not adjacent.
   *                                4. Cell being moved to is out of bounds.
   *                                5. Worker cannot build on Cell that is occupied.
   */
  public void build(Worker worker, int rDir, int cDir) throws IllegalStateException {
    worker.build(getNextCell(worker, rDir, cDir));

  }

  /**
   * Determine if a Cell that is faced by a given Worker in the given Direction is occupied.
   * @param worker Represents the Worker that this query is being called from
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @return Boolean representing whether or not the target Cell is occupied or not.
   * @throws IllegalStateException Thrown when trying to check occupancy of a Cell that is out
   *                                  of bounds or is not adjacent to the given Worker.
   */
  public boolean occupyQuery(Worker worker, int rDir, int cDir) throws IllegalStateException {
    try {
      return getNextCell(worker, rDir, cDir).containsWorker();
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot determine occupancy of a Cell that is not adjacent to" +
              "the given Worker nor a Cell that is out of bounds.");
    }
  }


  /**
   * Returns the height of an adjacent Cell in the given direction faced by a given Worker
   * @param worker Represents the Worker that this query is being called from
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @return an Integer representing the height of the Cell faced by the given Worker.
   * @throws IllegalStateException Thrown when trying to return height of a Cell that is out
   *                                  of bounds or is not adjacent to the given Worker.
   */
  public int heightQuery(Worker worker, int rDir, int cDir) throws IllegalStateException {
    try {
      return getNextCell(worker, rDir, cDir).getHeight();
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot determine height of a Cell that is not adjacent to" +
              "the given Worker nor a Cell that is out of bounds.");
    }
  }



  /**
   * Initializes the Board with new Cells
   */
  private void initializeBoard() {
    this.gameBoard = new Cell[this.numRows][this.numColumns];

    for (int i = 0; i < this.numRows; i++) {
      for (int j = 0; j < this.numColumns; j++) {
        this.gameBoard[i][j] = new Cell(i, j);
      }
    }
  }

  /**
   *
   * @param row
   * @param column
   * @return
   */
  public Cell getCell(int row, int column) {
    if (row < 0 || row >= MAX_ROWS || column < 0 || column >= MAX_COLUMNS) {
      throw new IllegalArgumentException("Cannot Return a Cell that is out of this Board's bounds.");
    }
    return this.gameBoard[row][column];
  }

  /**
   *
   * @param worker
   * @param rDir
   * @param cDir
   * @return
   * @throws IllegalStateException
   */
  private Cell getNextCell(Worker worker, int rDir, int cDir) throws IllegalStateException {
    try {
      if (neighborQuery(worker, rDir, cDir)) {
        return this.getCell(worker.getRow() + rDir, worker.getColumn() + cDir);
      }
      else {
        throw new IllegalStateException("Cannot return a Cell that is out of bounds.");

      }
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Cannot return a Cell that is not adjacent" +
              "to this Cell");
    }

  }




}
