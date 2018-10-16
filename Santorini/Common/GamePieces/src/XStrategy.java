import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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


    Board board = new Board(6, 6);
    RuleChecker ruleChecker = new RuleChecker();
    StringBuilder log = new StringBuilder();
    Interpreter.execute(board, (ArrayNode)requests.get(1), log, "");
    Interpreter.execute(board, (ArrayNode) requests.get(3), log, "");

    ArrayList<String> p1Workers = new ArrayList<>();
    ArrayList<String> p2Workers = new ArrayList<>();
    p1Workers.add("one1");
    p1Workers.add("one2");
    p2Workers.add("two1");
    p2Workers.add("two2");


    HashMap<String, List<String>> playerWorkers = new HashMap<>();
    playerWorkers.put("one", p1Workers);
    playerWorkers.put("two", p2Workers);

    FurthestStrat strat = new FurthestStrat(player, numTurns);

    if (strat.nextMove(board, playerWorkers, ruleChecker) == "") {
      System.out.println("\"no\"");
    }
    else {
      System.out.println("\"yes\"");
    }

  }
}