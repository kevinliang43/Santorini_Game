/**
 * Class that contains information about a single Game.
 * To be Used as a Container object for Game Result information.
 */
public class GameResult {

  Player winner; // Loser of the game
  Player loser; //

  GameResult(Player winner, Player loser) {
    this.winner = winner;
    this.loser = loser;
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

  /**
   * Converts the Result Object into a readable JSON Format
   * @return JSONFormat of this Result Object
   */
  public String asJSONString() {
    return "[\"" + winner.getName() + ", \"" + loser.getName() + "\"]";

  }

}
