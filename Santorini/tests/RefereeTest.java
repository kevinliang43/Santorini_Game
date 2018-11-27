import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
//tests for the referee class
public class RefereeTest{

  Referee ref;
  Board board;
  Strategy diag;
  Strategy furthest;

  public RefereeTest(){

  }


  /**
   * Set up initial state of referee
   */
  @Before
  public void setupReferee() {
    this.ref = new Referee();
    this.diag = new StayAliveStrategy(true, 1);
    this.furthest = new StayAliveStrategy(false, 1);
  }

  /**
   * Sets up referee and adds two players.
   */
  @Before
  public void setupGame() {
    setupReferee();
    ref.addPlayer("one", this.diag);
    ref.addPlayer("two", this.furthest);
  }

  /**
   * Test the initial Referee state:
   * Should be Player 1's turn
   * Should be Place status
   * Should have 0 players
   * Should have no workers.
   */
  @Test
  public void testInitialize() {
    setupReferee();
    assertEquals(ref.getCurrentTurn(), Turn.PLAYER1);
    assertEquals(ref.getCurrentStatus(), Status.PLACE);
    assertEquals(ref.getPlayers(), new ArrayList<Player>());
    assertEquals(ref.getOfficialBoard().getWorkerIDs().size(), 0);
  }

  /**
   * Basic Tests for adding Player.
   * Referee should be updated with new players.
   */
  @Test
  public void testAddPlayer() {
    setupReferee();
    // Test player lists
    assertEquals(ref.getPlayers().size(), 0);
    ref.addPlayer("one", this.diag);
    assertEquals(ref.getPlayers().size(), 1);
    ref.addPlayer("two", this.furthest);
    assertEquals(ref.getPlayers().size(), 2);
    //Test Names
    assertEquals(ref.getPlayers().get(0).getName(), "one");
    assertEquals(ref.getPlayers().get(1).getName(), "two");
  }

  /**
   * Test that you cannot add a player with the same name
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalAddDuplicatePlayer() {
    setupGame();
    ref.addPlayer("one", this.diag);
  }

  /**
   * Test remove player
   */
  @Test
  public void testRemovePlayer() {
    setupGame();
    assertEquals(ref.getPlayers().size(), 2);
    assertEquals(ref.getPlayers().get(0).getName(), "one");
    assertEquals(ref.getPlayers().get(1).getName(), "two");
    ref.removePlayer("one");
    assertEquals(ref.getPlayers().size(), 1);
    assertEquals(ref.getPlayers().get(0).getName(), "two");
    ref.removePlayer("three");
    assertEquals(ref.getPlayers().size(), 1);
  }


