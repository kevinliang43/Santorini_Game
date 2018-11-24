import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class TournamentManagerTests {

    TournamentManager tm;

    //initializes the tournament manager
    public void init() {
        this.tm = new TournamentManager(2, 10, 2000, 1, 3);
    }
    //commented out tests for private functions
//    //tests that begin game successfully runs a game between two players
//    @Test
//    public void testBeginGame() {
//        init();
//        Player p1 = new StayAlivePlayer("marina",0);
//        Player p2 = new BreakerPlayer("kevin", 1);
//        Referee ref = tm.beginGame(p1,p2);
//        assertEquals(ref.getKickedPlayer(), p2);
//        assertEquals(ref.getWinner(), p1);
//    }
//
//    //tests that begin game returns null when one player is null
//    @Test
//    public void testBeginGameNull() {
//        init();
//        Player p1 = new StayAlivePlayer("marina",0);
//        Player p2 = null;
//        Referee ref = tm.beginGame(p1,p2);
//        assertEquals(ref, null);
//    }
//
    //tests if run tournament correctly removes the players that make bad moves
//    @Test
//    public void testRunTournament() {
//        init();
//        Player p1 = new StayAlivePlayer("marina",0);
//        Player p2 = new BreakerPlayer("kevin", 1);
//        Player p3 = new InfinitePlayer("miso", 2);
//        tm.players.add(p1);
//        tm.players.add(p2);
//        tm.players.add(p3);
//
//        tm.runTournament();
//        assertEquals(tm.removedPlayers.get(0), p3);
//        assertEquals(tm.removedPlayers.get(1), p2);
//    }
//
//    //tests if run tournament keeps all players that play well
//    @Test
//    public void testRunTournament() {
//        init();
//        Player p1 = new StayAlivePlayer("marina",0);
//        Player p2 = new StayAlivePlayer("kevin", 1);
//        Player p3 = new StayAlivePlayer("miso", 2);
//        tm.players.add(p1);
//        tm.players.add(p2);
//        tm.players.add(p3);
//
//        tm.runTournament();
//        assertEquals(tm.removedPlayers.size(), 0);
//    }

//    //testing if the tournament runs correctly by using removedPlayersAsJSON
//    @Test
//    public void testTournamentMain() {
//        init();
//        Player p1 = new StayAlivePlayer("marina",0);
//        Player p2 = new BreakerPlayer("kevin", 1);
//        Player p3 = new InfinitePlayer("miso", 2);
//
//        ArrayList<Player> players = new ArrayList<>();
//        players.add(p1);
//        players.add(p2);
//        players.add(p3);
//
//        assertEquals(tm.main(players, new ArrayList<Observer>()), "[\"miso\", \"kevin\"]");
//    }
    //tests the creation of game result objects
//    @Test
//    public void testGetGameResults() {
//        init();
//        Player p1 = new StayAlivePlayer("marina",0);
//        Player p2 = new BreakerPlayer("kevin", 1);
//        Player p3 = new InfinitePlayer("miso", 2);
//
//        Referee r1 = tm.beginGame(p1, p2);
//        Referee r2 = tm.beginGame(p2, p3);
//        tm.allGames.add(r1);
//        tm.allGames.add(r2);
//        ArrayList<GameResult> gameResults = tm.getGameResults();
//        assertEquals(gameResults.get(0).loser.getName(), "kevin");
//        assertEquals(gameResults.get(1).winner.getName(), "miso");
//
//
//    }

}

