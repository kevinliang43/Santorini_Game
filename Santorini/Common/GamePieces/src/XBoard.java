import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Mock environment to test Board components
 */
public class XBoard {

  public static void main(String[] args) {


    if (args.length != 2) {
      throw new IllegalArgumentException("Please enter a correct File path to read from");
    }

    // Read the file
    String fullPath = args[0];
    String fileName = args[1];

    FileReader fileReader;
    StringBuilder requestStringBuilder = new StringBuilder();

    try {
      fileReader = new FileReader(fullPath + "/" + fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;

      while((line = bufferedReader.readLine()) != null) {
        requestStringBuilder.append(line);
      }
      bufferedReader.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    String requestString = requestStringBuilder.toString();

    ByteArrayInputStream testStream = new ByteArrayInputStream(requestString.getBytes());
    Board board = new Board(6,6);
    StringBuilder log = new StringBuilder();
    try {
      String parsedString = JSONParse.readInput(testStream);
      Interpreter.executeInitialRequests(board, JSONParse.parse(parsedString), log);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.print(log.toString());



  }

}
