package client;

import com.google.gson.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Parser for JsonArrays that are handed to the client to be parsed into actions on a Sheet
 */
public class ArrayParser {

  private Gson gson = new Gson();
  //private JsonArray array;

  private boolean debug = false;

  private HashMap<String, Object> sheetMap;

  private SheetLoader sheetLoader;

  public ArrayParser() throws ClassNotFoundException {
    this.sheetLoader = new SheetLoader();
    this.sheetMap = new HashMap<String, Object>();
  }

  /**
   * Parse the JsonArray into Sheet components
   * @param array   JsonArray to parse
   */
  public void parse(JsonArray array) throws ClassNotFoundException, NoSuchMethodException,
          InstantiationException, IllegalAccessException, InvocationTargetException {
    //debug
    String message = "Not a valid request.";

    if (array.size() < 2) {
      message = "Valid Sheet actions must contain a request and a sheet name.";
      if (debug) {System.out.println(message);}
      return;
    }

    String sheetName = gson.fromJson(array.get(1), String.class);
    switch (gson.fromJson(array.get(0), String.class)) {
      case "sheet":
        //["sheet", name:string, [[JF, ...] ...]]
        if (array.size() == 3) {
          message = createSheet(sheetName, gson.fromJson(array.get(2), JsonArray.class));
        } else {
          message = "Usage of the 'sheet' command is [\"sheet\", name:string, [[JF, ...] ...]]";
        }
        break;
      case "set":
        if (!sheetMap.containsKey(sheetName)) {
          message = "Sheet " + sheetName + " not found.";
          break;
        }
        //["set" , name:string, x:N, y:N, JF]
        if (array.size() == 5) {
          message = writeToCell(sheetName,
                  gson.fromJson(array.get(2), int.class),
                  gson.fromJson(array.get(3), int.class),
                  gson.fromJson(array.get(4), JsonElement.class));
        } else {
          message = "Usage of the 'set' command is [\"set\" , name:string, x:N, y:N, JF]";
        }
        break;
      case "at":
        if (!sheetMap.containsKey(sheetName)) {
          message = "Sheet " + sheetName + " not found.";
          break;
        }
        //["at", name:string, x:N, y:N]
        if (array.size() == 4) {
          message = atCell(sheetName,
                  gson.fromJson(array.get(2), int.class),
                  gson.fromJson(array.get(3), int.class));
          if (!debug) {System.out.println(message);}  //Print 'at' requests only once, whether debug is on or off
        } else {
          message = "Usage of the 'at' command is [\"at\", name:string, x:N, y:N]";
        }
        break;
      default:  //message is still "Not a valid request."
        break;
    }
    if (debug) {System.out.println(message);}
  }

