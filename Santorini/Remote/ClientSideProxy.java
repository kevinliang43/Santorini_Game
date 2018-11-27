import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This component exist on the client side and proxies the interaction between the player
 * on one side and the tournament manager and referee on the other. By linking this proxy with the
 * player implementation, it becomes straightforward to establish a TCP-based communication link
 * between the client side and the server side at the appropriate level (individual games,
 * best-of game series, tournament).
 */
public class ClientSideProxy {

  Player player;
  Socket clientSocket;
  InputStream inputStream;
  OutputStream outputStream;
  BufferedReader inputReader;

  /**
   * Constructor for a Client Side Proxy.
   * Will Create a Proxy, and attempts to connect to a given IP and PORT Number.
   * Afterwards, it will send the initial sign up message request.
   * @param player Player that this Proxy is connected to.
   * @param ip IP to connect to.
   * @param port PORT to connect to.
   */
  ClientSideProxy(Player player, String ip, int port) {
    this.player = player;
    try {
      // Connect to socket
      this.clientSocket = new Socket(ip, port);
      // Setup I/O Streams
      this.inputStream = clientSocket.getInputStream();
      this.outputStream = clientSocket.getOutputStream();
      // Setup Buffered Reader for inputStream
      this.inputReader = new BufferedReader(new InputStreamReader(this.inputStream));

      //Send JSON String of this Proxy's Player Name
      // AKA, Send "SIGNUP" message to server
      String name = "\"" + this.player.getName() + "\"\n";
      this.outputStream.write(name.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Main Function of this Client proxy.
   * While the connection remains open to the Server,
   * it will read in the next JSON Message, and handle it accordingly.
   */
  public void main() {
    // After Signup, There is either:
    // there is a possible optional receive ["playing-as", Name]
    // OR JSON String name of first opponent

    while (!this.clientSocket.isClosed()) {
      // Get the next Message
      JsonNode message = this.readInStream();
      messageHandler(message);
    }


  }

  /**
   * Logic that Handles the different types of messages
   * and directs this Proxy's actions accordingly.
   * Currently, there are five different types of messages that this
   * Proxy will encounter FROM the server.
   * 1. Playing-As (["playing-as" Name])
   * 2. other (JSON String of next opponent.)
   * 3. Placement ([[Worker,Coordinate,Coordinate],...])
   * 4. Take Turn (Board)
   * 5. Informing Players (JSON array of EncounterOutcomes)
   *    EncounterOutcome is one of the following:
   *        [String, String], which is the name of the winner followed by the loser;
   *        [String, String, "irregular"], which is like the first alternative but signals that the losing player misbehaved.
   */
  private void messageHandler(JsonNode current) {
    //Get the MessageType
    MessageType messageType = Translator.messageType(current);

    //1. Playing-As (["playing-as" Name])
    if (messageType.equals(MessageType.PLAYING_AS)) {
      // Print the Message
      // No Response needed from Client
      //System.out.println(current.asText());

    }
    //2. other (JSON String of next opponent.)
    else if (messageType.equals(MessageType.OPP_NAME)) {
      this.player.resetWorkers();
      // Print the Message
      // No Response needed from Client
      //System.out.println(current.asText());

    }
    //3. Placement ([[Worker,Coordinate,Coordinate],...])
    else if (messageType.equals(MessageType.PLACEMENT)) {
      if (current.size() <= 1) {
        this.player.resetWorkers();
      }
      placeHandler((ArrayNode) current);
    }
    //4. Take Turn (Board)
    else if (messageType.equals(MessageType.TAKE_TURN)) {
      moveBuildHandler((ArrayNode) current);

    }
    //5. JSON Array of Encounter Outcomes
    else if (messageType.equals(MessageType.INFORM_PLAYERS)) {
      // Print the Message
      // No Response needed from Client
      //System.out.println(current.asText());
      try {
        this.clientSocket.close();
      } catch (IOException e) {

      }

    }

  }

  /**
   * Handler for a Placement Message, stored in an ArrayNode.
   * Placement Message contains information about currently placed Workers and their locations.
   * This information is then passed to the Player proxied by this Proxy, and requests an
   * Action from the Player.
   * Player action is then obtained and converted into JSON, where it is sent to the Server.
   *
   * @param node Placement Message Stored in an ArrayNode. Contains information about currently
   *             placed workers and their location in the form of
   *             JSON Array of [Name, Coordinate, Coordinate]
   */
  private void placeHandler(ArrayNode node) {
    // Setup GetNextAction Args
    Board b = new Board();
    for (int i = 0; i < node.size(); i++) {
      String workerName = node.get(i).get(0).asText();
      int x = node.get(i).get(1).asInt();
      int y = node.get(i).get(2).asInt();
      b.placeWorker(x, y, workerName, Board.INVALID_WORKER_ID);
    }
    Action placeAction = (Action)player.getNextAction(b, Status.PLACE);

    //add to this worker
    this.player.addWorkerID(b.getWorkerIDs().size());
    this.player.addWorkerName(this.player.getName() + (this.player.getWorkerIDs().size() + 1));

    // Convert to JSON and write to OutStream
    String placeJSON = Translator.placeActionAsJSON(placeAction);
    this.writeToOutStream(placeJSON);
  }

  /**
   * Handles a Take-turn request.
   * A ArrayNode storing information about the current Board state in the form of JSON
   * is transformed into a usable Board Object and sent to this Proxy's connected Player
   * to allow the Player to determine its next action.
   *
   * This Action (Move/MoveBuild/Giveup) is then converted back into JSON and sent to the Server
   * to be handled.
   * @param node Container for Board State Information in the form of JSON Array of Cells
   */
  private void moveBuildHandler(ArrayNode node) {
    Board b = Translator.convertJSONToBoard(this.player.getName(), node, this.player.getWorkerIDs());
    MoveBuild moveBuild = (MoveBuild)this.player.getNextAction(b, Status.MOVEBUILD);

    String mbJSON = Translator.moveBuildAsJSON(b, moveBuild);
    this.writeToOutStream(mbJSON);
  }



  /**
   * Reads the next full JSON String from the Server.
   * @return JSON String
   */
  private JsonNode readInStream() {
    StringBuilder messageBuilder = new StringBuilder();
    try {
      // Read Input from Client
      String registerMessage = this.inputReader.readLine();
      messageBuilder.append(registerMessage);
      while (!Translator.isValidJSON(messageBuilder.toString())) {
        registerMessage = this.inputReader.readLine();
        messageBuilder.append(registerMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    JsonNode returnObj = null;
    try {
      returnObj = ConfigReader.parse(messageBuilder.toString()).get(0);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return returnObj;
  }

  /**
   * Writes Message to this Strategy's connected Server
   * in the form of a JSON String.
   * @param message Message to be sent.
   */
  private void writeToOutStream(String message) {
    try {
      message += "\n";
      this.outputStream.write(message.getBytes());
    } catch (IOException e) {
      e.printStackTrace();

    }
  }


}
