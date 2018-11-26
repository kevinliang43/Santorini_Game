import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Class that contains information about a single Game.
 * To be Used as a Container object for Game Result information.
 */
public class GameResult {

  private Player winner; // winner of the game
  private Player loser; // loser of the game
  private boolean irregular; // Did either Player misbehave? (Timeout/Breaker/Infinite..)

  GameResult(Player winner, Player loser, boolean irregular) {
    this.winner = winner;
    this.loser = loser;
    this.irregular = irregular;
  }

  /**
   * Setter for Winner
   * @param winner new Winner
   */
  public void setWinner(Player winner) {
    this.winner = winner;
  }

  /**
   * Setter for Loser
   * @param loser new Loser
   */
  public void setLoser(Player loser) {
    this.loser = loser;
  }

  /**
   * Get the Winner
   * @return Winner
   */
  public Player getWinner() {
    return winner;
  }

  /**
   * Get Loser
   * @return Loser
   */
  public Player getLoser() {
    return loser;
  }

  /**
   * Converts the Result Object into a readable String format
   * @return String representation of this Result Object
   */
  public String toString() {
    return "Winner: " + winner.getName() + " Loser: " + loser.getName();
  }

  public boolean isIrregular() {
    return this.irregular;
  }

  /**
   * Converts the Result Object into a readable JSON Format
   * @return JSONFormat of this Result Object
   */
  public String asJSONString() {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(this.winner.getName());
    arrayNode.add(this.loser.getName());
    if (this.irregular) {
      arrayNode.add("irregular");
    }
    return arrayNode.toString();
  }

}
