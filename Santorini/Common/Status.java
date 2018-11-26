/**
 * A common enum to represent the next Action the game is expecting.
 * Paired with Turn, it describes the state of the game.
 *
 *  - PLACE implies the next Action is expected to be a Player Placing a Worker somewhere on the Board
 *  - MOVE implies the next Action is expected to be a Player Moving a Worker to a new Square on the Board
 *  - BUILD implies the next Action is expected to be a Player Building a Floor on a Square
 *  - GAMEOVER implies that no further Actions are expected, because the game is over
 *      The Turn enum will represent the winner if there is one, or DRAW if there isn't.
 */
public enum Status {
  PLACE, MOVE, BUILD, MOVEBUILD, GAMEOVER, GIVEUP
}
