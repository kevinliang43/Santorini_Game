import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class For A Tournament Server.
 *
 * A game server allows remote clients to sign up for a tournament.
 * After waiting for a limited amount of time and signing up a certain minimal number of players,
 * the server creates a tournament manager from the players and runs a complete tournament.
 */
public class Server {

  // Default Server Variables
  private static int MIN_PLAYERS = 2;
  private static int PORT_NUMBER = 50000;
  private static int ADD_PLAYER_TIMEOUT = 10000;
  private static int REPEAT = 0;

  // Default Tournament Variables
  private static int MAX_PLAYERS = 64;
  private static int ROUND_TIMEOUT = 30000;
  private static int NUM_ROUNDS = 2;
  private static int GAMES_PER_ROUND = 3;

  // Server Setup Variables
  private ServerSocket serverSocket = null;
  private ArrayList<String> signedUp = new ArrayList<>();
  private ArrayList<Player> newPlayers = new ArrayList<>();

  //Field Names
  private static ArrayList<String> FIELD_NAMES = new ArrayList<>(Arrays.asList(
          "min players", "port", "waiting for", "repeat"));

  public void main() {
    // Setup Server
    this.serverSetup();
    // Accept Players Phase
    this.acceptPlayersPhase();
    // Construct Tournament
    TournamentManager manager = new TournamentManager(this.newPlayers, MIN_PLAYERS, MAX_PLAYERS, ROUND_TIMEOUT, NUM_ROUNDS, GAMES_PER_ROUND);
    //Run the Tournament
    manager.mainNoConfig();
    // Close
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      // Close the Server
    }



  }

  /**
   * Opens up a new Thread that accepts new Players for an amount of time
   * defined as this Server's ADD_PLAYER_TIMEOUT.
   */
  private void acceptPlayersPhase() {
    // Accept Players
    Thread acceptPlayersThread = new Thread() {
      @Override
      public void run() {
        acceptPlayers();
      }
    };
    acceptPlayersThread.start();
    try {
      acceptPlayersThread.join(ADD_PLAYER_TIMEOUT);
      if (acceptPlayersThread.isAlive()) {
        acceptPlayersThread.interrupt();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Within a given amount of time to Sign up (Defined as ADD_PLAYER_TIMEOUT)
   * This Server will begin accepting new requests from Clients to sign up.
   * Threads are spawned for each Client connection to handle:
   *    - Accepting Connections
   *    - Accepting Signup Requests and Verifying the Request Format
   *    - Building the Proxy Players
   *
   * No Connections to this server will be allowed after the Signup Period.
   * Any Client in the middle of the signup process (that have not finished) will be
   * disconnected from this server and have the thread terminated.
   *
   * Clients are expected to do the following before the signup period is over for a complete signup:
   *     - Connect to this Server
   *     - Send a JSON String with their requested name
   *
   * Clients that have correctly completed the signup process will have
   * a ProxyPlayer created.
   *
   * Clients that have completed the signup process, but submitted a name that
   * already exists will be generated a new unique name for their Proxy Player,
   * and will also be sent back a JSON String with their updated name.
   */
  private void acceptPlayers() {
    // Start Time
    long startTime = System.currentTimeMillis();

    // Setup Thread Executor Service
    ExecutorService executor = Executors.newFixedThreadPool(4); //TODO make more efficient max num threads
    // List of opened up Sockets waiting for input
    ArrayList<Socket> clientSockets = new ArrayList<>();
    // while sign up time is still available
    //while (System.currentTimeMillis() - startTime < ADD_PLAYER_TIMEOUT) {
    for (int i = 0; i < this.MAX_PLAYERS; i++) {
      // Build new Thread to listen for and accept incoming sign ups
      Thread listenerThread = new Thread() {
        @Override
        public void run() {
          Socket clientSocket = null;
          try {
            // Listen for Connection
            clientSocket = serverSocket.accept();
            // Connection received, get reference to the client socket
            clientSockets.add(clientSocket);
            // Handle Client Socket for Signup
            clientSocketHandle(clientSocket);
            // Once Process has been completed, remove reference of client socket from
            // list of Sockets that are mid-process in their respective threads
            clientSockets.remove(clientSocket);
          } catch (IOException e) {
            // Error Running thread, do nothing
            // (Thread repurposed by executor)
          }
        }
      };
      // Execute the thread
      executor.execute(listenerThread);
    }
    // WAIT FOR TIMEOUT
    try {
      Thread.sleep(ADD_PLAYER_TIMEOUT);
    } catch (InterruptedException e ){
    }

    // After Timeout has been reached, Shutdown all threads from executor
    executor.shutdownNow();
    // Any hanging threads will have their associated Clients
    // disconnected and the threads are shutdown.
//    for (Socket clientSocket : clientSockets) {
//      try {
//        clientSocket.close();
//      } catch (IOException e) {
//        // If there is an error with closing the socket,
//        // The socket is already closed. Do nothing
//      }
//    }

  }


  /**
   * Handles the signup portion of a connected Client
   * Receives JSON from the Client and parses it to see if it
   * is in the correct Name format (String).
   *
   * If so, it signs up the Client with the given name
   * If the Given name is acceptable, but taken, it will generate a new name for the client
   * and sign up the client with the generated name and send information back to the Client
   * informing them of their new name.
   *
   * If the JSON message is of bad format, then it will close the connection to the
   * Client Immediately.
   *
   * @param clientSocket Connection to the Client signing up
   */
  private void clientSocketHandle(Socket clientSocket) {
    try {
      // Read Input from Client
      InputStream clientInStream = clientSocket.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientInStream));
      StringBuilder messageBuilder = new StringBuilder();
      String registerMessage = "";

      JsonNode messageNode = null;

      while (messageNode == null) {
        registerMessage = bufferedReader.readLine();
        messageBuilder.append(registerMessage);
        try {
          messageNode = ConfigReader.parse(messageBuilder.toString()).get(0);
        } catch (Exception e) {

        }
      }
      if (messageNode.isTextual()) { //TODO ENFORCE LOWERCASE
        String newSignUp = messageNode.asText().toLowerCase();
        this.signup(clientSocket, newSignUp, bufferedReader);
        }
        // Otherwise Immediately Close Connection
        else {
          clientSocket.close();
        }
    } catch (IOException e) {
      // Errors in obtaining connection to client
      // Do nothing, connection does not exist.
    }

  }

  /**
   * Updates the Server with the new Client signup.
   * Checks to see if given name is taken already.
   * If not, the name is added to a preprocessing list.
   * If so, a unique name is generated for the new Client,
   * and the Client is sent back a JSON message in the form: ["playing-as" Name]
   *
   * Afterwards, a new Proxy Player is constructed for the Client and saved for later use.
   *
   * @param clientSocket Client requesting a new Signup
   * @param newSignUp Name sent by the Client.
   */
  private synchronized void signup(Socket clientSocket, String newSignUp, BufferedReader reader) {

    try {
      // If New Signup Name Exists:
      if (this.signedUp.contains(newSignUp)) {
        // Generate new Name
        while (signedUp.contains(newSignUp)) {
          newSignUp += signedUp.size();
        }
        // Send Back new Name
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode responseNode = mapper.createArrayNode();
        responseNode.add("playing-as");
        responseNode.add(newSignUp);
        clientSocket.getOutputStream().write((responseNode.toString() + "\n").getBytes());
      }
      // Add name to preprocessing List.
      this.signedUp.add(newSignUp);

      // Create new ProxyPlayer
      RemoteStrategy strat = new RemoteStrategy(clientSocket, reader);
      ProxyPlayer newPlayer = new ProxyPlayer(newSignUp, this.newPlayers.size(), strat);
      this.newPlayers.add(newPlayer);
    } catch (IOException e) {
      // If Connection is closed while signing up, do nothing
    }

  }

  /**
   * Reads Config File through STDIN in JSON Format and converts into a usable format.
   * Checks to see if the Config is formatted correctly.
   * Updates this Server's properties to match the given Config File.
   *
   * After static variables are setup, this Server's Socket is then setup
   */
  private void serverSetup() {
    // Take in Config file
    JsonNode configNode = ConfigReader.readAndParse().get(0);
    // Check Fields
    if (!Translator.checkFields(configNode, FIELD_NAMES)) {
      throw new IllegalArgumentException("Config File is incorrectly formatted.\n");
    }

    // Add appropriate values to class fields
    MIN_PLAYERS = configNode.get(FIELD_NAMES.get(0)).asInt();
    PORT_NUMBER = configNode.get(FIELD_NAMES.get(1)).asInt();
    ADD_PLAYER_TIMEOUT = configNode.get(FIELD_NAMES.get(2)).asInt() * 1000;
    REPEAT = configNode.get(FIELD_NAMES.get(3)).asInt();

    // Setup Socket
    this.setupSocket();
  }

  /**
   * Sets up this Server's Socket with this Server's PORT_NUMBER
   */
  private void setupSocket() {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(PORT_NUMBER);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.serverSocket = serverSocket;
  }


}
