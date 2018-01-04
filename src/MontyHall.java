import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Define logic for simulating monty hall problem
 */
class MontyHallSimulation {
    private boolean[] cards = new boolean[] {false,false,false}; // true = car, false = goat
    private int selection = 0;
    private int goat = 0;

    public MontyHallSimulation() {
        cards[new Random().nextInt(3)] = true;
    }

    private void simulatePick() {
        selection = new Random().nextInt(3);
    }

    private void simulateShowGoat() {
        for (int i = 0; i < cards.length; i++) {
            if (!cards[i] && i != selection) {
                goat = i;
                return;
            }
        }
    }

    private void simulateSwitch() {
        selection = IntStream.range(0, cards.length)
                .filter((i) -> i != goat && i != selection)
                .findFirst()
                .getAsInt();
    }

    private boolean won() {
        return cards[selection];
    }

    static boolean runSimulation(boolean simulateSwitch) {
        MontyHallSimulation montyHallSimulation = new MontyHallSimulation();
        montyHallSimulation.simulatePick();
        montyHallSimulation.simulateShowGoat();
        if (simulateSwitch) montyHallSimulation.simulateSwitch();
        return montyHallSimulation.won();
    }

    public static void main(String args[]) {
        // CONFIGURATION
        int simulationCount = 100000;

        AtomicInteger winsNoSwitchStrategy = new AtomicInteger(0);
        IntStream.range(0, simulationCount).parallel().forEach(i -> {
            if (MontyHallSimulation.runSimulation(false)) {
                winsNoSwitchStrategy.incrementAndGet();
            }
        });

        AtomicInteger winsSwitchStrategy = new AtomicInteger(0);
        IntStream.range(0, simulationCount).parallel().forEach(i -> {
            if (MontyHallSimulation.runSimulation(true)) {
                winsSwitchStrategy.incrementAndGet();
            }
        });

        System.out.println("Total Simulations: " + simulationCount + "\n" +
                "Total Wins With No Switch: " + winsNoSwitchStrategy.get() + ", or " +
                Math.round(((float)winsNoSwitchStrategy.get()/(float)simulationCount)*100) + "%\n" +
                "Wins With Switch Strategy: " + winsSwitchStrategy.get() + ", or " +
                Math.round(((float)winsSwitchStrategy.get()/(float)simulationCount)*100) + "%\n");

    }
}
