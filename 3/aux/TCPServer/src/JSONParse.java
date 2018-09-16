import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class JSONParse {

  /**
   * Main function that initializes STDIN to accept JSON input to be parsed.
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {



    Scanner scanner = new Scanner(System.in);
    String jsonString = "";
    ArrayList<String> values = new ArrayList<>();

    // Build JSON String
    while (scanner.hasNext()) {
      jsonString += scanner.nextLine();
    }
    // Parse JSON
    parseJson(jsonString, values);
    // Print the values
    printValues(values);
  }

  /**
   * Prints out a given list of JSON values
   * @param values Contains a list of JSON values to be printed
   */
  private static void printValues(ArrayList<String> values) {
    for (int i = 0; i < values.size(); i++) {
      System.out.println("[" + (values.size() - i - 1) + ", " + values.get(i) + "]");
    }
  }

  /**
   * Parses a given string of JSON values, parses the string into individual values and updates a
   * given list with the individual JSON values
   * @param jsonString String to be Parsed
   * @param values list where individial JSON Values will be added to
   * @throws IOException
   */
  private static void parseJson(String jsonString, ArrayList<String> values) throws IOException{
    // Create JSONParser
    JsonFactory factory = new JsonFactory();
    JsonParser parser = factory.createParser(jsonString);
    ObjectMapper objectMapper = new ObjectMapper();

    // Parse JSON String
    while (!parser.isClosed()) {
      JsonNode currentNode = objectMapper.readTree(parser);

      if (currentNode != null) {
        values.add(objectMapper.writer().writeValueAsString(currentNode));
      } }
  }
}





