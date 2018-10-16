import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


public class WorkerTests {

  Board board;
  Worker m1;
  Worker k1;
  Worker m2;
  Worker k2;

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("WorkerTests");

  }

  public void init() {
    //small board for testing purposes
    this.board = new Board(6, 6);
    this.board.addWorker(0,0, "m1");
    this.board.addWorker(1,1, "k1");
    this.board.addWorker(5,5, "m2");
    this.board.addWorker(4,5, "k2");

    this.m1 = board.getWorker("m1");
    this.k1 = board.getWorker("k1");
    this.m2 = board.getWorker("m2");
    this.k2 = board.getWorker("k2");


  }

  //testing that the initilization of the board and the workers are as expected
  @Test
  public void testInitialization() {
    init();
    Assert.assertEquals(this.m1.getRow(), 0);
    Assert.assertEquals(this.m1.getColumn(), 0);
    Assert.assertEquals(this.m1.getCell(), this.board.getCell(0,0));
    Assert.assertEquals(this.m1.hasMoved(), false);

    Assert.assertEquals(this.k1.getRow(), 1);
    Assert.assertEquals(this.k1.getColumn(), 1);
    Assert.assertEquals(this.k1.getCell(), this.board.getCell(1,1));
    Assert.assertEquals(this.k1.hasMoved(), false);

    Assert.assertEquals(this.m2.getRow(), 5);
    Assert.assertEquals(this.m2.getColumn(), 5);
    Assert.assertEquals(this.m2.getCell(), this.board.getCell(5,5));
    Assert.assertEquals(this.m2.hasMoved(), false);

    Assert.assertEquals(this.k2.getRow(), 4);
    Assert.assertEquals(this.k2.getColumn(), 5);
    Assert.assertEquals(this.k2.getCell(), this.board.getCell(4,5));
    Assert.assertEquals(this.k2.hasMoved(), false);

  }


  //tests that valid moves are done
  @Test
  public void testMove1() {
    init();
    Cell c10 = this.board.getCell(1,0);
    Cell c00 = this.board.getCell(0,0);
    Assert.assertEquals(this.m1.getCell(), c00);
    this.m1.move(c10);
    Assert.assertEquals(this.m1.getCell(), c10);

  }
}