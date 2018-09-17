import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

  /**
   * Main function that initializes STDIN to accept JSON input to be parsed.
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    //Scanner scanner = new Scanner(System.in);
    InputStreamReader reader = new InputStreamReader(System.in);


    StringBuilder jsonString = new StringBuilder();
    ArrayList<String> values = new ArrayList<>();

    // Build JSON String
    //while (scanner.hasNext()) {
    //  jsonString.append(scanner.nextLine());
    //}
    // Parse JSON
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
   * @param reader String to be Parsed
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





