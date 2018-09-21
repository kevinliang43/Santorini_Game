import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by KevinLiang on 9/20/18.
 */
public class Client implements IClient {

  private Socket socket;


  /**
   * Constructor for the Client Class
   */
  Client() {
    this.socket = null;
  }

  /**
   * Get this Client's Socket
   * @return The socket this Client is connected to.
   */
  public Socket getSocket() {
    return socket;
  }

  /**
   * Connects to a given IP address and port number
   * @param portNumber Integer representing the Port in which to connect to.
   * @param address String representation of the IP Address to connect to.
   * @throws IOException Thrown when unable to connect to the given address.
   */
  @Override
  public void connect(int portNumber, String address) throws IOException {

      InetAddress inetAddress = InetAddress.getByName(address);
      Socket socket = new Socket(inetAddress, portNumber);

      this.socket = socket;

  }

  /**
   * Determines if this Client is still connected to the socket
   * @return Boolean representing the connection state of this client.
   */
  @Override
  public boolean isConnected() {
    return this.socket != null;
  }

  /**
   * Sends a given name to be signed up on the Connected Server
   * @param name String representation of a name to be sent to Server
   * @throws IOException Thrown when socket outputstream is unable to be written to.
   */
  @Override
  public void signupName(String name) throws IOException{
    byte[] nameBytes = name.getBytes();
    this.socket.getOutputStream().write(nameBytes);

  }

  /**
   * Receives a Name from the connected Server
   * @return A String from the outputStream of the connected server.
   * @throws IOException Thrown when the client cannot read from the socket inputStream.
   */
  @Override
  public String recieveName() throws IOException{
    BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    String line;
    line = input.readLine();
    return line;

  }

  /**
   * Sends a Batch of Requests to the connected Server
   * @param requests an Array List of String that contains the string representation of one or
   *                 Multiple requests.
   * @return An Integer representing the validity of the requests.
   *         -666 will be returned if the Server deems the requests invalid.
   * @throws IOException Thrown when socket output stream into the Server cannot be written to.
   */
  @Override
  public int sendBatch(ArrayList<String> requests) throws IOException {
    String json = "[";
    //adds each string in the batch into a json-formatted array
    for(int i = 0; i < requests.size() - 1; i ++) {
      json += (requests.get(i).toString() + ",");
    }
    json += requests.get(requests.size()-1) + "]";
    // Write to Server
    this.socket.getOutputStream().write(json.getBytes());
    // Get Response back from server
    BufferedReader response = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    return Integer.parseInt(response.readLine());


  }

  /**
   * Takes JSON Input from Command Line. Generates a List of well formed JSON Requests and returns it
   * @return List of well-formed JSON Request Strings
   * @throws IOException Error in taking in STDIN
   */
  @Override
  public ArrayList<String> takeInput() throws IOException{

    ArrayList<String> batch = new ArrayList<>();
    InputStreamReader reader = new InputStreamReader(System.in);


    // Parse JSON string
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<JsonNode> requestList = new ArrayList<>();

    BufferedReader in = new BufferedReader(reader);
    String line;
    String acc = "";

    //while the parser has more JSONNodes,
    //check if each node is valid and add it to the list of requests
    while ((line = in.readLine()) != null) {
      JsonNode currentNode = null;
      line = acc + line;

      try {
        currentNode = objectMapper.readTree(line);
      } catch (Exception e) {
        acc = line;
      }

      if (currentNode != null) {
        try {
          if (checkValidLine(currentNode)) {
            requestList.add(currentNode);
            if (isProperAtRequest((ArrayNode) currentNode)) {
              break;
            }
          }

        } catch (NullPointerException e) {
          // Do nothing
        }
      }
    }
    for(JsonNode node : requestList) {
      batch.add(node.toString());
    }
    return batch;

  }

  //given a json node, will check that it's of type array
  private boolean checkValidLine(JsonNode node) {
    if (!node.isArray()) {
      return false;
    } else {
      ArrayNode jsonNodes = (ArrayNode) node;

      String s = jsonNodes.get(0).toString();
      switch (s) {
        case "\"sheet\"":
          return isProperSheetRequest(jsonNodes);
        case "\"set\"":
          return isProperSetRequest(jsonNodes);
        case "\"at\"":
          return isProperAtRequest(jsonNodes);
        default:
          return false;
      }
    }
  }

