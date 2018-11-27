import jdk.internal.util.xml.impl.Input;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//tests for the remote strategy class
public class RemoteStrategyTest {
  Socket mocket;
  InputStream in;
  OutputStream out;
  BoardStatus boardStat;

  //sets up an empty board for worker placement tests
  void setupForPlace() {
    Board b = new Board();
    this.boardStat = new BoardStatus(b, Status.PLACE);
  }

  //A test for the first worker placement on a newly initialized board reading from
  //a mock socket, tests that this remote client is shown an empty list JSON string of workers
  //that have already been placed as this is the first placement
  @Test
  public void testGetPlaceAction(){
    setupForPlace();
    String data = "[1,1]";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    Action action = (Action)strat.getNextAction(this.boardStat, new ArrayList<>());
    Action expected = new Action(Status.PLACE, Board.INVALID_WORKER_ID, 1, 1, null);
    assertEquals(action.getActionType(), expected.getActionType());
    assertEquals(action.getWorkerID(), expected.getWorkerID());
    assertEquals(action.getX(), expected.getX());
    assertEquals(action.getY(), expected.getY());
    //the outstream should show the client that no one has been placed yet
    assertEquals(this.out.toString(), "[]\n");
  }

  //A test for the third worker placement on a newly initialized board reading from
  //a mock socket, tests that this remote client is shown a list JSON string of
  //where the workers are currently on this board
  @Test
  public void testGetThirdPlaceAction(){
    setupForPlace();
    String data = "[2,2]";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    Board b = new Board();
    b.placeWorker(0, 0, "marina1", 0);
    b.placeWorker(1, 1, "kevin1", 1);
    this.boardStat = new BoardStatus(b, Status.PLACE);
    ArrayList<Integer> workerIDs = new ArrayList<>();
    workerIDs.add(0);
    workerIDs.add(1);
    Action action = (Action)strat.getNextAction(this.boardStat, workerIDs);
    Action expected = new Action(Status.PLACE, Board.INVALID_WORKER_ID, 2, 2, null);
    assertEquals(action.getActionType(), expected.getActionType());
    assertEquals(action.getWorkerID(), expected.getWorkerID());
    assertEquals(action.getX(), expected.getX());
    assertEquals(action.getY(), expected.getY());
    //the outstream should show the client that no one has been placed yet
    assertEquals(this.out.toString(), "[[\"marina1\",0,0],[\"kevin1\",1,1]]\n");
  }

  //A test for the first worker placement on a newly initialized board reading from
  //a mock socket if the placement is invalid, this should not break as the strategy is not
  //responsible for rule checking and should send in whatever action the remote
  //client requests
  @Test
  public void testGetPlaceActionInvalid(){
    setupForPlace();
    String data = "[-1,1]";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    Action action = (Action)strat.getNextAction(this.boardStat, new ArrayList<>());
    Action expected = new Action(Status.PLACE, Board.INVALID_WORKER_ID, -1, 1, null);
    assertEquals(action.getActionType(), expected.getActionType());
    assertEquals(action.getWorkerID(), expected.getWorkerID());
    assertEquals(action.getX(), expected.getX());
    assertEquals(action.getY(), expected.getY());
  }

  //setting up a board with workers along the diagonal to check getting
  //MoveBuild and GiveUpAction IActions
  void setupForMB() {
    Board b = new Board();
    b.placeWorker(0, 0, "marina1", 0);
    b.placeWorker(1, 1, "kevin1", 1);
    b.placeWorker(2, 2, "marina2", 2);
    b.placeWorker(3, 3, "kevin2", 3);
    this.boardStat = new BoardStatus(b, Status.MOVEBUILD);
  }

  //testing getting the next MoveBuild action from a remote player
  @Test
  public void testGetMoveBuildAction(){
    setupForMB();
    String data = "[\"marina1\",\"EAST\",\"SOUTH\",\"WEST\",\"NORTH\"]";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    ArrayList<Integer> workerIDs = new ArrayList<>();
    workerIDs.add(0);
    workerIDs.add(2);
    MoveBuild action = (MoveBuild)strat.getNextAction(this.boardStat, workerIDs);

    //generating the expected moveBuild output
    Action move = new Action(Status.MOVE, 0, 1, 1, "marina1");
    Action build = new Action(Status.BUILD, 0, 0, 0, "marina1");
    MoveBuild expected = new MoveBuild(move, build);


    assertEquals(action.getActionType(), expected.getActionType());
    assertEquals(action.getWorkerID(), expected.getWorkerID());
    assertEquals(action.getxMove(), expected.getxMove());
    assertEquals(action.getyMove(), expected.getyMove());
    assertEquals(action.getxBuild(), expected.getxBuild());
    assertEquals(action.getyBuild(), expected.getyBuild());
  }

