import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Represents a Board for the game Santorini
 * The Board does not know the rules of the game. It enforces:
 *  - physical game pieces (Workers and Buildings) can only be placed on existing Squares
 *  - a Square can only be occupied by one Worker at a time
 *  - a Building can only have between 0 and MAX_BUILDING_HEIGHT floors, inclusive
 *  - only Workers the Board knows about can be placed on it
 *
 * A Board has:
 *  - A final int X size BOARD_X
 *  - A final int Y size BOARD_Y
 *  - A 2D array of Squares[BOARD_X][BOARD_Y]
 *  - An ArrayList of Workers
 *
 * A Board can:
 *  - initialize and re-initialize itself
 *
 *  - return its current BoardStatus as a read-only copy, using Squares
 *  - get the Floor height of a specific Square
 *  - get the Square a Work is occupying
 *
 *  - place a Worker in a Square
 *  - move a Worker to a Square
 *  - build a Floor on a Square
 *  - set the Floor height of a Square
 *
 *  - get a List of all Workers on the Board
 *  - check if the Position (x,y) is a Square that exists
 *  - check if a Square is occupied by a Worker
 */
public class Board implements IBoard {
  // Board Size in the x and y dimensions
  public static final int BOARD_X = 6;
  public static final int BOARD_Y = 6;
  public static final int INVALID_WORKER_ID = -1;


  // Cells for game board
  private Square[][] cells;
  // List of Workers
  private ArrayList<Worker> workerList;

//  private Stack<Action> actionStack;

  /**
   * Initialize a Board object
   * This constructor builds the Square array and calls initBoard()
   */
  public Board() {
    cells = new Square[BOARD_X][BOARD_Y];
    initBoard();
  }

  /**
   * Copy constructor. Returns a deep-copy of the passed Board object.
   * @param boardToCopy
   */
  public Board(Board boardToCopy) {
    this.cells = new Square[BOARD_X][BOARD_Y];
    for (int x = 0; x < BOARD_X; x++) {
      for (int y = 0; y < BOARD_Y; y++) {
        this.cells[x][y] = new Square(boardToCopy.cells[x][y]);
      }
    }
    this.workerList = new ArrayList<>();
    for (Worker w : boardToCopy.workerList) {
      this.workerList.add(new Worker(w));
    }
  }

  /**
   * Start a fresh Board, with every cell initialized to 0 height, and no Workers Placed
   */
  public void initBoard() {
    workerList = new ArrayList<>();
    for (int i = 0; i < BOARD_X; i++) {
      for (int j = 0; j < BOARD_Y; j++) {
        cells[i][j] = new Square(i, j);
      }
    }
  }

  /**
   * Gets a deep-copy of the current Board object.
   * @return    A deep-copy of the current Board object.
   */
  public Board getBoardState() {
    return new Board(this);
  }

  /**
   * Getter for the height of a Floor at the Square at (x,y)
   * @param x   x Position of the Square
   * @param y   y Position of the Square
   * @return    an int from 0 to MAX_BUILDING_HEIGHT representing the height of the Building on the Square
   */
  public int getFloorHeight(int x, int y) {
    return this.cells[x][y].getHeight();
  }


  /**
   * Getter for the position of a specified Worker
   * @param workerID  int unique to the Worker
   * @return          An Square that the Worker is occupying
   * @throws IllegalArgumentException   if no Worker with that workerID exists
   */
  public Square getWorkerSquare(int workerID) throws IllegalArgumentException {
    for (Worker w : this.workerList) {
      if (w.getID() == workerID) {
        return this.cells[w.getX()][w.getY()];
      }
    }

    throw new IllegalArgumentException("Worker with specified ID does not exist");
  }

  /**
   * Creates a new Worker and places it on a specified Square
   * @param x   x Position of the Square the new Worker will occupy
   * @param y   y Position of the Square the new Worker will occupy
   * @return    the workerID of the new Worker
   * @throws IllegalArgumentException   if the Square is already occupied
   */
  public int placeWorker(int x, int y, String name, int workerID) throws IllegalArgumentException {
    validateOnBoard(x, y);
    if (cells[x][y].isOccupied()) {
      throw new IllegalArgumentException("Square is already occupied");
    }

    Worker newW = new Worker(x, y, name, workerID);
    this.workerList.add(newW);
    this.cells[x][y].setOccupied(true, newW.getID(), newW.getName());
    return newW.getID();
  }

  /**
   * Gets the List of all the Workers on the Board
   * @return  An ArrayList of Integers, each unique and representing a specific Worker
   */
  public ArrayList<Integer> getWorkerIDs() {
    ArrayList<Integer> ids = new ArrayList<>();

    for (Worker w : this.workerList) {
      ids.add(w.getID());
    }

    return ids;
  }


