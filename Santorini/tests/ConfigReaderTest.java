import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
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

    //tests building an arraylist of players without duplicates
    @Test
    public void testBuildPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        ArrayList<String> p1 = new ArrayList<>();
        p1.add("infinite");
        p1.add("ilikefruitloop");
        p1.add("noloopforme");
        p1.add("0");

        ArrayList<String> p2 = new ArrayList<>();
        p2.add("breaker");
        p2.add("snap");
        p2.add("cracklepop");
        p2.add("1");

        requests.add(p1);
        requests.add(p2);

        players = cr.buildPlayers(requests, 0);
        assertEquals(players.get(0).getName(), "ilikefruitloop");
        assertEquals(players.get(1).getName(), "snap");

    }

    //tests building an arraylist of players with duplicates
    @Test
    public void testBuildPlayers2() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        ArrayList<String> p1 = new ArrayList<>();
        p1.add("infinite");
        p1.add("tina");
        p1.add("turner");
        p1.add("0");

        ArrayList<String> p2 = new ArrayList<>();
        p2.add("breaker");
        p2.add("tina");
        p2.add("fey");
        p2.add("1");

        requests.add(p1);
        requests.add(p2);

        players = cr.buildPlayers(requests, 0);
        assertEquals(players.get(0).getName(), "tina");
        assertEquals(players.get(1).getName(), "tina1");

    }

    //tests building an arraylist of observers with no duplicate names
    @Test
    public void testBuildObservers() {
        ArrayList<Observer> obs = new ArrayList<>();
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        ArrayList<String> o1 = new ArrayList<>();
        o1.add("isea");
        o1.add("nopath");

        ArrayList<String> o2 = new ArrayList<>();
        o2.add("hullo");
        o2.add("world");

        requests.add(o1);
        requests.add(o2);

        obs = cr.buildObservers(requests);
        assertEquals(obs.get(0).getName(), "isee");
        assertEquals(obs.get(1).getName(), "hullo");
    }

    //tests building an arraylist of observers with duplicate names
    @Test
    public void testBuildObservers2() {
        ArrayList<Observer> obs = new ArrayList<>();
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        ArrayList<String> o1 = new ArrayList<>();
        o1.add("bob");
        o1.add("thebuilder");

        ArrayList<String> o2 = new ArrayList<>();
        o2.add("bob");
        o2.add("notthebuilder");

        requests.add(o1);
        requests.add(o2);

        obs = cr.buildObservers(requests);
        assertEquals(obs.get(0).getName(), "bob");
        assertEquals(obs.get(1).getName(), "bob1");
    }

    //tests the parse method on json strings
    @Test
    public void testParse() {
        try {
            ArrayList<JsonNode> place = ConfigReader.parse("[1,2]");
            int x = place.get(0).get(0).asInt();
            int y = place.get(0).get(1).asInt();
            assertEquals(x, 1);
            assertEquals(y, 2);

            ArrayList<JsonNode> giveUp = ConfigReader.parse("\"kevin\"");
            assertEquals(giveUp.get(0).isTextual(), true);
            assertEquals(giveUp.get(0).isInt(), false);

            ArrayList<JsonNode> move = ConfigReader.parse("[\"kevin1\", 1, 2]");
            assertEquals(move.get(0).size(), 3);
            assertEquals(move.get(0).get(0).asText(), "kevin1");
            assertEquals(move.get(0).get(1).asInt(), 1);
            assertEquals(move.get(0).get(2).asInt(), 2);

            ArrayList<JsonNode> movebuild = ConfigReader.parse("[\"kevin1\", 1, 2, 1, 3]");
            assertEquals(movebuild.get(0).size(), 5);
            assertEquals(movebuild.get(0).get(0).asText(), "kevin1");
            assertEquals(movebuild.get(0).get(1).asInt(), 1);
            assertEquals(movebuild.get(0).get(2).asInt(), 2);
            assertEquals(movebuild.get(0).get(3).asInt(), 1);
            assertEquals(movebuild.get(0).get(4).asInt(), 3);
        }
        catch (IOException e) {

        }


    }

    //test for readAndParse single-line input
    @Test
    public void testReadAndParse() {
        String data = "\"hello\"";
        ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        ArrayList<JsonNode> jsons = ConfigReader.readAndParse();
        // assertEquals(jsons.size(), 3);
        assertEquals(jsons.get(0).toString(), "\"hello\"");
        System.setIn(System.in);
    }

    //test for readAndParse multi-line input
    @Test
    public void testReadAndParseM() {
        String data = "\"hello\n\"";
        ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        ArrayList<JsonNode> jsons = ConfigReader.readAndParse();
       // assertEquals(jsons.size(), 3);
        assertEquals(jsons.get(0).toString(), "\"hello\"");
        System.setIn(System.in);
    }

    //tests getFields
    @Test
    public void testGetFields() {
        String node = "{\"name\" : [[\"miso\"]]}";
        ArrayList<JsonNode> nodes = null;
        try{
            nodes = ConfigReader.parse(node);
        }
        catch(IOException e) {

        }
        assertEquals(ConfigReader.getFields("name", nodes.get(0)).get(0).get(0), "miso");

    }

}
