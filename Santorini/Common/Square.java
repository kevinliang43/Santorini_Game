/**
 * Represents an unchanging Square on a Board
 * This is a read-only Square that has:
 *  - ints x and y representing its position on the Board
 *  - int height representing the height of the Building on this Square
 *  - boolean occupied indicating whether there is a Worker currently occupying this Square
 *
 *  An Square can:
 *  - return its x Position
 *  - return its y Position
 *  - return its Building height
 *  - indicate whether it is occupied with a Worker or not
 */
public class Square {
  public static final int MAX_BUILDING_HEIGHT = 4;

  //x and y coordinates
  private final int x, y;
  //Building height
  protected int height;
  //occupied by a Worker status
  protected boolean occupied;
  // ID of worker if it is occupied. If not occupied, value is -1
  protected int workerID;
  // Name of worker if it is occupied. If not occupied, value is null;
  protected String workerName;

  /**
   * Initial Constructor for an Square
   * @param x   x Position of the Square
   * @param y   y Position of the Square
   */
  public Square(int x, int y) {
    this.x = x;
    this.y = y;
    this.height = 0;
    this.occupied = false;
    this.workerID = Board.INVALID_WORKER_ID;
    this.workerName = null;
  }

  /**
   * Copy constructor. Returns a deep-copy of the passed Square.
   * @param squareToCopy The square to copy
   */
  public Square(Square squareToCopy) {
    this.x = squareToCopy.x;
    this.y = squareToCopy.y;
    this.height = squareToCopy.height;
    this.occupied = squareToCopy.occupied;
    this.workerID = squareToCopy.workerID;
    this.workerName = squareToCopy.workerName;
  }

  /**
   * Constructor for an Square with unchangeable data
   * @param x         x Position of the Square
   * @param y         y Position of the Square
   * @param height    height of the Building of the Square
   * @param occupied  True if there's a Worker on the Square, false if there isn't
   */
  Square(int x, int y, int height, boolean occupied, int workerID, String workerName) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.occupied = occupied;
    this.workerID = workerID;
    this.workerName = workerName;
  }

  /**
   * Getter for the x Position of the Square
   * @return  int from 0 to BOARD_X - 1
   */
  public int getX() {
    return this.x;
  }

  /**
   * Getter for the y Position of the Square
   * @return  int from 0 to BOARD_Y - 1
   */
  public int getY() {
    return this.y;
  }

  /**
   * Getter for the height of the Building of the Square
   * @return  int from 0 to MAX_BUILDING_HEIGHT defined in implementing class
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Check if there is a Worker occupying this Square
   * @return  True if there is a Worker occupying the Square, false if there isn't
   */
  public boolean isOccupied() {
    return this.occupied;
  }

  /**
   * Getter for the ID of the worker occupying this square. If the square is not occupied
   * INVALID_WORKER_ID is returned.
   * @return  int representing the worker ID. If no worker is present, INVALID_WORKER_ID
   */
  public int getWorkerID() {
    return this.workerID;
  }

  /**
   * Getter for the String name of the worker occupying this square. If the square is not occupied
   * null is returned.
   * @return String representing the Worker name. If no worker is present, null is returned.
   */
  public String getWorkerName() {
    return this.workerName;
  }

  /**
   * Set the height of the Building on this Square to a given height
   * @param height  new height of the Building
   * @throws IllegalStateException  if the new height isn't between 0 and MAX_BUILDING_HEIGHT
   */
  void setFloorHeight(int height) throws IllegalStateException {
    if (height < 0 || height > MAX_BUILDING_HEIGHT) {
      throw new IllegalArgumentException("Height is not between 0 and " + MAX_BUILDING_HEIGHT);
    }
    this.height = height;
  }

  /**
   * Setter the status of a Worker occupying this Square
   * @param occupied    True if there is a Worker is now on the Square, false if there is not
   */
  void setOccupied(boolean occupied, int workerID, String workerName) {
    this.occupied = occupied;
    this.workerID = workerID;
    this.workerName = workerName;
  }

  public String asString() {
    if (this.getWorkerID() == -1) {
      return Integer.toString(this.getHeight());
    } else {
      return Integer.toString(this.getHeight()) + this.workerName;
    }
  }



}