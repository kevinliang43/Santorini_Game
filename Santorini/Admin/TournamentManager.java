import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Design of Tournament Manager Interface
 */
public class TournamentManager implements ITournamentManager{

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

  // List of all Players
  private List<Player> allPlayers;
  //list of all Players active
  private List<Player> players;
  //list of removed Players
  private List<Player> removedPlayers;
  //list of all Referee games active
  private List<Referee> allGames;
  //list of all Observers
  private List<Observer> observers;
  //map of players Names to number of games won so far
  private HashMap<String, Integer> scoreMap;

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
    // List of All players
    this.players = new ArrayList<>();
    // List of active Players
    this.allPlayers = new ArrayList<>();
    //list of removed Players
    this.removedPlayers = new ArrayList<>();
    //list of all Referee games active
    this.allGames = new ArrayList<>();
    //list of all Observers
    this.observers = new ArrayList<>();
    // score Map
    this.scoreMap = new HashMap<>();
  }

  public TournamentManager(List<Player> players, int minPlayers, int maxPlayers, int timeout, int numRoundRobins, int gamesPerRound) throws IllegalStateException {
    this.MIN_PLAYERS = minPlayers;
    this.MAX_PLAYERS = maxPlayers;
    this.TIMEOUT = timeout;
    this.NUM_ROUND_ROBINS = numRoundRobins;
    this.NUM_GAMES_PER_ROUND = gamesPerRound;
    // List of All players
    this.players = players;
    // List of active Players
    this.allPlayers = new ArrayList<>();
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

    // Check to see if Config file is properly formatted:


    // Check if Field names are valid (i.e Config File was properly formatted)
    if (!Translator.checkFields(config, new ArrayList<>(Arrays.asList("players", "observers")))) {
      throw new IllegalStateException("Config File is not formatted correctly.");
    }

    ArrayList<ArrayList<String>> playersArgs = ConfigReader.getFields("players", config);
    ArrayList<ArrayList<String>> observersArgs = ConfigReader.getFields("observers", config);
    // Add AI Players and Observers to this Tournament from Config File
    this.players.addAll(ConfigReader.buildPlayers(playersArgs, this.players.size()));
    this.allPlayers.addAll(new ArrayList<>(this.players));
    this.observers.addAll(ConfigReader.buildObservers(observersArgs));


    // Start Tournament
    this.runTournament();

    // End Tournament
    // Print [[Removed Players], [[Game1Result], [Game2Result] ...]

    String JSONResults = this.tournamentResultAsJSON();

    // Inform Players of Outcome of game
    for (Player player : this.allPlayers) {
      player.sendMessage(JSONResults);
    }


    //System.out.println(JSONResults);
    System.exit(0);
  }

  /**
   * Main function (Same as Main defined above), however, it does not read in a config file.
   * Rather, it will run a Tournament with Players already added.
   */
  public void mainNoConfig() {
    this.allPlayers.addAll(new ArrayList<>(this.players));
    // Start Tournament
    this.runTournament();

    // End Tournament
    // Print [[Removed Players], [[Game1Result], [Game2Result] ...]

    String JSONResults = this.encountersAsJSON();

    // Inform Players of Outcome of game
    for (Player player : this.allPlayers) {
      player.sendMessage(JSONResults);
    }

    System.out.println(JSONResults);
    System.exit(0);
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

      for (int game = 0; game < playersCopy.size() - 1; game++) {
        for (int j = 0; j < playersCopy.size()/2; j++) {
          Referee currentGame = this.beginGame(playersCopy.get(j), playersCopy.get(playersCopy.size()/2 + j));
          if (currentGame != null) {
            this.allGames.add(currentGame);
            //If there is a kicked player, add it to the kicked players.
            if (currentGame.getKickedPlayer() != null) {
              this.removedPlayers.add(currentGame.getKickedPlayer());
              this.players.remove(currentGame.getKickedPlayer());
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
  //will be used and implemented later when we add clients connecting to the tournament via tcp server
/*
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
*/

  /**
   * Creates a Referee between two Players, and begins the game.
   * runTournament will input the Players based on "foo-est".
   * @param player1 Player 1 to be added to the game.
   * @param player2 Player 2 to be added to the game.
   * @return Referee Object that is overseeing the game between the two given Players.
   */
  private Referee beginGame(Player player1, Player player2) {
    // Begin game if:
    // Both Players are not null
    // removedPlayers does not contain either player.
    if (player1 != null && player2 != null &&
            !this.removedPlayers.contains(player1) && !this.removedPlayers.contains(player2)) {

      // Update with names
      player1.sendMessage("\"" + player2.getName() + "\"");
      player2.sendMessage("\"" + player1.getName() + "\"");

      Referee ref = new Referee(player1, player2);
      ref.runGame(this.NUM_GAMES_PER_ROUND);
      return ref;
    }
    else {
      return null;
    }

  }

  /**
   * Get List of GameResults from this Tournament.
   * Game Results store the winner and loser of a game.
   * Alterations are made to GameResults as such:
   * 1. If both players in a game were removed at any point, the result is thrown.
   * 2. If a Winner of a game is removed in any game, the Loser of that game is made the winner
   *    (Assuming the loser is not removed from any game)
   *
   * @return List of Game Results
   */
  private ArrayList<GameResult> getGameResults() {
    ArrayList<GameResult> results = new ArrayList<>();

    for(Referee game : this.allGames) {
      // 1  person was removed
      if (game.getKickedPlayer() != null) {
        // If the other person has not been kicked in any other game, construct the game result object
        if (!this.removedPlayers.contains(game.getWinner())) {
          results.add(new GameResult(game.getWinner(), game.getKickedPlayer(), true));
        }
      }
      // No people were removed
      else {
        Player winner = game.getWinner();
        ArrayList<Player> currentPlayers = (ArrayList<Player>) game.getPlayers().clone();
        currentPlayers.remove(winner);
        Player loser = currentPlayers.get(0);

        // Base Case, winner is not in removedPlayers
        if (!this.removedPlayers.contains(winner)) {
          results.add(new GameResult(winner, loser, false));
        }
        // Case: Winner is in  removed players, loser is not
        else if (this.removedPlayers.contains(winner) && !this.removedPlayers.contains(loser)) {
          results.add(new GameResult(loser, winner, false));
        }
        // Other wise, both players are in removedplayers, result of match is thrown away.
      }
    }
    return results;
  }

  /**
   * Returns the Tournament Results as an array of arrays
   * First Array:
   *      List of Kicked Players in the form of: ["NAME", "NAME2", ...]
   *
   * Second Array:
   *      List of GameResult. Format: [["NAME1", "NAME2"], ["NAME1", "NAME3"], ...]
   *      Sorted in order of First vs rest, second vs rest, etc...
   *
   * Full Format:
   *      [[FirstArray], [[SECOND], [ARRAYS], ...]
   *
   *
   * @return JSON String representing Tournament Results
   */
  private String tournamentResultAsJSON() {

    List<GameResult> results = this.getGameResults();

    // Sort game results by p1 v rest, p2 v rest ... etc
    ArrayList<GameResult> sortedResults = new ArrayList<>();

    for (int i = 0; i < this.allPlayers.size(); i++) {
      Player currentPlayer = this.allPlayers.get(i);
      for (GameResult result : results) {
        if (result.getWinner().getName().equals(currentPlayer.getName()) || result.getLoser().getName().equals(currentPlayer)) {
          sortedResults.add(result);
        }
      }
      results.removeAll(sortedResults);
    }
    return Translator.tournamentResultsAsJSON((ArrayList<Player>)this.removedPlayers, sortedResults);
  }

  /**
   * Returns the list of encounters outcomes from this completed Tournament.
   * where EncounterOutcome is one of the following:
   *    1. [String, String], which is the name of the winner followed by the loser;
   *    2. [String, String, "irregular"], which is like the first alternative but signals that the losing player misbehaved.
   *
   * @return JSON String representing a list of Encounter Outcomes.
   */
  private String encountersAsJSON() {
    List<GameResult> results = this.getGameResults();
    // Sort game results by p1 v rest, p2 v rest ... etc
    ArrayList<GameResult> sortedResults = new ArrayList<>();

    for (int i = 0; i < this.allPlayers.size(); i++) {
      Player currentPlayer = this.allPlayers.get(i);
      for (GameResult result : results) {
        if (result.getWinner().getName().equals(currentPlayer.getName()) || result.getLoser().getName().equals(currentPlayer)) {
          sortedResults.add(result);
        }
      }
      results.removeAll(sortedResults);
    }

    return Translator.encountersAsJSON(sortedResults);

  }

  //CLIENT

  /**
   * The only parts a client of this code will need will be the Constructor and the Main() method. All methods needed
   * to run a tournament will be kept private and handled within the Main.
   */


}