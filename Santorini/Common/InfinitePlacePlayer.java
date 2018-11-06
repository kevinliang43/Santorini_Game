/**
 * Player Instance that goes into an infinite loop in the Place Phase.
 */
public class InfinitePlacePlayer extends Player {


  /**
   * Constructor for the Player Class
   *
   * @param name  name of the player
   * @param id    ID of the player
   */
  public InfinitePlacePlayer(String name, int id) {
    super(name, id, new InfinitePlaceStrategy());
  }
}
