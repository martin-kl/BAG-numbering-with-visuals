package bag;

import javafx.event.ActionEvent;

public class MainController {

    private static final int LOOP_TIMES = 1000;

    public void startAlgorithmNormally(ActionEvent actionEvent) {
        System.out.println("start normally");
        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm();

        stNrAlgorithm.starteAlgorithmus(true, false);
    }

    public void startAlgorithmLoop(ActionEvent actionEvent) {
        System.out.println("start in loop");
        StNrAlgorithm stNrAlgorithm = new StNrAlgorithm();
        int posCounter = 0;
        for (int i = 0; i < LOOP_TIMES - 1; i++) {
            System.out.println("\n\t\tDurchlauf Nummer = " + (i + 1));
            if (stNrAlgorithm.starteAlgorithmus(false, true)) {
                posCounter++;
            }
        }
        System.out.println("\n\t\tDurchlauf Nummer = " + LOOP_TIMES);
        if (stNrAlgorithm.starteAlgorithmus(true, true)) {
            posCounter++;
        }
        System.out.println("\n\tGesamtanzahl der Zuweisungen: " + LOOP_TIMES);
        System.out.println("\tDavon gelungenen Zuweisungen: " + posCounter);
        System.out.println("\tDaraus folgt: " + (LOOP_TIMES - posCounter)
            + " Zuweisungen sind nicht gelungen!");

    }
}
