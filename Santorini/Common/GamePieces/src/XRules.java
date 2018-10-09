import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by KevinLiang on 10/6/18.
 */
public class XRules {

  public static void main(String[] args) {

    // Get the input
    Scanner scanner = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (scanner.hasNextLine()) {
      builder.append(scanner.nextLine());
    }
    String requestString = builder.toString();

    // Parse input to ArrayNodes
    ArrayList<ArrayNode> requests = new ArrayList<>();
    try {
      requests = JSONParse.parse(requestString);
    } catch (IOException e) {
      e.printStackTrace();
    }

    StringBuilder log = new StringBuilder();
    ArrayList<ArrayNode> sequence = new ArrayList<>();
    Board board = new Board(6, 6);

    // Build Sequence and Run rule checker once it hits a new sequence
    for (ArrayNode request : requests) {
      if (Interpreter.isBoardRequest(request)) {
        checkTurn(sequence, board, log);
        board = new Board(6, 6);
        sequence = new ArrayList<>();
      }
      sequence.add(request);
    }

    // Run the last sequence
    checkTurn(sequence, board, log);

    System.out.print(log.toString());
  }

  /**
   * Uses a rule checker to check this sequence of requests on the given board
   * @param requests ArrayList of ArrayNodes representing requests to be checked
   * @param board Board of the game to check requests on
   * @param log Appendable to log responses from checking the requests
   */
  public static void checkTurn(ArrayList<ArrayNode> requests, Board board, Appendable log) {

    // Check to see if first is BoardRequest. If so, execute.
    // If not, return "no"

    if (requests.size() > 0) {

      // Build Board
      Interpreter.execute(board, requests.get(0), log, "");
      requests.remove(0); // remove the board requests

      Player currentPlayer = new Player("m");

      // Check All other requests.
      RuleChecker rc = new RuleChecker();
      rc.checkRequests(board, currentPlayer, currentPlayer, requests, log);

    }


  }


}
