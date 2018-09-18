import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // Creates a Client instance
        Client client = new Client();
        // Get STDIN
        Scanner scanner = new Scanner(System.in);
        String json = "";

        // Take in JSON input
        while(scanner.hasNext()) {
            json += scanner.nextLine() + "\n";
        }

        // Parse input and check for Validity
        ArrayList<JsonNode> nodes = client.parseOuterJson(json);
        ArrayList<ArrayNode> validNodes = new ArrayList<>();
        for (JsonNode node : nodes) {
            if (client.checkValidLine(node)) {
                //for testing purposes print valid things
                //System.out.println(client.checkValidLine(node));
                validNodes.add((ArrayNode)node);
            }
        }

        // Takes User input and creates a Hashmap of Spreadsheets.
        client.generateSpreadSheets(validNodes);
    }
}
