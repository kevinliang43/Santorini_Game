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

  static ArrayList<JsonNode> parseNonArrayNode(String jsonString) throws IOException{

    // Parse JSON string
    ArrayList<JsonNode> requestList = new ArrayList<>();
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
      else if (currentNode != null && currentNode.isTextual()) {
        requestList.add(currentNode);
      }
      else if (currentNode != null && currentNode.isInt()) {
        requestList.add(currentNode);
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
