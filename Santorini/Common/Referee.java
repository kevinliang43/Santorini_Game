import java.util.ArrayList;

/**
 * A Referee oversees and performs actions for a game of Santorini.
 * It accepts players until MAX_PLAYERS is reached. Then waits until
 * runGame is called. From there the placePhase begins until all
 * workers have been placed, then moves to the turnPhases alternating
 * between players. If a Player provides an invalid Action, the game
 * is ended and the other Player is defined as the winner.
 */
public class Referee {
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
  public Turn runGame() throws IllegalStateException {
    // Check that there are MAX_PLAYERS contained
    if (this.players.size() != MAX_PLAYERS) {
      throw new IllegalStateException("Cannot Start a game without max number of players.");
    }

    // Place phase
    for (int i = 0; i < WORKERS_PER_PLAYER; i++) {
      for (Player player : this.players) {
        if (this.currentStatus == Status.PLACE) {
          placePhase(player); //TODO: TIMEOUT
        }
      }
    }

    // Check if game over
    if (this.currentStatus == Status.GAMEOVER) {
      return this.currentTurn;
    }

    this.currentStatus = Status.MOVEBUILD;

    // Move onto Turn Phase
    while(RuleChecker.isGameOver(this.officialBoard, this.officialBoard.getWorkerIDs()) == GameOverStatus.NOT_OVER) {
      if (this.currentTurn == Turn.PLAYER1) {
        turnPhase(players.get(0));
      }
      else {
        turnPhase(players.get(1));
      }
    }
    return this.currentTurn;
  }

  /**
   * Perform the Place action on the officialBoard for the passed Player.
   * An invalid place will result in the Player being kicked.
   * @param p The Player to get the next Place action from.
   */
  private void placePhase(Player p) {
    Action nextAction = (Action)p.getNextAction(this.officialBoard, this.currentStatus);
    // Check if the move is a Place
    // Or if the place is not legal,
    // If not, the player loses. and the player gets kicked
    if (nextAction.actionType != Status.PLACE || RuleChecker.isPlaceLegal(this.officialBoard, nextAction.x, nextAction.y)) {
      this.gameOver(p);
      return;
    }
    // Else, the place is valid
    else {
      int workerID = this.officialBoard.placeWorker(nextAction.x, nextAction.y);
      p.addWorkerID(workerID);
    }

    // Update turn
    if (this.currentTurn == Turn.PLAYER1) {
      this.currentTurn = Turn.PLAYER2;
    }
    else {
      this.currentTurn = Turn.PLAYER1;
    }

  }

  /**
   * Perform the Move and Build actions provided by the Player. Checks if
   * the Player moved to the RuleChecker.WINNING_FLOOR after the Move Action.
   * An invalid Move or Build will result in the Player being kicked.
   * @param p The Player to get the next Move and Build Actions from.
   */
  private void turnPhase(Player p) {
    IAction nextAction = p.getNextAction(this.officialBoard, this.currentStatus);

    MoveBuild nextMoveBuild = null;

    if (nextAction instanceof MoveBuild) {
      nextMoveBuild = (MoveBuild) nextAction;
    }
    else {
      this.gameOver(p);
    }

    // Check if the move is a MoveBuild
    // Or if the place is not legal,
    // If not, the player loses. and the player gets kicked
    if (nextMoveBuild.actionType == Status.MOVEBUILD ||
            RuleChecker.isMoveLegal(this.officialBoard, nextMoveBuild.move.workerID, nextMoveBuild.move.x, nextMoveBuild.move.y)) {
      // Execute the Move
      this.officialBoard.moveWorker(nextMoveBuild.move.workerID, nextMoveBuild.move.x, nextMoveBuild.move.y);
      // Check if the move results in a win
      if (RuleChecker.isGameOver(this.officialBoard, p.getWorkerIDs())  == GameOverStatus.WINNING_FLOOR) {
        this.winningPlayer = p;
        this.currentStatus = Status.GAMEOVER;
        return;
      }
      // Check if the Build is legal
      if (RuleChecker.isBuildLegal(this.officialBoard, nextMoveBuild.build.workerID, nextMoveBuild.build.x, nextMoveBuild.build.y)) {
        // execute the build
        this.officialBoard.buildFloor(nextMoveBuild.build.x, nextMoveBuild.build.y);
        // Check if the build results in the other player losing
        BoardStatus bs = new BoardStatus(this.officialBoard, this.currentStatus);
        if (RuleChecker.isGameOver(this.officialBoard, bs.filterWorkers(p.getWorkerIDs())) == GameOverStatus.NO_MOVE_BUILD) {
          this.winningPlayer = p;
          this.currentStatus = Status.GAMEOVER;
          return;
        }
      }
      else {
        this.gameOver(p);
      }

    }
    else {
      this.gameOver(p);
    }

    // Update turn
    if (this.currentTurn == Turn.PLAYER1) {
      this.currentTurn = Turn.PLAYER2;
    }
    else {
      this.currentTurn = Turn.PLAYER1;
    }

  }

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
    this.players.add(aNewChallenger);
  }

  /**
   * Removes a Player from the game. They lose.
   * @param name Name of the Player to remove.
   */
  public void removePlayer(String name) {
    for (Player player: this.players) {
      if (player.getName().equals(name)) {
        this.players.remove(player);
      }
    }
  }

  /**
   * Get the reference to the Player who has won the game. Returns
   * null if nobody has won.
   * @return Player who has won the game or null if nobody has won.
   */
  public Player getWinner() {
    return this.winningPlayer;
  }

  /**
   * Function that updates the state of the game to Game Over once a losing player has been
   * determined
   * @param p Player that lost the game.
   */
  private void gameOver(Player p) {
    removePlayer(p.getName());
    this.winningPlayer = this.players.get(0); // Works for a game of 2 players
    this.currentStatus = Status.GAMEOVER;
  }

}
