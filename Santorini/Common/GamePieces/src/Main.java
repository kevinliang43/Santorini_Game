import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KevinLiang on 9/30/18.
 */
public class Main {

  public static void main(String[] args) {

    String testString = "[[0,0,0,\"0m2\"],[\"0m1\",0,\"0k1\"],[0,0,\"0k2\"]] [\"move\", \"m1\", \n [\"EAST\", \"NORTH\"]] [\"build\", \"m1\",[\"WEST\", \"SOUTH\"]]" +
            "[\"neighbors\", \"m1\", [\"WEST\", \"NORTH\"]] [\"neighbors\", \"m1\", [\"WEST\", \"SOUTH\"]] [\"height\", \"m1\", [\"WEST\", \"SOUTH\"]] [\"occupied?\", \"m1\", [\"WEST\", \"SOUTH\"]]" +
            "[\"occupied?\", \"k1\", [\"PUT\", \"SOUTH\"]]" ;
    ByteArrayInputStream testStream = new ByteArrayInputStream(testString.getBytes());
    Board board = new Board(6,6);

    try {
      String parsedString = JSONParse.readInput(testStream);
      Interpreter.executeRequests(board, JSONParse.parse(parsedString));
    } catch (IOException e) {
      e.printStackTrace();
    }



  }

}
