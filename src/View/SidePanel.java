package View;
import Model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Widok bocznego panelu.
 * Zaweira informacje dotyczace sterowania i liste najlepszych graczy.
 */
public class SidePanel extends JPanel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Duza czcionka.
     */
    private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 30);

    /**
     * Srednia czcionka.
     */
    private static final Font MEDIUM_FONT = new Font("Tahoma", Font.BOLD, 16);

    /**
     * Mala czcionka
     */
    private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

    /**
     * Offset Higscore
     */
    private static final int HIGHSCORE_OFFSET = 100;
    /**
     * Offset sterowania
     */
    private static final int CONTROLS_OFFSET = 290;
    /**
     * Offset miedzy liniami
     */
    private static final int MESSAGE_STRIDE = 30;

    /**
     * Maly offset wykorzystywany do mapisow Highscore oraz Controls.
     */
    private static final int SMALL_OFFSET = 30;
    /**
     * Duzy offset wykorzystywany do ustawienia wspolrzednych X napisow pod danymi sekcjami.
     */
    private static final int LARGE_OFFSET = 50;
    /**
     * Kolor czcionki napisow.
     */
    private static final Color TEXT_COLOR = new Color(153,158,8);

    private Model model;

    /**
     * Konstruktor bocznego widoku.
     * @param m Model
     */
    public SidePanel(Model m) {
        this.model = m;

        setPreferredSize(new Dimension(300, BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE));
        setBackground(Color.BLACK);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.setColor(TEXT_COLOR);

        g.setFont(LARGE_FONT);
        g.drawString("Snake Game", getWidth() / 2 - g.getFontMetrics().stringWidth("Snake Game") / 2, 40);


        g.setFont(MEDIUM_FONT);
        g.drawString("Highscore", SMALL_OFFSET, HIGHSCORE_OFFSET);
        g.drawString("Controls", SMALL_OFFSET, CONTROLS_OFFSET);

        g.setFont(SMALL_FONT);

        int drawY = HIGHSCORE_OFFSET;
        for (Score s : model.scores)
        {
            g.drawString("" + model.getScore(s), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        }

        drawY = CONTROLS_OFFSET;
        g.drawString("Move Up: W / Up Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Down: S / Down Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Left: A / Left Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Right: D / Right Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Pause Game: P", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
    }

}
