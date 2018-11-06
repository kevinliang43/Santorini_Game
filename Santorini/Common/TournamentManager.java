import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Design of Tournament Manager Interface
 */
public class TournamentManager {

  //IMPLEMENTER, needs all the following:

  //minimum number of players needed to run a tournament
  private static int MIN_PLAYERS;
  //maximum number of players a tournament can have
  private static int MAX_PLAYERS;
  // Timeout for adding players
  private static int TIMEOUT;
  // Number of matches between each player
  // Or number of round robins to be played
  private static int NUM_ROUND_ROBINS;
  // Number of games per round between two individual players in a single game
  private static int NUM_GAMES_PER_ROUND;

  //list of all Players active
  List<Player> players;
  //list of removed Players
  List<Player> removedPlayers;
  //list of all Referee games active
  List<Referee> allGames;
  //list of all Observers
  List<Observer> observers;
  //map of players Names to number of games won so far
  HashMap<String, Integer> scoreMap;

  /**
   * Constructor for a tournamentManager
   * @param minPlayers minimum players required to play a tournament, must be greater than 2
   * @param maxPlayers maximum players that this tournament can hold
   * @param timeout time given to accept new players before the tournament is begun
   * @param numRoundRobins number of rounds to play
   * @param gamesPerRound number of games to play within each round, winner is best of gamesPerRound
   * @throws IllegalStateException thrown when: 1) minPlayers < 2
   *                                            2) maxPlayers < minPlayers
   *                                            3) timeout < 0
   *                                            4) numRoundRobins < 1
   *                                            5) gamesPerRound < 1
   */
  public TournamentManager(int minPlayers, int maxPlayers, int timeout, int numRoundRobins, int gamesPerRound) throws IllegalStateException {
    this.MIN_PLAYERS = minPlayers;
    this.MAX_PLAYERS = maxPlayers;
    this.TIMEOUT = timeout;
    this.NUM_ROUND_ROBINS = numRoundRobins;
    this.NUM_GAMES_PER_ROUND = gamesPerRound;
    this.players = new ArrayList<>();
    //list of removed Players
    this.removedPlayers = new ArrayList<>();
    //list of all Referee games active
    this.allGames = new ArrayList<>();
    //list of all Observers
    this.observers = new ArrayList<>();
    // score Map
    this.scoreMap = new HashMap<>();
  }


  /**
   * Main function for the Tournament Manager.
   * The Tournament will go through multiple phases:
   *
   * 1. Setup
   *         - Tournament will wait and accept new Players for a set amount of Time.
   *         - Once the Tournament has hit capacity (MAX_PLAYERS reached) or the TIMEOUT has been reached,
   *           this phase is over, and it will go onto the next phase.
   * 2. Run Tournament
   *         - Tournament Manager will attempt to run the Tournament between all players that have connected so far.
   *         - Tournament will NOT begin if there is less than MIN_PLAYERS in the current Tournament
   *         - If the Tournament Manager has enough players, then it will run NUM_ROUND_ROBINS number of Round Robins between all players
   *           with each round having NUM_GAMES_PER_ROUND number of games per each Referee game.
   *              - Example: A single Round Robin can be defined as Tournament between all players, where each player
   *                         faces each other player exactly once.
   *              - Example: A double Round Robin can be defined as Tournament between all players, where each player
   *                         faces each other player exactly twice.
   *              - If at any point, a player is removed by a Referee, the Round Robin will continue without that player
   *                being placed in any other match. Previous wins from the removed Player will also be rewarded to the
   *                player that was playing the removed player at the time.
   *         - Tournament Manager will update and keep track of games won, who won each game, if any players are removed
   *           from games for each Referee being run.
   *
   * 3. Shutdown
   *         - Once the Round Robin phase has been finished (runTournament) an overall winner is selected.
   *         - Tournament is shutdown.
   *
   *
   */
  public void main() throws IllegalStateException{

    //SETUP
    // Add Players
    // Read Config File to Get Players and Observers
    JsonNode config = ConfigReader.readAndParse().get(0);
    ArrayList<ArrayList<String>> playersArgs = ConfigReader.getFields("players", config);
    ArrayList<ArrayList<String>> observersArgs = ConfigReader.getFields("observers", config);
    // Add AI Players and Observers to this Tournament from Config File
    this.players = ConfigReader.buildPlayers(playersArgs);
    this.observers = ConfigReader.buildObservers(observersArgs);
    if (this.players.size() > this.MAX_PLAYERS || this.players.size() < this.MIN_PLAYERS) {
      throw new IllegalStateException("Cannot Start a Tournament with the current number of Players.");
    }

    //TODO: KEEP TRACK OF NUMBER OF PLAYERS IF ADDING PLAYERS LATER.

    // Start Tournament
    this.runTournament();

    // End Tournament
    // Print Removed Players
    System.out.println(this.removedPlayers);


    // TODO: Inform Players and Observers of names


  }


