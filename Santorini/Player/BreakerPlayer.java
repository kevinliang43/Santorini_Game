/**
 * Player that by default uses a strategy that is intended to make bad moves, BreakerStrategy
 */
public class BreakerPlayer extends Player{
    /**
     * Constructor for the BreakerPlayer Class
     *
     * @param name  name of the player
     * @param id    ID of the player
     */
    public BreakerPlayer(String name, int id) {
        super(name, id, new BreakerStrategy());
    }
}
