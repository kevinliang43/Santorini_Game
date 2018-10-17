//import java.util.List;
//
//public class PlayerImpl {
//  public static final int MAX_WORKERS = 2;
//  private int[] workerIDs;
//  private Square[][] board;
//  private Turn myTurnValue;
//  private Turn gameTurn;
//  private Status gameStatus;
//
//  public PlayerImpl(Square[][] board, Turn myTurnValue) {
//    // Ensure that the assigned Turn value is not DRAW
//    if (myTurnValue != Turn.DRAW) {
//      this.myTurnValue = myTurnValue;
//    }
//
//    // Maintain an list of invalid worker IDs until they are placed
//    workerIDs = new int[MAX_WORKERS];
//    for (int i = 0; i < workerIDs.length; i++) {
//      workerIDs[i] = Board.INVALID_WORKER_ID;
//    }
//  }
//
//  public void setTurnAndStatus(Turn newTurn, Status newStatus) {
//    this.setTurn(newTurn);
//    this.setStatus(newStatus);
//  }
//
//
//
//  public void setTurn(Turn newTurn) {
//    this.gameTurn = newTurn;
//  }
//
//  public void setStatus(Status newStatus) {
//    this.gameStatus = newStatus;
//  }
//
//  @Override
//  public Square[][] getBoard() {
//    return this.board;
//  }
//
//  @Override
//  public boolean myTurn() {
//    return this.myTurnValue == this.gameTurn;
//  }
//
//  public void getStatus() {
//    // Write status to OutputStream?
//  }
//
//  @Override
//  public boolean isGameOver() {
//
//  }
//
//  @Override
//  public boolean checkPlace(int x, int y) {
//    return false;
//  }
//
//  @Override
//  public boolean checkMove(int workerID, int x, int y) {
//    return false;
//  }
//
//  @Override
//  public boolean checkBuild(int workerID, int x, int y) {
//    return false;
//  }
//
//  @Override
//  public List<Integer> getWorkers() {
//    return null;
//  }
//
//  @Override
//  public boolean placeWorkerIfLegal(int x, int y) {
//    return false;
//  }
//
//  @Override
//  public boolean moveWorkerIfLegal(int workerID, int x, int y) {
//    return false;
//  }
//
//  @Override
//  public boolean buildFloorIfLegal(int workerID, int x, int y) {
//    return false;
//  }
//
//  @Override
//  public Turn id() {
//    return null;
//  }
//}
