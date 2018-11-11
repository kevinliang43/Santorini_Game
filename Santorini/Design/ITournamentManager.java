import java.util.HashMap;

/**
 * Design of Tournament Manager Interface
 */
public interface ITournamentManager {

//    //IMPLEMENTER, needs all the following:
//
//    //minimum number of players needed to run a tournament
//    private static int MIN_PLAYERS;
//    //maximum number of players a tournament can have
//    private static int MAX_PLAYERS;
//    // Timeout for adding players
//    private static int TIMEOUT;
//    // Number of matches between each player
//    // Or number of round robins to be played
//    private static int NUM_ROUND_ROBINS;
//    // Number of games per round between two individual players in a single game
//    private static int NUM_GAMES_PER_ROUND;
//
//    //list of all Players active
//    List<Player> players;
//    //list of removed Players
//    List<Player> removedPlayers;
//    //list of all Referee games active
//    List<Referee> currentGames;
//    //map of players Names to number of games won so far
//    HashMap<String, Integer> scoreMap;
//
//    /**
//     * Constructor for a tournamentManager
//     * @param minPlayers minimum players required to play a tournament, must be greater than 2
//     * @param maxPlayers maximum players that this tournament can hold
//     * @param timeout time given to accept new players before the tournament is begun
//     * @param numRoundRobins number of rounds to play
//     * @param gamesPerRound number of games to play within each round, winner is best of gamesPerRound
//     * @throws IllegalStateException thrown when: 1) minPlayers < 2
//     *                                            2) maxPlayers < minPlayers
//     *                                            3) timeout < 0
//     *                                            4) numRoundRobins < 1
//     *                                            5) gamesPerRound < 1
//     */
//    TournamentManager(int minPlayers, int maxPlayers, int timeout, int numRoundRobins, int gamesPerRound) throws IllegalStateException;


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
    void main();

    /**
     * Creates a new instance of a Player and adds it to the match lobby of players before a Tournament is begun.
     * @param name      - String representing a new player's ID
     * @param strat     - Strategy to be given to the new Player
     */
    //void addPlayer(String name, Strategy strat);


    //CLIENT

    /**
     * The only parts a client of this code will need will be the Constructor and the Main() method. All methods needed
     * to run a tournament will be kept private and handled within the Main.
     */


}