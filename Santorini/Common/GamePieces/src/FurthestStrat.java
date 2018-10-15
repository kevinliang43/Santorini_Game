import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * IStrategy Implementation
 * FurthestStrat determines the placement of new Workers by placing new Workers at the point furthest
 * away from the opponent's Workers using the notion of geometric distance on a Cartesian plane.
 */
public class FurthestStrat implements IStrategy {

  private String playerId;
  private int numTurns;

  FurthestStrat(String playerId, int numTurns) {
    this.playerId = playerId;
    this.numTurns = 4;
  }

  @Override
  public String nextMove(Board board, HashMap<String, List<String>> playerWorkerMap, RuleChecker rc) {

    // Get the current Player's Workers
    List<String> currentPlayerWorkers = playerWorkerMap.get(this.playerId);

    // Get Opposing Player's Workers
    List<String> opposingPlayerWorkers = new ArrayList<>();

    for (String key : playerWorkerMap.keySet()) {
      if (!key.equals(this.playerId)) {
        opposingPlayerWorkers.addAll(playerWorkerMap.get(key));
      }
    }

    // Get All possible turns for this player and opposing player
    LinkedList<String> currentPlayerTurns = generatePossibleTurns(currentPlayerWorkers);
    LinkedList<String> opposingPlayerTurns = generatePossibleTurns(opposingPlayerWorkers);

    // Set Up Array of LinkedList of Possible Turns
    LinkedList<String>[] nTurns = new LinkedList[this.numTurns];
    for (int i = 0; i < this.numTurns; i++) {
      if (i % 2 == 0) {
        nTurns[i] = (LinkedList) currentPlayerTurns.clone();
      }
      else {
        nTurns[i] = (LinkedList) opposingPlayerTurns.clone();
      }
    }

    // Determine list of turns of size numTurns that is valid/will not cause opposing player to lose.
    int currentDepth = 0;
    Appendable log = new StringBuilder();
    while(currentDepth < this.numTurns && currentDepth >= 0) {
      //initializes a new copy of a board
      Board b = new Board(board);

      if(nTurns[currentDepth].isEmpty()) {
        nTurns[currentDepth] = assignWorkers(currentDepth, playerWorkerMap);
        currentDepth--;
        nTurns[currentDepth].remove();
        b = new Board(board); // Creates new Board and executes all requests currently leading to the step
        for (int i = 0; i < currentDepth - 1; i++) {
          ArrayList<ArrayNode> requests = null;
          try {
            requests = JSONParse.parse(nTurns[i].peek().toString());
          }
          catch (IOException e){
            e.printStackTrace();
          }
          Interpreter.executeRequests(b, requests, log);
        }
        continue;

      }

      else {
        ArrayList<ArrayNode> requests = null;
        try {
          requests = JSONParse.parse(nTurns[currentDepth].peek().toString());
        }
        catch (IOException e){
          e.printStackTrace();
        }
        if (rc.checkRequests(b, "player", "player", requests, log )) { //If this is a valid move
          Board copy = new Board(b);
          Interpreter.executeRequests(copy, requests, log);
          if (currentDepth % 2 == 0) {
            rc.checkWin(copy, opposingPlayerWorkers)
            nTurns[currentDepth].remove();
          }
          else {
            b = new Board(copy);
            currentDepth++;
          }
        }
        else {
          nTurns[currentDepth].remove();
        }
      }

    }
    if (currentDepth < 0) {
      return "";
    }
    else {
      return nTurns[0].peek().toString();
    }

  }

  /**
   * assigns a linked list to the player's workers represented by this int, player % 2 = 0 is p1 and player % 2 = 1 is p2
   * @param player integer representing what level of the tree we are at to know which player is needed
   * @return creates a list of workers for this player and returns the LinkedList created in possibleTurns
   */
  LinkedList<String> assignWorkers(int player, HashMap<String,List<String>> playerWorkers) {
    List<String> thisPlayerWorkers = playerWorkers.get(this.playerId);
    List<String> opposingPlayerWorkers = new ArrayList<>();
    for (String key : playerWorkers.keySet()) {
      opposingPlayerWorkers.addAll(playerWorkers.get(key));
    }

    List<String> workers;
    if (player % 2 == 0) {
      workers = thisPlayerWorkers;
    }
    else {
      workers = opposingPlayerWorkers;
    }

    return possibleTurns(workers);
  }

