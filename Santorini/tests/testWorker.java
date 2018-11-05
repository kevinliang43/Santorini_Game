import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNull;

public class testWorker {

  ArrayList<Integer> workerIDs = new ArrayList<>();
  Random r = new Random();

  public testWorker() {

  }

  @Test   // Test the creation of a Worker
  public void testWorkerConstructor() {

    Worker w1 = new Worker(1,2, "one", 0);
    assertEquals(w1.getX(), 1);
    assertEquals(w1.getY(), 2);
    assertEquals(w1.getName(), "one");
    workerIDs.add(w1.getID());

    Worker w2 = new Worker(3,4, "two", 1);
    assertEquals(w2.getX(), 3);
    assertEquals(w2.getY(), 4);

    workerIDs.add(w2.getID());

  }

  @Test   // Test the getters for the x and y position of the worker
  public void testGetXandY() {
    int x = r.nextInt(6);
    int y = r.nextInt(6);
    Worker wxy = new Worker(x, y, "one", 0);
    workerIDs.add(wxy.getID());
    assertEquals(x, wxy.getX());
    assertEquals(y, wxy.getY());

  }

  @Test   // Test the setter for the overall position of the worker
  public void testSetPosition() {

    int x = 3;
    int y = 4;
    Worker wxy = new Worker(x, y, "one", 0);
    workerIDs.add(wxy.getID());
    assertEquals(wxy.getX(), 3);
    assertEquals(wxy.getY(), 4);
    wxy.setPosition(5, 6);
    workerIDs.add(wxy.getID());
    assertEquals(wxy.getX(), 5);
    assertEquals(wxy.getY(), 6);


  }

  @Test   // Test the getter for workerID and ensure they are unique
  public void testGetID() {
    Worker wid;

    for(int i = 0; i < 1000; i++) {
      wid = new Worker(r.nextInt(6), r.nextInt(6), "one", i);
      assertFalse(workerIDs.contains(wid.getID()));
      workerIDs.add(wid.getID());
    }

  }

}
