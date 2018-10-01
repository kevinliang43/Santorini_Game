
public class Worker {

  String name;
  Cell cell;
  boolean moved; // Represents whether this Worker has been moved during this turn


  /**
   * Constructor for a Worker
   * @param cell Represents the initial location of this Worker when it spawns
   * @param name Represents the name of this Worker when initialized
   */
  Worker(String name, Cell cell) {
    if (cell == null) {
      throw new IllegalArgumentException("Cannot Initialize a Worker on a non Cell");
    }
    this.name = name;
    this.cell = cell;
    this.moved = false;
  }

  /**
   * Moves this Worker to a given Cell
   * @param targetCell Represents the new location of this Worker
   * @throws IllegalStateException Thrown when trying to move a Worker to Cell that
   *                                already contains a worker.
   */
  public void move(Cell targetCell) throws IllegalStateException {

    if (cell.containsWorker()) {
      throw new IllegalStateException("Cannot move a Worker to a Cell that contains a Worker");
    }
    if (this.moved) {
      throw new IllegalStateException("Cannot move a Worker that already has been moved.");
    }

    this.cell.removeWorker();
    cell.addWorker(this);
    this.cell = targetCell;
    this.moved = true;

  }

  /**
   * Builds a new floor on a given Cell. In other words, adds 1 to the height of the given Cell.
   * @param targetCell Cell to be built on
   * @throws IllegalStateException Thrown when trying to build a floor on a non adjacent Cell.
   */
  public void build(Cell targetCell) throws IllegalStateException {
    if (!this.cell.adjacentCell(targetCell)) {
      throw new IllegalStateException("Cannot build onto a non-adjacent Cell");
    }
    if (!this.moved) {
      throw new IllegalStateException("Cannot build before this Worker has moved.");
    }

    targetCell.addFloor();

  }

  /**
   * Determines if a given Cell is adjacent to this Worker and the Cell that it is on.
   * @param targetCell Cell to compare adjacency to this Worker
   * @return Boolean representing whether the given Cell is adjacent to this Worker.
   */
  public boolean adjacentCell(Cell targetCell) {
    return this.cell.adjacentCell(targetCell);
  }

  /**
   * Returns the Row Index of the Cell that this Worker is on.
   * @return Integer representation of the Row Index of this Worker.
   */
  public int getRow() {
    return this.cell.getRow();
  }

  /**
   * Returns the Column Index of the Cell that this Worker is on.
   * @return Integer representation of the Column Index of this Worker.
   */
  public int getColumn() {
    return this.cell.getColumn();
  }


  /**
   * Refreshes this Worker's moved status to unmoved.
   * To be used for when a Player's turn has ended.
   */
  public void refreshWorker() {
    this.moved = false;
  }





}