  //["sheet", name:string, [[JF, ...] ...]]
  //must check if the array in the third position forms a rectangle and all > formulas point to valid cells
  private boolean isProperSheetRequest(ArrayNode node) {
    if(node.size() != 3) {
      return false;
    }
    else if(!node.get(1).isTextual()) {
      return false;
    }
    else if(node.get(2).isArray()) {
      ArrayNode formulas = (ArrayNode)(node.get(2));
      return (areValidRectangularFormulas(formulas));
    }
    else {
      return true;
    }
  }

  //["at", name:string, x:N, y:N]
  //checks if this is a valid at request
  //this expects an evaluation from the server
  //checking if the location is valid will be done in the server
  private boolean isProperAtRequest(ArrayNode node) {
    if (node.size() != 4) {
      return false;
    }
    else {
      return node.get(1).isTextual() && node.get(2).isInt() && node.get(3).isInt();
    }
  }

  //["set" , name:string, x:N, y:N, JF]
  //checks if valid set request, checking if the location is valid will be done in the server
  private boolean isProperSetRequest(ArrayNode node) {
    if (node.size() != 5) {
      return false;
    }
    else {
      return (node.get(1).isTextual() && node.get(2).isInt() && node.get(3).isInt() &&
              isValidJF(node.get(4)));
    }
  }
  //checks if valid formula, used when no sheet information is needed/known
  private boolean isValidJF(JsonNode formula) {
    //checking to see if it's a constant
    if (formula.isInt()) {
      return true;
    }
    //checking to see if it's an array
    else if (formula.isArray()) {
      //checks if the array is of the proper size
      if (formula.size() == 3) {
        ArrayNode form = (ArrayNode) formula;
        //checks if its a cell reference
        if (form.get(0).asText().equals(">")) {
          //checking if the cell references itself will be handled in the server
          return formula.get(1).isInt() && formula.get(2).isInt();
        }
        else {
          return (isValidJF(formula.get(0)) && isValidJF(formula.get(2))) &&
                  ((formula.get(1).asText().equals("\"*\"")) || (formula.get(1).asText().equals("+")));
        }
      }
    }
    return false;
  }

  //checks if valid arrangement of formulas and valid location in the arrangement of cells
  private boolean isValidJF(JsonNode formula, int numRows, int numCols) {
    boolean validLocation = false;
    if (formula.isArray()) {
      if ((formula.size() == 3) && (formula.get(0).asText().equals("\">\""))) {
        if (formula.get(1).isInt() && formula.get(2).isInt()) {
          validLocation = checkBounds(formula.get(1).asInt(), numRows) && checkBounds(formula.get(2).asInt(), numCols);
          return validLocation && isValidJF(formula);
        }
      }
    }
    return isValidJF(formula);


  }

  //helper that checks to see if the coordinate falls within the bounds of the spreadsheet
  private boolean checkBounds(int coordinate, int bound) {
    return (coordinate < bound) && (coordinate >= 0);
  }

  //checks to see if the 2D array of formulas is valid
  private boolean areValidRectangularFormulas(ArrayNode formulas) {
    int numRows = formulas.size();
    int numCols = 0;
    //checking to see if the first element of the array is an array
    if (formulas.get(0).isArray()) {
      //sets the number of columns to the size of the first array
      //in a rectangular structure, all rows must have the same length
      numCols = ((ArrayNode)formulas.get(0)).size();
    }
    // if the first element of formulas is not an array, return false
    else {
      return false;
    }

    //checking that each row in the 2D array of formulas is valid
    for (JsonNode row : formulas) {
      //if the row is not an array return false
      if (!row.isArray()) {
        return false;
      }
      else {
        //row converted to an arrayNode
        ArrayNode arrRow = (ArrayNode)row;

        //if the row size doesn't match the first row size then it's not rectangular or
        //check that each formula refers to a cell in the bounds of this spreadsheet
        if (arrRow.size() != numCols) {
          return false;
        }
        //checks if each element in the array is a proper formula
        else {
          for (JsonNode node : arrRow) {
            if(!isValidJF(node, numRows, numCols)) {
              return false;
            }
          }
        }
      }
    } return true;
  }


}


