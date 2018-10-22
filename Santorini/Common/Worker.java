/**
 * represents a Worker game object in a game of Santorini
 */
public class Worker {

  // represents the number of workers that have been created in this game instance
  private static int workerCount = 0;

  private int x, y;
  private int id;

  /**
   * creates a new worker with the ID of the current worker count, then increments the count
   */
  public Worker(int x, int y) {
    this.setPosition(x, y);
    this.id = workerCount;
    workerCount++;
  }

  /**
   * constructor that creates a new worker that is a copy of the given worker
   * @param workerToCopy
   */
  public Worker(Worker workerToCopy) {
    this.x = workerToCopy.x;
    this.y = workerToCopy.y;
    this.id = workerToCopy.id;
  }

  /**
   * getter method for the x coordinate
   * @return int x coordinate of this worker
   */
  public int getX() {
    return this.x;
  }

  /**
   * getter method for the y coordinate
   * @return int y coordinate of this worker
   */
  public int getY() {
    return this.y;
  }

  /**
   * setter method for the position of this worker
   * @param x int x coordinate to move this worker to
   * @param y int y coordinate to move this worker to
   */
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * getter method for the id of this worker
   */
  public int getID() {
    return this.id;
  }
}
