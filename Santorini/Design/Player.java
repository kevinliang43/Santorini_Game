/**
 * Interface representing a player in the Santorini Game.
 * A Player object sits in between the client and the Game, clients send requests to their Player
 * and the Game administrative component will grab these requests, validate them, and execute them
 * when it is a Player's turn.
 */
public interface Player {

    /**
     * Fields:
     *
     * StringBuilder turn: collects the client input so that the administrative component can later
     *                     pull the requests for this turn and execute them.
     *      Socket socket: Socket for the client to send requests to this Player to execute and to send
     *                     messages to the client when needed for invalid move/build requests, queries, etc.
     *                     This Socket is a reference to the Socket a client connects to in the overall
     *                     tournament handler.
     *
     * Constructor:
     *
     * A Player is constructed with a reference to a Socket where the client is connected to.
     * Player(Socket socket)
     *
     * When a Player is created, turn and the hashmap of workers are initialized to be empty and added to later
     */

    /**
     * used for the game to get the clients requests
     * when called, information sent from the client is taken from the server and appended to the StringBuilder turn
     * @return the String representing the requests that the client has given
     */
    String readInput();

    /**
     * Writes the given output to the output stream of this Players socket
     * @param output the String representing the output from the Game
     */
    void sendOutput(String output);
}
