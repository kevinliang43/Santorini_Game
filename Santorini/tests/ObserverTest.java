import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObserverTest {

    Observer o;
    public ObserverTest() {

    }

    //initializes the observer to test
    public void init() {
        this.o = new Observer();
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
        assertEquals(o.readNewUpdates(), "testing again");
        assertEquals(o.getHistory(), "test string\ntesting again\n");
    }


}
