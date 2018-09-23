/**
 * Created by KevinLiang on 9/23/18.
 */

/**
 * Interface representing the model of a Board of a Santorini Game, its methods, functionality, and
 * fields. The Board itself does not check for validity of game moves; it solely represents the
 * game grid. All rule checking is delegated to other relevant classes.
 */
public interface board {

  /**
   * Fields:
   *
   * row (Integer) - Represents how many rows this Board contains.
   * column (Integer) - Represents how many columns this Board contains.
   * gameBoard (Array<Array<Square>>) - Represents the grid of squares that the game is played on.
   *
   * Constructor:
   * A Board is constructed with two given Integers representing the number of columns and rows
   * that exist in this Board.
   * Board(int row, int column).
   *
   * When a Board is created, the gameBoard is initialized with new Squares (height == 0).
   *
   */

  /**
   * Adds a Worker to a target Square.
   * This method is intended to only be used during the initialization of the Game (all Squares will
   * have a height of 0), and NOT during the middle of an existing game.
   * @param worker Worker to be added to the given Square.
   * @param targetSquare Location where the new Worker will be added to
   * @throws IllegalArgumentException - Thrown when adding the worker to the target square is illegal.
   *                                    illegal Worker adds include:
   *                                    1. targetSquare does not exist (Out of Bounds).
   *                                    2. targetSquare already contains a Worker.
   */
  void addWorker(Worker worker, Square targetSquare) throws IllegalArgumentException;

  /**
   * Moves a given Worker Piece to the given target Square.
   * @param worker Worker to be moved.
   * @param targetSquare Location where the worker is being moved to.
   * @throws IllegalArgumentException - Thrown when the worker move is illegal. Specifically, this
   *                                    error is thrown when calling the move method within the
   *                                    Worker class.
   *                                    The board itself does not check for valid moves.
   *                                    For more information on illegal moves please see:
   *                                    1. Worker.move(Square targetSquare) method.
   */
  void moveWorker(Worker worker, Square targetSquare) throws IllegalArgumentException;

  /**
   * Uses a given Worker to build another floor on the given target Square.
   * @param worker Worker that is building another floor
   * @param targetSquare Square that is being built on.
   * @throws IllegalArgumentException - Thrown when the build on the Square by the Worker is illegal.
   *                                    Specifically, this error is thrown when calling the build
   *                                    method within the Worker class.
   *                                    The board itself does not check for valid moves.
   *                                    For more information on illegal build moves please see:
   *                                    1. Worker.build(Square targetSquare) method
   *                                    2. Square.addFloor() method
   */
  void addFloor(Worker worker, Square targetSquare) throws IllegalArgumentException;



}

/**
 * A Interface representing a Square on a Santorini Game Board, its methods and fields.
 *
 * Note: A Building is defined to be a Square of height greater than 0.
 *       If a Square has a height of 0, then no Building exists on this Square.
 *
 */
interface Square {

  /**
   * Fields:
   *
   * row (Integer) - Integer representing the row where this square exists on the game board.
   * column (Integer) - Integer representing the column where this square exists on the game board.
   * height (Integer) - Integer representing the height of a building on this Square (if it exists).
   *                    ** A Square is defined to have a Building if the height is greater than 0.
   *                    ** A Square is defined to NOT have a Building if the height is 0.
   * worker (Worker) - A reference to the Worker that is on this current Square.
   *                   ** If there is no Worker on this square, this field will be null.
   *
   * Constructor:
   *
   * A Square is constructed with two Integers representing the column and the row of this Square.
   * The height field of a new Square is 0 by default.
   * The worker field of a new Square is null by default.
   * Square(int row, int column)
   *
   */


  /**
   * Builds another Floor onto the building of this Square. If no building exists on this square,
   * then the first floor of a new building is created.
   * Essentially, this method adds 1 to the current height of this Square, if adding another floor
   * is a valid move.
   * @throws IllegalArgumentException - Thrown when adding a floor to this square is an invalid.
   *                                    Invalid additions of floors to this Square include:
   *                                    1. Height of this Square is already 4.
   *                                    2. There is a Worker on this Square.
   */
  void addFloor() throws IllegalArgumentException;

  /**
   * Determines whether a given square is adjacent to this one.
   * @param targetSquare Square to compare adjacency to this Square.
   * @return Boolean representing whether or not the given Square is adjacent to this Square.
   */
  boolean adjacentSquare(Square targetSquare);



}


/**
 * An Interface to describe a Worker game piece, its functionality, and its fields.
 */
interface Worker {

  /**
   * Fields:
   * square (Square) - Current Location of this Worker
   * moved (Boolean) - Boolean representing whether or not this Worker has moved already
   *
   * Constructor:
   * A Worker is constructed with a given Square where it is to be spawned.
   * Worker(Square targetSquare)
   *
   */

  /**
   * Moves this worker to a given Square. Updates the previous Square's Worker field to null.
   * Updates the given Square's Worker field to this Worker, if the move is valid.
   * @param targetSquare - The Square that this worker is moving to
   * @throws IllegalArgumentException - Thrown when moving this worker to the targetSquare
   *                                    is an illegal move. Illegal moves include:
   *                                    1. targetSquare is not adjacent to the current Square that
   *                                       this worker is on.
   *                                    2. targetSquare does not exist on the given game board
   *                                    3. targetSquare already contains another worker
   *                                    4. targetSquare contains a building of height 4
   *                                    5. targetSquare contains a building with a height more than
   *                                       1 floor higher than the current square's height.
   */
  void move(Square targetSquare) throws IllegalArgumentException;


  /**
   * Adds a floor to a building on the given targetSquare. If no building exists, then this worker
   * builds the first floor of a new building on the targetSqaure
   * @param targetSquare - Location where this worker adds a new floor to an existing building, or
   *                       builds the first floor of a new building.
   * @throws IllegalArgumentException - Thrown when this worker illegally tries to build a new floor
   *                                    on the target Square. Illegal build moves include:
   *                                    1. targetSquare is not adjacent to this current Worker's location
   *                                    2. targetSquare contains a building of height 4 already
   *                                    3. This worker has not yet moved to a new square on this turn
   *                                    4. targetSquare contains a Worker
   */
  void build(Square targetSquare) throws IllegalArgumentException;


}
