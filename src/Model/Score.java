package Model;


import java.io.Serializable;


/**
 * Klasa przechowujaca wynik gracza.
 */
public class Score implements Comparable<Score> , Serializable{
    int score;
    String name;

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }

    /**
     * Funkcja sluzaca do porownywania.
     * @param o Obiekty z ktym bedzie porownywany obecny
     * @return Zwraca 1 jezeli wynik obecnego jest mniejszy, -1 jezeli wiekszy, 0 gdy rowne.
     */
    public int compareTo(Score o)
   {
        return score < o.score ? 1 : score > o.score ? -1 : 0;
    }

    /**
     * Porownanie obecnego obiektu z innym wynikiem.
     * @param s Wynik
     * @return Zwraca 1 jezeli wynik obecnego jest mniejszy, -1 jezeli wiekszy, 0 gdy rowne.
     */
    public int compareTo(int s)
    {
        return score < s ? -1 : score > s ? 1 : 0;
    }

    /**
     * Zwraca score
     * @return Wartosc Score obecnego obiektu
     */
    public String getScore()
    {
        return name + " " + score;
    }
}