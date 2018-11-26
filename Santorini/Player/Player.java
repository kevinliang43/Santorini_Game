import java.util.ArrayList;

/**
 * Player Representation in a game of Santorini
 */
public class Player implements IPlayer{
  private String name;
  private int id;
  private ArrayList<Integer> workerIDs;
  private Strategy bestStrategy;
  private ArrayList<String> workerNames;

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
    this.workerNames = new ArrayList<>();
  }

  /**
   * Adds a Worker to this Player's list of workers
   * @param id ID of new worker
   */
  public void addWorkerID(int id) {
    this.workerIDs.add(id);
  }

  /**
   * Adds A Worker to this Player's list of workers
   * @param name Name of new Worker
   */
  public void addWorkerName(String name) {
    this.workerNames.add(name);
  }

  /**
   * Gets all Worker IDs of workers this player owns
   * @return List of Worker IDs
   */
  public ArrayList<Integer> getWorkerIDs() {
    return workerIDs;
  }

  public ArrayList<String> getWorkerNames() {
    return workerNames;
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

  /**
   * Resets this Player's workers
   */
  public void resetWorkers() {
    this.workerIDs = new ArrayList<>();
    this.workerNames = new ArrayList<>();
  }

  /**
   * Sends a message in the form of JSON Text to the Client (if exists)
   * If client doesnt exists (AI Player), then this method does nothing.
   * @param message JSON Message To send
   */
  public void sendMessage(String message) {
    this.bestStrategy.sendMessage(message);
  }
}
