import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//Marina Karr: karr.m@husky.neu.edu
//Kevin Liang: liang.ke@husky.neu.edu
/**
 * A Referee oversees and performs actions for a game of Santorini.
 * It accepts players until MAX_PLAYERS is reached. Then waits until
 * runGame is called. From there the placePhase begins until all
 * workers have been placed, then moves to the turnPhases alternating
 * between players. If a Player provides an invalid Action, the game
 * is ended and the other Player is defined as the winner.
 */
public class Referee implements IReferee{
  public final int MAX_PLAYERS = 2;
  public final int WORKERS_PER_PLAYER = 2;
  public final long TIMEOUT = 10000; //in milliseconds. divide by 1000 for seconds

  private ArrayList<Player> players;
  private Board officialBoard;
  private Status currentStatus;
  private Turn currentTurn;
  private Player winningPlayer;
  private Player kickedPlayer;
  private HashMap<Player, Integer> playerWins;
  private ArrayList<Observer> observers;

  //String messages sent to users
  private final String NOT_ENOUGH_PLAYERS = "Cannot Start a game without max number of players.";
  private final String INVALID_NUM_GAMES = "Cannot run a best of N games, where N is less than 1 or even.";
  private final String ILLEGAL_PLACE = " has made an illegal place action.";
  private final String WINNING_MOVE = " has made a winning Move!";
  private final String WINNING_BUILD = " has made a winning Build! The other player is unable to move.";
  private final String ILLEGAL_BUILD = " has made an illegal build action.";
  private final String ILLEGAL_MOVE = " has made an illegal move action.";
  private final String INVALID_PLAYER_NAME = "A Player with that name already exists!";

  public Referee() {
    this.players = new ArrayList<>();
    this.officialBoard = new Board();
    this.currentStatus = Status.PLACE;
    this.currentTurn = Turn.PLAYER1;
    this.playerWins = new HashMap<>();
    this.observers = new ArrayList<>();
  }
  public Referee(Player p1, Player p2) {
    this.officialBoard = new Board();
    this.currentStatus = Status.PLACE;
    this.currentTurn = Turn.PLAYER1;
    this.playerWins = new HashMap<>();
    this.observers = new ArrayList<>();
    this.players = new ArrayList<>();
    this.players.add(p1);
    this.players.add(p2);
    this.playerWins.put(p1, 0);
    this.playerWins.put(p2, 0);

  }


