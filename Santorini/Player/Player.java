/**
 * Class representing a player in the Santorini Game.
 * A Player object sits in between the client and the Game, clients send requests to their Player
 * and the Game administrative component will grab these requests, validate them, and execute them
 * when it is a Player's turn.
 */
public class Player {

  private String id;
  private IStrategy strategy;

  /**
   * Constructor for the Player Class
   * A Player Class should be constructed with:
   * @param id String representation of this Player's ID
   * @param strategy Strategy object that implements the IStrategy Interface.
   */
  Player(String id , IStrategy strategy) {

    if (id == null) {
      throw new IllegalArgumentException("Player class must be instantiated with a valid ID.");
    }
    if (strategy == null) {
      throw new IllegalArgumentException("Player class must be instantiated with a valid Strategy Object.");
    }

    this.id = id;
    this.strategy = strategy;

  }


  /**
   * Retrieves the next Move for this player, based on this Player's Strategy logic.
   * @param board Copy of the Current Game State.
   * @return String representation of a JSON request
   */
  public String getRequest(Board board){
    return this.strategy.nextMove(board);
  }

  /**
   * Sends information to be received and handled by this Player's strategy Object
   * @param message To be used to communicate messages in English, from the Admin to some "Actor"
   */
  public void sendInformation(String message) {
    this.strategy.sendInformation(message);
  }

  /**
   * Returns the Hash Code of this Player.
   * HashCode is determined by a function of this Player's ID
   * @return Integer representing the Hash Code of this player
   */
  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }

  /**
   * OVERRIDE: Equality of a Player Class will be determined by the equality of the given object's
   * ID and this Player's ID
   * @param obj Object to be compared to this Player
   * @return Boolean representing whether or not a given Object is equivalent to this Player.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Player)) {
      return false;
    }

    Player playerObj = (Player) obj;

    return playerObj.getId().equals(this.getId());
  }

  /**
   * Returns the ID of this Player
   * @return String representation of Player's ID
   */
  public String getId() {
    return id;
  }

}