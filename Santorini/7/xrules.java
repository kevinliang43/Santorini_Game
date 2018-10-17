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

public class xrules {
  TestPlayer player1, player2;
  int lastWorkerID;

  Board b = new Board();

  private int previousID;

  // Direction representations
  private enum DIRECTION {
    NORTH(-1), SOUTH(1), EAST(1), WEST(-1), PUT(0);

    int v;
    DIRECTION(int v) {
      this.v = v;
    }
  }

  public static void main(String[] args) throws IOException {
    xrules x = new xrules();
    boolean isLegal = true;
    int state = 0;

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
            if (state == 1) {
              printLegal(isLegal);
            }
            state = 0;
            isLegal = true;
            x.doBoard(command);
          } else {
            // Check the command
            switch (type.getAsString()) {
              case ("move"):
                state = 1;
                isLegal = x.doMove(command);
                break;
              case ("+build"):
                state = 2;
                if (isLegal) {
                  isLegal = x.doBuild(command);
                }
                printLegal(isLegal);
                break;
              default:
                // System.out.println("Ignoring: " + command.toString());
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

    if (state == 1) {
      printLegal(isLegal);
    }
  }

  private void doBoard(JsonArray command) {
    b = new Board();
    player1 = null;
    player2 = null;
    lastWorkerID = -1;
    for (int i = 0; i < command.size(); i++) {
      JsonArray row = command.get(i).getAsJsonArray();
      for (int j = 0; j < row.size(); j++) {
        String cell = row.get(j).getAsString();
        b.setFloor(j, i, Character.getNumericValue(cell.charAt(0)));

        // System.out.println(j + ", " + i);

        // Case where Cell is a BuildingWorker
        if (cell.length() != 1) {
          int id = b.placeWorker(j, i);
          // System.out.println("NEW WORKER");

          if (player1 == null) {
            player1 = new TestPlayer(cell.substring(1, cell.length()-1));
            player1.addWorker(id);
          } else if (player1.playerName.equals(cell.substring(1, cell.length()-1))) {
            player1.addWorker(id);
          } else if (player2 == null) {
            player2 = new TestPlayer(cell.substring(1, cell.length()-1));
            player2.addWorker(id);
          } else if (player2.playerName.equals(cell.substring(1, cell.length()-1))) {
            player2.addWorker(id);
          }
        }
      }
    }
  }

  private boolean doMove(JsonArray command) {
    Square s = getWorkerSquare(command);

    int x = s.getX();
    int y = s.getY();

    DIRECTION[] d = getDirection(command);

    // System.out.println(command);
    // System.out.println(s.getWorkerID());
    // System.out.println(x + ", " + y);
    // System.out.println((x + d[0].v) + ", " + (y + d[1].v));
    boolean legal = RuleChecker.isMoveLegal(b, getBoardID(command), x + d[0].v, y + d[1].v);

    if (legal) {
      b.moveWorker(getBoardID(command), x + d[0].v, y + d[1].v);
      // System.out.println("Move was legal!");
    }

    return legal;
  }

  private boolean doBuild(JsonArray command) {
    Square s = b.getWorkerSquare(lastWorkerID);
    int x = s.getX();
    int y = s.getY();

    DIRECTION[] d = getDirectionFromArray(command.get(1).getAsJsonArray());

    return RuleChecker.isBuildLegal(b, lastWorkerID, x + d[0].v, y + d[1].v);
  }


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

  private String workerStringToPlayerName(String s) {
    int size = s.length();
    if (size == 1) {
      return "";
    } else {
      return s.substring(0, size - 1);
    }
  }

  private int workerStringToWorkerInt(String s) {
    int size = s.length();
    return Character.getNumericValue(s.charAt(size - 1));
  }

  private DIRECTION[] getDirectionFromArray(JsonArray jsonArray) {
    DIRECTION[] d = new DIRECTION[2];
    String ew = jsonArray.get(0).getAsString();
    String ns = jsonArray.get(1).getAsString();
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

  private DIRECTION[] getDirection(JsonArray command) {
    JsonArray dirCom = command.get(2).getAsJsonArray();
    return getDirectionFromArray(dirCom);
  }

  // Documentation...
  private int getBoardID(JsonArray command) {
    String playerName = workerStringToPlayerName(command.get(1).getAsString());
    // System.out.println(playerName);
    int id = workerStringToWorkerInt(command.get(1).getAsString());
    int boardID;
    if (player1.playerName.equals(playerName)) {
      boardID = player1.workerIDs[id - 1];
    } else if (player2.playerName.equals(playerName)) {
      boardID = player2.workerIDs[id - 1];
    } else {
      // This should never happen
      throw new IllegalArgumentException("Invalid command: " + playerName);
    }

    return boardID;
  }

  private Square getWorkerSquare(JsonArray command) {
    int boardID = getBoardID(command);
    lastWorkerID = boardID;

    return b.getWorkerSquare(boardID);
  }

  private static void printLegal(boolean legal) {
    if (legal) {
      System.out.println("\"yes\"");
    } else {
      System.out.println("\"no\"");
    }
  }
}


