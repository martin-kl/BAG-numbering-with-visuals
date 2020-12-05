package bag.numbering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class StNrAlgorithm {

    public static final int MAX_TRIES = 10;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final ArrayList<Kapelle> kapellen; //Teilnehmende Kapellen
    private final Map<Kapelle, ArrayList<Kapelle>> kapellenMitAbhaengigkeit; //Kapellen mit Doppelmusikern

    private final ArrayList<Kapelle> kritischeKapellen1; //Kapellen mit Doppelmusikern oder großer Startnummern-Einschränkungen
    private final ArrayList<Kapelle> kritischeKapellen2; //Kapellen mit mittlerer Startnummern-Einschränkungen
    private final ArrayList<Kapelle> kritischeKapellen3; //Kapellen ohne Startnummern-Einschränkungen

    private Kapelle[] startingNumbers; //Startnummern
    private ArrayList<Integer> vergebeneStNr; //bereits vergebene Startnummern

    private static Random generator; //Zufallszahlengenerator
    private boolean zugeteilt;
    private int z; //Zufallszahl

    public StNrAlgorithm(ArrayList<Kapelle> kapellen, Map<Kapelle, ArrayList<Kapelle>> dependencies) {
        this.kapellen = kapellen;
        this.kapellenMitAbhaengigkeit = dependencies;

        generator = new Random();

        //Erstellen der Listen zur Klassifizierung der Kapellen nach deren kritischen Ausprägung
        this.kritischeKapellen1 = new ArrayList<>();
        this.kritischeKapellen2 = new ArrayList<>();
        this.kritischeKapellen3 = new ArrayList<>();
    }

    public void init() {
        //Aufruf der Methode zur Einteilung der Kapellen
        this.kritischeKlassifizierung();

        //Sortieren der kritischen Listen aufsteigend nach der Startnummern-Differenz
        this.sort(kritischeKapellen1);
        this.sort(kritischeKapellen2);

        /*Erstellen des Startnummern-Arrays
        kapellen.size() + 1 cause kapellen[0] stays empty, so that kapellen[1] contains the kapelle
        with starting number 1 and so on
         */
        this.startingNumbers = new Kapelle[kapellen.size() + 1];

        //Erstellen der Liste mit bereits vergebenen Startnummern
        this.vergebeneStNr = new ArrayList<>();

        this.zugeteilt = false;
        this.z = 0;
    }

    public boolean startAlgorithmOnce() {
        this.vergebeneStNr = new ArrayList<>();
        this.startingNumbers = new Kapelle[kapellen.size() + 1];

        this.zugeteilt = false;
        this.z = 0;

        //start assigning starting numbers
        if (!teileStartnummernZu()) {
            logger.warning("Mit der begonnenen Zuweisung konnte kein passendes Ergebnis für"
                    + " alle Kapellen gefunden werden, breche diesen Versuch ab...");
            return false;
        }
        return true;
    }

    /**
     * This method calls the assignment method till it gets a positive assignment, max times=MAX_TRIES
     */
    public boolean startAlgorithmLoop() {
        boolean assignmentFound = false;
        int attempt_counter = 0;
        while (!assignmentFound && attempt_counter < MAX_TRIES) {
            this.vergebeneStNr = new ArrayList<>();
            this.startingNumbers = new Kapelle[kapellen.size() + 1];
            this.zugeteilt = false;
            this.z = 0;

            if (teileStartnummernZu()) {
                //found possible asssignment, exit this method
                assignmentFound = true;
            } else {
                attempt_counter++;
            }
        }

        if (assignmentFound) {
            //found solution, print it if necessary
            logger.info("Ergebnis in Versuch Nummer " + (attempt_counter + 1) + " gefunden.");
            return true;
        } else {
            //no solution found, print error message
            logger.severe("\n\n\t\t" + MAX_TRIES + " Versuche um Startnummer zu belegen wurden durchgeführt" +
                    "aber keine Belegung hat funktioniert ==> Programm beendet sich.\n\n");
        }
        return false;
    }


    private void kritischeKlassifizierung() {
        for (Kapelle k : kapellen) {
            boolean added = false;
            for (Kapelle j : kapellenMitAbhaengigkeit.keySet()) {
                if (k.equals(j) || k.getStNrDif() < 5) {
                    kritischeKapellen1.add(k);
                    added = true;
                    break;
                }
            }
            if (!added) {
                if (k.getStNrDif() < kapellen.size() - 1) {
                    kritischeKapellen2.add(k);
                } else {
                    kritischeKapellen3.add(k);
                }
            }
        }
    }

    private void sort(ArrayList<Kapelle> list) {
        int n = list.size();
        int m;
        do {
            m = 1;
            for (int i = 0; i < n - 1; i++) {
                if (list.get(i).getStNrDif() > list.get(i + 1).getStNrDif()) {
                    Collections.swap(list, i, i + 1);
                    m = i + 1;
                }
            }
            n = m;
        } while (n > 1);
    }

    private boolean teileStartnummernZu() {
        for (Kapelle k : kritischeKapellen1) {
            if (!kapellenMitAbhaengigkeit.containsKey(k)) {
                if (!ermittleStartnummerII(k)) {
                    return false;
                }
            } else {
                if (!ermittleStartnummerI(k)) {
                    //System.out.println("Exit detected - cancel this try");
                    return false;
                }
            }
        }

        for (Kapelle k : kritischeKapellen2) {
            if (!ermittleStartnummerII(k)) {
                return false;
            }
        }

        for (Kapelle k : kritischeKapellen3) {
            if (!ermittleStartnummerII(k)) {
                return false;
            }
        }
        return true;
    }

    private boolean ermittleStartnummerI(Kapelle k) {
        zugeteilt = false;

        int attempt_counter = 0;

        while (!zugeteilt) {
            attempt_counter++;
            if (attempt_counter > 200) {
                //stop it
                logger.warning("ermittleStartnummerI\tEs wurden bereits 200 Versuche für eine Startnummer für"
                        + " " + k.getBez() + " getätigt, Abbruch folgt.");
                return false;
            }
            if (!generateStartingNumber(k))
                continue;
            if (attempt_counter < 200) {
                //check if there is another band with a dependence in range 3
                if (!criticalBandsNotPossible(3, k)) {
                    zugeteilt = true;
                    startingNumbers[z] = k;
                    vergebeneStNr.add(z);
                    return true;
                }
            } else {
                //check if there is another band with a dependence in range 2
                if (!criticalBandsNotPossible(2, k)) {
                    logger.info("Zuweisung für " + k.getBez() + " gefunden, jedoch nur mit 2 Abstand zu einer " +
                            "anderen Kapelle mit Abhängigkeit!");
                    zugeteilt = true;
                    startingNumbers[z] = k;
                    vergebeneStNr.add(z);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean criticalBandsNotPossible(int differenceBetweenBands, Kapelle k) {
        for (int j = z - differenceBetweenBands; j <= z + differenceBetweenBands; j++) {
            if (j < 1) {
                continue;
            }
            if (j == z) {
                j++;
            }
            if (j >= startingNumbers.length) {
                break;
            }
            for (Kapelle l : kapellenMitAbhaengigkeit.get(k)) {
                if (l.equals(startingNumbers[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ermittleStartnummerII(Kapelle k) {
        zugeteilt = false;
        //the attempt_counter is added in case that two orchestras have just one possible value
        int attempt_counter = 0;
        while (!zugeteilt && attempt_counter < 400) {
            if (generateStartingNumber(k)) {
                zugeteilt = true;
                startingNumbers[z] = k;
                vergebeneStNr.add(z);
            }
            attempt_counter++;
        }
        if (!zugeteilt) {
            //algorithm was not able to find possible starting numbers
            //most likely 2 orchestras have just one possible starting number (the same)
            logger.warning("ermittleStartnummer2\tEs wurden bereits 400 Versuche für eine Startnummer für"
                    + " " + k.getBez() + " getätigt, Abbruch folgt.");
            return false;
        }
        return true;
    }

    /**
     * generates a possible starting number between earliest possible and latest possible
     * then checks, if the number is already given to another orchestra and returns the negated value of this comparison
     */
    private boolean generateStartingNumber(Kapelle k) {
        z = generator.nextInt(k.getStNrDif() + 1) + k.getFrStNr();
        for (int i : vergebeneStNr) {
            if (z == i) {
                return false;
            }
        }
        return true;
    }

    public Kapelle[] getResult() {
        return startingNumbers;
    }
}