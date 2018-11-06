import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/**
 * Test Harness for Tournament Manager
 */
public class xrun {

  public static void main(String args[]) {
    // Create a new Tournament
    TournamentManager tournamentManager = new TournamentManager(2, 32, 2000, 1, 3);
    tournamentManager.main();
  }




}
