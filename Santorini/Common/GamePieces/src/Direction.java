/**
 * Created by KevinLiang on 10/14/18.
 */
public enum Direction {
  EAST, WEST, NORTH, SOUTH, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST;


  @Override
  public String toString() {
    switch (this) {
      case EAST:
        return "[\"EAST\",\"PUT\"]";
      case WEST:
        return "[\"WEST\",\"PUT\"]";
      case NORTH:
        return "[\"NORTH\",\"PUT\"]";
      case SOUTH:
        return "[\"SOUTH\",\"PUT\"]";
      case NORTHEAST:
        return "[\"EAST\",\"NORTH\"]";
      case NORTHWEST:
        return "[\"WEST\",\"NORTH\"]";
      case SOUTHEAST:
        return "[\"EAST\",\"SOUTH\"]";
      default:
        return "[\"WEST\",\"SOUTH\"]";
    }
  }
}