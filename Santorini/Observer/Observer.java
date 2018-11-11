/**
 * Implementation of the Observer Class
 * The Observer Class contains two StringBuilders that will hold:
 * 1. Total history of the game
 * 2. History of the game from the last read to the next read call
 *
 * The History of a game will contain all Board, Actions and String messages in the form of:
 *
 * Board:
 *       JSON array of JSON arrays
 *
 * Actions :
 *      1. String, which represents the name of a player that is giving up.
 *
 *      2. [Worker,EastWest,NorthSouth], which represents a winning move.
 *
 *      3. [Worker,EastWest,NorthSouth,EastWest,NorthSouth],
 *      which represents a request to move the specified worker and build in the specified directions.
 *      The first pair of directions specify the move, the second one the building step.
 *
 * String:
 *      Which represents an error message or a final result notification.
 *
 *
 * Overall History will contain messages of roughly alternating sequence of boards and actions ended
 * in a message that indicates who won the game.
 *
 */
public class Observer implements IObserver{

  private StringBuilder history; // Holds the entire history of the current game
  private StringBuilder newUpdates; // Holds history between last read() and next read()
  private String name;

  public Observer(String name) {
    this.name = name;
    this.history = new StringBuilder();
    this.newUpdates = new StringBuilder();
  }

  /**
   * Appends new updates to both the overall history builder, as well as the newUpdates builder.
   * @param updates new Updates to add.
   */
  public void append(String updates) {
    this.history.append(updates + "\n");
    this.newUpdates.append(updates + "\n");
  }

  /**
   * Returns all new updates since the last read, and clears the newUpdates Builder.
   * @return String representation of new updates since last read.
   */
  public String readNewUpdates() {
    String returnString = newUpdates.toString();
    newUpdates.setLength(0);
    return returnString;

  }

  /**
   * Returns the overall history of the game.
   * @return String representation of all game updates.
   */
  public String getHistory() {
    return this.history.toString();
  }

  /**
   * Getter for this Observer's Name
   * @return This Observer's Name
   */
  public String getName() {
    return this.name;
  }



}
