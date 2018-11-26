import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test Harness for runs the client side of a complete server-client tournament for a
 * configuration. That is, the remote harness reads a tournament configuration
 * file and connects the specified players to a remote server.
 */
public class xclients {

  public static void main(String args[]) {
    // Read Config File
    JsonNode config = ConfigReader.readAndParse().get(0);

    // Check if Field names are valid (i.e Config File was properly formatted)
    if (!Translator.checkFields(config, new ArrayList<>(Arrays.asList("players", "observers", "ip", "port")))) {
      throw new IllegalStateException("Config File is not formatted correctly.");
    }

    // Set up Variables into usable format
    ArrayList<ArrayList<String>> playersArgs = ConfigReader.getFields("players", config);
    ArrayList<ArrayList<String>> observersArgs = ConfigReader.getFields("observers", config);

    ArrayList<Player> players = ConfigReader.buildPlayers(playersArgs, 0);
    ArrayList<Observer> observers = ConfigReader.buildObservers(observersArgs);
    String ip = config.get("ip").asText();
    int port = config.get("port").asInt();

    // Create Client Side Proxies
    ArrayList<ClientSideProxy> proxies = new ArrayList<>();

    for (Player player: players) {
      ClientSideProxy curProxy = new ClientSideProxy(player, ip, port);
      proxies.add(curProxy);
    }




  }
}
