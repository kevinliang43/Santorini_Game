import java.util.ArrayList;

/**
 * Representation of the Board and Status. Our use case will take a copy
 * of the actual Board (using getBoardState) to use an object to pass to
 * the players. Although a reference to the Board may be more efficient,
 * we decided on using a copy over potentially missing a mutable side-effect
 * in a created ImmutableBoard class. Additionally we limit the functions
 * on Board to be methods without side-effects on Board. This is a "just-in-case"
 * measure to ensure an Admin does not give a BoardStatus with a reference to
 * the actual Board when it is implemented.
 */
public final class BoardStatus {
  private Board board;
  private Status status;

  /**
   * Create a new BoardStatus using the passed Board and Status. Board
   * and Status should be the up-to-date Board and Status values of the
   * game.
   * @param b The Board to use.
   * @param s The turn Status to use.
   */
  public BoardStatus(Board b, Status s) {
    board = b;
    status = s;
  }

  /**
   * Returns the Square the specified Worker is on.
   * @param workerID The ID of the Worker to get the Square of
   * @return The Square the specified Worker is on
   * @throws IllegalArgumentException if workerID is not valid
   */
  public Square getWorkerSquare(int workerID) {
    return board.getWorkerSquare(workerID);
  }

  /**
   * Checks if the specified (x,y) position is occupied by a Worker.
   * @param x X position
   * @param y Y position
   * @return True if the position is occupied by a Worker
   * @throws IllegalArgumentException if (x,y) is not on the Board
   */
  public boolean isOccupied(int x, int y) {
    return board.isOccupied(x, y);
  }

  /**
   * Gets the current height of a building.
   * @param x X position
   * @param y Y position
   * @return Value of the height of the floor at (x,y)
   */
  public int getFloorHeight(int x, int y) {
    return board.getFloorHeight(x, y);
  }

  /**
   * Update the Status of this BoardStatus
   * @param newStatus The Status to update to
   */
  public void setStatus(Status newStatus) {
    this.status = newStatus;
  }

  /**
   * Update the Board of this BoardStatus
   * @param newBoard The Board to update to
   */
  public void setBoard(Board newBoard) {
    this.board = newBoard;
  }

  /**
   * Get the current Status of this BoardStatus
   * @return The current Status of this BoardStatus
   */
  public Status getStatus() {
    return this.status;
  }

  /**
   * Gets a copy of the current board
   * @return a new board that is a copy of current board
   */
  public Board getBoard() {
    return new Board(this.board);
  }

  /**
   * gets the workers that aren't in the given list from this boards workers
   * @param workersToFilter list of workers that we don't want
   * @return list of workers where workersToFilter have been filtered out
   */
  public ArrayList<Integer> filterWorkers(ArrayList<Integer> workersToFilter) {
    ArrayList<Integer> filteredList = new ArrayList<>();
    for (int id : this.board.getWorkerIDs()) {
      if (!workersToFilter.contains(id)) {
        filteredList.add(id);
      }
    }

    return filteredList;
  }
}