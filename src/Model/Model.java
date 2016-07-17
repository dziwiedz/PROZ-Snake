package Model;

import com.sun.javafx.scene.traversal.Direction;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Klasa Model.
 * Zawiera logike systemu gry.
 */
public class Model
{
    /**
     * Ilosc kolumn areny
     */
    public static final int COL_COUNT = 25;

    /**
     * Ilosc wierszy areny.
     */
    public static final int ROW_COUNT = 25;
    /**
     * Lista punktow ktore naleza do weza
     */
    private LinkedList<Point> snake;
    /**
     * Lista 5 najlepszych graczy
     */
    public List<Score> scores;
    /**
     * Flaga Pauzy
     */
    private boolean isPaused;
    /**
     * Flaga konca gry
     */
    private boolean isGameOver;
    /**
     * Flaga poczatku gry
     */
    private boolean isNewGame;
    /**
     * Aktualny wynik
     */
    private int score;
    /**
     * Ilosc zjedzonych owocow
     */
    private int fruitsEaten;
    /**
     * Wartosc nastepnego owoca
     */
    private int nextFruitScore;

    /**
     * Minimalna dlugosc weza
     */
    private static final int MIN_SNAKE_LENGTH = 5;
    /**
     * Maksymalna ilosc kierunkow
     */
    private static final int MAX_DIRECTIONS = 3;
    /**
     * Lista kierunkow
     */
    private LinkedList<Direction> directions;
    /**
     * Pola planszy
     */
    private TileType[] tiles;
    /**
     * Zegar
     */
    private Clock logicTimer;
    /**
     * Losowy element
     */
    private Random random;

    /**
     * Inicjalizacja modelu.s
     * Podczas inicjalizacji ustawiane sa flagi,
     * tworzone zostaja listy, ustawiany zegar oraz ladowana jest lista najelpszych graczy.
     */
    public Model()
    {
        this.tiles = new TileType[ROW_COUNT * COL_COUNT];
        isPaused=false;
        isGameOver=false;
        isNewGame=true;

        this.snake = new LinkedList<>();
        this.directions = new LinkedList<>();
        this.scores = new ArrayList<>();

        loadScores();

        this.random = new Random();
        this.logicTimer = new Clock(9.0f);
        logicTimer.setPaused(true);
    }

    /**
     * Aktualizacja logiki modelu.
     * W zaleznosci od bloku na ktory wchodzi waz,
     * uaktualniany jest wynik, powstaje nowy owoc lub nastepuje koniec gry.
     * Typ bloku ktory bedzie nastepny jest zwracany poprzez funkcje updateSnake();
     * Przy kazdym wywolaniu zmienjszana jest wartosc owoca o 1.
     */
    public void updateGame()
    {

        TileType collision = updateSnake();

        if(collision == TileType.FRUIT) {
            fruitsEaten++;
            score += nextFruitScore;
            spawnFruit();
        } else if(collision == TileType.SNAKEBODY) {
            isGameOver = true;
            logicTimer.setPaused(true);
        } else if(nextFruitScore > 10) {
            nextFruitScore--;
        }
    }

    /**
     * Aktualizacja pozycji weza i rozmiaru.
     * Wybieramy nastepny kierunek. Kierunek nie zostanie usuniety.
     * W przypadku wybrania innej funkcji powtsaje problem, gdyz waz
     * po zakonczeniu gry moze zmienic kierunek swojej glowy.
     * @return Tile blok w kierunku ktorego zmierza waz
     */
    private TileType updateSnake() {

        Direction direction = directions.peekFirst();

	    /*Obliczanie punktu w ktorym znajdzie sie waz*/
        Point head = new Point(snake.peekFirst());
        switch(direction) {
            case UP:
                head.y--;
                break;

            case DOWN:
                head.y++;
                break;

            case RIGHT:
                head.x--;
                break;

            case LEFT:
                head.x++;
                break;
        }
        /*Jezeli nastepne pole bedzie poza arena, zwracamy cialo snake (koniec gry)*/
        if(head.x < 0 || head.x >= COL_COUNT || head.y < 0 || head.y >= ROW_COUNT) {
            return TileType.SNAKEBODY;
        }
        /*Obliczanie nowych pozycji weza*/
        TileType old = getTile(head.x, head.y);
        if(old != TileType.FRUIT && snake.size() > MIN_SNAKE_LENGTH) {
            Point tail = snake.removeLast();
            setTile(tail, null);
            old = getTile(head.x, head.y);
        }

        if(old != TileType.SNAKEBODY) {
            setTile(snake.peekFirst(), TileType.SNAKEBODY);
            snake.push(head);
            setTile(head, TileType.SNAKEHEAD);
            if(directions.size() > 1) {
                directions.poll();
            }
        }

        return old;
    }

    /**
     * Zwraca typ bloku.
     * @param x wspolrzedna na osi X
     * @param y wspolrzedna na osi Y
     * @return Typ bloku o wspolrzednych x, y
     */
    public TileType getTile(int x, int y)
    {
        return tiles[y * ROW_COUNT + x];
    }

    /**
     * Ustawia dany blok
     * @param point Punkt areny
     * @param type typ bloku na jaki ma byc ustawiony
     */
    public void setTile(Point point, TileType type)
    {
        setTile(point.x, point.y, type);
    }

    /**
     * Ustawia dany blok poprzez wspolrzedne
     * @param x wspolrzedna osi X
     * @param y wspolrzedna osi Y
     * @param type typ bloku
     */
    public void setTile(int x, int y, TileType type)
    {
        tiles[y * ROW_COUNT + x] = type;
    }

