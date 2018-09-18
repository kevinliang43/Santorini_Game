package spreadsheet;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Sheet implements KeyListener{


  public static Cell[][] cellArray;

  private Point currentLocation;

  Sheet(int x, int y) {
    cellArray = new Cell[x][y];
    currentLocation = new Point(0,0);

  }

  Sheet() {
    this(20,20);
  }

  public void addCell(Cell c) {
    addCell(currentLocation, c);
  }

  String getValueAt(Point position) {
    return cellArray[position.x][position.y].getValue();
  }

  public void addCell(Point position, Cell c) {
    cellArray[position.x][position.y] = c;
    c.setPoint(position);
  }

  /**
   * Invoked when a key has been typed. See the class description for {@link KeyEvent} for a
   * definition of a key typed event.
   */
  @Override
  public void keyTyped(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_UP) {
      if(currentLocation.x > 0) {
        currentLocation.x--;
      }
    }
    if(e.getKeyCode() == KeyEvent.VK_DOWN) {
      if(currentLocation.x < cellArray.length - 1) {
        currentLocation.x++;
      }
    }
    if(e.getKeyCode() == KeyEvent.VK_LEFT) {
      if(currentLocation.y > 0) {
        currentLocation.y--;
      }
    }
    if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
      if(currentLocation.y < cellArray[0].length - 1) {
        currentLocation.y++;
      }
    }


  }

  /**
   * Invoked when a key has been pressed. See the class description for {@link KeyEvent} for a
   * definition of a key pressed event.
   */
  @Override
  public void keyPressed(KeyEvent e) {

  }

  /**
   * Invoked when a key has been released. See the class description for {@link KeyEvent} for a
   * definition of a key released event.
   */
  @Override
  public void keyReleased(KeyEvent e) {

  }
}
