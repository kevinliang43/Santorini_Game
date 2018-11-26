import java.io.IOException;
import java.io.InputStream;
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

  ClientSideProxy(Player player, String ip, int port) {
    this.player = player;
    try {
      this.clientSocket = new Socket(ip, port);
      this.inputStream = clientSocket.getInputStream();
      this.outputStream = clientSocket.getOutputStream();
      //TEST
      String name = "\""+ this.player.getName() + "\"";
      this.outputStream.write(name.getBytes());
    }catch (IOException e) {
      e.printStackTrace();
    }




  }






}
