import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class For A Tournament Server.
 *
 *  A game server allows remote clients to sign up for a tournament.
 *  After waiting for a limited amount of time and signing up a certain minimal number of players,
 *  the server creates a tournament manager from the players and runs a complete tournament.
 */
public class Server {

  public static void main(String args[]) {
    JsonNode configNode = ConfigReader.readAndParse().get(0);
    ArrayList<String> fields = new ArrayList<>(Arrays.asList("min players", "port", "waiting for", "repeat"));
    if (!Translator.checkFields(configNode, fields)) {
      throw new IllegalArgumentException("Config File is incorrectly formatted.\n");
    }


  }
}
