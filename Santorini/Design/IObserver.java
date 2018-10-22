/**
 * Interface outlining the utilities that an Observer component should provide
 */
public interface IObserver {

  /**
   * Description/Purpose:
   * Observers should be able to "plug" into a Referee Component and "observe" certain aspects of
   * the game. Observers should not be able to modify the Referee or any of the Game Components but
   * should be able to see how the game is progressing.
   *
   * Outline of Functionalities:
   * 1. View Board State
   * 2. View Information (Status/Phase/Turns/) about the Game.
   * 3. View Information about the Players (Wins, Name)
   * 4. View which Workers belong to which Players
   * 5. If it's a best of n game, View how many games have been played so far
   *
   * Specifications:
   * The Observer class will not have direct access to the Referee Model, rather they will have
   * access to a ViewReferee class that limits the Observer's access to the Referee and the
   * components it contains.
   *
   * The ViewReferee will be a wrapper class that encapsulates a Referee. Functionality that the
   * wrapper class will have will be limited to READ-ONLY methods.
   */

}