// Class for running system-level tests on Board. Takes JSON input on STDIN and prints results
// to STDOUT.

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

public class xboard {

  // Board instance to test on
  private Board b;

  // Objects for holding basic player data
  TestPlayer player1, player2;

  // Main method that starts reading JSON input, and doing commands
  public static void main(String[] args) {
    xboard x = new xboard();
    try {
      x.readJson();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Direction representations
  private enum DIRECTION {
    NORTH(-1), SOUTH(1), EAST(1), WEST(-1), PUT(0);

    int v;
    DIRECTION(int v) {
      this.v = v;
    }
  }

  // Reads JSON input until EOF
  public void readJson() throws IOException {
    // Scanner for reading STDIN
    Scanner in = new Scanner(System.in);

    // JsonParser for getting well-formatted strings
    JsonParser parser = new JsonParser();

    // Builder for input
    StringBuilder s = new StringBuilder();

    // Read until EOF
    while (in.hasNextLine()) {
      // Keep appending to StringBuilder
      s.append(in.nextLine());

      // Parse the current input string
      JsonReader read = new JsonReader(new StringReader(s.toString()));

      // Set as lenient to avoid exceptions when a malformed JSON is read
      read.setLenient(true);

      try {
        // Add all valid JSON values to list
        ArrayList<JsonElement> elements = new ArrayList<>();
        while (read.peek() != JsonToken.END_DOCUMENT) {
          elements.add(parser.parse(read));
        }

        // Reset the input string when all the JSON is valid.
        // If JSON is not valid, will wait for complete valid JSON before processing
        s = new StringBuilder();

        // Check each input JSON
        for (JsonElement e : elements) {

          // Get the element as a JSON array
          JsonArray command = e.getAsJsonArray();

          JsonElement type = command.get(0);

          if (type.isJsonArray()) {
            doBoard(command);
          } else {
            // Check the command
            switch (type.getAsString()) {
              case ("move"):
                doMove(command);
                break;
              case ("build"):
                doBuild(command);
                break;
              case ("neighbors"):
                doNeighbor(command);
                break;
              case ("occupied?"):
                doOccupied(command);
                break;
              case ("height"):
                doHeight(command);
                break;
              default:
                System.out.println("Ignoring: " + command.toString());
                break;
            }
          }
        }
      } catch (JsonSyntaxException e) {
        continue;
      } catch (IOException e) {
        break;
      }
    }
  }

  /**
   * Performs a "height" command. Gets the height at a specified position
   * @param command The JSON array of the height command
   */
  private void doHeight(JsonArray command) {
    // Get the square the worker is on
    ImmutableSquare s = getWorkerSquare(command);

    // Extract the two direction values out of the command
    DIRECTION[] d = getDirection(command);

    // Print the floor height at worker position + direction
    System.out.println(b.getFloorHeight(s.getX() + d[0].v, s.getY() + d[1].v));
  }

  /**
   * Performs an "occupied?" command. Prints if the cell is occupied by a worker.
   * @param command Full JSON "occupied" command
   */
  private void doOccupied(JsonArray command) {
    // Get the square the worker is on
    ImmutableSquare s = getWorkerSquare(command);

    // Extract the two directions from the command
    DIRECTION[] d = getDirection(command);

    // If the cell is occupied, print "yes" otherwise "no"
    if (b.isOccupied(s.getX() + d[0].v, s.getY() + d[1].v)) {
      System.out.println("yes");
    } else {
      System.out.println("no");
    }
  }

  /**
   * Performs a "neighbors" command. Prints if there is a cell in the given position.
   * @param command Full "neighbors" JSON command
   */
  private void doNeighbor(JsonArray command) {
    // Get the square the worker is on
    ImmutableSquare s = getWorkerSquare(command);

    // Extract the two directions from the command
    DIRECTION[] d = getDirection(command);

    // Get the neighbor cell position
    int neighX = s.getX() + d[0].v;
    int neighY = s.getY() + d[1].v;

    if (b.isXYOnBoard(neighX, neighY)) {
      System.out.println("yes");
    } else {
      System.out.println("no");
    }
  }

  /**
   * Performs a "build" command. Increases building height by 1 on the given cell.
   * @param command Full "build" JSON command
   */
  private void doBuild(JsonArray command) {
    // Get the square the worker is on
    ImmutableSquare s = getWorkerSquare(command);

    int x = s.getX();
    int y = s.getY();

    // Extract the two directions from the command
    DIRECTION[] d = getDirection(command);

    // Build a floor on the correct cell
    b.buildFloor(x + d[0].v, y + d[1].v);
    System.out.println("[]");
  }

  /**
   * Performs a "move" command. Moves a worker in the specified direction.
   * @param command Full "move" JSON command
   */
  private void doMove(JsonArray command) {
    // Gets the square the worker is on
    ImmutableSquare s = getWorkerSquare(command);

    int x = s.getX();
    int y = s.getY();

    // Extract the two directions from the command
    DIRECTION[] d = getDirection(command);

    // Move the worker in the specified direction
    b.moveWorker(getBoardWorkerID(command), x + d[0].v, y + d[1].v);
    System.out.println("[]");
  }

  /**
   * Performs a command for creating the Board. Only performed once per sequence of commands.
   * @param command Full command for creating the Board
   */
  private void doBoard(JsonArray command) {
    // Create a Board instance to act on
    b = new Board();

    // Set to null to allow checking if they have been created later
    player1 = null;
    player2 = null;

    // Iterate over each row in the command
    for (int i = 0; i < command.size(); i++) {
      JsonArray row = command.get(i).getAsJsonArray();

      // Iterate over each column in the row
      for (int j = 0; j < row.size(); j++) {

        // Extract the command at the current position
        String cell = row.get(j).getAsString();

        // The first value is always the floor height
        b.setFloor(j, i, Character.getNumericValue(cell.charAt(0)));

        // Case where Cell is a BuildingWorker
        if (cell.length() != 1) {
          // Create a TestWorker to simplify data parsing
          TestWorker worker = new TestWorker(j, i, cell.substring(1));
          int id = b.placeWorker(worker.x, worker.y);

          // Add the worker id to the correct players list. Create a TestPlayer players are not
          // already created.
          if (player1 == null) {
            player1 = new TestPlayer(worker.playerName);
            player1.addWorker(id);
          } else if (player1.playerName.equals(worker.playerName)) {
            player1.addWorker(id);
          } else if (player2 == null) {
            player2 = new TestPlayer(worker.playerName);
            player2.addWorker(id);
          } else if (player2.playerName.equals(worker.playerName)) {
            player2.addWorker(id);
          }
        }
      }
    }
  }

  /**
   * Convenience class for maintaining player data
   */
  private class TestPlayer {
    int workerCount;
    String playerName;
    int[] workerIDs;

    TestPlayer(String name) {
      this.playerName = name;
      workerCount = 0;
      workerIDs = new int[2];
    }

    public void addWorker(int id) {
      this.workerIDs[workerCount] = id;
      this.workerCount++;
    }
  }

  /**
   * Convenience class for parsing worker data
   */
  private class TestWorker {
    int x, y;
    String playerName;
    int workerNum;

    TestWorker(int x, int y, String w) {
      this.x = x;
      this.y = y;

      int size = w.length();
      this.workerNum = Character.getNumericValue(w.charAt(size - 1));

      if (size == 1) {
        playerName = "";
      } else {
        playerName = w.substring(0, size - 1);
      }
    }
  }

  /**
   * Extract the player name from a Worker string. Worker is defined as a string of lowercase
   * letters that ends in either 1 or 2.
   * @param s The Worker string
   * @return The player name
   */
  private String workerStringToPlayerName(String s) {
    int size = s.length();
    if (size == 1) {
      return "";
    } else {
      return s.substring(0, size - 1);
    }
  }

  /**
   * Extracts the worker number from a Worker string.
   * @param s The Worker string
   * @return The worker number
   */
  private int workerStringToWorkerInt(String s) {
    int size = s.length();
    return Character.getNumericValue(s.charAt(size - 1));
  }


  /**
   * Extract direction from the command array. The array should be size = 3 and the direction
   * array is the 2nd item (zero indexed).
   * @param command The full JSON command
   * @return An array of two direction values. First one of EAST, WEST, or PUT. Second one of
   * NORTH, SOUTH, or PUT.
   */
  private DIRECTION[] getDirection(JsonArray command) {
    // Pull out the direction item, and each item in the direction array.
    JsonArray dirCom = command.get(2).getAsJsonArray();
    String ew = dirCom.get(0).getAsString();
    String ns = dirCom.get(1).getAsString();
    DIRECTION[] d = new DIRECTION[2];

    // Assign values based on string. If this needs to get used in other places a String->DIRECTION
    // method may be useful
    if (ew.equals("EAST")) {
      d[0] = DIRECTION.EAST;
    } else if (ew.equals("WEST")) {
      d[0] = DIRECTION.WEST;
    } else if (ew.equals("PUT")) {
      d[0] = DIRECTION.PUT;
    }
    if (ns.equals("NORTH")) {
      d[1] = DIRECTION.NORTH;
    } else if (ns.equals("SOUTH")) {
      d[1] = DIRECTION.SOUTH;
    } else if (ns.equals("PUT")) {
      d[1] = DIRECTION.PUT;
    }

    return d;
  }

  /**
   * Gets the Board's internal ID of the specified worker.
   * @param command JSON command that is acting on a worker.
   * @return The Board's internal worker ID for the worker.
   */
  private int getBoardWorkerID(JsonArray command) {
    // Extract player name
    String playerName = workerStringToPlayerName(command.get(1).getAsString());

    // Extract worker id in JSON
    int id = workerStringToWorkerInt(command.get(1).getAsString());
    int boardID;

    // Check who owns the player, and get the internal ID from the TestPlayer
    if (player1.playerName.equals(playerName)) {
      boardID = player1.workerIDs[id - 1];
    } else if (player2.playerName.equals(playerName)) {
      boardID = player2.workerIDs[id - 1];
    } else {
      // This represents invalid or non-well-formatted JSON
      throw new IllegalArgumentException("Invalid command");
    }

    return boardID;
  }

  /**
   * Get the ImmutableSquare the specified worker is on.
   * @param command Full JSON command where Worker is the 1st item of the array (zero indexed)
   * @return ImmutableSquare the Worker is currently on
   */
  private ImmutableSquare getWorkerSquare(JsonArray command) {
    int boardID = getBoardWorkerID(command);

    return b.getWorkerPosition(boardID);
  }
}
