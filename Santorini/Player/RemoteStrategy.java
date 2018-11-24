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

  /**
   * Constructor for Remote Strategy
   * @param clientSocket Socket to communicate with Client
   */
  RemoteStrategy(Socket clientSocket) {
    this.clientSocket = clientSocket;
    try {
      this.outputStream = this.clientSocket.getOutputStream();
      this.inputStream = this.clientSocket.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public IAction getNextAction(BoardStatus b, ArrayList<Integer> workerIDs) {
    // Setup Readers
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
    StringBuilder requestBuilder = new StringBuilder();
    String line = "";
    // Build Request from Client
    try {
      while ((line = bufferedReader.readLine()) != null) {
        requestBuilder.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
//    if (b.getStatus() == Status.PLACE) {
//
//    }
//    else {
//
//    }

  }
}
