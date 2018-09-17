import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class JSONParse {

  /**
   * Main function that initializes STDIN to accept JSON input to be parsed.
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    InputStreamReader reader = new InputStreamReader(System.in);
    ArrayList<String> values = new ArrayList<>();

    // Parse Json
    parseJson(reader, values);


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
   * @param reader Provides the JSON to be parsed
   * @param values list where individual JSON Values will be added to
   * @throws IOException
   */
  private static void parseJson(InputStreamReader reader, ArrayList<String> values) throws IOException{
    // Create JSONParser

    ObjectMapper objectMapper = new ObjectMapper();
    JsonFactory factory = objectMapper.getFactory();
    JsonParser parser = factory.createParser(reader);

    // Parse JSON String
    while (!parser.isClosed()) {
      JsonNode currentNode = objectMapper.readTree(parser);

      if (currentNode != null) {
        String currentNodeString = currentNode.toString();
        values.add(currentNodeString);
      } }
  }
}




