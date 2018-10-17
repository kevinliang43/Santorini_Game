import java.util.ArrayList;

/**
 * This interface defines the specifications for a board in the game Santorini.
 *
 * Santorini is a two player game on a 6x6 board. Each square on the board is a building, of height
 * 0, 1, 2, 3, or 4. Every building starts at 0. Each player controls 2 workers. At the start of
 * each turn, a player moves one worker and builds one floor of a building.
 *
 *
 * A board for the game Santorini contains:
 *  -a 6x6 grid of floors of up to height 4
 *  -a list of up to 4 workers, 2 from each player
 *
 *  It can:
 *  -create a new fresh game
 *  -return the board state
 *  -return the height of a specific floor
 *  -return the location of each worker
 *  -return if a square is occupied by a worker
 *  -place a worker
 *  -move a worker to a new position
 *  -let a worker build a floor on a square
 *
 * A class implementing Board has a private class that implements ImmutableSquare.
 * Refer to Mutable Square.
 *
 */
interface Board {

  /**
   * Begin a new game with no workers and all floors at height 0. All Squares are represented as
   * MutableSquares internally, freshly instantiated.
   * @return  Returns a fresh board with 0 floor height on all squares
   */
  Board initBoard();

  /**
   * Returns a 2D array of ImmutableSquares containing the floor height of each building
   * @return  ImmutableSquare[][] containing a 6x6 array of buildings
   */
  ImmutableSquare[][] getBoardState();

  /**
   * Returns the height of the floor at position (x,y)
   * @param x   the x position of the square, from 0 to 5
   * @param y   the y position of the square, from 0 to 5
   * @return    An int representing the height of the square, from 0 to 4
   */
  int getFloorHeight(int x, int y);

  /**
   * Returns the location of the specified Worker
   * @param workerID    the unique ID of the Worker
   * @return            An ImmutableSquare representing the worker's location
   * @throws IllegalArgumentException   if there is no Worker with ID workerID
   */
  ImmutableSquare getWorkerPosition(int workerID) throws IllegalArgumentException;

  /**
   * Places a new Worker in a specified location.
   * Creates and stores the Worker in an internal list.
   * @param x   the x position the worker will be placed in, from 0 to 5
   * @param y   the y position the worker will be placed in, from 0 to 5
   * @return    the workerID of a new Worker, which has been placed in the specified location
   * @throws IllegalArgumentException   if (x,y) is out of bounds
   */
  int placeWorker(int x, int y) throws IllegalArgumentException;

  /**
   * Gives the list of workerIDs belonging to the Workers stored in the Board
   * @return    ArrayList of Integers containing the workerIDs of every Worker
   */
  ArrayList<Integer> getWorkerIDs();

  /**
   * Moves a worker from an old position to a new position, if the old position is occupied.
   * Updates the new position for the Worker referenced by the workerID
   * @param workerID  the ID of the worker being moved
   * @param x         the new x position to move to
   * @param y         the new y posiiton to move to
   * @throws IllegalArgumentException   if (x,y) is out of bounds
   *                                    or if there is no Worker with ID workerID
   */
  void moveWorker(int workerID, int x, int y) throws IllegalArgumentException;

  /**
   * Increments a floor int
   * @param x   the x position of the Square to be incremented
   * @param y   the y position of the Square to be incremented
   * @throws IllegalArgumentException   if (x,y) is out of bounds or already has 4 floors
   */
  void buildFloor(int x, int y) throws IllegalArgumentException;

}

/**
 * Player-controlled piece that can occupy a Square and is contained in the Board.
 *
 * It can:
 * -return its x and y coordinates
 * -change its x and y coordinates
 * -return its workerID
 */
abstract class Worker {

  /**
   * Int counting the total number of workers created
   */
  private static int workerCount = 0;

  /**
   * Position coordinates x and y
   */
  private int x, y;

  /**
   * Unique int representing this Worker
   */
  private int workerID;

  /**
   * Constructor for a basic Worker at a given position.
   * Also increases the count of total workers and assigns each new instance a unique ID.
   * @param x   initial x position of the Worker
   * @param y   initial y position of the Worker
   */
  Worker(int x, int y) {
    this.x = x;
    this.y = y;
    workerCount++;
    this.workerID = workerCount;
  }

  /**
   * Gets the x position for the Worker on the Board
   * @return  the x position of the Square this Worker occupies
   */
  abstract int getX();

  /**
   * Gets the y position for the Worker on the Board
   * @return  the y position of the Square this Worker occupies
   */
  abstract int getY();

  /**
   * Sets the new position for the Worker on the Board
   * @param x   the new x position for the Worker
   * @param y   the new y position for the Worker
   */
  abstract void setPosition(int x, int y);

  /**
   * Gets the ID for this Worker
   * @return  the unique identifier for this instance of Worker
   */
  int getID() {
    return workerID;
  }

}

/**
 * Represents an unchanging building, can be occupied by a Worker
 * This class is allowed to leave the Board because it cannot change the state of the Square
 */
abstract class ImmutableSquare {

  /**
   * Unchanging x position of this Square
   */
  public final int x;

  /**
   * Unchanging y position of this Square
   */
  public final int y;

  /**
   * Height of this floor
   */
  private int height;

  /**
   * If a Worker is currently occupying this Square
   */
  private boolean occupied;

  /**
   * Constructor for a Square on the Board
   * @param x   int representing the x position of this Square
   * @param y   int representing the y position of this Square
   */
  ImmutableSquare(int x, int y) {
    this.x = x;
    this.y = y;
    this.height = 0;
    this.occupied = false;
  }

  /**
   * Gets the number of floors in the building on this Square
   * @return    an int representing the number of floors
   */
  abstract int getHeight();

  /**
   * Gets the x position of this Square
   * @return    an int representing the x position of this Square
   */
  abstract int getX();

  /**
   * Gets the y position of this Square
   * @return    an int representing the y position of this Square
   */
  abstract int getY();

  /**
   * Method checking if the Square has a Worker placed on it
   * @return    true if the Square has a Worker placed on it, false otherwise
   */
  abstract boolean isOccupied();
}

/**
 * Represents a building, can be occupied by a Worker
 * This class is not allowed to leave the Board because it can change the state of the Square.
 * This should be declared as a private inner class in Board to prevent modifying data.
 */
abstract class MutableSquare extends ImmutableSquare {

  /**
   * Constructor for a Square on the Board
   * @param x   int representing the x position of this Square
   * @param y   int representing the y position of this Square
   */
  MutableSquare(int x, int y) {
    super(x, y);
  }

  /**
   * Increments the height of this Square's floor by 1
   */
  abstract void buildFloor();

  /**
   * Sets the height of this Square's floor to the specified height
   * @param height    int representing the new height of this floor
   */
  abstract void setFloorHeight(int height);

  /**
   * Sets the boolean determining if this Square has a Worker on it or not
   * @param occupied    boolean representing if this Square has a Worker occupying it
   */
  abstract void setOccupied(boolean occupied);
}