  /**
   * Tests the following components of Place Phase:
   * Place phase alternates between both players.
   * Updates the board.
   * Updates turn after placing.
   * Updates the Player's Worker IDs
   */
//  @Test
//  public void testPlacePhase() {
//    setupGame();
//    //Player 1 Places
//    assertEquals(this.ref.getOfficialBoard().getWorkerIDs().size(), 0);
//    assertEquals(this.ref.getCurrentTurn(), Turn.PLAYER1);
//    assertEquals(this.ref.getCurrentStatus(), Status.PLACE);
//    Player p1 = ref.getPlayers().get(0);
//    Player p2 = ref.getPlayers().get(1);
//    assertEquals(p1.getWorkerIDs().size(), 0);
//    assertEquals(p2.getWorkerIDs().size(), 0);
//    IAction place = p1.getNextAction(this.board, Status.PLACE);
//    Action placeAction = (Action) place;
//    this.ref.placePhase(p1, placeAction);
//
//    //Check status of board after player 1 places.
//    assertEquals(this.ref.getOfficialBoard().getWorkerIDs().size(), 1);
//    assertEquals(this.ref.getCurrentTurn(), Turn.PLAYER2);
//    assertEquals(this.ref.getCurrentStatus(), Status.PLACE);
//    assertEquals((int)this.ref.getOfficialBoard().getWorkerIDs().get(0), 0);
//    assertEquals(p1.getWorkerIDs().size(), 1); // Player 1 added worker
//    assertEquals(p2.getWorkerIDs().size(), 0); // Player 2 should be the same
//    Square player1WorkerSquare = this.ref.getOfficialBoard().getWorkerSquare(0);
//
//    // Check square of placed worker
//    // should be 0,0 since player 1 has diag strat.
//    assertEquals(player1WorkerSquare.getX(), 0);
//    assertEquals(player1WorkerSquare.getY(), 0);
//
//    //Player 2 Places
//    IAction place2 = p2.getNextAction(this.board, Status.PLACE);
//    Action placeAction2 = (Action) place2;
//    this.ref.placePhase(p2, placeAction);
//    assertEquals(this.ref.getOfficialBoard().getWorkerIDs().size(), 2);
//    assertEquals(this.ref.getCurrentTurn(), Turn.PLAYER1);
//    assertEquals(this.ref.getCurrentStatus(), Status.PLACE);
//    assertEquals((int)this.ref.getOfficialBoard().getWorkerIDs().get(1), 1);
//    assertEquals(p1.getWorkerIDs().size(), 1); //Player 1 should be the same
//    assertEquals(p2.getWorkerIDs().size(), 1); // Player 2 added worker
//    Square player2WorkerSquare = this.ref.getOfficialBoard().getWorkerSquare(1);
//
//    // Check square of player 2's placed worker
//    // should be 5,5 since player 2 has furthest strat.
//    assertEquals(player2WorkerSquare.getX(), 5);
//    assertEquals(player2WorkerSquare.getY(), 5);
//
//  }

    /**
     * Tests placing a worker when the game is not in the place phase, player should get kicked out and the game is over
     */
//  @Test
//  public void testPlacePhaseNotPlaceStatus() {
//    setupGame();
//    Player p1 = ref.getPlayers().get(0);
//    Player p2 = ref.getPlayers().get(1);
//    ref.placePhase(p1);
//    assertEquals(ref.getPlayers().size(), 2);
//    assertEquals(ref.getCurrentStatus(), Status.PLACE);
//    ref.setCurrentStatus(Status.MOVEBUILD); // Move onto move build phase
//    ref.placePhase(p1); // P1 Should be kicked out
//    assertEquals(ref.getPlayers().size(), 1);
//    assertEquals(ref.getCurrentStatus(), Status.GAMEOVER);
//  }

    /**
     * checks a moveBuild and if it properly executes when the player is requested to move
     */
//  @Test
//  public void testTurnPhase() {
//    setupGame();
//    Player p1 = ref.getPlayers().get(0);
//    Player p2 = ref.getPlayers().get(1);
//    ref.placePhase(p1);
//    ref.placePhase(p2);
//    ref.placePhase(p1);
//    ref.placePhase(p2);
//    // Check Before Status of Workers
//    ArrayList<Integer> workerIDs = ref.getOfficialBoard().getWorkerIDs();
//    // w0sq should be worker 0 on square 0,0
//    Square w0sq = ref.getOfficialBoard().getWorkerSquare(workerIDs.get(0));
//    // w1sq should be worker 1 on square 5,5
//    Square w1sq = ref.getOfficialBoard().getWorkerSquare(workerIDs.get(1));
//    // w2sq should be worker 2 on square 1,1
//    Square w2sq = ref.getOfficialBoard().getWorkerSquare(workerIDs.get(2));
//    // w3sq should be worker 3 on square 4,5
//    Square w3sq = ref.getOfficialBoard().getWorkerSquare(workerIDs.get(3));
//
//    assertEquals(w0sq.getWorkerID(), 0);
//    assertEquals(w0sq.getX(), 0);
//    assertEquals(w0sq.getY(), 0);
//    assertEquals(w0sq.getHeight(), 0);
//
//    assertEquals(w1sq.getWorkerID(), 1);
//    assertEquals(w1sq.getX(), 5);
//    assertEquals(w1sq.getY(), 5);
//    assertEquals(w1sq.getHeight(), 0);
//
//    assertEquals(w2sq.getWorkerID(), 2);
//    assertEquals(w2sq.getX(), 1);
//    assertEquals(w2sq.getY(), 1);
//
//    assertEquals(w3sq.getWorkerID(), 3);
//    assertEquals(w3sq.getX(), 4);
//    assertEquals(w3sq.getY(), 5);
//
//    ref.setCurrentStatus(Status.MOVEBUILD);
//    ref.turnPhase(p1);
//
//
//    //Player 1 should move worker 0 and build on 0,0
//    Square newW0square = ref.getOfficialBoard().getWorkerSquare(0);
//    assertEquals(newW0square.getX(), 0);
//    assertEquals(newW0square.getY(), 1);
//
//    assertEquals(ref.getOfficialBoard().isOccupied(0,0), false);
//
//  }

