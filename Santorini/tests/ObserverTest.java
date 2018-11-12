import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObserverTest {

    Observer o;
    Referee ref;
    BreakerPlayer p1;
    StayAlivePlayer p2;

    public ObserverTest() {

    }

    //initializes the observer to test
    public void init() {
        this.o = new Observer("Kevin");
        this.p1 = new BreakerPlayer("Marina", 0);
        this.p2 = new StayAlivePlayer("Bob", 1);
        this.ref = new Referee(p1, p2);

    }

    //tests that appending a string adds to the newUpdates appendable
    //and that it gets cleared with readNewUpdates
    @Test
    public void appendTest() {
        init();
        o.append("test string");
        assertEquals(o.readNewUpdates(), "test string\n");
        assertEquals(o.readNewUpdates(), "");
        o.append("testing again");
        assertEquals(o.readNewUpdates(), "testing again\n");
    }

    //tests that the history keeps log of strings even when newUpdates is cleared
    @Test
    public void historyTest() {
        init();
        o.append("test string");
        assertEquals(o.getHistory(), "test string\n");
        assertEquals(o.readNewUpdates(), "test string\n");
        assertEquals(o.readNewUpdates(), "");
        assertEquals(o.getHistory(), "test string\n");
    }

    //tests that the history keeps log of strings even when newUpdates is cleared
    @Test
    public void historyTest1() {
        init();
        o.append("test string");
        assertEquals(o.getHistory(), "test string\n");
        assertEquals(o.readNewUpdates(), "test string\n");
        assertEquals(o.readNewUpdates(), "");
        assertEquals(o.getHistory(), "test string\n");
        o.append("testing again");
        assertEquals(o.readNewUpdates(), "testing again\n");
        assertEquals(o.getHistory(), "test string\ntesting again\n");
    }

    //tests that running a game will report the entire history
    @Test
    public void runGameTest1() {
        init();
        this.ref.addObserver("Kevin");
        this.ref.runGame();
        String expected = "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bob1\"]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,\"0Marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bob1\"]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,\"0Marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bob2\"],[0,0,0,0,0,\"0Bob1\"]]\n" +
                "\n" +
                "Marina has made an illegal move action.\n" +
                "Bob has won.\n";
        assertEquals(this.ref.getObservers().get(0).getHistory(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), "");

    }

    //tests that running a game will report the entire history in a best of 3 with a kicked out player
    @Test
    public void runNGameTest1() {
        init();
        this.ref.addObserver("Kevin");
        this.ref.runGame(3);
        String expected = "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bob1\"]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,\"0Marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bob1\"]]\n" +
                "\n" +
                "[[\"0Marina1\",0,0,0,0,0],[0,\"0Marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bob2\"],[0,0,0,0,0,\"0Bob1\"]]\n" +
                "\n" +
                "Marina has made an illegal move action.\n" +
                "Bob has won.\n";
        assertEquals(this.ref.getObservers().get(0).getHistory(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), "");

    }

    //tests that running a game will report the entire history in a best of 3 with both good players
    @Test
    public void runNGameTest2() {
        init();
        StayAlivePlayer p3 = new StayAlivePlayer("Bobbo", 2);
        this.ref = new Referee(p2, p3);
        this.ref.addObserver("Kevin");
        this.ref.runGame(3);
        String expected = "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bobbo1\"]]\n" +
                "\n" +
                "[[\"0Bob1\",\"0Bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bobbo1\"]]\n" +
                "\n" +
                "[[\"0Bob1\",\"0Bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",\"0Bobbo1\"]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[1,\"0Bob2\",0,0,0,0],[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",\"0Bobbo1\"]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[1,\"0Bob2\",0,0,0,0],[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0Bobbo1\",0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"1Bob1\",\"0Bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0Bobbo1\",0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[\"1Bob1\",\"0Bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1Bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[2,\"0Bob2\",0,0,0,0],[\"1Bob1\",0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1Bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[2,\"0Bob2\",0,0,0,0],[\"1Bob1\",1,0,0,0,0],[0,0,\"1Bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"2Bob1\",\"0Bob2\",0,0,0,0],[2,1,0,0,0,0],[0,0,\"1Bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"EAST\",\"NORTH\"]\n" +
                "[[\"2Bob1\",\"0Bob2\",1,0,0,0],[2,\"1Bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[3,\"0Bob2\",1,0,0,0],[\"2Bob1\",\"1Bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"EAST\",\"NORTH\",\"EAST\",\"PUT\"]\n" +
                "[[3,\"0Bob2\",\"1Bobbo1\",1,0,0],[\"2Bob1\",1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"NORTH\"]\n" +
                "[[\"3Bob1\",\"0Bob2\",\"1Bobbo1\",1,0,0],[2,1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "Bob has made a winning Move!\n" +
                "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bobbo1\"]]\n" +
                "\n" +
                "[[\"0Bob1\",\"0Bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0Bobbo1\"]]\n" +
                "\n" +
                "[[\"0Bob1\",\"0Bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",\"0Bobbo1\"]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[1,\"0Bob2\",0,0,0,0],[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",\"0Bobbo1\"]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[1,\"0Bob2\",0,0,0,0],[\"0Bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0Bobbo1\",0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"1Bob1\",\"0Bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0Bobbo1\",0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[\"1Bob1\",\"0Bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1Bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[2,\"0Bob2\",0,0,0,0],[\"1Bob1\",0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1Bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[2,\"0Bob2\",0,0,0,0],[\"1Bob1\",1,0,0,0,0],[0,0,\"1Bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"2Bob1\",\"0Bob2\",0,0,0,0],[2,1,0,0,0,0],[0,0,\"1Bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"WEST\",\"NORTH\",\"EAST\",\"NORTH\"]\n" +
                "[[\"2Bob1\",\"0Bob2\",1,0,0,0],[2,\"1Bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[3,\"0Bob2\",1,0,0,0],[\"2Bob1\",\"1Bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bobbo1\",\"EAST\",\"NORTH\",\"EAST\",\"PUT\"]\n" +
                "[[3,\"0Bob2\",\"1Bobbo1\",1,0,0],[\"2Bob1\",1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "[\"Bob1\",\"PUT\",\"NORTH\"]\n" +
                "[[\"3Bob1\",\"0Bob2\",\"1Bobbo1\",1,0,0],[2,1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0Bobbo2\",0]]\n" +
                "\n" +
                "Bob has made a winning Move!\n";
        assertEquals(this.ref.getObservers().get(0).getHistory(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), "");

    }

}
