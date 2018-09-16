import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by KevinLiang on 9/15/18.
 */
public class Main {

  private static int PORT_NUMBER = 8000;

  public static void main(String args[]) {


    // Initialize Variables
    ServerSocket serverSocket = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;

    // Builds new Server Socket with given port number
    try {
      serverSocket = new ServerSocket(PORT_NUMBER);
      serverSocket.setSoTimeout(10000);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Socket server = connectServer(serverSocket);

    // Get input and output streams
    try {
      inputStream = server.getInputStream();
      outputStream = server.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }



/* TEST ONLY
    String initialString = "[2,3]";
    InputStream inputStream = new ByteArrayInputStream(initialString.getBytes());
    */

    System.setIn(inputStream);

    // Call the JSON Parser
    try {
      JSONParse.main(new String[0]);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }








  }


  private static Socket connectServer(ServerSocket serverSocket) {

    while(true) {
      try {
        Socket server = serverSocket.accept();
        return server;
      } catch (IOException e) {
        System.err.println("IO Exception when trying to connect to Client.\n");
        break;
      }

    }

  }




}
