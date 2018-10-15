import com.fasterxml.jackson.databind.node.ArrayNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

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
  public String nextMove(Board board, HashMap<String, List<String>> playerWorkerMap) {

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

    // Set Up Array of







    return "";

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