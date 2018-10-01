import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KevinLiang on 9/30/18.
 */
public class Main {

  public static void main(String[] args) {

    String testString = "[\"move\", \"player1\", \n [\"EAST\", \"NORTH\"]][\"move\", \"player1\",";
    ByteArrayInputStream testStream = new ByteArrayInputStream(testString.getBytes());

    try {
      String parsedString = JSONParse.readInput(testStream);
      JSONParse.printRequests(JSONParse.parse(parsedString));
    } catch (IOException e) {
      e.printStackTrace();
    }



  }

}
