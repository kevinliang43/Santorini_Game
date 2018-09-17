import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
      System.out.println("Listening for client on port " + serverSocket.getLocalPort());
      serverSocket.setSoTimeout(10000);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Socket server = connectServer(serverSocket);
    System.out.println("Connection to Client at " + server.getRemoteSocketAddress() + " was successful.");


    // Get input and output streams
    try {
      inputStream = server.getInputStream();
      outputStream = server.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }


    // Set up input Stream
    System.setIn(inputStream);

    // Set up output Stream

    ByteArrayOutputStream jsonParserByteOutStream = new ByteArrayOutputStream();
    PrintStream jsonParserPrintStream = new PrintStream(jsonParserByteOutStream);
    System.setOut(jsonParserPrintStream);


    // Call the JSON Parser
    try {
      JSONParse.main(new String[0]);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // Send the JSON Parser output to client
    try {
      outputStream.write(jsonParserByteOutStream.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // Close the Server
    try {
      server.close();
    } catch (IOException e) {

    }


  }

  private static Socket connectServer(ServerSocket serverSocket) {
    Socket server = null;
    while(server == null) {
      try {
        server = serverSocket.accept();
      } catch (IOException e) {
        System.err.println("IO Exception when trying to connect to Client.\n");
        break;
      }
    }
    return server;
  }





}
