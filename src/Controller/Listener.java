package Controller;

import java.awt.event.KeyEvent;

/**
 * Interfejs sluchacza.
 */
public interface Listener
{
    public void moveUp();
    public void moveDown();
    public void moveRight();
    public void moveLeft();
    public void pauseGame();
    public void newGame();
}
