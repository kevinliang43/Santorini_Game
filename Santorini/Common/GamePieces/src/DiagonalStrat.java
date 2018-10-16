import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * IStrategy Implementation
 * DiagonalStrat determines the placement of new Workers by placing the Worker on the first
 * point of the diagonal of the board that is still free, starting from (0,0).
 */
public class DiagonalStrat implements IStrategy {

  private int numTurns;
  private String playerId;

  DiagonalStrat(String playerId, int numTurns) {
    this.playerId = playerId;
    this.numTurns = numTurns;

  }

  @Override
  public String nextMove(Board board, HashMap<String, List<String>> playerWorkerMap, RuleChecker rc) {

    // Get the current Player's Workers
    List<String> currentPlayerWorkers = playerWorkerMap.get(this.playerId);

    // Get Opposing Player's Workers
    List<String> opposingPlayers = new ArrayList<>();

    for (String key : playerWorkerMap.keySet()) {
      if (!key.equals(this.playerId)) {
        opposingPlayers.add(key);
      }
    }

    // Get All possible turns for this player and opposing player
    LinkedList<String> currentPlayerTurns = generatePossibleTurns(currentPlayerWorkers);


    Appendable log = new StringBuilder();

    while (!currentPlayerTurns.isEmpty()) {
      Board copy = new Board(board);

      ArrayList<ArrayNode> requests = null;
      try {
        requests = JSONParse.parse(currentPlayerTurns.peek().toString());
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (rc.checkRequests(copy, "player", "player",  requests, log)) {
        Interpreter.executeRequests(copy, requests, log);
        if (!canWinInNMoves(copy, playerWorkerMap, rc, this.numTurns/2, opposingPlayers.get(0))) {
          return currentPlayerTurns.pop();
        }
        else {
          currentPlayerTurns.remove();
        }
      }

      else {
        currentPlayerTurns.remove();
      }

    }

    return "";


  }

  /**
   * Determines if there exists a move that wins in n moves
   * @param board
   * @param playerWorkerMap
   * @param rc
   * @return
   */
  boolean canWinInNMoves(Board board, HashMap<String, List<String>> playerWorkerMap, RuleChecker rc, int n, String player) {

    // Get the current Player's Workers
    List<String> currentPlayerWorkers = playerWorkerMap.get(player);

    // Get All possible turns for this player and opposing player
    LinkedList<String> currentPlayerTurns = generatePossibleTurns(currentPlayerWorkers);

    // Set Up Array of LinkedList of Possible Turns
    LinkedList<String>[] nTurns = new LinkedList[n];
    for (int i = 0; i < n; i++) {
      nTurns[i] = (LinkedList) currentPlayerTurns.clone();
    }

    // Determine list of turns of size numTurns that is valid/will not cause opposing player to lose.
    int currentDepth = 0;
    Appendable log = new StringBuilder();
    boolean moveFound = false;

    while(!nTurns[0].isEmpty() && !moveFound) {
      //initializes a new copy of a board
      Board b = new Board(board);

      if (nTurns[currentDepth].isEmpty()) {
        nTurns[currentDepth] = possibleTurns(currentPlayerWorkers);
        currentDepth--;
        nTurns[currentDepth].remove();
        b = new Board(board); // Creates new Board and executes all requests currently leading to the step
        for (int i = 0; i < currentDepth - 1; i++) {
          ArrayList<ArrayNode> requests = null;
          try {
            requests = JSONParse.parse(nTurns[i].peek().toString());
          } catch (IOException e) {
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
        } catch (IOException e) {
          e.printStackTrace();
        }

        if (rc.checkRequests(b, "player", "player", requests, log)) { //If this is a valid move
          Board copy = new Board(b);
          Interpreter.executeRequests(copy, requests, log);
          if (rc.checkWin(copy, currentPlayerWorkers)) {
            moveFound = true;
          }
          else if (currentDepth != n - 1) {

            b = new Board(copy);
            currentDepth++;
          }
          else {
            nTurns[currentDepth].remove();
          }
        }

        else {
          nTurns[currentDepth].remove();
        }


      }
    }

    return moveFound;

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
    //Function not used for 'AI" implementation

  }


  /**
   * Sends a JSON String for the placement of a new Worker.
   * The placement is determined by:
   * Place the worker on the first point of the diagonal that is still free, starting from (0,0).
   * @param board Copy of the Current State of the Board
   * @param playerWorkerMap View Copy of the Player Worker Mapping
   * @return JSON String of the placement of a new Worker
   */
  @Override
  public String placeWorker(Board board, HashMap<String, List<String>> playerWorkerMap) {
    int idx = 0;
    Cell currentCell = board.getCell(idx,idx); //Initial Cell

    while (currentCell.containsWorker()) {
      idx++;
      currentCell = board.getCell(idx,idx); // Get next Diagonal Cell if this current is occupied
    }

    // Cell has been determined to be unoccupied.
    // Construct JSON String
    String returnString = String.format("[\"add\", %d, %d]", idx, idx);
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