  /**
   * Given the list of worker IDs, returns a list of possible requests made on these workers
   * @param workers List of String worker Ids that represents the current player whose turn it is' workers.
   * @return Linked List<String> representing the requests that can be made on these workers, does not check
   *         if these requests are valid or not.
   */
  LinkedList<String> possibleTurns(List<String> workers) {
    LinkedList<String> requests = new LinkedList<>();
    for (String worker: workers) {
      for (Direction moveDirections : Direction.values()) {
        String move = "[\"move\",\"" + worker + "\"," + moveDirections.toString() + "] ";
        for (Direction buildDirections : Direction.values()) {
          String moveBuild = move + "[\"+build\"," + buildDirections.toString() + "]";
          requests.add(moveBuild);
        }
      }
    }
    return requests;
  }

  @Override
  public void sendInformation(String message) {

  }

  /**
   * Sends a JSON String for the placement of a new Worker.
   * The placement is determined by:
   * Place new Workers at the point furthest away from the opponent's Workers
   * using the notion of geometric distance on a Cartesian plane.
   * @param board Copy of the Current State of the Board
   * @param playerWorkerMap View Copy of the Player Worker Mapping
   * @return JSON String of the placement of a new Worker
   */
  @Override
  public String placeWorker(Board board, HashMap<String, List<String>> playerWorkerMap) {

    List<String> allOppWorkers = new ArrayList<>();

    for (String key : playerWorkerMap.keySet()) {
      if (!key.equals(this.playerId)) {
        allOppWorkers.addAll(playerWorkerMap.get(key));
      }
    }

    // Find the first empty cell
    Cell furthestCell = board.getCell(0, 0);
    Random randGen = new Random();
    while (furthestCell.containsWorker()) {
      // Randomly choose Cell until it does not contain a worker.
      furthestCell = board.getCell(randGen.nextInt(board.getNumRows()),
              randGen.nextInt(board.getNumColumns()));
    }
    double furthestCellDist = -1; //Set Maximum of Mins to arbitrarily small number

    // Maximum of minimum distances to currentCell and all opposing workers
    for (int col = 0; col < board.getNumColumns(); col++) {
      for (int row = 0; row < board.getNumRows(); row++) {
        Cell currentCell = board.getCell(row, col); // get the current cell to be analyzed for distance
        double currentMin = board.getNumColumns() * board.getNumColumns(); // set Minimum to initial arbritrarily large distance

        // Calculate the Minimum distance from the current cell and each worker
        for (String workerID : allOppWorkers) {
          Worker currentWorker = board.getWorker(workerID);
          int workerRow = currentWorker.getRow();
          int workerCol = currentWorker.getColumn();
          currentMin = Math.min(Math.sqrt(Math.pow(workerRow - row, 2) + Math.pow(workerCol - col, 2)), currentMin);
        }

        // If the minimum distance of all workers and this cell is greater than the Maximum distance so far,
        // Update the new Cell and the distance.
        if (currentMin > furthestCellDist) {
          furthestCell = currentCell;
          furthestCellDist = currentMin;
        }
      }
    }

    // Cell has been determined to be unoccupied.
    // Construct JSON String
    String returnString = String.format("[\"add\", %d, %d]", furthestCell.getRow(), furthestCell.getColumn());
    return returnString;
  }

  @Override
  public void setNumTurns(int numTurns) {
    this.numTurns = numTurns;
  }

  public LinkedList<String> generatePossibleTurns(List<String> workers) {
    LinkedList<String> allPossibleTurns = new LinkedList<>();

    for (String worker : workers) {
      for(Direction moveDirection : Direction.values()) {
        for (Direction buildDirection : Direction.values()) {
          StringBuilder turnBuilder = new StringBuilder();
          turnBuilder.append(String.format(
                  "[\"move\",\"%s\",%s] [\"+build\", %s]", worker, moveDirection.toString(), buildDirection.toString()));
          allPossibleTurns.add(turnBuilder.toString());
        }

      }
    }
    return allPossibleTurns;
  }
}