/**
 * Class StayAlivePlayer is a subclass that extends the Player class
 * and holds the same methods as Player.
 * StayAlivePlayer is an instance of Player, but instantiated with a StayAliveStrategy.
 */
public class StayAlivePlayer extends Player{

  /**
   * Constructor for the Player Class
   *
   * @param name  name of the player
   * @param id    ID of the player
   */
  public StayAlivePlayer(String name, int id) {
    super(name, id, new StayAliveStrategy(false, 1));
  }

}
