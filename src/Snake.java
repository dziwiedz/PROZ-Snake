import Model.*;
import View.*;
import Controller.*;
public class Snake
{
    /**
     * @author Lukasz Niedzwiedz
     * Tworzenie Modelu, Kontrolera i widoku.
     * Start gry jest w kontrolerze.
     * @param args Standardowe paramaetry podawane na wejscie.
     */
    public static void main(String[] args)
    {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
    }
}
