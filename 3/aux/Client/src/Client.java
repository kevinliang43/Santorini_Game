import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class Client {
    /*
    users can:
        request the creation of a spreadsheet
        give a name to a spreadsheet
        place a new formula into a cell of a specific spreadsheet
        ask for the value of a cell in a specific spreadsheet

        ["sheet", name:string, [[JF, ...] ...]]
        ["set" , name:string, x:N, y:N, JF]
        ["at", name:string, x:N, y:N]

        JF is one of:

          -- number

          -- [">", N, N]

          -- [JF , "+", JF]

          -- [JF , "*", JF]
     */

    public HashMap<String,Spreadsheet> sheets;
    Client() {
        this.sheets = new HashMap<>();
    }


    //takes in the valid user input and generates the spreadsheets
    //in order to manipulate or call from a spreadsheet, user must create the sheet first using the "sheet" input
    public void generateSpreadSheets(ArrayList<ArrayNode> nodes) {
        for(ArrayNode node : nodes) {
            String name = node.get(1).toString();
            switch(node.get(0).toString()) {
                //initializing the sheet
                case "\"sheet\"":
                    ArrayNode formulas = (ArrayNode)node.get(2);
                    int rows = formulas.size();
                    int cols = formulas.get(0).size();

                    //creates the new spreadsheet and adds it to the collection
                    this.sheets.put(name, new Spreadsheet(rows, cols));
                    //iterates through each row of formulas
                    for (int r = 0; r < rows; r++) {
                        ArrayNode row = (ArrayNode)formulas.get(r);
                        //iterates through each formula
                        for (int c = 0; c < cols; c++) {
                            //generates a formula given the JsonNode
                            IFormula f = generateFormula(row.get(c));
                            //adds the cell with it's position to the spreadsheet
                            sheets.get(name).addCell(new Cell(r, c, f));
                        }
                    }
                    break;
                //sets the given location to the given formula cell if the spreadsheet is created
                case "\"set\"":
                    int row = node.get(2).asInt();
                    int col = node.get(3).asInt();
                    IFormula f = generateFormula(node.get(4));
                    //if the user tries to add a formula to a non-existent sheet, the request is ignored and passed over
                    if(sheets.containsKey(name)){
                        sheets.get(name).addCell(new Cell(row, col, f));
                    }
                    break;
                //["at", name:string, x:N, y:N]
                case "\"at\"":
                    int r = node.get(2).asInt();
                    int c = node.get(3).asInt();
                    //prints the evaluated function to the console
                    //if the point is within the scope of the spreadsheet will be done in the server
                    System.out.print(sheets.get(name).getValueAt(new Point(r, c)));

            }
        }
    }

    //generates the formula object from the given json node
    //to access the function we are guaranteed to be one of these
    IFormula generateFormula(JsonNode formula) {
        if (formula.isInt()) {
            return new Constant(formula.asInt());
        }
        else {
            ArrayNode formulaPieces = (ArrayNode)formula;
            //if its a cell reference
            if(formulaPieces.get(0).asText().equals("\">\"")) {
                return new cellReference(new Point(formulaPieces.get(1).asInt(), formulaPieces.get(2).asInt()));
            }
            else {
                //if its an addition formula
                if (formula.get(1).asText().equals("\"+\"")) {
                    return new formula(generateFormula(formula.get(0)), generateFormula(formula.get(2)), true);
                }
                else {
                    //if its a multiply formula
                    return new formula(generateFormula(formula.get(0)), generateFormula(formula.get(2)), false);
                }
            }
        }
    }

    //parses the input to an array list of JsonNodes and each of those is a JSON Array
    public ArrayList<JsonNode> parseOuterJson(String json) throws IOException {

        ObjectMapper objMapper = new ObjectMapper();
        JsonFactory factory = objMapper.getFactory();
        JsonParser parser = factory.createParser(json);
        ArrayList<JsonNode> nodes = new ArrayList<>();

        while (!parser.isClosed()) {
            JsonNode currentNode = objMapper.readTree(parser);
            if (currentNode != null) {
                nodes.add(currentNode);
            }
        }

        return nodes;
    }

    //given a json node, will check that it's of type array
    public boolean checkValidLine(JsonNode node) {
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
    public boolean isProperSheetRequest(ArrayNode node) {
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
    public boolean isProperAtRequest(ArrayNode node) {
        if (node.size() != 4) {
            return false;
        }
        else {
            return node.get(1).isTextual() && node.get(2).isInt() && node.get(3).isInt();
        }
    }

    //["set" , name:string, x:N, y:N, JF]
    //checks if valid set request, checking if the location is valid will be done in the server
    public boolean isProperSetRequest(ArrayNode node) {
        if (node.size() != 5) {
            return false;
        }
        else {
            return (node.get(1).isTextual() && node.get(2).isInt() && node.get(3).isInt() &&
                    isValidJF(node.get(4)));
        }
    }
    //checks if valid formula, used when no sheet information is needed/known
    public boolean isValidJF(JsonNode formula) {
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
    public boolean isValidJF(JsonNode formula, int numRows, int numCols) {
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
    public boolean checkBounds(int coordinate, int bound) {
        return (coordinate < bound) && (coordinate >= 0);
    }

    //checks to see if the 2D array of formulas is valid
    public boolean areValidRectangularFormulas(ArrayNode formulas) {
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
