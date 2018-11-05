import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Test Harness for Tournament Manager
 */
public class xrun {

  public static void main(String args[]) {
    ArrayList<JsonNode> requests = readAndParse();

    ArrayList<ArrayList<String>> players = getFields("players", requests.get(0));
    ArrayList<ArrayList<String>> observers = getFields("observers", requests.get(0));


    for (int i = 0; i < players.size(); i++) {
      System.out.println(players.get(i));
    }

    for (int i = 0; i < observers.size(); i++) {
      System.out.println(observers.get(i));
    }

  }

  /**
   * Reads Tournament Manager Config from STDIN and returns a parsed JSON Node
   * @return JSONNode representing the config file
   */
  static ArrayList<JsonNode> readAndParse() {
    Scanner scanner = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();

    while (scanner.hasNextLine()) {
      builder.append(scanner.nextLine());
    }

    String requestString = builder.toString();



    // Parse input to JsonNode
    ArrayList<JsonNode> requests = new ArrayList<>();
    try {
      requests = parse(requestString);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return requests;
  }

  /**
   * Parses JSONStrings into usable JSON Node
   * @param jsonString to be parsed
   * @return JSONNode object representing the json input
   * @throws IOException Parser unavailable.
   */
  static ArrayList<JsonNode> parse(String jsonString) throws IOException{

    // Parse JSON string
    ArrayList<JsonNode> requestList = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonParser parser = objectMapper.getFactory().createParser(jsonString);


    while (!parser.isClosed()) {
      JsonNode currentNode = null;

      try {
        currentNode = objectMapper.readTree(parser);
      } catch (Exception e) {
        break;
      }

      if (currentNode != null) {
        requestList.add(currentNode);
      }
    }
    return requestList;
  }

  /**
   * Converts a field of a Config file into an arraylist of arraylist of properties
   * @param field name of the field
   * @param config JSONNode representing the config
   * @return ArrayList of ArrayList of properties. Inner arrays represent each instance of the field.
   */
  static ArrayList<ArrayList<String>> getFields(String field, JsonNode config) {

    // Get all Players, convert into arrays
    ArrayNode playerNode = (ArrayNode)config.get(field);
    Iterator<JsonNode> playerIt = playerNode.elements();
    ArrayList<ArrayList<String>> players = new ArrayList<>();

    while (playerIt.hasNext()) {
      ArrayNode node = (ArrayNode)playerIt.next();
      ArrayList<String> currentPlayer = new ArrayList<>();
      for (int i = 0; i < node.size(); i++) {
        currentPlayer.add(node.get(i).asText());
      }
      players.add(currentPlayer);

    }

    return players;

  }



}
