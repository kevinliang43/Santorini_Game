import java.util.ArrayList;

/**
 * Class that Serves as a proxy between the Tournament and Referee and
 * a remote Client Player
 */
public class ProxyPlayer extends Player {

  /**
   * Constructor for the Player Class
   *
   * @param name  name of the player
   * @param id    ID of the player
   * @param strat strategy to be used
   */
  public ProxyPlayer(String name, int id, Strategy strat) {
    super(name, id, strat);
  }
}