  /**
   * Create a Sheet object from the dynamically loaded class
   * @param sheetName   Name of the Sheet being created
   * @param cellMatrix  Contents of the Sheet
   * @return            String message describing success or failure
   * @throws ClassNotFoundException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  private String createSheet(String sheetName, JsonArray cellMatrix) throws ClassNotFoundException,
          NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    int y = cellMatrix.size();
    int x = gson.fromJson(cellMatrix.get(0), JsonArray.class).size();

    //Create a Sheet
    Object sheet = sheetLoader.invokeSheetConstructor(x, y);
    JsonArray row;
    for (int j = 0; j < y; j++) {
      row = gson.fromJson(cellMatrix.get(j), JsonArray.class);
      if (row.size() != x) {
        return "Cannot create sheet: input not rectangular.";
      }
      for (int i = 0; i < x; i++) {
        if (typeOfJF(row.get(i)) == JF.NA) {
          return "Element at (" + i + "," + j + ") is ot a valid JF.";
        }
        setCell(sheet, i, j, row.get(i));
      }
    }

    sheetMap.put(sheetName, sheet);
    return "Sheet " + sheetName + " created";
  }

  /**
   * Write to one specific cell in an existing sheet.
   * @param sheetName   Name of the Sheet
   * @param x           x location of the cell to alter
   * @param y           y location of the cell to alter
   * @param je          new value for the cell
   * @return            String message describing success or failure
   */
  private String writeToCell(String sheetName, int x, int y, JsonElement je) throws ClassNotFoundException,
          NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    if (setCell(sheetMap.get(sheetName), x, y, je)) {
      return "Cell (" + x + "," + y + ") set in Sheet " + sheetName;
    } else {
      return "Not a valid JF.";
    }
  }

  /**
   * Get the value of a specific cell in an existing sheet.
   * @param sheetName   Name of the Sheet being accessed
   * @param x           x location of the cell to access
   * @param y           y location of the cell to access
   * @return            String message describing success or failure
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  private String atCell(String sheetName, int x, int y) throws NoSuchMethodException,
          IllegalAccessException, InvocationTargetException {
    if (sheetMap.containsKey(sheetName)){
      return sheetLoader.getValue(sheetMap.get(sheetName), x, y);
    }
    return "false";
  }

  private JF typeOfJF(JsonElement je) {
    if (je.isJsonPrimitive() && je.getAsJsonPrimitive().isNumber()) {
      return JF.CONSTANT;
    }

    if (je.isJsonArray()) {
     JsonArray jArray = je.getAsJsonArray();
     if (jArray.size() != 3) {
       return JF.NA;
     }
     if (jArray.get(0).isJsonPrimitive() && jArray.get(0).getAsString().equals(">")) {
       try {
         gson.fromJson(jArray.get(1), int.class);
         gson.fromJson(jArray.get(2), int.class);
       } catch (IllegalStateException e) {
         return JF.NA;
       }
       return JF.REFERENCE;

     } else if (jArray.get(1).isJsonPrimitive()
             && (jArray.get(1).getAsJsonPrimitive().getAsString().equals("+")
             || jArray.get(1).getAsJsonPrimitive().getAsString().equals("*"))){
       // index 0 and 2 must be valid JFs in a Function
       if (typeOfJF(jArray.get(0)) != JF.NA && typeOfJF(jArray.get(2)) != JF.NA) {
         return JF.FUNCTION;
       }
     } else {
       // index 0 and 1 aren't JsonPrimatives, or index 1 is not a valid operation; Valid JF not possible.
       return JF.NA;
     }
    }

    // Whatever this is, it's not a JF
    return JF.NA;
  }

  /**
   * Set a specified cell in a specified sheet to a specified value.
   * @param o   Sheet to alter
   * @param x   x location of the cell to alter
   * @param y   y location of the cell to alter
   * @param je  new value for the cell
   * @return    true if cell altered, false if not
   */
  private boolean setCell(Object o, int x, int y, JsonElement je) throws ClassNotFoundException,
          NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    JF jf = typeOfJF(je);
    if (jf == JF.NA) {
      return false;
    }

    Object v = null;
    if (jf == JF.CONSTANT) {
      v = sheetLoader.invokeConstantConstructor(gson.fromJson(je, double.class));
    }

    else if (jf == JF.REFERENCE) {
      v = sheetLoader.invokeReferenceConstructor(gson.fromJson(je.getAsJsonArray().get(1), int.class),
              gson.fromJson(je.getAsJsonArray().get(2), int.class));
    }

    else if (jf == JF.FUNCTION) {
      Object iv1 = parseIValue(je.getAsJsonArray().get(0));
      Object iv2 = parseIValue(je.getAsJsonArray().get(2));

      Object op = parseOperation(je.getAsJsonArray().get(1));

      if (iv1 != null && iv2 !=null && op != null){
        v = sheetLoader.invokeFunctionConstructor(iv1, iv2, op);
      }
    }
    if (v == null) {
      return false;
    }
    sheetLoader.setCell(o, x, y, v);
    return true;
  }

  private Object parseIValue(JsonElement je) throws ClassNotFoundException, NoSuchMethodException,
          InvocationTargetException, InstantiationException, IllegalAccessException {
    JF jf = typeOfJF(je);
    if (jf == JF.NA) {
      return null;
    } else if (jf == JF.CONSTANT) {
      return sheetLoader.invokeConstantConstructor(gson.fromJson(je, double.class));
    } else if (jf == JF.REFERENCE) {
      return sheetLoader.invokeReferenceConstructor(gson.fromJson(je.getAsJsonArray().get(1), int.class),
              gson.fromJson(je.getAsJsonArray().get(2), int.class));
    } else if (jf == JF.FUNCTION) {
      Object iv1 = parseIValue(je.getAsJsonArray().get(0));
      Object iv2 = parseIValue(je.getAsJsonArray().get(2));

      Object op = parseOperation(je.getAsJsonArray().get(1));

      if (iv1 != null && iv2 !=null && op != null){
        return sheetLoader.invokeFunctionConstructor(iv1, iv2, op);
      }
      return null;
    }
    return null;
  }

  private Object parseOperation(JsonElement je) throws ClassNotFoundException, NoSuchMethodException,
          InvocationTargetException, InstantiationException, IllegalAccessException {
    String op = gson.fromJson(je, String.class);
    if (op.equals("+")){
      return sheetLoader.invokeAdditionConstructor();
    } else if (op.equals("*")){
      return sheetLoader.invokeMultiplicationConstructor();
    } else {
      return null;
    }
  }
}
