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

    ArrayList<ArrayList<String>> playersArgs = ConfigReader.getFields("players", config);
    ArrayList<ArrayList<String>> observersArgs = ConfigReader.getFields("observers", config);

    ArrayList<Player> players = ConfigReader.buildPlayers(playersArgs, 0);
    ArrayList<Observer> observers = ConfigReader.buildObservers(observersArgs);
    String ip = config.get("ip").asText();
    int port = config.get("port").asInt();

    ArrayList<ClientSideProxy> proxies = new ArrayList<>();

    for (Player player: players) {
//      try {
//        Thread.sleep(250);
//      } catch (InterruptedException e) {
//      }

      System.out.println(player.getName());
      ClientSideProxy curProxy = new ClientSideProxy(player, ip, port);
      proxies.add(curProxy);
    }

//    try {
//      Socket socket = new Socket("localhost", 56789);
//      InputStream inputStream = socket.getInputStream();
//      OutputStream outputStream = socket.getOutputStream();
//      outputStream.write("\"Kevin\"".getBytes());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }



  }
}
