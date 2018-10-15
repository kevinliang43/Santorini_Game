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

    /*
    // Get the input
    Scanner scanner = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (scanner.hasNextLine()) {
      builder.append(scanner.nextLine());
    }

    String requestString = builder.toString();

    ByteArrayInputStream testStream = new ByteArrayInputStream(requestString.getBytes());
    Board board = new Board(6,6);
    StringBuilder log = new StringBuilder();
    try {
      String parsedString = JSONParse.readInput(testStream);
      Interpreter.executeInitialRequests(board, JSONParse.parse(parsedString), log);
    } catch (IOException e) {
      e.printStackTrace();
    }
    */

    /*
    Board board = new Board(6,6);
    HashMap<String, List<String>> map = new HashMap<>();

    ArrayList<String> klist = new ArrayList<>();
    klist.add("kevin1");
    klist.add("kevin2");
    ArrayList<String> mlist = new ArrayList<>();
    mlist.add("marina1");
    mlist.add("marina2");

    map.put("k", klist);
    map.put("m", mlist);


    FurthestStrat strat = new FurthestStrat("k", 3);

    strat.nextMove(board, map);
    */
    Board board = new Board(6, 6);
    RuleChecker ruleChecker = new RuleChecker();

    board.printBoard();

    FurthestStrat strat = new FurthestStrat("one", 2);
    Appendable log = new StringBuilder();

    String request = "[[\"0one1\", \"1one2\", 3, \"2two1\"],\n" +
            "\n" +
            "     [0,       \"2two2\", 3]] [\"move\",\"one2\",[\"WEST\",\"SOUTH\"]] [\"+build\",[\"PUT\",\"SOUTH\"]]\n";

    try {
      Interpreter.executeInitialRequests(board, JSONParse.parse(request), log);
    } catch (IOException e) {

    }

    board.printBoard();

    /*
    board.getWorker("one1");
    board.getWorker("two1");
    board.getWorker("one2");
    board.getWorker("two2");
    */




    ArrayList<String> p1Workers = new ArrayList<>();
    ArrayList<String> p2Workers = new ArrayList<>();
    p1Workers.add("one1");
    p1Workers.add("one2");
    p2Workers.add("two1");
    p2Workers.add("two2");


    HashMap<String, List<String>> playerWorkers = new HashMap<>();
    playerWorkers.put("one", p1Workers);
    playerWorkers.put("two", p2Workers);


    //TODO throws the io exception because of the .toString on linked list
    System.out.println(strat.nextMove(board, playerWorkers, ruleChecker));


  }
}