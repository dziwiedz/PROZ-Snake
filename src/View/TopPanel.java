package View;

import Model.Model;

import javax.swing.*;
import java.awt.*;

/**
 * Gorny panel. Wyswietlany na nim jest stan aktualnej rozgrywki.
 */
public class TopPanel extends JPanel
{
    /**
     * Offset obecnego wyniku.
     */
    private static final int TOTAL_SCORE_OFFSET=0;
    /**
     * Offset ilosc zjedzonych owocow
     */
    private static final int FRUIT_EATEN_OFFSET=210;
    /**
     * Offset aktualnej wartosci zjedznia owocu
     */
    private static final int FRUIT_SCORE_OFFSET=360;
    /**
     * Ustawienie na osi Y.
     */
    private static final int POSITION_Y_OFFSET = 25;
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     *Srednia czcionka wykorzystywana do napisow statystyk.
     */
    private static final Font MEDIUM_FONT = new Font("Tahoma", Font.BOLD, 16);
    /**
     * Kolor czcionki
     */
    private static final Color TEXT_COLOR = new Color(153,158,8);
    private Model model;

    /**
     * Konstruktor gornego panelu.
     * @param m Model
     */
    public TopPanel(Model m)
    {
        this.model = m;
        setPreferredSize(new Dimension(10, 50));
        setBackground(Color.BLACK);
    }

    /**
     * Rysowanie statystyk.
     * @param g Graphics
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(TEXT_COLOR);
        g.setFont(MEDIUM_FONT);
        g.drawString("Total Score  : " + model.getScore(), TOTAL_SCORE_OFFSET, POSITION_Y_OFFSET );
        g.drawString("Fruit Eaten  : " + model.getFruitsEaten(), FRUIT_EATEN_OFFSET, POSITION_Y_OFFSET );
        g.drawString("Fruit Score  : " + model.getNextFruitScore(), FRUIT_SCORE_OFFSET, POSITION_Y_OFFSET );

    }

}