    /**
     * Tests that running a game will have the status GAMEOVER at the end and the proper winner is selected
     */
    @Test
    public void testRunFullGame() {
        setupGame();
        Player p1 = ref.getPlayers().get(0);
        Player p2 = ref.getPlayers().get(1);
        Player winner = this.ref.runGame();
        assertEquals(this.ref.getCurrentStatus(), Status.GAMEOVER);
        assertEquals(winner, p1);
    }

    /**
     * Tests that running a game will have the status GAMEOVER at the end and the proper winner is selected
     */
    @Test
    public void testRunFullGameOfN() {
        setupGame();
        Player p1 = ref.getPlayers().get(0);
        Player p2 = ref.getPlayers().get(1);
        Player winner = this.ref.runGame(3);
        assertEquals(this.ref.getCurrentStatus(), Status.GAMEOVER);
        assertEquals(winner, p1);
        int numWins = ref.getPlayerWins().get(p1.getName());
        assertEquals(numWins, 2);
        int numWins2 = ref.getPlayerWins().get(p2.getName());
        assertEquals(numWins2, 0);
    }

  @Test
  public void testRunFullGameOf1() {
    setupGame();
    Player p1 = ref.getPlayers().get(0);
    Player p2 = ref.getPlayers().get(1);
    Player winner = this.ref.runGame(1);
    assertEquals(winner, p1);
    assertEquals(ref.getPlayers().size(), 2);
    assertEquals(ref.getKickedPlayer(), null);
  }

  @Test
  public void testTournamentBeginGame() {
    setupGame();
    Player p1 = ref.getPlayers().get(0);
    Player p2 = ref.getPlayers().get(1);
    Player player1 = new StayAlivePlayer("kevin", 0);
    Player player2 = new StayAlivePlayer("kevin", 1);
    Referee ref = new Referee(player1, player2);
    ref.addObserver("kevin");
    ref.runGame();

    TournamentManager tournamentManager = new TournamentManager(2, 32, 2000, 1, 3);
    //tournamentManager.beginGame(p1, p2);

  }

