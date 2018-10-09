import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Utils Class for Parsing JSON String input
 */
public class JSONParse {

  /**
   * Reads the input from the given input stream
   * @param stream Input Stream that the requests are being sent in from
   * @return String of the input
   * @throws IOException thrown when Input Stream is invalid
   */
  static String readInput(InputStream stream) throws IOException {
    // Read Input
    InputStreamReader reader = new InputStreamReader(stream);
    BufferedReader in = new BufferedReader(reader);
    String line;
    StringBuilder inputStringBuilder = new StringBuilder();
    while ((line = in.readLine()) != null) {
      inputStringBuilder.append(line);

    }
    return inputStringBuilder.toString();
  }

  /**
   * Parses the given String and turns it into a list of requests as an ArrayList of ArrayNodes
   * @param jsonString String of the requests
   * @return Arraylist of ArrayNodes representing the list of requests to be checked and executed
   */
  static ArrayList<ArrayNode> parse(String jsonString) throws IOException{

    // Parse JSON string
    ArrayList<ArrayNode> requestList = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonParser parser = objectMapper.getFactory().createParser(jsonString);


    //while the parser has more JSONNodes,
    //check if each node is valid and add it to the list of requests
    while (!parser.isClosed()) {
      JsonNode currentNode = null;

      try {
        currentNode = objectMapper.readTree(parser);
      } catch (Exception e) {
        break;
      }

      if (currentNode != null && currentNode.isArray()) {
        requestList.add((ArrayNode)currentNode);
      }
    }
    return requestList;
  }

  static void printRequests(ArrayList<ArrayNode> requests) {
    for (ArrayNode node : requests) {
      System.out.println(node.toString());
    }
  }

  /**
   * Determines how to handle the given string direction
   * @param direction is one of EAST, SOUTH, PUT, NORTH, or WEST representing the direction we're moving in
   * @return 1, 0, or -1 representing the direction to move in
   */
  public static int parseDirection(String direction) {
    if(direction.equals("EAST") || direction.equals("SOUTH")) {
      return 1;
    }
    else if(direction.equals("PUT")) {
      return 0;
    }
    else if(direction.equals("WEST") || direction.equals("NORTH")) {
      return -1;
    }
    else {
      throw new IllegalArgumentException(direction + " is not a valid direction");
    }
  }

}
