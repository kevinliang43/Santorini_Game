import java.util.ArrayList;

/**
 * Player Representation in a game of Santorini
 */
public class Player {
  private String name;
  private int id;
  private ArrayList<Integer> workerIDs;
  private Strategy bestStrategy;

  // Note: Strategy could be TCPStrategy

  /**
   * Constructor for the Player Class
   * @param name name of the player
   * @param id ID of the player
   * @param strat strategy to be used
   */
  public Player(String name, int id, Strategy strat) {
    this.name = name;
    this.id = id;
    this.bestStrategy = strat;
    this.workerIDs = new ArrayList<>();
  }

  /**
   * Adds a Worker to this Player's list of workers
   * @param id ID of new worker
   */
  public void addWorkerID(int id) {
    this.workerIDs.add(id);
  }

  /**
   * Gets all Worker IDs of workers this player owns
   * @return List of Worker IDs
   */
  public ArrayList<Integer> getWorkerIDs() {
    return workerIDs;
  }

  /**
   * Get next Action asks the strategy for both Placing Worker and for Move/Builds
   * @param b Board representing current game state
   * @param s Status of the game.
   * @return IAction containing the next Action to be completed.
   */
  public IAction getNextAction(Board b, Status s) {
    return this.bestStrategy.getNextAction(new BoardStatus(b, s), this.workerIDs);
  }

  /**
   * Get the name of this Player
   * @return The name of this Player.
   */
  public String getName() {
    return this.name;
  }
}
