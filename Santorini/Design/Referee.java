import java.util.ArrayList;

/**
 * A Referee oversees and performs actions for a game of Santorini.
 * It accepts players until MAX_PLAYERS is reached. Then waits until
 * runGame is called. From there the placePhase begins until all
 * workers have been placed, then moves to the turnPhases alternating
 * between players. If a Player provides an invalid Action, the game
 * is ended and the other Player is defined as the winner.
 */
public abstract class AReferee {
  public final int MAX_PLAYERS = 2;
  public final int WORKERS_PER_PLAYER = 2;

  private ArrayList<Player> players;
  private Board officialBoard;
  private Status currentStatus;
  private Turn currentTurn;
  private Player winningPlayer;

  public Referee() {
    this.players = new ArrayList<>();
    this.officialBoard = new Board();
    this.currentStatus = Status.PLACE;
    this.currentTurn = Turn.PLAYER1;
  }

  /**
   * Starts a game. The placePhase is executed until WORKERS_PER_PLAYER
   * are placed, then the game moves to the alternating turnPhase. When
   * the game is over, the method silently returns. A winner can be found
   * using getWinner. If a Player ever provides an invalid move, they are
   * removed and the game is determined as over with the other Player
   * being designated the winner.
   * @throws IllegalStateException if MAX_PLAYERS are not contained
   */
  public abstract void runGame() throws IllegalStateException;

  /**
   * Perform the Place action on the officialBoard for the passed Player.
   * An invalid place will result in the Player being kicked.
   * @param p The Player to get the next Place action from.
   */
  private abstract void placePhase(Player p);

  /**
   * Perform the Move and Build actions provided by the Player. Checks if
   * the Player moved to the RuleChecker.WINNING_FLOOR after the Move Action.
   * An invalid Move or Build will result in the Player being kicked.
   * @param p The Player to get the next Move and Build Actions from.
   */
  private abstract void turnPhase(Player p);

  /**
   * Creates a new Player instance using the given parameters.
   * @param aChallengersName Name of the Player. Must be unique.
   * @param aChallengersStrategy Stratgy instance to use for the Player.
   * @throws IllegalArgumentException if aChallengersName is already in use
   */
  public void addPlayer(String aChallengersName, Strategy aChallengersStrategy)
          throws IllegalArgumentException{
    for (Player p : this.players) {
      if (p.getName().equals(aChallengersName)) {
        throw new IllegalArgumentException("A Player with that name already exists!");
      }
    }
    Player aNewChallenger = new Player(aChallengersName, players.size(), aChallengersStrategy);
  }

  /**
   * Removes a Player from the game. They lose.
   * @param name Name of the Player to remove.
   */
  public abstract void removePlayer(String name);

  /**
   * Get the reference to the Player who has won the game. Returns
   * null if nobody has won.
   * @return Player who has won the game or null if nobody has won.
   */
  public abstract Player getWinner();
}
