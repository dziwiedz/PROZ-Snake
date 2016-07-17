package Model;

/**
 * Dane zegara dla kontrolera.
 */
public class Clock
{

    private float millisPerCycle;
    private long lastUpdate;
    private int elapsedCycles;
    private float excessCycles;
    private boolean isPaused;

    /**
     * Tworzy nowy zegar i ustawia jego ilosc cykli na sekunde.
     * @param cyclesPerSecond Ilosc cykli na sekunde
     */
    public Clock(float cyclesPerSecond) {
        setCyclesPerSecond(cyclesPerSecond);
        reset();
    }

    /**
     * Ustawia ilosc cykli.
     * @param cyclesPerSecond Ilosc cykli na sekunde
     */
    public void setCyclesPerSecond(float cyclesPerSecond) {
        this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
    }

    /**
     * Resetuje statystyki zegara.
     */
    public void reset() {
        this.elapsedCycles = 0;
        this.excessCycles = 0.0f;
        this.lastUpdate = getCurrentTime();
        this.isPaused = false;
    }

    /**
     * Akualizacja statystyk zegara.
     * Moze nastapic tylko wtedy jezeli flaga isPaused jest false
     */
    public void update() {
        //Get the current time and calculate the delta time.
        long currUpdate = getCurrentTime();
        float delta = (float)(currUpdate - lastUpdate) + excessCycles;

        //Update the number of elapsed and excess ticks if we're not paused.
        if(!isPaused) {
            this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
            this.excessCycles = delta % millisPerCycle;
        }

        //Set the last update time for the next update cycle.
        this.lastUpdate = currUpdate;
    }

    /**
     * Wstrztmanie/Wznowienie odliczania zegara.
     * Podczas gdy jest wstrzymany zegar nie bedzie aktualizowac ilosc uplynietych cykli.
     * @param paused Czy zegar ma byc wstrzymany/wznowiony.
     */
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
    /**
     * Sprawdza czy uplynal cykl do tej pory.
     * Jezeli tak, ilosc cykli zosanie zmniejszona o 1.
     * @return Prawda lub falsz zaleznie od wyniku.
     */
    public boolean hasElapsedCycle() {
        if(elapsedCycles > 0) {
            this.elapsedCycles--;
            return true;
        }
        return false;
    }


    /**
     * Oblicza obecny czas w milisekundach.
     * @return Obecny czas w milisekundach.
     */
    private static final long getCurrentTime() {
        return (System.nanoTime() / 1000000L);
    }
}