  /**
   * Moves a Worker from its current location to a specified new location
   * @param workerID  the ID of the Worker being moved
   * @param x         the x Position of the Square the Worker is being moved to
   * @param y         the y Position of the Square the Worker is being moved to
   * @throws IllegalArgumentException   if (x,y) isn't a valid Square,
   *                                    or if workerID does not identify a Worker
   */
  public void moveWorker(int workerID, int x, int y) throws IllegalArgumentException {
    validateOnBoard(x, y);
    for (Worker w : this.workerList) {
      if (w.getID() == workerID) {
        cells[w.getX()][w.getY()].setOccupied(false, INVALID_WORKER_ID, null);
        w.setPosition(x, y);
        cells[x][y].setOccupied(true, w.getID(), w.getName());
        return;
      }
    }
    throw new IllegalArgumentException("Invalid worker ID");
  }

  /**
   * Builds a Floor on the specified Square
   * @param x   the x Position of the Square being built on
   * @param y   the y Position of the Square being built on
   * @throws IllegalArgumentException   if (x,y) isn't a valid Square
   */
  public void buildFloor(int x, int y) throws IllegalArgumentException {
    validateOnBoard(x, y);

    this.setFloor(x, y, this.cells[x][y].height + 1);
  }

  /**
   * Sets the height of the Building on the specified Square to a new height
   * @param x   the x Position of the Building being set
   * @param y   the y Position of the Building being set
   * @param h   the new height of the Building
   * @throws IllegalArgumentException   if (x,y) isn't a valid Square
   */
  public void setFloor(int x, int y, int h) throws IllegalArgumentException {
    validateOnBoard(x, y);

    this.cells[x][y].setFloorHeight(h);
  }



  /**
   * Determines if (x,y) is a valid Square
   * @param x   the x Position of the hypothetical Square
   * @param y   the y Position of the hypothetical Square
   * @return    True if the Square exists, False if it doesn't
   */
  public boolean isXYOnBoard(int x, int y) {
    return !(x < 0 || x >= BOARD_X || y < 0 || y >= BOARD_Y);
  }

  /**
   * Indicates if the Square is occupied by a Worker
   * @param x   the x Position of the Square
   * @param y   the y Position of the Square
   * @return    True if there is a Worker on the Square, false if there is not
   * @throws IllegalArgumentException if (x,y) is not a valid Square
   */
  public boolean isOccupied(int x, int y) {
    validateOnBoard(x, y);
    return this.cells[x][y].isOccupied();
  }


  /**
   * Indicates if the two (x,y) pairs are neighbors. A position is not considered a
   * a position is not considered a neighbor to itself.
   * @param x1  first x position
   * @param y1  first y position
   * @param x2  second x position
   * @param y2  second y position
   * @return    True is the two pairs a neighbors.
   * @throws    IllegalArgumentException if either (x1,y1) or (x2,y2) are not valid Squares
   */
  public boolean neighboring(int x1, int y1, int x2, int y2) {
    validateOnBoard(x1, y1);
    validateOnBoard(x2, y2);
    if(x1 == x2 && y1 == y2) {
      return false;
    }
    return Math.abs(x2 - x1) <= 1 && Math.abs(y2 - y1) <= 1;
  }

  /**
   * Ensure the given (x,y) is on the Board. Throws an exception if not on the Board.
   * @param x X position to check
   * @param y Y position to check
   * @throws IllegalArgumentException if (x,y) is not on the Board
   */
  private void validateOnBoard(int x, int y) {
    if (!isXYOnBoard(x, y)) {
      throw new IllegalArgumentException("Invalid (x,y) position");
    }
  }

    /**
     * Generates a string visualization of the board
     * @return String representation of the board
     */
  public String asString() {
    String board = "";
    for (int row = 0; row < this.BOARD_X; row++) {
      for (int col = 0; col < this.BOARD_Y; col++) {
        board += this.cells[row][col].asString() + "\t";
      }
      board += "\n";
    }
    return board;
  }

  public String asJSONArray() {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode resultNode = mapper.createArrayNode();

    for (int row = 0; row < this.BOARD_X; row++) {
      ArrayNode currentRow = mapper.createArrayNode();
      for (int col = 0; col < this.BOARD_Y; col++) {
        Square currentCell = this.cells[row][col];

        if (currentCell.isOccupied()) {
          currentRow.add(currentCell.asString());
        } else {
          currentRow.add(Integer.parseInt(currentCell.asString()));
        }

      }
      resultNode.add(currentRow);
    }

    return resultNode.toString();
  }
}