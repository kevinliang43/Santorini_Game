/**
 * A common enum to represent which player is to act.
 * Paired with Status, it describes the state of the game.
 *
 *  - PLAYER1 implies it is the first Player's turn to act,
 *      or means that the first Player has won if Status is GAMEOVER
 *  - PLAYER2 implies it is the second Player's turn to act,
 *      or means that the second Player has won if Status is GAMEOVER
 */
public enum Turn {
  PLAYER1, PLAYER2
}
