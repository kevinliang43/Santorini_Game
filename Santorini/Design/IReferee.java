import java.util.ArrayList;

/**
 * A Referee oversees and performs actions for a game of Santorini.
 * It accepts players until MAX_PLAYERS is reached. Then waits until
 * runGame is called. From there the placePhase begins until all
 * workers have been placed, then moves to the turnPhases alternating
 * between players. If a Player provides an invalid Action, the game
 * is ended and the other Player is defined as the winner.
 */
public interface IReferee {
  /**
   * Runs a game between this classes two players n amount of times and determines the winner based off of which
   * player won the most games out of n
   * @param numGames odd number of games to play
   * @return Player that won the most games within this set of n games
   * @throws IllegalArgumentException when the given n is even, must be odd to guarantee a winner
   */
  Player runGame(int numGames) throws IllegalArgumentException;

  /**
   * Starts a game. The placePhase is executed until WORKERS_PER_PLAYER
   * are placed, then the game moves to the alternating turnPhase. When
   * the game is over, the method silently returns. A winner can be found
   * using getWinner. If a Player ever provides an invalid move, they are
   * removed and the game is determined as over with the other Player
   * being designated the winner.
   * @throws IllegalStateException if MAX_PLAYERS are not contained
   */

  Player runGame() throws IllegalStateException;

  /**
   * Creates a new Player instance using the given parameters.
   * @param aChallengersName Name of the Player. Must be unique.
   * @param aChallengersStrategy Stratgy instance to use for the Player.
   * @throws IllegalArgumentException if aChallengersName is already in use
   */
  void addPlayer(String aChallengersName, Strategy aChallengersStrategy)
          throws IllegalArgumentException;

  /**
   * Removes a Player from the game. They lose.
   * @param name Name of the Player to remove.
   */
  void removePlayer(String name);

  /**
   * Adds a new Observer to this Referee
   * @param name String name of the new Observer
   */
  void addObserver(String name);

  /**
   * Updates all Observers with either :
   * 1. new Updates to the board
   * 2. new Board Requests
   * 3. End of Game messages
   * @param jsonString Updates to be sent to observers
   */
  void updateObserver(String jsonString);


}
