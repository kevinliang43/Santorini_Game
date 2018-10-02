/**Interface representing the rules of the game.
 * Actions and Queries are checked within this interface along with game over and other game states.
 */
public interface RuleChecker {

    /**
     * Checks if the move from the given old location to the new location is valid on the given Board
     * @param oldRow represents the row the Worker is moving from.
     * @param oldCol represents the column the Worker is moving from.
     * @param newRow represents the row the Worker is moving to.
     * @param newCol represents the column the Worker is moving to.
     * @param board represents the board of the game to be checked.
     * @return boolean representing whether or not this move is valid
     *
     * Checks for
     *      1) moving to an adjacent location
     *      2) moving within the bounds of the game board
     * Catches and handles exceptions when
     *      1) moving to a cell with a worker on it
     *      2) moving to a cell of more than 1 height greater than the current cell
     */
    boolean isValidMove(int oldRow, int oldCol, int newRow, int newCol, Board board);


    /**
     * Checks if the build from the worker it's location on the given building location is valid.
     * @param wRow represents the row of where the worker is.
     * @param wCol represents the column of where the worker is.
     * @param bRow represents the row where the building is to be built.
     * @param bCol represents the column where the building is to be built.
     * @param board represents the board of the game to be checked.
     * @return boolean representing whether or not this build is valid.
     *
     * Checks for
     *      1) building on an adjacent location
     *      2) building within the bounds of the game board
     * Catches and handles exceptions when
     *      1) building on a Cell with a Worker on it
     *      2) building on a Cell already of maximum height
     *      3) building from a Worker that has not been moved in this turn
     */
    boolean isValidBuild(int wRow, int wCol, int bRow, int bCol, Board board);


    /**
     * Checks if the game is over, determined from the state of the given board
     * @param board representing the baord of the game to be checked.
     * @return boolean representing whether or not the game is over
     *
     * The game is over when:
     *      1) a player is not able to make any moves
     *      2) a player is not able to make any builds
     *      3) a player is disconnected from their socket/this game
     *      4) a player moves a worker onto a building of height 3
     */
    boolean isGameOver(Board board);

}
