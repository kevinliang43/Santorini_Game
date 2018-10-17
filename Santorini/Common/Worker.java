public class Worker {

  private static int workerCount = 0;

  private int x, y;
  private int id;

  public Worker(int x, int y) {
    this.setPosition(x, y);
    this.id = workerCount;
    workerCount++;
  }

  public Worker(Worker workerToCopy) {
    this.x = workerToCopy.x;
    this.y = workerToCopy.y;
    this.id = workerToCopy.id;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getID() {
    return this.id;
  }
}
