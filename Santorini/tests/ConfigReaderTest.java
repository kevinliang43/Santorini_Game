import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//tests for the configuration reader class
public class ConfigReaderTest {

    ConfigReader cr;

    //initializes the configuration reader
    public void init() {
        this.cr = new ConfigReader();
    }
    //test that building a good player creates a stay alive player
    @Test
    public void testBuildPlayer() {
        Player p = cr.buildPlayer("good", "miso", "fakePath", 0);
        assertEquals(p.getName(), "miso");
        assertEquals(p instanceof StayAlivePlayer, true);
    }
    //test that building a looping player creates an infinite player
    @Test
    public void testLoopPlayer() {
        Player p = cr.buildPlayer("infinite", "fruitloop", "itFitManyLoop", 0);
        assertEquals(p.getName(), "fruitloop");
        assertEquals(p instanceof InfinitePlayer, true);
    }
    //test that building a breaking player creates a breaking
    @Test
    public void testBreakerPlayer() {
        Player p = cr.buildPlayer("breaker", "ralph", "wreckitralph", 0);
        assertEquals(p.getName(), "ralph");
        assertEquals(p instanceof BreakerPlayer, true);
    }

    //test building an observer
    @Test
    public void testBuildObserver() {
        Observer o = cr.buildObserver("for", "loop");
        assertEquals(o.getName(), "for");
        assertEquals(o instanceof Observer, true);
    }

    //tests building an arraylist of players
    @Test
    public void testBuildPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        ArrayList<String> p1 = new ArrayList<>();
        p1.add("infinite");
        p1.add("ILikeFruitLoop");
        p1.add("noLoopForMe");
        p1.add("0");

        ArrayList<String> p2 = new ArrayList<>();
        p2.add("breaker");
        p2.add("snap");
        p2.add("cracklepop");
        p2.add("1");

        requests.add(p1);
        requests.add(p2);

        players = cr.buildPlayers(requests);
        assertEquals(players.get(0).getName(), "ILikeFruitLoop");
        assertEquals(players.get(1).getName(), "snap");

    }

    //tests building an arraylist of observers
    @Test
    public void testBuildObservers() {
        ArrayList<Observer> obs = new ArrayList<>();
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        ArrayList<String> o1 = new ArrayList<>();
        o1.add("iSee");
        o1.add("noPath");

        ArrayList<String> o2 = new ArrayList<>();
        o2.add("hullo");
        o2.add("world");

        requests.add(o1);
        requests.add(o2);

        obs = cr.buildObservers(requests);
        assertEquals(obs.get(0).getName(), "iSee");
        assertEquals(obs.get(1).getName(), "hullo");

    }

}