  //testing getting the next MoveBuild action from a remote player with an invalid action.
  //the invalid action should be returned as the strategy does not deal with rule
  //checking
  @Test
  public void testGetMoveBuildActionInvalid(){
    setupForMB();
    //this move will try to move "marina1" onto another worker
    String data = "[\"marina1\",\"EAST\",\"NORTH\",\"WEST\",\"NORTH\"]";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    ArrayList<Integer> workerIDs = new ArrayList<>();
    workerIDs.add(0);
    workerIDs.add(2);
    MoveBuild action = (MoveBuild)strat.getNextAction(this.boardStat, workerIDs);

    //generating the expected moveBuild output
    Action move = new Action(Status.MOVE, 0, 1, -1, "marina1");
    Action build = new Action(Status.BUILD, 0, 0, -2, "marina1");
    MoveBuild expected = new MoveBuild(move, build);


    assertEquals(action.getActionType(), expected.getActionType());
    assertEquals(action.getWorkerID(), expected.getWorkerID());
    assertEquals(action.getxMove(), expected.getxMove());
    assertEquals(action.getyMove(), expected.getyMove());
    assertEquals(action.getxBuild(), expected.getxBuild());
    assertEquals(action.getyBuild(), expected.getyBuild());

  }

  //testing if only a single Move is passed in, not a MoveBuild
  @Test
  public void testOnlyMove(){
    setupForMB();
    //this move will try to move "marina1" onto another worker
    String data = "[\"marina1\",\"EAST\",\"SOUTH\"]";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    ArrayList<Integer> workerIDs = new ArrayList<>();
    workerIDs.add(0);
    workerIDs.add(2);
    Action action = (Action)strat.getNextAction(this.boardStat, workerIDs);

    //generating the expected moveBuild output
    Action expected = new Action(Status.MOVE, 0, 1, 1, "marina1");


    assertEquals(action.getActionType(), expected.getActionType());
    assertEquals(action.getWorkerID(), expected.getWorkerID());
    assertEquals(action.getX(), expected.getX());
    assertEquals(action.getY(), expected.getY());
    }

  //tests that when the input sees only a json string that it's interpreted as a
  //GiveUpAction
  @Test
  public void testGivingUpAction(){
    setupForMB();
    //having the player "marina" give up
    String data = "\"marina\"";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    this.boardStat.setStatus(Status.MOVEBUILD);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    ArrayList<Integer> workerIDs = new ArrayList<>();
    workerIDs.add(0);
    workerIDs.add(2);
    IAction action = strat.getNextAction(this.boardStat, workerIDs);

    assertEquals(action instanceof GiveUpAction, true);
  }


  //tests if invalid json is input, null is returned as the action
  //this throws an error, is this what we want? TODO
  @Test (expected = NullPointerException.class)
  public void testInvalidJSONRequest(){
    setupForMB();
    //invalid json text request
    String data = "5";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    this.boardStat.setStatus(Status.MOVEBUILD);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);
    ArrayList<Integer> workerIDs = new ArrayList<>();
    workerIDs.add(0);
    workerIDs.add(2);
    IAction action = strat.getNextAction(this.boardStat, workerIDs);
    assertEquals(action, null);

  }

  //tests that sending messages to the remote strategy correctly writes to the output stream
  @Test
  public void sendMessageTest(){
    String data = "yay input streams";
    this.in = new ByteArrayInputStream(data.getBytes());
    this.out = new ByteArrayOutputStream();
    this.mocket = new Mocket(this.in, this.out);
    RemoteStrategy strat = new RemoteStrategy(this.mocket);

    strat.sendMessage("yay output streams");
    String actual = this.out.toString();
    assertEquals(actual, "yay output streams\n");
  }
}

//Mock Socket class for testing purposes
//Takes in an Input and Output stream to initialize with so our tests
//can mock system.in and system.out
class Mocket extends Socket {
  InputStream in;
  OutputStream out;

  Mocket(InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
  }

  //overriding getOutputStream to return the outputStream passed in in the constructor
  @Override
  public OutputStream getOutputStream() {
    return this.out;
  }

  //overriding getInputStream to return the inputStream passed in in the constructor
  @Override
  public InputStream getInputStream() {
    return this.in;
  }
}