  /**
   * Test to check that a valid winner is returned and the expected output is returned.
   */
  @Test
  public void testObserver() {
      setupGame();
      ref.addObserver("kevin");
      Player p1 = ref.getPlayers().get(0);
      Player p2 = ref.getPlayers().get(1);
      Player winner = this.ref.runGame();
      String resultGame = ref.getObservers().get(0).getHistory();
      String lastLine = resultGame.substring(resultGame.indexOf("one has"));
      String expected = "one has made a winning Move!\n";
      assertEquals(expected, lastLine);

    }
    //used private methods to test so are commented out
//
//  /**
//   * Test that observers are notified when a player makes an invalid placement
//   */
//  @Test
//  public void testObserverGameInvalid() {
//    setupGame();
//    ref.addObserver();
//    Player p1 = ref.getPlayers().get(0);
//    Player p2 = ref.getPlayers().get(1);
//
//    IAction place1 = p1.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement1 = (Action)place1;
//    this.ref.placePhase(p1, placement1);
//
//    IAction place2 = p2.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement2 = (Action)place2;
//    this.ref.placePhase(p2, placement2);
//
//    IAction place3 = p1.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement3 = (Action)place3;
//    this.ref.placePhase(p1, placement3);
//
//
//    IAction place4 = p2.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement4 = (Action)place4;
//    this.ref.placePhase(p2, placement4);
//
//
//    Action move = new Action(Status.BUILD, 0, 3, 3, "one1");
//    this.ref.placePhase(p1, move);
//
//
//    String resultGame = this.ref.getObservers().get(0).getHistory();
//    String lastLine = resultGame.substring(resultGame.indexOf("one has"));
//    String expected = "one has made an illegal place action.\ntwo has won.\n";
//    assertEquals(expected, lastLine);
//  }
//
//  /**
//   * Test that observers are notified when a player makes an invalid move
//   */
//  @Test
//  public void testObserverGameInvalid1() {
//    setupGame();
//    ref.addObserver();
//    Player p1 = ref.getPlayers().get(0);
//    Player p2 = ref.getPlayers().get(1);
//
//    IAction place1 = p1.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement1 = (Action)place1;
//    this.ref.placePhase(p1, placement1);
//
//    IAction place2 = p2.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement2 = (Action)place2;
//    this.ref.placePhase(p2, placement2);
//
//    IAction place3 = p1.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement3 = (Action)place3;
//    this.ref.placePhase(p1, placement3);
//
//
//    IAction place4 = p2.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement4 = (Action)place4;
//    this.ref.placePhase(p2, placement4);
//
//
//    Action move = new Action(Status.MOVE, 0, 1, 1, "one1");
//    Action build = new Action(Status.BUILD, 0, 3, 2, "one1");
//    MoveBuild movebuild = new MoveBuild(move, build);
//
//    this.ref.turnPhase(p1, movebuild);
//
//    String resultGame = this.ref.getObservers().get(0).getHistory();
//    //System.out.println(resultGame);
//    //String lastLine = resultGame.substring(resultGame.indexOf("one has"));
//    //String expected = "one has made an illegal place action.\ntwo has won.\n";
//    //assertEquals(expected, lastLine);
//  }
//
//  /**
//   * Test that observers are notified when a player makes an invalid build
//   */
//  @Test
//  public void testObserverGameInvalid2() {
//    setupGame();
//    ref.addObserver();
//    Player p1 = ref.getPlayers().get(0);
//    Player p2 = ref.getPlayers().get(1);
//
//    IAction place1 = p1.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement1 = (Action)place1;
//    this.ref.placePhase(p1, placement1);
//
//    IAction place2 = p2.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement2 = (Action)place2;
//    this.ref.placePhase(p2, placement2);
//
//    IAction place3 = p1.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement3 = (Action)place3;
//    this.ref.placePhase(p1, placement3);
//
//
//    IAction place4 = p2.getNextAction(this.ref.getOfficialBoard(), Status.PLACE);
//    Action placement4 = (Action)place4;
//    this.ref.placePhase(p2, placement4);
//
//
//    Action move = new Action(Status.MOVE, 0, 0, 1, "one1");
//    Action build = new Action(Status.BUILD, 0, 1, 1, "one1");
//    MoveBuild movebuild = new MoveBuild(move, build);
//
//    this.ref.turnPhase(p1, movebuild);
//    String resultGame = this.ref.getObservers().get(0).getHistory();
//
//    String lastLine = resultGame.substring(resultGame.indexOf("one has"));
//    String expected = "one has made an illegal move action.\ntwo has won.\n";
//    assertEquals(expected, lastLine);
//  }
//
//    @Test
//    Tests that a game with a bad player will have a kicked player in the end
//    public void testGameOver() {
//      Player good = new Player("good", 0, this.diag);
//      Player bad = new BreakerPlayer("bad", 1);
//      this.ref = new Referee(good, bad);
//      Player winner = this.ref.runGame(3);
//      assertEquals(this.ref.getCurrentStatus(), Status.GAMEOVER);
//      assertEquals(winner, good);
//      assertEquals(this.ref.getKickedPlayer(), bad);
//    }

}
