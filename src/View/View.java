package View;
import Controller.Listener;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.sun.javafx.scene.traversal.Direction;

/**
 * Widok.
 * Zawiera 3 panele. Gorny - TopPanel, Srodkowy - BorderPanel oraz boczny - SidePanel.
 * W widoku dodawani sa sluchacze.
 * Kontroler nasluchuje wcisniecia klawiszy.
 * Kontroler moze wywolac pojawienia sie okna dialogowego, w ktorym gracz podaje swoje imie.
 */
public class View extends  JFrame
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private Model model;
    private BoardPanel board;
    private SidePanel side;
    private TopPanel top;

    /**
     * Konstruktor widoku
     * @param m Model
     */
    public View(Model m)
    {
        this.model = m;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        this.setTitle("Snake");
        this.board = new BoardPanel(m);
        this.side = new SidePanel(m);
        this.top = new TopPanel(m);
        add(board, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);
        add(top, BorderLayout.PAGE_START);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }



    /**
     * Zapytnie o imie gracza.
     * @return Imie gracza.
     */
    public String askForPlayerName()
    {
        return JOptionPane.showInputDialog("You have one of the highest score. Please write your name:");
    }

    /**
     * Dodaje do widoku sluchacza. Przy nacisnieciu klawisza kontroler przekazuje modelowi zadanie do wykonania.
     * @param l interfejs sluchacza
     */
    public void addKeyListener(final Listener l )
    {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                    {
                        l.moveUp();
                        break;
                    }
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                    {
                        l.moveDown();
                        break;
                    }
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                    {
                        l.moveLeft();
                        break;
                    }
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                    {
                        l.moveRight();
                        break;
                    }
                    case KeyEvent.VK_P:
                    {
                        l.pauseGame();
                        break;
                    }
                    case KeyEvent.VK_ENTER:
                    {
                        l.newGame();
                        break;
                    }
                }
            }
        });
    }
}
