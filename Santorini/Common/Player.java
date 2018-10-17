import java.util.ArrayList;

public class Player {
  private String name;
  private int id;
  private ArrayList<Integer> workerIDs;
  private Strategy bestStrategy;

  // Note: Strategy could be TCPStrategy
  public Player(String name, int id, Strategy strat) {
    this.name = name;
    this.id = id;
    this.bestStrategy = strat;
    this.workerIDs = new ArrayList<>();
  }

  public void addWorkerID(int id) {
    this.workerIDs.add(id);
  }

  public ArrayList<Integer> getWorkerIDs() {
    return workerIDs;
  }

  public Action getNextAction(Board b, Status s) {
    return this.bestStrategy.getNextAction(new BoardStatus(b, s), this.workerIDs);
  }

  public String getName() {
    return this.name;
  }
}
