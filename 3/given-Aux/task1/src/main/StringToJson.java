package main;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class StringToJson {

  // Converts a string, which is a series of well-formed JSON values, to an ArrayList of JsonElements
  // Throws IOException in the case of a non well-formed JSON value
  public static ArrayList<JsonElement> convert(String json) throws IOException {
    // JSON
    // Create JsonReader for parsing the input string
    JsonReader r = new JsonReader(new StringReader(json));

    // Set as lenient to allow reading multiple JSON values in a row
    r.setLenient(true);

    // JsonParser for getting well-formatted strings
    JsonParser parser = new JsonParser();

    // Holds each JSON value
    ArrayList<JsonElement> elements = new ArrayList<>();

    // Read each JSON value until the END_DOCUMENT (EOF) token
    while (r.peek() != JsonToken.END_DOCUMENT) {
      elements.add(parser.parse(r));
    }

    return elements;
  }
}
