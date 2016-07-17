package Controller;
import Model.*;
import View.*;
import com.sun.javafx.scene.traversal.Direction;


/**
 * Kontroler.
 * Zawiera petle czasu, obsluguje zdarzenia przesylane przez widok.
 */
public class Controller implements Listener
{
    /**
     * Dlugosc klatki
     * */
    private static final long FRAME_TIME = 1000L / 50L;
    Model model;
    View view;

    /**
     * Do konstruktora przekazywany jest model i widok.
     * @param m model
     * @param v widok
     */
    public Controller(Model m, View v)
    {
        this.model = m;
        this.view = v;
        view.addKeyListener(this);
        startGame();
    }

    /**
     * Uruchomienie gry.
     * Uaktualniany jest zegar i midol. Widok jest przerysowywany na nowo.
     * Na poczatku kazdej iteracji petli, pobierany jest aktulny czas systemu, po czym nastepuje aktualizacja zegara.
     * Podczas uktualizowania modelu, sprawdzane jest czy nie nastapil koneic gry.
     * Jezeli tak sprawdzy jest wynik graczy i jezeli jest wyzszy od obecnych 5 najlepszych, jest dodawany.
     */
    public void startGame()
    {
        while(true) {

            long start = System.nanoTime();

            model.UpdateClock();

			/*
			 * Jezeli minal cykl aktualizuj model i sprawdz czy koniec gry.
			 * Jezeli jest koniec, sprawdz wynik gracza i ewentualnie dodaj do listy.
			 */
            if(model.getElapsedTime()) {
                model.updateGame();
                if(model.isGameOver() && model.isBetterThanRest())
                {
                    model.addToList(view.askForPlayerName());

                }
            }

            view.repaint();

            long delta = (System.nanoTime() - start) / 1000000L;
            if(delta < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - delta);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * Zwraca sie do modelu by ten ruszyl sie w lewo
     */
    @Override
    public void moveLeft()
    {
        model.moveIntoDirection(Direction.LEFT);
    }

    /**
     * Zwraca sie do modelu by ten ruszyl sie w lewo.
     */
    @Override
    public void moveRight()
    {
        model.moveIntoDirection(Direction.RIGHT);
    }

    /**
     * Zwraca sie do modelu by ten ruszyl sie do gory.
     */
    @Override
    public void moveUp()
    {
        model.moveIntoDirection(Direction.UP);
    }

    /**
     * Zwraca sie do modelu by ten ruszyl sie do dolu.
     */
    @Override
    public void moveDown()
    {
        model.moveIntoDirection(Direction.DOWN);
    }

    /**
     * Wstrzymanie/Wznowienie gry.
     * Wywolanie odpowiedniej funkcji modelu, obslugujacej to zdarzenie.
     */
    @Override
    public void pauseGame()
    {
        model.setPaused();
    }

    /**
     * Wcisniety zostal klawisz odpowiedzialny za rozpoczecie nowej gry.
     * Wywolanie odpowiedniej funkcji modelu, obslugujacej to zdarzenie.
     */
    @Override
    public void newGame()
    {
        model.isStartGame();
    }

}
