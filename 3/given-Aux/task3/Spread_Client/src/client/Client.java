package client;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * Client component for Spreadsheets
// * @param <T> Dynamically loaded Sheet class
 */
public class Client {

  private ArrayParser ap;

  // Expected locations of classes
  public static final String SHEET_CLASS = "spreadsheet.Sheet";
  public static final String IVALUE_CLASS = "spreadsheet.IValue";
  public static final String CONSTANT_CLASS = "spreadsheet.Constant";
  public static final String REFERENCE_CLASS = "spreadsheet.Reference";
  public static final String FUNCTION_CLASS = "spreadsheet.Function";
  public static final String IOPERATION_CLASS = "spreadsheet.IOperation";
  public static final String ADDITION_CLASS = "spreadsheet.Addition";
  public static final String MULTIPLICATION_CLASS = "spreadsheet.Multiplication";

  private static SheetLoader sheetLoader;


  public Client() throws ClassNotFoundException {
    //clientSheetMap = new HashMap<String, Object>();
    //ap = new ArrayParser(clientSheetMap);
    ap = new ArrayParser();
    //sheetLoader = new SheetLoader(SHEET_CLASS);
    sheetLoader = new SheetLoader();
  }

  public static void main(String[] args) throws ClassNotFoundException, IOException,
          InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {

    Client client = new Client();

    //Gson gson = new Gson();

    // Scanner for reading STDIN
    Scanner in = new Scanner(System.in);

    // Create JsonReader for parsing the input string
    JsonReader r = new JsonReader(new StringReader(in.toString()));
    // Set as lenient to allow reading multiple JSON values in a row
    r.setLenient(true);
    // JsonParser for getting well-formatted strings
    JsonParser parser = new JsonParser();

    // Read until EOF
    while(r.peek() != JsonToken.END_DOCUMENT) {

      while(in.hasNext()) {
        r = new JsonReader(new StringReader(in.nextLine()));
        //in.nextLine();
        JsonArray array = parser.parse(r).getAsJsonArray();

        // Hand to the parser
        client.ap.parse(array);
      }
    }
  }
}
