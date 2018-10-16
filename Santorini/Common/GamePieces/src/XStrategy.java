import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by KevinLiang on 10/14/18.
 */
public class XStrategy {

  public static void main(String[] args) {



    Scanner scanner = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (scanner.hasNextLine()) {
      builder.append(scanner.nextLine());
    }
    String requestString = builder.toString();

    // Parse input to ArrayNodes
    ArrayList<JsonNode> requests = new ArrayList<>();
    try {
      requests = JSONParse.parseNonArrayNode(requestString);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String player = requests.get(0).asText();

    int numTurns = requests.get(2).asInt();

    System.out.println(player);
    System.out.println(numTurns);

    Board board = new Board(6, 6);
    RuleChecker ruleChecker = new RuleChecker();
    StringBuilder log = new StringBuilder();
    Interpreter.execute(board, (ArrayNode)requests.get(1), log, "");
    Interpreter.execute(board, (ArrayNode) requests.get(3), log, "");

    board.printBoard();




    /*

    Board board = new Board(6, 6);
    RuleChecker ruleChecker = new RuleChecker();

    FurthestStrat strat = new FurthestStrat("two", 1);
    FurthestStrat strat2 = new FurthestStrat("two", 2);

    Appendable log = new StringBuilder();

    String request = "[[\"0one1\", \"1one2\", 3, \"2two1\"],\n" +
            "\n" +
            "     [0,       \"2two2\", 3]] [\"move\",\"one2\",[\"WEST\",\"SOUTH\"]] [\"+build\",[\"PUT\",\"SOUTH\"]]\n";


    String request2 = "[[\"0one1\", \"0one2\"], [3,       0],\n" +
            "\n" +
            "     [\"0two1\", \"0two2\"]] [\"move\",\"one1\",[\"EAST\",\"SOUTH\"]] [\"+build\",[\"WEST\",\"PUT\"]]";

    try {
      Interpreter.executeInitialRequests(board, JSONParse.parse(request), log);
    } catch (IOException e) {

    }

    board.printBoard();


    ArrayList<String> p1Workers = new ArrayList<>();
    ArrayList<String> p2Workers = new ArrayList<>();
    p1Workers.add("one1");
    p1Workers.add("one2");
    p2Workers.add("two1");
    p2Workers.add("two2");


    HashMap<String, List<String>> playerWorkers = new HashMap<>();
    playerWorkers.put("one", p1Workers);
    playerWorkers.put("two", p2Workers);

    if (strat.canWinInNMoves(board, playerWorkers, ruleChecker, 1)) {
      System.out.println("yes");
    }
    else {
      System.out.println("no");
    }

    //System.out.println(strat.nextMove(board, playerWorkers, ruleChecker));


*/

  }
}