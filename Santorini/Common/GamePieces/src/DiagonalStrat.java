import java.util.HashMap;
import java.util.List;

/**
 * IStrategy Implementation
 * DiagonalStrat determines the placement of new Workers by placing the Worker on the first
 * point of the diagonal of the board that is still free, starting from (0,0).
 */
public class DiagonalStrat implements IStrategy {

  private int numTurns;

  DiagonalStrat(int numTurns) {

  }

  @Override
  public String nextMove(Board board, HashMap<String, List<String>> playerWorkerMap, RuleChecker rc) {
    return null;
  }

  @Override
  public void sendInformation(String message) {
    //Function not used for 'AI" implementation

  }


  /**
   * Sends a JSON String for the placement of a new Worker.
   * The placement is determined by:
   * Place the worker on the first point of the diagonal that is still free, starting from (0,0).
   * @param board Copy of the Current State of the Board
   * @param playerWorkerMap View Copy of the Player Worker Mapping
   * @return JSON String of the placement of a new Worker
   */
  @Override
  public String placeWorker(Board board, HashMap<String, List<String>> playerWorkerMap) {
    int idx = 0;
    Cell currentCell = board.getCell(idx,idx); //Initial Cell

    while (currentCell.containsWorker()) {
      idx++;
      currentCell = board.getCell(idx,idx); // Get next Diagonal Cell if this current is occupied
    }

    // Cell has been determined to be unoccupied.
    // Construct JSON String
    String returnString = String.format("[\"add\", %d, %d]", idx, idx);
    return returnString;

  }

  @Override
  public void setNumTurns(int numTurns) {
    this.numTurns = numTurns;
  }
}
