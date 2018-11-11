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
 */
interface IBoard {

  /**
   * Begin a new game with no workers and all floors at height 0. All Squares are represented as
   * MutableSquares internally, freshly instantiated.
   */
  void initBoard();

  /**
   * Returns a 2D array of ImmutableSquares containing the floor height of each building
   * @return  Board containing a 6x6 array of buildings
   */
  Board getBoardState();

  /**
   * Returns the height of the floor at position (x,y)
   * @param x   the x position of the square, from 0 to 5
   * @param y   the y position of the square, from 0 to 5
   * @return    An int representing the height of the square, from 0 to 4
   */
  int getFloorHeight(int x, int y);


  /**
   * Places a new Worker in a specified location.
   * Creates and stores the Worker in an internal list.
   * @param x   the x position the worker will be placed in, from 0 to 5
   * @param y   the y position the worker will be placed in, from 0 to 5
   * @return    the workerID of a new Worker, which has been placed in the specified location
   * @throws IllegalArgumentException   if (x,y) is out of bounds
   */
  int placeWorker(int x, int y, String name, int workerID) throws IllegalArgumentException;

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