    /**
     * Resetuje ustawienia gry.
     */
    public void ResetGame()
    {
        this.score = 0;
        this.fruitsEaten = 0;

        this.isNewGame = false;
        this.isGameOver = false;

        Point head = new Point(COL_COUNT / 2, ROW_COUNT / 2);

        snake.clear();
        snake.add(head);

        ClearTiles();
        setTile(head, TileType.SNAKEHEAD);

        directions.clear();
        directions.add(Direction.UP);
        logicTimer.reset();
        spawnFruit();
    }

    /**
     * Losuje polozenia nowego owoca.
     * Petla przechodzi przez wszystkie bloki i sprawdza czy na danym bloku mozna utworzyc owoc
     */
    private void spawnFruit()
    {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;

        int index = random.nextInt(COL_COUNT * ROW_COUNT - snake.size());

        int freeFound = -1;
        for(int x = 0; x < COL_COUNT; x++) {
            for(int y = 0; y < ROW_COUNT; y++) {
                TileType type = getTile(x, y);
                if(type == null || type == TileType.FRUIT) {
                    if(++freeFound == index) {
                        setTile(x, y, TileType.FRUIT);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Zwraca obecny wynik
     * @return score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Zwraca ilosc owocow zjedzonych.
     * @return ilosc owocow zjedzonych.
     */
    public int getFruitsEaten() {
        return fruitsEaten;
    }

    /**
     * Zwraca wartosc punktowa nastepnego owoca.
     * @return Wartosc punktowa nastepnego owoca.
     */
    public int getNextFruitScore() {
        return nextFruitScore;
    }
    /**
     * Zwraca flage ktora mowi czy gramy nowa gre.
     * @return Flaga nowej gry
     */
    public boolean isNewGame() {
        return isNewGame;
    }

    /**
     * Zwraca flage czy nastapil koniec gry.
     * @return gameOver flag
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Gets the flag that indicates whether or not the game is paused.
     * @return The paused flag.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     *  Zwraca nastepny kierunek
     * @return Nastepny kierunek
     */
    public Direction getDirection() {
        return directions.peek();
    }

    /**
     * Dodaje do listy kierunkow nowy kierunek, w ktorym waz bedzie sie poruszal.
     * @param d Kierunek, ktory wybral gracz.
     */
    public void moveIntoDirection(Direction d)
    {
        if (!isPaused && !isGameOver) {
            if (directions.size() < MAX_DIRECTIONS) {
                Direction last = directions.peekLast();
                switch (d)
                {
                    case UP:
                    {
                        if(last != Direction.DOWN && last != Direction.UP) {
                            directions.addLast(Direction.UP);
                        }
                        break;
                    }
                    case DOWN:
                    {
                        if(last != Direction.UP && last != Direction.DOWN) {
                            directions.addLast(Direction.DOWN);
                        }
                        break;
                    }
                    case RIGHT:
                    {
                        if(last != Direction.LEFT && last != Direction.RIGHT) {
                            directions.addLast(Direction.LEFT);
                        }
                        break;
                    }
                    case LEFT:
                    {
                        if(last != Direction.LEFT && last != Direction.RIGHT) {
                            directions.addLast(Direction.RIGHT);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Zmienia na przeciwna flage pauzy, tylko wtedy kiedy nie nastapil koniec gry.
     */
    public void setPaused()
    {
        if(!isGameOver)
        {
            isPaused=!isPaused;
            logicTimer.setPaused(isPaused);
        }
    }

    /**
     * Sprawdza, czy moze resetowac gre. Jezeli flagi nowej gry i konca gry ie sa podniesione, resetuje gre.
     */
    public void isStartGame()
    {
        if(isNewGame || isGameOver) {
            ResetGame();
        }
    }

    /**
     * Uaktalnienie zegara
     */
    public void UpdateClock()
    {
        logicTimer.update();
    }

    /**
     * Zwraca czas jaki uplynal od ostatniej ramki.
     * @return Czas jaki uplynal od ostaniej ramki.
     */
    public boolean getElapsedTime()
    {
        return logicTimer.hasElapsedCycle();
    }

    /**
     * Ustanowienie wszystkich blokow na puste.
     */
    public void ClearTiles()
    {
        for (int i = 0; i < tiles.length ; ++i)
        {
            tiles[i]=null;
        }
    }

    /**
     * Sprawdzenie czy wynik gracza jest lepszy od najelpszych 5 wynikow.
     * @return true - wynik gracza jest lepszy od ktoregos na liscie
     */
    public boolean isBetterThanRest()
    {
        if (scores.size() < 5) return true;
        for ( Score s : scores)
        {
            if(s.compareTo(score)==-1)
            {

                return true;
            }
        }
        return false;
    }

    /**
     * Dodaje gracza listy wynikow, po czym lista jest sortowana.
     * Jezeli wynikow jest wiecej niz 5, ostatni wynik zostanie usuwany.
     * @param playerName Nazwa wprowdzona przez gracza
     */
    public void addToList(String playerName)
    {
        if (playerName.isEmpty())
        {
            playerName = "NoName";
        }
        scores.add(new Score(score, playerName));
        Collections.sort(scores);
        if (scores.size()>5) scores.remove(scores.size()-1);
        saveScores();

    }

    /**
     * Pobiera wynik gracza.
     * @param s Klasa gracza
     * @return zwarca napis w postaci "Nazwa Wynik"
     */
    public String getScore(Score s)
    {
        return s.getScore();
    }

    /**
     * Zapisanie wynikow do pliku.
     */
    private void saveScores()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("highscore.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(scores);
            out.close();
            fileOut.close();
        } catch(IOException i)
        {
            i.printStackTrace();
        }

    }

    /**
     * Odczyt wynikow z pliku
     */
    private void loadScores()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("highscore.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            scores =  (List<Score>) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException i)
        {
            i.printStackTrace();
        } catch (ClassNotFoundException c)
        {
            c.printStackTrace();
        }
    }
}
