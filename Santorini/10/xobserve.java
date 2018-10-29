/**
 * Test Harness for Observer testing.
 * Runs a game with a Referee between two players and an Observer.
 * Returns information sent to the Observer into STDOUT
 */
public class xobserve {

  public static void main(String args[]) {
    //Setup
    Referee ref = new Referee();
    Strategy diag = new StayAliveStrategy(true, 1);
    Strategy furthest = new StayAliveStrategy(false, 1);
    ref.addPlayer("one", diag);
    ref.addPlayer("two", furthest);
    ref.addObserver();

    //Run a game
    Player winner = ref.runGame();

    // Get Information sent to Observer
    System.out.println(ref.getObservers().get(0).getHistory());
  }

}