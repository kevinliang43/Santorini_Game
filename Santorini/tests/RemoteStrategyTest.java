import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RemoteStrategyTest {
  Socket serverSocket;
  Socket clientSocket;
  InputStream clientInputStream;
  OutputStream clientOutputStream;



  @Before
  public void init() {
    try {
//      Thread serverAccept = new Thread() {
//        @Override
//        public void run() {
//          try {
//            // Open up mock server Socket for accepting
//            Socket serverSocket = new Socket("localhost", 4444);
//            //ServerSocket acceptServerSocket = new ServerSocket("localhost", 4444);
//            //acceptServerSocket.setReuseAddress(true);
//            //acceptServerSocket.bind(new InetSocketAddress(4444));
//            serverSocket = acceptServerSocket.accept();
//          } catch (IOException e) {
//            e.printStackTrace();
//          }
//        }
//      };
//
//      serverAccept.start();
//
//      SocketAddress address = new InetSocketAddress(4444);
//      // Create and connect client socket to test server
//      this.clientSocket = new Socket();
//      this.clientSocket.connect(address);
      this.clientSocket = new Socket("localhost", 4444);

      this.clientInputStream = this.clientSocket.getInputStream();
      this.clientOutputStream = this.clientSocket.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

//  @Test
//  public void testGetNextAction() {
//    this.init();
//
//  }

}