  /**
   * Runs a tournament between this tournaments players in a Round Robin fashion
   * Updates the scoreMap of Player Wins after every Referee is finished running a game between its
   * respective players.
   *
   * If at any time, a player is removed from a game by the Referee, then the removedPlayers data structure will
   * also be updated with the removed player.
   *
   * @return the winning player of the tournament
   */
  //TODO CHANGE TO RETURN PLAYER?
  private void runTournament() {

    for (int i = 0; i < this.NUM_ROUND_ROBINS; i++) {
      // Get a Copy of All current Players
      ArrayList<Player> playersCopy = new ArrayList(this.players);
      // Make sure size is even
      if (playersCopy.size()%2 == 1) {
        playersCopy.add(null);
      }

      for (int game = 0; game < playersCopy.size(); game++) {
        for (int j = 0; j < playersCopy.size()/2; j++) {
          System.out.println("Starting Game between: " + playersCopy.get(j).getName() + " and " + playersCopy.get(playersCopy.size()/2 + j).getName());
          Referee currentGame = this.beginGame(playersCopy.get(j), playersCopy.get(playersCopy.size()/2 + j));
          //FIXME: TEST


          if (currentGame != null) {
            this.allGames.add(currentGame);
            //If there is a kicked player, add it to the kicked players.
            if (currentGame.getKickedPlayer() != null) {
              this.removedPlayers.add(currentGame.getKickedPlayer());
            }
          }
        }
        //SHIFT
        Player shift = playersCopy.get(playersCopy.size() -1);
        playersCopy.remove(playersCopy.size() - 1);
        playersCopy.add(1, shift);
      }


    }
  }


  /**
   * Creates a new instance of a Player and adds it to the match lobby of players before a Tournament is begun.
   * If the tournament manager discovers that several players with the same name are to participate in a tournament,
   * it picks a unique name for all but one of the players and informs these players of the chosen names.
   * @param name      - String representing a new player's ID
   * @param strat     - Strategy to be given to the new Player
   * @return String representation of the selected name for the new Player.
   */
  private String addPlayer(String name, Strategy strat) {

    // Get a unique name for the new player.
    for (int i = 0; i < this.players.size(); i++) {
      Player currentPlayer = this.players.get(i);
      if (currentPlayer.getName().equals(name)) {
        name+=this.players.size();
        i = 0;
      }
    }
    // Create new Player
    Player newPlayer = new Player(name, this.players.size(), strat);
    this.players.add(newPlayer);

    return name;

  }

  /**
   * Creates a Referee between two Players, and begins the game.
   * runTournament will input the Players based on "foo-est".
   * @param player1 Player 1 to be added to the game.
   * @param player2 Player 2 to be added to the game.
   * @return Referee Object that is overseeing the game between the two given Players.
   */
  //TODO MAKE PRIVATE
  private Referee beginGame(Player player1, Player player2) {
    // Begin game if:
    // Both Players are not null
    // removedPlayers does not contain either player.
    if (player1 != null && player2 != null &&
            !this.removedPlayers.contains(player1) && !this.removedPlayers.contains(player2)) {
      Referee ref = new Referee(player1, player2);
      //FIXME TEST
      ref.addObserver("TEST");
      ref.runGame(this.NUM_GAMES_PER_ROUND);
      System.out.println(ref.getObservers().get(0).getHistory());
      System.out.println(ref.getPlayers().size());
      return ref;
    }
    else {
      return null;
    }

  }


  //CLIENT

  /**
   * The only parts a client of this code will need will be the Constructor and the Main() method. All methods needed
   * to run a tournament will be kept private and handled within the Main.
   */


}