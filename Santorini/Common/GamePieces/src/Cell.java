/**
 * A Interface representing a Cell on a Santorini Game Board, its methods and fields.
 *
 * Note: A Building is defined to be a Cell of height greater than 0.
 *       If a Cell has a height of 0, then no Building exists on this Cell.
 *
 */
public class Cell {

  private static final int MAX_HEIGHT = 4;
  private int row;
  private int column;
  private int height;
  private Worker worker;

  /**
   * Constructor for Default Cell.
   * Default Cell will have a height of 0, and no worker on to it.
   * @param row Integer representation of the row index of this Cell
   * @param column Integer representation of the column index of this Cell
   */
  Cell(int row, int column) throws IllegalArgumentException{

    if (row < 0) {
      throw new IllegalArgumentException("Not a Valid Row Index for a Cell");
    }
    if (column < 0) {
      throw new IllegalArgumentException("Not a Valid Column Index for a Cell");
    }

    this.row = row;
    this.column = column;
    this.height = 0;
    this.worker = null;
  }

  /**
   *
   * @param row
   * @param column
   * @param height
   * @param worker
   * @throws IllegalArgumentException
   */
  Cell(int row, int column, int height, Worker worker) throws IllegalArgumentException {

    if (row < 0) {
      throw new IllegalArgumentException("Not a Valid Row Index for a Cell");
    }
    if (column < 0) {
      throw new IllegalArgumentException("Not a Valid Column Index for a Cell");
    }
    if (height > MAX_HEIGHT || height < 0 ) {
      throw new IllegalArgumentException("Not a Valid Height for a Cell");
    }

    this.row = row;
    this.column = column;
    this.height = 0;
    this.worker = null;
  }

  /**
   * Builds another Floor onto the building of this Cell. If no building exists on this Cell,
   * then the first floor of a new building is created.
   * Essentially, this method adds 1 to the current height of this Cell, if adding another floor
   * is a valid move.
   * @throws IllegalArgumentException - Thrown when adding a floor to this Cell is an invalid.
   *                                    Invalid additions of floors to this Cell include:
   *                                    1. Height of this Cell is already 4.
   *                                    2. There is a Worker on this Cell.
   */
  public void addFloor() throws IllegalStateException {
    if (this.height >= MAX_HEIGHT) {
      throw new IllegalStateException("Cannot a floor on a Cell that is of height " + MAX_HEIGHT);
    }

    if (this.containsWorker()) {
      throw new IllegalStateException("Cannot build a floor on a Cell with a Worker");
    }

    this.height++;


  }

  /**
   * Returns the height of this Cell
   * @return Integer Representation of this Cell's height.
   */
  public int getHeight() {
    return this.height;
  }

  public void setHeight(int height) {
    if (height < 0 || height >= MAX_HEIGHT) {
      throw new IllegalArgumentException("Cannot set height that is out of bounds.");
    }
    this.height = height;
  }

  /**
   * Returns the Row index of this Cell
   * @return Integer representation of this Cell's Row Index.
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Returns the Column index of this Cell
   * @return Integer representation of this Cell's Column Index.
   */
  public int getColumn() {
    return this.column;
  }

  /**
   * Returns whether this Cell currently contains a Worker on it.
   * @return a Boolean representing whether or not this Cell has a Worker on it.
   */
  public boolean containsWorker() {
    return this.worker != null;
  }

  /**
   * Adds a Worker to this Cell.
   * @param worker Reference to the Worker being added to this Cell
   * @throws IllegalStateException Thrown when trying to add a Worker to this Cell that already
   *                                Has a Worker
   */
  public void addWorker(Worker worker) throws IllegalStateException {
    if (this.containsWorker()) {
      throw new IllegalStateException("Cannot add a Worker to a Cell that already contains a Worker.");
    }

    this.worker = worker;

  }
  /**
   * Removes a Worker from this Cell.
   * @throws IllegalStateException Thrown when trying to remove a Worker from this Cell that
   *                                does not contain a Worker.
   */
  public void removeWorker() throws IllegalStateException {
    if (!this.containsWorker()) {
      throw new IllegalStateException("Cannot remove a Worker from a Cell that doesn't contain a Worker.");
    }

    this.worker = null;
  }

  /**
   * Determines if a given Cell is adjacent to this Cell. If the given Cell contains the same row
   * and column index as this Cell, then it is determined that the given Cell is not adjacent to
   * this cell.
   * @param targetCell Cell to be compared to this Cell for adjacency.
   * @return Boolean representing whether or not the given Cell is adjacent to this Cell.
   * @throws IllegalArgumentException Thrown when a non Cell class is given to this method.
   */
  public boolean adjacentCell(Cell targetCell) throws IllegalArgumentException {
    if (targetCell == null) {
      throw new IllegalArgumentException("Cannot Determine adjacency of this Cell to a non Cell");
    }

    if (this.row == targetCell.getRow() && this.column == targetCell.getColumn()) {
      return false;
    }

    else {
      return (Math.abs(this.row - targetCell.getRow()) <= 1) &&
              (Math.abs(this.column - targetCell.getColumn()) <= 1);
    }




  }

  //for testing purposes
  public String getWorkerName() {
    if (this.containsWorker()) {
      return this.worker.getName();
    }
    else {
      return "";
    }
  }








}
