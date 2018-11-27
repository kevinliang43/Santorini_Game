import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class for Reading, Interpretting and Translating Tournament Config Files into actual Objects.
 */
public class ConfigReader {


  /**
   * Creates a List of Observers, given a list of Observer Args
   * Observer Args are in the form [Name, PathString]
   *
   * Name is the String representation of the new Player
   *
   * PathString is a Linux Path to a dynamically loadable component that
   * implements the respective player or observer.
   *
   *
   * @param observersArgs List of observer Args
   * @return List of generated Observers
   */
  public static ArrayList<Observer> buildObservers(ArrayList<ArrayList<String>> observersArgs) {

    ArrayList<Observer> observerObjs = new ArrayList<>();
    for (ArrayList<String> observerArgs : observersArgs) {

      ArrayList<String> existingObserverNames = new ArrayList<>();
      for (Observer observer : observerObjs) {
        existingObserverNames.add(observer.getName());
      }
      while (existingObserverNames.contains(observerArgs.get(0))) {
        observerArgs.set(0, observerArgs.get(0)+existingObserverNames.size());
      }

      observerObjs.add(buildObserver(observerArgs.get(0), observerArgs.get(1)));

    }
    return observerObjs;
  }

  /**
   * Creates a List of Players, given a List of Player Args
   * Player Args are in the form [Kind, Name, PathString]
   * Kind is a String and is one of:
   *    "good", meaning a player that is intended to be well-behaved;
   *    "breaker", referring to a player that terminates in a timely manner but misbehaves;
   *    "infinite", denoting a player that goes into an infinite loop.
   *
   * Name is the String representation of the new Player
   *
   * PathString is a Linux Path to a dynamically loadable component that
   * implements the respective player or observer.
   *
   * If more than one player contains the same name, a new name will be automatically generated
   * for subsequent players.
   *
   * @param playersArgs List of Player Args
   * @return List of Generated Players
   */
  public static ArrayList<Player> buildPlayers(ArrayList<ArrayList<String>> playersArgs, int numStart) {
    ArrayList<Player> playerObjs = new ArrayList<>();
    int numPlayer = numStart;
    for (ArrayList<String> playerArgs : playersArgs) {
      // Check for existing name, make new name
      ArrayList<String> existingPlayerNames = new ArrayList<>();
      for (Player player : playerObjs) {
        existingPlayerNames.add(player.getName());
      }
      while (existingPlayerNames.contains(playerArgs.get(1))) {
        playerArgs.set(1, playerArgs.get(1)+existingPlayerNames.size());
      }
      // Build the Player
      playerObjs.add(buildPlayer(playerArgs.get(0), playerArgs.get(1), playerArgs.get(2), numPlayer));
      numPlayer++;
    }
    return playerObjs;
  }

  /**
   * Builds a Player object using given Kind, Name, PathString and an ID
   * If PathString leads to an unloadable class, then the Kind is used as a default mechanism
   * Default Classes include:
   * "good" - StayAlivePlayer
   * "breaker" - BreakerPlayer
   * "infinite" - InfinitePlayer
   * @param kind Kind of Player. One of: "good", "breaker", "infinite"
   * @param name Name of the new Player
   * @param path String representation of Linux Path of a Player class to be loaded.
   * @param id ID of the new Player
   * @return newly Built Player
   */
  public static Player buildPlayer(String kind, String name, String path, int id) {

    // Parse the Path
    String className = "";

    while (path.length() > 0 && path.charAt(path.length() - 1) != '/') {
      className = path.charAt(path.length() - 1) + className;
      path = path.substring(0, path.length() - 1);
    }

    // Player ["KIND", "NAME", "PATH_STRING"]
    // Dynamically Load the required class FROM PATH_STRING
    URL[] url = null;
    try {
      url = new URL[] {new URL("file://" + path)};
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Class clazz = null; // Dynamically Loaded Class
    try {
      // Get the Class name
      //String[] parsedPath = path.split("/");
      className = className.split("\\.")[0];

      // Load the class
      clazz = URLClassLoader.newInstance(url).loadClass(className);
    } catch (Exception e) {
      // Class Not found, do nothing
    }
    // default Case, class is unloadable, use the kind as default
    if (clazz == null) {
      switch (kind) {
        case "good":
          clazz = StayAlivePlayer.class;
          break;
        case "breaker":
          clazz = BreakerPlayer.class;
          break;
        case "infinite":
          clazz = InfinitePlayer.class;
      }
    }
    // Create Player
    Object obj = null;
    try {
      obj = clazz.getConstructors()[0].newInstance(name.toLowerCase(), id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (Player) obj;
  }

  /**
   * Builds a single observer given the name and PATH_STRING
   * @param name name of the observer
   * @param path Path of a new Observer Implementation.
   * @return Observer constructed with given arguments
   */
  public static Observer buildObserver(String name, String path) {

    // Parse the Path
    String className = "";

    while (path.length() > 0 && path.charAt(path.length() - 1) != '/') {
      className = path.charAt(path.length() - 1) + className;
      path = path.substring(0, path.length() - 1);
    }

    // Dynamically Load the required Observer class FROM PATH_STRING
    URL[] url = null;
    try {
      url = new URL[] {new URL("file://" + path)};
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Class clazz = null; // Dynamically Loaded Class
    try {
      // Get the Class name
      //String[] parsedPath = path.split("/");
      className = className.split("\\.")[0];

      // Load the class
      clazz = URLClassLoader.newInstance(url).loadClass(className);
    } catch (Exception e) {
      // Class Not found, do nothing
    }
    // default Case, class is unloadable, use the Observer Class as default
    if (clazz == null) {
      clazz = Observer.class;
    }
    // Create Player
    Object obj = null;
    try {
      obj = clazz.getConstructors()[0].newInstance(name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (Observer)obj;
  }

  /**
   * Reads Tournament Manager Config from STDIN and returns a parsed JSON Node
   * @return JSONNode representing the config file
   */
  public static ArrayList<JsonNode> readAndParse() {
    Scanner scanner = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    builder.append(scanner.nextLine());

    while (!Translator.isValidJSON(builder.toString())) {
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
  public static ArrayList<JsonNode> parse(String jsonString) throws IOException{

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
  public static ArrayList<ArrayList<String>> getFields(String field, JsonNode config) {

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
