package View;
import javax.imageio.ImageIO;
import javax.swing.*;
import Model.*;

import java.awt.*;
import java.io.File;

/**
 * Widok areny gry.
 */
public class BoardPanel extends JPanel
{
    private Model model;

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ilosc kolumn areny.
     */
    public static final int COL_COUNT = 25;

    /**
     * Ilosc wierszy areny.
     */
    public static final int ROW_COUNT = 25;

    /**
     * Rozmiar kazdego bloku areny.
     */
    public static final int TILE_SIZE = 20;

    /**
     * Ilosc pixeli na offset oczu od boku.
     */
    private static final int EYE_LARGE_INSET = TILE_SIZE / 3;

    /**
     * Ilosc pixeli od frontu.
     */
    private static final int EYE_SMALL_INSET = TILE_SIZE / 6;

    /**
     * Odleglosc oczu od podstawy.
     */
    private static final int EYE_LENGTH = TILE_SIZE / 5;

    /**
     * Czcionka tekstu.
     */
    private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);
    /**
     * Kolor tekstu
     */
    private static final Color TEXT_COLOR = new Color(153,158,8);
    /**
     * Kolor tla planszy.
     */
    private static final Color BACKGROUND_COLOR = new Color(1,16,0);

    /**
     * Konstruktor planszy.
     * @param m Model
     */
    Image img = Toolkit.getDefaultToolkit().createImage("bg.jpg");
    public BoardPanel(Model m)
    {

        this.model = m;
        setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT * TILE_SIZE));
        setBackground(BACKGROUND_COLOR);
    }


    /**
     * Rysowanie planszy.
     * Na poczatku przechodzi przez wszystkie pola i sprawdza czy sa nullami, jezeli nie maluje
     * blok na dany typ.
     * Jezeli podniesiona jest ktoras z flag, pojawia sie napis zalezny od danej flagi.
     * @param g Graphics
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int x = 0; x < COL_COUNT; x++) {
            for(int y = 0; y < ROW_COUNT; y++) {
                TileType type = model.getTile(x, y);
                if(type != null) {
                    drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
                }
            }
        }
		/*
		 * Rysowanie szachownicy wraz z obrysem.
		 */
        g.setColor(Color.DARK_GRAY);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        for(int x = 0; x < COL_COUNT; x++) {
            for(int y = 0; y < ROW_COUNT; y++) {
                g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
                g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
            }
        }

		/*
		 * Pokazuje wiadomosc zaleznie od obecnego stanu gry.
		 */
        if(model.isGameOver() || model.isNewGame() || model.isPaused()) {
            g.setColor(TEXT_COLOR);

			/*
			 * Wspolrzedne srodka planszy.
			 */
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

			/*
			 * Wypisanie tekstu, w zaleznosci od stanu gry.
			 */
            String largeMessage = null;
            String smallMessage = null;
            if(model.isNewGame()) {
                largeMessage = "Snake Game!";
                smallMessage = "Press Enter to Start";
            } else if(model.isGameOver()) {
                largeMessage = "Game Over!";
                smallMessage = "Press Enter to Restart";
            } else if(model.isPaused()) {
                largeMessage = "Paused";
                smallMessage = "Press P to Resume";
            }

			/*
			 * Ustawia wiadomosc na srodku planszy
			 */
            g.setFont(FONT);
            g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
            g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
        }
        g.drawImage(img, 0, 0, null);
    }

    /**
     * Rysowanie blokow.
     * W zalezbiscu od aktualnego bloku, switch rysuje odpowoedni blok.
     * Jezeli jest rysowna glowa weza, rysowane sa jego oczy, w zaleznosci od kierunku w jakim waz bedzie isc.
     * @param x Wspolrzedna x
     * @param y wspolrzedna y
     * @param type typ bloku
     * @param g Graphics
     */
    private void drawTile(int x, int y, TileType type, Graphics g)
    {

        switch(type)
        {

            case FRUIT:
                g.setColor(Color.RED);
                g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                break;

            case SNAKEBODY:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                break;

            case SNAKEHEAD:
                //Fill the tile in with green.
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                g.setColor(Color.BLACK);

                /*Rysowanie oczow weza.*/
                switch(model.getDirection()) {
                    case UP: {
                        int baseY = y + EYE_SMALL_INSET;
                        g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY + EYE_LENGTH);
                        g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);
                        break;
                    }

                    case DOWN: {
                        int baseY = y + TILE_SIZE - EYE_SMALL_INSET;
                        g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY - EYE_LENGTH);
                        g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY - EYE_LENGTH);
                        break;
                    }

                    case LEFT: {
                        int baseX = x + EYE_SMALL_INSET;
                        g.drawLine(baseX, y + EYE_LARGE_INSET, baseX + EYE_LENGTH, y + EYE_LARGE_INSET);
                        g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
                        break;
                    }

                    case RIGHT: {
                        int baseX = x + TILE_SIZE - EYE_SMALL_INSET;
                        g.drawLine(baseX, y + EYE_LARGE_INSET, baseX - EYE_LENGTH, y + EYE_LARGE_INSET);
                        g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
                        break;
                    }

                }
                break;
        }
    }

}