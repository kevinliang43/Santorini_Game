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

  }
}