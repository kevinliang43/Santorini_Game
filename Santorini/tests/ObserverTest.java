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
        System.out.println(this.ref.getObservers().get(0).getHistory());

    }


}
