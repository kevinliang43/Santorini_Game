/**
 * Interface to represent the Referee class that will supervise a game of two players
 * in a game of Santorini
 */
public interface Referee {

/**
 * The Referee Class will supervise a game of Santorini between two players.
 *
 * Components that the Referee will have access to / keep track of:
 *   1) Board
 *   2) Rule Checker
 *   3) All Players
 *   4) Interpreter (Executes Requests)
 *   5) JSONParser (Parses JSON Requests)
 *   6) Map of Players to List of their Worker Strings
 *   7) Player Turn
 *
 * Phases:
 *
 * Initialization Phase:
 *
 *    1) The Referee will be initialized given two Players that are ready to begin a game.
 *    2) After the Referee is initialized, it will then begin to initialize a new game:
 *      A) Initializes a new Board.
 *      B) Choose the "Foo"-est Player as the beginning Player.
 *      C) Prompts Players to place Workers on the Board, while updating the
 *         Player - List of Worker ID Map
 *
 * Game Phase:
 *    1) The Referee will check to see if the Game is over from the state of the Game Board.
 *    2) If the game is over, the Referee will determine a Winner.
 *    3) If the game is not over, the Referee will go through another turn which will include the following:
 *        A) Prompt the Player for a Request/Action through the Player.getRequest()
 *        B) Parse the JSON String request from the Player with the JSONParser class (into JsonNode format)
 *        C) Determine if the Request is a valid Request with the RuleChecker.
 *        D1) If the Request is NOT valid, then the game is over, where the Player with the invalid request
 *            loses, and the other Player is the winner. The Player with the invalid request will also be
 *            reported to the Tournament and the Tournament will handle this type of game Over.
 *        D2) If the Request is valid, then execute the request with the Interpreter on the Board.
 *        E) Update the Player Turn field to the next Player
 *        NOTE: If a player fails to make a move within the given time, then the player loses. (Prevents filibusters)
 *
 *  Shutdown Phase:
 *    1) Once the game has been decided to be over by the Referee, the Winner is returned to the tournament
 *       along with the type of victory and this Referee and Game will be closed.
 *
 *
 * Determination of Winner / Game Over
 *
 *    A Game will be over if any of the conditions are achieved:
 *    1) Player A can reach the third floor of a building. (Player A wins, Player B loses)
 *    2) Player A cannot make a valid move determined by the Rule Checker. (Player A loses, Player B wins)
 *    3) Player A can make a valid move, but cannot make a valid Build determined by the Rule Checker. (Player A loses, Player B wins)
 *    4) Player A makes any invalid move through any part of the game.
 *       Player A gets eliminated from the Tournament and Player B gets all previous wins from Player A. (Player A loses, Player B wins)
 *    5) TIMEOUT: Player A runs out of time to make a move/build (Player A loses, Player B wins)
 *
 */

  /**
   * Initializes the Game with the necessary steps, as stated in the Initialization Phase
   * detailed above.
   */
  void initialize();

  /**
   * Determines if the game is over, based on the board state and conditions as detaield above.
   * @return Boolean representing if the game is over.
   */
  boolean gameOver();

  /**
   * Plays the Game and drives the player through the Game Phase of the game, as detailed above.
   * @return Player representing the winner of the game
   */
  Player playGame();
}
