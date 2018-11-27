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
        this.o = new Observer("kevin");
        this.p1 = new BreakerPlayer("marina", 0);
        this.p2 = new StayAlivePlayer("bob", 1);
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
        this.ref.addObserver("kevin");
        this.ref.runGame();
        String expected = "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bob1\"]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,\"0marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bob1\"]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,\"0marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bob2\"],[0,0,0,0,0,\"0bob1\"]]\n" +
                "\n" +
                "marina has made an illegal move action.\n" +
                "bob has won.\n";
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
                "[[\"0marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bob1\"]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,\"0marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bob1\"]]\n" +
                "\n" +
                "[[\"0marina1\",0,0,0,0,0],[0,\"0marina2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bob2\"],[0,0,0,0,0,\"0bob1\"]]\n" +
                "\n" +
                "marina has made an illegal move action.\n" +
                "mob has won.\n";
        assertEquals(this.ref.getObservers().get(0).getHistory(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), "");

    }

    //tests that running a game will report the entire history in a best of 3 with both good players
    @Test
    public void runNGameTest2() {
        init();
        StayAlivePlayer p3 = new StayAlivePlayer("bobbo", 2);
        this.ref = new Referee(p2, p3);
        this.ref.addObserver("kevin");
        this.ref.runGame(3);
        String expected = "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bobbo1\"]]\n" +
                "\n" +
                "[[\"0bob1\",\"0bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bobbo1\"]]\n" +
                "\n" +
                "[[\"0bob1\",\"0bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",\"0bobbo1\"]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[1,\"0bob2\",0,0,0,0],[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",\"0bobbo1\"]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[1,\"0bob2\",0,0,0,0],[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0bobbo1\",0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"1bob1\",\"0bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0bobbo1\",0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[\"1bob1\",\"0bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[2,\"0bob2\",0,0,0,0],[\"1bob1\",0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[2,\"0bob2\",0,0,0,0],[\"1bob1\",1,0,0,0,0],[0,0,\"1bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"2bob1\",\"0bob2\",0,0,0,0],[2,1,0,0,0,0],[0,0,\"1bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"EAST\",\"NORTH\"]\n" +
                "[[\"2bob1\",\"0bob2\",1,0,0,0],[2,\"1bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[3,\"0bob2\",1,0,0,0],[\"2bob1\",\"1bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"EAST\",\"NORTH\",\"EAST\",\"PUT\"]\n" +
                "[[3,\"0bob2\",\"1bobbo1\",1,0,0],[\"2bob1\",1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"NORTH\"]\n" +
                "[[\"3bob1\",\"0bob2\",\"1bobbo1\",1,0,0],[2,1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "bob has made a winning Move!\n" +
                "[[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]]\n" +
                "\n" +
                "[[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bobbo1\"]]\n" +
                "\n" +
                "[[\"0bob1\",\"0bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,\"0bobbo1\"]]\n" +
                "\n" +
                "[[\"0bob1\",\"0bob2\",0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",\"0bobbo1\"]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[1,\"0bob2\",0,0,0,0],[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",\"0bobbo1\"]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[1,\"0bob2\",0,0,0,0],[\"0bob1\",0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0bobbo1\",0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"1bob1\",\"0bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,1,0,0],[0,0,0,0,\"0bobbo1\",0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[\"1bob1\",\"0bob2\",0,0,0,0],[1,0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[2,\"0bob2\",0,0,0,0],[\"1bob1\",0,0,0,0,0],[0,0,1,0,0,0],[0,0,0,\"1bobbo1\",0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"WEST\",\"NORTH\"]\n" +
                "[[2,\"0bob2\",0,0,0,0],[\"1bob1\",1,0,0,0,0],[0,0,\"1bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"NORTH\",\"PUT\",\"SOUTH\"]\n" +
                "[[\"2bob1\",\"0bob2\",0,0,0,0],[2,1,0,0,0,0],[0,0,\"1bobbo1\",0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"WEST\",\"NORTH\",\"EAST\",\"NORTH\"]\n" +
                "[[\"2bob1\",\"0bob2\",1,0,0,0],[2,\"1bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"SOUTH\",\"PUT\",\"NORTH\"]\n" +
                "[[3,\"0bob2\",1,0,0,0],[\"2bob1\",\"1bobbo1\",0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bobbo1\",\"EAST\",\"NORTH\",\"EAST\",\"PUT\"]\n" +
                "[[3,\"0bob2\",\"1bobbo1\",1,0,0],[\"2bob1\",1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "[\"bob1\",\"PUT\",\"NORTH\"]\n" +
                "[[\"3bob1\",\"0bob2\",\"1bobbo1\",1,0,0],[2,1,0,0,0,0],[0,0,1,0,0,0],[0,0,0,1,0,0],[0,0,0,0,0,0],[0,0,0,0,\"0bobbo2\",0]]\n" +
                "\n" +
                "bob has made a winning Move!\n";
        assertEquals(this.ref.getObservers().get(0).getHistory(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), expected);
        assertEquals(this.ref.getObservers().get(0).readNewUpdates(), "");

    }

}
