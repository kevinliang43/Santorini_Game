/**
 * Player that by default uses a strategy that is intended to loop infinitely, InfiniteStrategy
 */
public class InfinitePlayer extends Player{
    /**
     * Constructor for the Player Class
     *
     * @param name  name of the player
     * @param id    ID of the player
     */
    public InfinitePlayer(String name, int id) {
        super(name, id, new InfiniteStrategy());
    }
}