  /**
   * Runs a game between this classes two players n amount of times and determines the winner based off of which
   * player won the most games out of n
   * @param numGames odd number of games to play
   * @return Player that won the most games within this set of n games
   * @throws IllegalArgumentException when the given n is even, must be odd to guarantee a winner
   */
  public Player runGame(int numGames) throws IllegalArgumentException {
    if (numGames < 1 || numGames % 2 == 0) {
      throw new IllegalArgumentException(this.INVALID_NUM_GAMES);
    }

    if (this.players.size() != MAX_PLAYERS) {
      throw new IllegalStateException(this.NOT_ENOUGH_PLAYERS);
    }

    for (int currentGame = 0; currentGame < numGames; currentGame++) {
      // Check conditions for a valid game.
      if (this.players.size() != MAX_PLAYERS) {
        // If there isnt a MAX_PLAYERS number of players in the Referee, then return the remaining
        // Player as the winner.
        return this.players.get(0);
      }

      //TODO Break after n/2 wins
      if (Collections.max(playerWins.values()) > numGames/2) {
        break;
      }

      // Reinitialize the GameBoard, the Players, Workers, Turn
      this.officialBoard = new Board();
      this.currentStatus = Status.PLACE;
      this.currentTurn = Turn.PLAYER1;
      for (Player player : this.players) {
        player.resetWorkers();
      }

      Player winner = this.runGame();
      // Update the Player wins
      this.playerWins.put(winner, this.playerWins.get(winner) + 1);
    }

    //Determine overall winner

    Player winningPlayer = null;
    int numWins = -1;

    for (Player player : this.playerWins.keySet()) {
      player.resetWorkers();
      int currentNumWins = this.playerWins.get(player);
      if (currentNumWins > numWins) {
        numWins = currentNumWins;
        winningPlayer = player;
      }
    }


    return winningPlayer;
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

  public Player runGame() throws IllegalStateException {
    // Check that there are MAX_PLAYERS contained
    if (this.players.size() != MAX_PLAYERS) {
      throw new IllegalStateException(this.NOT_ENOUGH_PLAYERS);
    }

    // Send initial board State to observers
    this.updateObserver(this.officialBoard.asJSONArray() + "\n");

    int workerNum = 0;

    // Place phase
    for (int i = 0; i < WORKERS_PER_PLAYER; i++) {
      for (Player player : this.players) {
        if (this.currentStatus == Status.PLACE) {
          // Attempt to get next Action with timeout
          IAction nextAction = this.tryGetMove(player);
          if (nextAction != null && nextAction instanceof Action) {
            // action was created in time
            placePhase(player, (Action)nextAction, workerNum);
            workerNum++;
            if (this.players.size() != this.MAX_PLAYERS) {
              // Player was Kicked out
              return this.winningPlayer;
            }
          }
          else {
            // Action was not created in time

            this.gameOver(player);
            return this.winningPlayer;
          }
        }
      }
    }
    this.currentStatus = Status.MOVEBUILD;
    // Move onto Turn Phase
    while(RuleChecker.isGameOver(this.officialBoard, this.officialBoard.getWorkerIDs()) == GameOverStatus.NOT_OVER && this.currentStatus != Status.GAMEOVER) {
      Player currentPlayer = players.get(0); // Get current Player
      if (this.currentTurn == Turn.PLAYER2) {
        currentPlayer = players.get(1);
      }
      // Get next action from player
      IAction nextAction = this.tryGetMove(currentPlayer);
      if (nextAction != null) {
        // action was created in time
        if (nextAction instanceof MoveBuild) {
          turnPhase(currentPlayer, (MoveBuild) nextAction);
        }
        else { // Giveup
          this.updateObserver("\"Player Gave Up.\"");
          // Determine Winner
          if (this.currentTurn == Turn.PLAYER1 || this.kickedPlayer != null) {
            this.winningPlayer = players.get(1);
          }
          else {
            this.winningPlayer = players.get(0);
          }
          return this.winningPlayer;
        }

      }
      else {
        // Action was not created in time
        this.updateObserver("\"Player Timed out\"");

        this.gameOver(currentPlayer);
        return this.winningPlayer;
      }
    }

    // Determine Winner
    if (this.currentTurn == Turn.PLAYER1 || this.kickedPlayer != null) {
      this.winningPlayer = players.get(0);
    }
    else {
      this.winningPlayer = players.get(1);
    }

    return this.winningPlayer;
  }


  /**
   * Tries to get the next move of the player, if a move is found, an IAction is returned. If it times out, null is returned
   * @param player Player taking the next action
   * @return an IAction or null based on whether or not a move was found
   */
  private IAction tryGetMove(Player player) {

    // Initialize Runnables
    ActionRunnable ActionRunnable = new ActionRunnable(player, this.officialBoard, this.currentStatus);

    //Initialize Thread
    Thread placeThread = new Thread(ActionRunnable);
    placeThread.start();

    // Join thread if thread is complete, or passes the TIMEOUT
    try {
      placeThread.join(TIMEOUT);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return ActionRunnable.getAction();
  }

  /**
   * Perform the Place action on the officialBoard for the passed Player.
   * An invalid place will result in the Player being kicked.
   * @param p The Player to get the next Place action from.
   */
  private void placePhase(Player p, Action nextPlace, int workerID) {

    if (nextPlace.getActionType() != Status.PLACE || !RuleChecker.isPlaceLegal(this.officialBoard, nextPlace.getX(), nextPlace.getY())) {
      this.updateObserver(p.getName() + this.ILLEGAL_PLACE);
      this.gameOver(p);
      return;

    }
    // Else, the place is valid
    else {
      String workerName = p.getName() + (p.getWorkerIDs().size() + 1);
      this.officialBoard.placeWorker(nextPlace.getX(), nextPlace.getY(), workerName, workerID);
      p.addWorkerID(workerID);
      p.addWorkerName(workerName);
    }

    // Send new updates to observers
    updateObserver(this.officialBoard.asJSONArray()+"\n");

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
  private void turnPhase(Player p, MoveBuild nextMoveBuild) {
    Board initBoard = this.getOfficialBoard();

    // Check if the move is a MoveBuild
    // Or if the place is not legal,
    // If not, the player loses. and the player gets kicked
    if (nextMoveBuild.getActionType() == Status.MOVEBUILD &&
            RuleChecker.isMoveLegal(this.officialBoard, nextMoveBuild.getMove().getWorkerID(), nextMoveBuild.getMove().getX(), nextMoveBuild.getMove().getY())) {
      // Execute the Move
      this.officialBoard.moveWorker(nextMoveBuild.getMove().getWorkerID(), nextMoveBuild.getMove().getX(), nextMoveBuild.getMove().getY());
      //FIXME UPDATE OBSERVER
//      // Send new updates to observers
//      updateObserver(this.officialBoard.asJSONArray());
      // Check if the move results in a win
      if (RuleChecker.isGameOver(this.officialBoard, p.getWorkerIDs())  == GameOverStatus.WINNING_FLOOR) {
        this.winningPlayer = p;
        this.currentStatus = Status.GAMEOVER;
        // Update Observer
        updateObserver(Translator.moveBuildAsJSON(initBoard, new MoveBuild(nextMoveBuild.getMove(), null)));
        updateObserver(this.officialBoard.asJSONArray()+"\n");
        updateObserver(p.getName() + this.WINNING_MOVE);
        return;
      }

      // Update Observer with BuildMove
      updateObserver(Translator.moveBuildAsJSON(initBoard, nextMoveBuild));

      // Check if the Build is legal
      if (RuleChecker.isBuildLegal(this.officialBoard, nextMoveBuild.getBuild().getWorkerID(), nextMoveBuild.getBuild().getX(), nextMoveBuild.getBuild().getY())) {
        // execute the build
        this.officialBoard.buildFloor(nextMoveBuild.getBuild().getX(), nextMoveBuild.getBuild().getY());
        // Check if the build results in the other player losing
        BoardStatus bs = new BoardStatus(this.officialBoard, this.currentStatus);
        if (RuleChecker.isGameOver(this.officialBoard, bs.filterWorkers(p.getWorkerIDs())) == GameOverStatus.NO_MOVE_BUILD) {
          this.winningPlayer = p;
          this.currentStatus = Status.GAMEOVER;
          // Update Observer
          updateObserver(this.officialBoard.asJSONArray()+"\n");
          updateObserver(p.getName() + this.WINNING_BUILD);
          return;
        }
        else {
          updateObserver(this.officialBoard.asJSONArray()+"\n");
        }



      }
      else {
        this.updateObserver(p.getName() + this.ILLEGAL_BUILD);
        this.gameOver(p);
      }

    }
    else {
      this.updateObserver(p.getName() + this.ILLEGAL_MOVE);
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
        throw new IllegalArgumentException(this.INVALID_PLAYER_NAME);
      }
    }
    Player aNewChallenger = new Player(aChallengersName, players.size(), aChallengersStrategy);
    this.players.add(aNewChallenger);
    this.playerWins.put(aNewChallenger, 0);
  }

  /**
   * Removes a Player from the game. They lose.
   * @param name Name of the Player to remove.
   */
  public void removePlayer(String name) {

    for (Player player: this.players) {
      if (player.getName().equals(name)) {
        this.kickedPlayer = player;
      }
    }

    this.players.remove(this.kickedPlayer);

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
   * Getter for Players of this game
   * @return List of Players
   */
  public ArrayList<Player> getPlayers() {
    return players;
  }

  /**
   * Get a Copy of the current board
   * @return Deep Copy of the current Board.
   */
  public Board getOfficialBoard() {
    return officialBoard.getBoardState();
  }

  /**
   * Gets the current status of the game
   * @return Status representing current status
   */
  public Status getCurrentStatus() {
    return currentStatus;
  }

  /**
   * Gets the current Turn that this game is on.
   * @return current Turn
   */
  public Turn getCurrentTurn() {
    return currentTurn;
  }

  /**
   * Function that updates the state of the game to Game Over once a losing player has been
   * determined
   * @param p Player that lost the game.
   */
  private void gameOver(Player p) {
    this.kickedPlayer = p;
    removePlayer(p.getName());
    this.winningPlayer = this.players.get(0); // Works for a game of 2 players
    this.currentStatus = Status.GAMEOVER;
    updateObserver(this.winningPlayer.getName() +" has won.");
  }

  /**
   * Adds a new Observer to this Referee
   * @param name String name of the new Observer
   */
  public void addObserver(String name) {
    this.observers.add(new Observer(name));
  }

  /**
   * Updates all Observers with either :
   * 1. new Updates to the board
   * 2. new Board Requests
   * 3. End of Game messages
   * @param jsonString Updates to be sent to observers
   */
  public void updateObserver(String jsonString) {
    for (Observer observer : this.observers) {
      observer.append(jsonString);
    }
  }

  /**
   * Get the Observers of this Referee
   * @return List of Observers
   */
  public List<Observer> getObservers() {
    return this.observers;
  }

  /**
   * Sets the current Status.
   * TESTING ONLY
   * @param s
   */
  private void setCurrentStatus(Status s) {
    this.currentStatus = s;
  }


  /**
   * Creates a ReadCopy of the PlayerWins map and returns it
   * String -> Int, rather than Player-> Int
   * @return ReadCopy of the PlayerWins map
   */
  public HashMap<String, Integer> getPlayerWins() {
    HashMap<String, Integer> newmap = new HashMap<>();
    for (Player player: this.playerWins.keySet()) {
      newmap.put(player.getName(), this.playerWins.get(player));
    }
    return newmap;
  }

  public Player getKickedPlayer() {
    return this.kickedPlayer;
  }

}


