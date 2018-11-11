import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Player
 *
 * A Player has:
 *  - A name (String)
 *  - A list of Worker's IDs
 *
 * A Player can:
 *  - Get a copy of the Board
 *  - Check if it is their turn to play
 *  - Check if the Game is over
 *  - Check if a Place Action is legal
 *  - Check if a Move Action is legal
 *  - Check if a Build Action is legal
 *
 *  - Get a list of the Workers it controls
 *
 *  - Place a Worker if it is a legal Action, and receive confirmation that the Place Action happened
 *  - Move a Worker if it is a legal Action, and receive confirmation that the Move Action happened
 *  - Build a Floor if it is a legal Action, and receive confirmation that the Build Action happened
 *
 */
public interface IPlayer {

  /**
   * Adds a Worker to this Player's list of workers
   * @param id ID of new worker
   */
  void addWorkerID(int id);

  /**
   * Adds A Worker to this Player's list of workers
   * @param name Name of new Worker
   */
  void addWorkerName(String name);

  /**
   * Gets all Worker IDs of workers this player owns
   * @return List of Worker IDs
   */
  ArrayList<Integer> getWorkerIDs();

  ArrayList<String> getWorkerNames();

  /**
   * Get next Action asks the strategy for both Placing Worker and for Move/Builds
   * @param b Board representing current game state
   * @param s Status of the game.
   * @return IAction containing the next Action to be completed.
   */
  IAction getNextAction(Board b, Status s);

  /**
   * Get the name of this Player
   * @return The name of this Player.
   */
  String getName();

  /**
   * Resets this Player's workers
   */
  void resetWorkers();



}
