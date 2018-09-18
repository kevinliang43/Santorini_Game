package main;

import com.google.gson.JsonElement;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String args[]) throws IOException {
    ServerSocket server = new ServerSocket(8000);
    Socket client = server.accept();

    // Create StringBuilder for all input
    StringBuilder s = new StringBuilder();

    // Scanner for reading STDIN
    Scanner in = new Scanner(client.getInputStream());

    // Define EOF character (really just ^D)
    char EOF = 0x04;

    // Read until EOF
    while (in.hasNextLine()) {
      String line = in.nextLine();

      // Check for EOF. EOF has to be on its own line to be read
      if (line.indexOf(EOF) != -1) {
        break;
      }

      s.append(line);
    }

    ArrayList<JsonElement> elements = StringToJson.convert(s.toString());

    // Print in descending order
    DataOutputStream clientStream = new DataOutputStream(client.getOutputStream());
    for (int i = elements.size(); i > 0; i--) {
      clientStream.writeBytes("[" + Integer.toString(i - 1) + "," + elements.get(elements.size() - i) + "]\n");
    }

    client.close();
    server.close();
  }
}
