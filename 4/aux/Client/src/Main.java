import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by KevinLiang on 9/20/18.
 */
public class Main {

  static final int PORT_NUMBER = 8000;

  public static void main(String[] args) {

    if (args.length != 1) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }

    // Initialize Client
    Client client = new Client();

    // Connect Client
    while (!client.isConnected()) {
      try {
        client.connect(PORT_NUMBER, args[0]);
        System.out.println("Successful Connection to: " + args[0]);
        System.out.println("Connection at port: " + PORT_NUMBER);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

//    // Get name from STDIN
//    // Name is valid if the string length is > 2
//    Scanner scanner = new Scanner(System.in);
//    String signupName = "";
//    while (signupName.length() < 2) {
//      System.out.println("Please enter a Valid Name: ");
//      signupName = scanner.nextLine();
//    }
//
//
//
//
//    // Signup Name
//    try {
//      client.signupName(signupName);
//    } catch (IOException e) {
//      System.err.println("Unable to sign up name. Please check your connection.");
//      e.printStackTrace();
//    }
//    String name = "";
//
//
//    // Receive Name to check for successful signup
//    try {
//      name = client.recieveName();
//    } catch (IOException e) {
//      System.err.println("Unable to Receive Name from server. Please check your connection.");
//      e.printStackTrace();
//    }
//
//    System.out.println(name);


    while (true) {
      try {

        System.out.println("Please enter requests: ");

        ArrayList<String> requests = client.takeInput();
        System.out.println(client.sendBatch(requests));

      } catch (IOException e) {
        System.err.println("Unable to parse JSON input");
      }
    }




  }

}
