/**
 * Created by KevinLiang on 9/20/18.
 */

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Client Interface
 */
public interface IClient {

  void connect(int portNumber, String address) throws IOException;

  boolean isConnected();

  void signupName(String name) throws IOException;

  String recieveName() throws IOException;

  int sendBatch(ArrayList<String> requests) throws IOException;

  ArrayList<String> takeInput() throws IOException;

}
