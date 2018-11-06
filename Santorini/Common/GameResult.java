public class GameResult {

  Player winner;
  Player loser;

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

  public String asJSONString() {
    return "[\"" + winner.getName() + ", \"" + loser.getName() + "\"]";

  }

}
