import java.util.HashMap;

/**
 * Created by KevinLiang on 9/30/18.
 */
public class Board {

  private static final int MAX_ROWS = 6;
  private static final int MAX_COLUMNS = 6;
  private int numRows;
  private int numColumns;
  private Cell[][] gameBoard;
  private HashMap<String, Worker> workers;

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
    this.workers = new HashMap<>();
  }

  Board(Board gameBoard) {
    this.numRows = gameBoard.getNumRows();
    this.numColumns = gameBoard.getNumColumns();
    this.workers = new HashMap<>();
    this.gameBoard = new Cell[this.numRows][this.numColumns];

    for (int i = 0; i < this.numRows; i++) {
      for (int j = 0; j < this.numColumns; j++) {
        Cell currentCell = gameBoard.getCell(i, j); // Get the current Cell
        Cell newCell = currentCell.getCopy(); // Make a Copy of the current Cell
        this.gameBoard[i][j] = newCell; // Set Copy to this board

        String newWorkerName = "";
        if (currentCell.containsWorker()) {
          newWorkerName = currentCell.getWorkerName();
          this.addWorker(i, j, newWorkerName);
        }

      }
    }

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
    return !(worker.getColumn() + rDir >= MAX_COLUMNS ||
            worker.getColumn() + rDir < 0 ||
            worker.getRow() + cDir >= MAX_ROWS ||
            worker.getRow() + cDir < 0);

  }

  /**
   * Determines if a Cell exists in the direction faced by a given Worker using String workerID
   * @param name Represents the name of the Worker that this query is being called from
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @return Boolean representing whether or not the neighbor Cell exists
   * @throws IllegalArgumentException thrown when trying to determine neighbor status of a Cell that
   *                                   is not adjacent to the given Worker.
   */
  public boolean neighborQuery(String name, int rDir, int cDir) throws IllegalArgumentException{
    Worker worker = workers.get(name);
    if (rDir > 1 || rDir < -1 || cDir > 1 || cDir < -1) {
      throw new IllegalArgumentException("Worker can only perform actions on adjacent Cells.");
    }
    return !(worker.getColumn() + rDir >= MAX_COLUMNS ||
            worker.getColumn() + rDir < 0 ||
            worker.getRow() + cDir >= MAX_ROWS ||
            worker.getRow() + cDir < 0);

  }

  /**
   * Moves a given Worker to a Cell that the given Worker is facing using the worker name instead of direct reference to worker
   * @param name Represents the name of the Worker that is building a Floor
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @throws IllegalStateException Thrown in one of 4 Cases:
   *                                1. Worker has already moved.
   *                                2. Cell being moved to is already occupied.
   *                                3. Cell being moved to is not adjacent.
   *                                4. Cell being moved to is out of bounds.
   *                                5. Worker cannot move to Cell containing Worker.
   */
  public void move(String name, int rDir, int cDir) throws IllegalStateException {
    Worker worker = workers.get(name);
    worker.move(getNextCell(worker, rDir, cDir));
  }

  /**
   * Builds another Floor onto the Cell that the given Worker is facing using the worker name instead of the Worker reference
   * @param name Represents the name of the Worker that is being moved
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @throws IllegalStateException Thrown in one of 5 Cases:
   *                                1. Worker has NOT moved
   *                                2. Worker cannot build on Cell with height 4
   *                                3. Cell being moved to is not adjacent.
   *                                4. Cell being moved to is out of bounds.
   *                                5. Worker cannot build on Cell that is occupied.
   */
  public void build(String name, int rDir, int cDir) throws IllegalStateException {
    Worker worker = workers.get(name);
    worker.build(getNextCell(worker, rDir, cDir));

  }

  /**
   * Determine if a Cell that is faced by a given Worker in the given Direction is occupied using worker String ID.
   * @param name Represents the Worker that this query is being called from
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @return Boolean representing whether or not the target Cell is occupied or not.
   * @throws IllegalStateException Thrown when trying to check occupancy of a Cell that is out
   *                                  of bounds or is not adjacent to the given Worker.
   */
  public boolean occupyQuery(String name, int rDir, int cDir) throws IllegalStateException {
    Worker worker = workers.get(name);
    try {
      return getNextCell(worker, rDir, cDir).containsWorker();
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot determine occupancy of a Cell that is not adjacent to" +
              "the given Worker nor a Cell that is out of bounds.");
    }
  }

  /**
   * Returns the height of an adjacent Cell in the given direction faced by a given Worker
   * @param name Represents the name of the Worker that this query is being called from
   * @param rDir represents the Row Direction faced by the Worker. (1 = West, -1 = East, 0 = Put)
   * @param cDir represents the Column Direction faced by the worker (1 = North, -1 = South, 0 = Put
   * @return an Integer representing the height of the Cell faced by the given Worker.
   * @throws IllegalStateException Thrown when trying to return height of a Cell that is out
   *                                  of bounds or is not adjacent to the given Worker.
   */
  public int heightQuery(String name, int rDir, int cDir) throws IllegalStateException {
    Worker worker = workers.get(name);
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
   * @param ewDir
   * @param nsDir
   * @return
   * @throws IllegalStateException
   */
  public Cell getNextCell(Worker worker, int ewDir, int nsDir) throws IllegalStateException {
    try {
      if (neighborQuery(worker, ewDir, nsDir)) {
        return this.getCell(worker.getRow() + nsDir, worker.getColumn() + ewDir);
      }
      else {
        throw new IllegalStateException("Cannot return a Cell that is out of bounds.");

      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      throw new IllegalStateException("Cannot return a Cell that is not adjacent" +
              "to this Cell");
    }

  }

  /**
   * Adds a new worker to the given row and column, used for initialization of the game
   * @param row represents the row in the game board we are adding a worker to
   * @param column represents the column in the game baord we are adding a worker to
   * @param workerID represents the name that should be given to the new worker being created
   */
  public void addWorker(int row, int column, String workerID) {
    Cell cell = this.gameBoard[row][column];
    Worker newWorker = new Worker(workerID, cell);
    cell.addWorker(newWorker);
    this.workers.put(workerID, newWorker);
  }

  /**
   * Prints the current state of the board to STDOUT
   */
  public void printBoard() {
    for (int row = 0; row < MAX_ROWS; row++) {
      String currentRow = "";
      for (int column = 0; column < MAX_COLUMNS; column++){
        Cell cell = this.getCell(row, column);
        currentRow += " " + cell.getHeight() + cell.getWorkerName() + " ";

      }
      System.out.println(currentRow);
    }
    System.out.println("\n");
  }

  /**
   * Gets a Worker reference, given a String representation of the Worker's name
   * @param workerName Worker's Name
   * @return the Worker Object associated with the given Worker's name
   * @throws IllegalArgumentException Worker being retrieved does not exist.
   */
  public Worker getWorker(String workerName) throws IllegalArgumentException {
    if (this.workers.containsKey(workerName)) {
      return this.workers.get(workerName);
    }
    else {
      throw new IllegalArgumentException("Worker does not exist.");
    }
  }


  public int getNumRows() {
    return numRows;
  }

  public int getNumColumns() {
    return numColumns;
  }
}
