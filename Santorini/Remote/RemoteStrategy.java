import com.fasterxml.jackson.databind.JsonNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Strategy Object that facilitates Player Actions for a Remote Player
 */
public class RemoteStrategy implements Strategy {

  Socket clientSocket;
  OutputStream outputStream;
  InputStream inputStream;
  BufferedReader reader;

  /**
   * Constructor for Remote Strategy
   * @param clientSocket Socket to communicate with Client
   */
  RemoteStrategy(Socket clientSocket) {
    this.clientSocket = clientSocket;
    try {
      this.outputStream = this.clientSocket.getOutputStream();
      this.inputStream = this.clientSocket.getInputStream();
      this.reader = new BufferedReader(new InputStreamReader(this.inputStream));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Convenience Constructor for a Remote Strategy that already has a
   * BufferedReader attached to the clientSocket's InputStream.
   * @param clientSocket Socket to communicate with Client.
   * @param reader BufferedReader attached to the Socket's inputStream
   */
  RemoteStrategy(Socket clientSocket, BufferedReader reader) {
    this.clientSocket = clientSocket;
    this.reader = reader;
    try {
      this.outputStream = clientSocket.getOutputStream();
      this.inputStream = clientSocket.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public IAction getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {

    if (b.getStatus() == Status.PLACE) {
      return getPlace(b.getBoard(), workerIDs);
    }
    else {
      return getMoveBuild(b.getBoard(), workerIDs);

    }
  }

  @Override
  public void sendMessage(String message) {
    this.writeToOutStream(message);
  }

  /**
   * Updates the Client with the Take-Turn Message (JSON Array of the Board)
   * and then reads the next Giveup/Move and Win/MoveBuild action requested
   * by the Client Player.
   * Action is then converted into an IAction Object and returned.
   * @param b Copy of the Board State
   * @param workerIDs worker IDs of this Player
   * @return Action requested by this Client
   */
  private IAction getMoveBuild(Board b, ArrayList<Integer> workerIDs) {
    // Write Take-Turn Message (Board)
    writeToOutStream(b.asJSONArray());
    // Read In Stream for next Move or MoveBuild
    try {
      JsonNode action = ConfigReader.parse(readInStream()).get(0);

      // Give up
      //String, which represents the name of a player that is giving up;
      if (action.isTextual()) {
        return new GiveUpAction();
      }
      // Winning Move [Worker,EastWest,NorthSouth], which represents a winning move
      // or Move Build
      else {
        String workerName = action.get(0).asText();
        int workerID = workerIDs.get(0);
        for (int i : workerIDs) {
          if (b.getWorkerSquare(i).getWorkerName() == workerName) {
            workerID = i;
          }
        }
        IAction mb = Translator.convertJSONToAction(b.getWorkerSquare(workerID), action.toString());
        return mb;

      }

    } catch (IOException e) {
      // Malformed Input
      e.printStackTrace();

    }

    return null;
  }
  /**
   * Updates the Client with the Placement Message (Array of this Client's
   * currently placed Workers in the form of [[WorkerName, X, Y] ...]
   *
   * and then reads the next Place Action requested by the Client and converts
   * it into a Place Action object and returns the object.
   * @param b Copy of the Board State
   * @param workerIDs worker IDs of this Player
   * @return Action requested by this Client
   */
  private IAction getPlace(Board b, ArrayList<Integer> workerIDs) {
    // Write Placement Message
    ArrayList<Integer> allWorkerIDs = b.getWorkerIDs();
    ArrayList<Square> workerSquares = new ArrayList<>();
    for (Integer id : allWorkerIDs) {
      workerSquares.add(b.getWorkerSquare(id));
    }
    writeToOutStream(Translator.placementMessage(workerSquares));
    // Read In Stream for Placement
    // Build Place Action
    Action placeAction = null;
    try {
      String request = readInStream();
      ArrayList<JsonNode> place = ConfigReader.parse(request);
      int x = place.get(0).get(0).asInt();
      int y = place.get(0).get(1).asInt();
      placeAction = new Action(Status.PLACE, Board.INVALID_WORKER_ID, x, y, null);
    } catch (IOException e ){
      e.printStackTrace();

    }
    return placeAction;
  }

  /**
   * Writes Message to this Strategy's connected Client
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

  /**
   * Reads the next full JSON String from the Client.
   * @return JSON String
   */
  private String readInStream() {
    StringBuilder messageBuilder = new StringBuilder();
    try {
      // Read Input from Client
      String registerMessage = this.reader.readLine();
      messageBuilder.append(registerMessage);
      while (!Translator.isValidJSON(messageBuilder.toString())) {
        registerMessage = this.reader.readLine();
        messageBuilder.append(registerMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return messageBuilder.toString();
  }
}
