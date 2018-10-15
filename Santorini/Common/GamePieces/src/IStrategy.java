import java.util.HashMap;
import java.util.List;

/**
 * Interface for Strategy Objects.
 * Strategy Objects are capable of somehow determining a next move for the Player class.
 */
public interface IStrategy {

  /**
   * Strategy-Player Interaction:
   *
   * Players serve as the mechanism/bridge that connects the game framework to some "Actor".
   * "Actors" can be interpreted as some Person/Remote Component/ Computerized Strategy that is able
   * to somehow methodically determine a next move for the player itself.
   *
   * The Player Class itself does not actually determine the next move, rather it will turn to its
   * Strategy object to help it determine their next move.
   *
   * The Player Class will then receive the decision from the Strategy Object, and will pass it to
   * the Administrative component when ready.
   *
   * Examples of some Implementations of Strategy:
   *
   * 1. Human Client:
   *      In this case, the Strategy object's nextMove is determined by User Input through
   *      Standard I/O.
   *
   * 2. Remote Component:
   *      Similar to the Human Client example, this Strategy object's nextMove is also determined by
   *      User Input, however this Strategy object interacts with the Remote Component through some
   *      network interface.
   *
   * 3. Computerized Strategy ("AI"):
   *      In this case, this Strategy Object determines its nextMove without any User Input.
   *      Instead, moves are decided based on some algorithm that determines the next move
   *      on given board state. To illustrate this, some examples of "viable" and simple
   *      strategies could include:
   *              A. Moving to/Building on random Cell.
   *              B. Moving to/Building on the tallest nearby Cell.
   *              C. Moving to/Building on Cells away from other Player's Workers.
   *
   *
   *
   *
   * Envisioned WorkFlow of the Admin-Player-Strategy Relationship:
   *
   *      1. When it is a given Player's turn, the Admin will instruct the Player to make an action.
   *
   *      2. Prompts and other text Information are passed from the Admin to the Player through the
   *         Player.sendInformation method. This method is mainly used to communicate in English to
   *         Human-based strategies.
   *
   *      3. Information about the board is passed through the Player.nextMove method to the
   *         Player, from the Admin. Strategy Object contained by the Player will determine the next
   *         Move and send JSON, in String format, back to the Player, and then to the Admin.
   *
   *      4. Admin will the parse and determine the validity of the requested Action. If the requested
   *         Action is viable, then the Admin will execute the requested Action. If the requested
   *         Action is NOT viable, then the Admin will then start back at Step 1.
   *
   *      5. When the Admin determines that the game is over, the Admin will also determine a winner,
   *         and then proceed to inform all Players about who won the game,
   *         using the sendInformation method.
   *
   *      6. Admins will also instruct/force Players to "shutdown" or exit from the game.
   *
   *
   */

  /**
   * Determines the Next Move based on a given Board state.
   * @param board Copy of the Current State of the Board
   * @return String representation of a JSON request
   */
  String nextMove(Board board, HashMap<String, List<String>> playerWorkerMap);

  /**
   * Sends information to be handled by the "Actor" on the receiving end of this Strategy object.
   * @param message To be used to communicate messages in English, from the Admin to some "Actor"
   */
  void sendInformation(String message);

  /**
   * Determines the placement of a new Worker and sends placement information in the form of a JSON
   * String
   * @param board Copy of the Current State of the Board
   * @param playerWorkerMap View Copy of the Player Worker Map
   * @return String representation of a JSON request
   */
  String placeWorker(Board board, HashMap<String, List<String>> playerWorkerMap);

  void setNumTurns(int numTurns);
}
