package bag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StNrAlgorithm {

    private ArrayList<Kapelle> kapellen; //Teilnehmende Kapellen
    private Map<Kapelle, ArrayList<Kapelle>> kapellenMitAbhaengigkeit; //Kapellen mit Doppelmusikern

    private ArrayList<Kapelle> kritischeKapellen1; //Kapellen mit Doppelmusikern oder großer Startnummern-Einschränkungen
    private ArrayList<Kapelle> kritischeKapellen2; //Kapellen mit mittlerer Startnummern-Einschränkungen
    private ArrayList<Kapelle> kritischeKapellen3; //Kapellen ohne Startnummern-Einschränkungen

    private Kapelle[] startnummern; //Startnummern
    private ArrayList<Integer> vergebeneStNr; //bereits vergebene Startnummern

    private static Random generator; //Zufallszahlengenerator
    private boolean zugeteilt;
    private int z; //Zufallszahl

    //Festlegung der Anzahl der teilnehmenden Kapellen
    private final int ANZAHLKAPELLEN = 21; //Anzahl der teilnehmenden Kapellen

    public StNrAlgorithm() {
        //Festlegung der Anzahl der teilnehmenden Kapellen
        //this.ANZAHLKAPELLEN = 21;
        generator = new Random();

        //Erstellen aller teilnehmenden Kapellen
        Kapelle goellersdorf = new Kapelle("Blasmusikkapelle Göllersdorf", 311, 1, ANZAHLKAPELLEN);
        Kapelle guntersdorf = new Kapelle("Trachtenkapelle Guntersdorf", 101, 1, ANZAHLKAPELLEN);
        Kapelle hadres = new Kapelle("Dorfmusik Hadres im Pulkautal", 345, 1, ANZAHLKAPELLEN);
        Kapelle hardegg = new Kapelle("Waldviertler Grenzlandkapelle Hardegg", 71, 1, 10);
        Kapelle heldenberg = new Kapelle("Jugend-Radetzkykapelle Heldenberg", 999, 1,
            ANZAHLKAPELLEN); //Nummer fehlt
        Kapelle hollabrunn = new Kapelle("Stadtmusik Hollabrunn", 998, 1,
            ANZAHLKAPELLEN); //Nummer fehlt
        Kapelle mailberg = new Kapelle("Weinviertler Hauerkapelle Mailberg", 340, 1, 10);
        Kapelle maissau = new Kapelle("Stadtmusik Maissau", 238, 1, 2);
        Kapelle obermarkersdorf = new Kapelle("Musikkapelle Obermarkersdorf", 248, 1, 15);
        Kapelle pulkau = new Kapelle("Trachtenkapelle Pulkau", 187, 1, ANZAHLKAPELLEN);
        Kapelle ravelsbach = new Kapelle("Jugend-Deutschmeisterkapelle Ravelsbach", 338, 1,
            ANZAHLKAPELLEN);
        Kapelle retz = new Kapelle("Stadtkapelle Retz", 278, 1, 1);
        Kapelle retzbach = new Kapelle("Trachtenkapelle Retzbach", 191, 1, ANZAHLKAPELLEN);
        Kapelle roeschitz = new Kapelle("Musikverein Röschitz", 122, 1, 5);
        Kapelle roseldorf = new Kapelle("Musikkapelle Roseldorf", 378, 1, ANZAHLKAPELLEN);
        Kapelle schmidatal = new Kapelle("Musikverein Schmidatal", 154, 1, 7);
        Kapelle theras = new Kapelle("Trachtenkapelle Theras", 245, 1, ANZAHLKAPELLEN);
        Kapelle unterduernbach = new Kapelle("Musikverein Unterdürnbach", 997, 1,
            ANZAHLKAPELLEN); //Nummer fehlt
        Kapelle zellerndorf = new Kapelle("Musikkapelle Zellerndorf", 170, 1, ANZAHLKAPELLEN);
        Kapelle ziersdorf = new Kapelle("Trachtenkapelle Ziersdorf und Umgebung", 369, 1, 10);
        Kapelle wullersdorf = new Kapelle("Jugend-Musikverein Wullersdorf", 435, 1, ANZAHLKAPELLEN);

        //Alle Kapellen-Objekte zu Liste aller Kapellen hinzufuegen
        this.kapellen = new ArrayList<Kapelle>();
        this.kapellen.addAll(Arrays.asList(goellersdorf, guntersdorf, hadres, hardegg, heldenberg,
            hollabrunn, mailberg, maissau, obermarkersdorf, pulkau, ravelsbach, retz,
            retzbach, roeschitz, roseldorf, schmidatal, theras, unterduernbach, zellerndorf,
            ziersdorf, wullersdorf));

        //Check if the starting-number range of every band is valid (latest number must be greater
        // than earliest number
        this.pruefeStNr(kapellen);

        //Hinzufügen aller Kapellen mit Doppelmusikern
        this.kapellenMitAbhaengigkeit = new HashMap<Kapelle, ArrayList<Kapelle>>();
        kapellenMitAbhaengigkeit
            .put(hardegg, new ArrayList<>(Arrays.asList(ziersdorf, obermarkersdorf)));
        kapellenMitAbhaengigkeit.put(ziersdorf, new ArrayList<>(Arrays.asList(hardegg)));
        kapellenMitAbhaengigkeit.put(obermarkersdorf, new ArrayList<>(Arrays.asList(hardegg)));
        kapellenMitAbhaengigkeit.put(schmidatal, new ArrayList<>(Arrays.asList(mailberg)));
        kapellenMitAbhaengigkeit.put(mailberg, new ArrayList<>(Arrays.asList(schmidatal)));
        kapellenMitAbhaengigkeit.put(hadres, new ArrayList<>(Arrays.asList(goellersdorf)));
        kapellenMitAbhaengigkeit.put(goellersdorf, new ArrayList<>(Arrays.asList(hadres)));

        //Überprüfung ob eine Kapelle zu sich selbst eine Abhängigkeit hat
        //this would be an invalid case
        this.pruefeAbhaengigkeit(kapellenMitAbhaengigkeit);

        //Erstellen der Listen zur Klassifizierung der Kapellen nach deren kritischen Ausprägung
        this.kritischeKapellen1 = new ArrayList<Kapelle>();
        this.kritischeKapellen2 = new ArrayList<Kapelle>();
        this.kritischeKapellen3 = new ArrayList<Kapelle>();

        //Aufruf der Methode zur Einteilung der Kapellen
        this.kritischeKlassifizierung();

        //Sortieren der kritischen Listen aufsteigend nach der Startnummern-Differenz
        this.sort(kritischeKapellen1);
        this.sort(kritischeKapellen2);
        
        /*Erstellen des Startnummern-Arrays
        kapellen.size() + 1 cause kapellen[0] stays empty, so that kapellen[1] contains the kapelle
        with starting number 1 and so on
         */
        this.startnummern = new Kapelle[kapellen.size() + 1];

        //Erstellen der Liste mit bereits vergebenen Startnummern
        this.vergebeneStNr = new ArrayList<Integer>();

        this.zugeteilt = false;
        this.z = 0;
    }

    public boolean starteAlgorithmus(boolean showStartingNumbers, boolean resetParameter) {
        //call methods to assign starting numbers and if flags are set print the results

        if (resetParameter) {
            this.vergebeneStNr = new ArrayList<Integer>();
            this.startnummern = new Kapelle[kapellen.size() + 1];

            this.zugeteilt = false;
            this.z = 0;
        }

        //start assigning starting numbers
        if (!teileStartnummernZu()) {
            System.out.println("Mit der begonnenen Zuweisung konnte kein passendes Ergebnis für"
                + " alle Kapellen gefunden werden, breche diesen Versuch ab...");
            return false;
        }

        if (showStartingNumbers) {
            this.zeigeStartnummernAn();
        }
        return true;
    }

    private void pruefeStNr(ArrayList<Kapelle> liste) {
        boolean invalid = false;
        for (Kapelle k : liste) {
            if (k.getStNrDif() < 0) {
                System.out.println(k.getBez() + " hat ungültigen Startnummernbereich (frühest"
                    + " möglich = " + k.getFrStNr() + " spätest möglich = " + k.getSpStNr() + ").");
                invalid = true;
            }
        }
        if (invalid) {
            System.out.println("\nInvalid starting range(s) - exiting program...");
            System.exit(1);
        }
    }

    private void pruefeAbhaengigkeit(Map<Kapelle, ArrayList<Kapelle>> map) {
        boolean invalid = false;
        for (Kapelle k : map.keySet()) {
            for (Kapelle j : map.get(k)) {
                if (k.equals(j)) {
                    System.out.println(k.getBez() + " hat Abhängigkeit zu sich selbst.");
                    invalid = true;
                }
            }
        }
        if (invalid) {
            System.out.println("\nInvalid dependence(s) detected - exiting program...");
            System.exit(1); //exit the program if there is an invalid dependence
        }
    }

    private void kritischeKlassifizierung() {
        for (Kapelle k : kapellen) {
            boolean hinzu = false;
            for (Kapelle j : kapellenMitAbhaengigkeit.keySet()) {
                if (k.equals(j) || k.getStNrDif() < 5) {
                    kritischeKapellen1.add(k);
                    hinzu = true;
                    break;
                }
            }
            if (!hinzu) {
                if (k.getStNrDif() < ANZAHLKAPELLEN) {
                    kritischeKapellen2.add(k);
                } else {
                    kritischeKapellen3.add(k);
                }
            }
        }
    }

    private void sort(ArrayList<Kapelle> liste) {
        int n = liste.size();
        int m;
        do {
            m = 1;
            for (int i = 0; i < n - 1; i++) {
                if (liste.get(i).getStNrDif() > liste.get(i + 1).getStNrDif()) {
                    Collections.swap(liste, i, i + 1);
                    m = i + 1;
                }
            }
            n = m;
        } while (n > 1);
    }

    private boolean teileStartnummernZu() {
        for (Kapelle k : kritischeKapellen1) {
            if (kapellenMitAbhaengigkeit.containsKey(k)) {
                if (!ermittleStartnummerI(k)) {
                    //System.out.println("Exit detected - cancel this try");
                    return false;
                }
            } else {
                ermittleStartnummerII(k);
            }
        }

        for (Kapelle k : kritischeKapellen2) {
            ermittleStartnummerII(k);
        }

        for (Kapelle k : kritischeKapellen3) {
            ermittleStartnummerII(k);
        }
        return true;
    }

    private boolean ermittleStartnummerI(Kapelle k) {
        zugeteilt = false;
        boolean abstandNichtAusreichend;

        int attempt_counter = 0;

        while (!zugeteilt) {
            attempt_counter++;
            abstandNichtAusreichend = false;
            if (!erzeugeStartnummer(k)) {
                continue;
            } else {
                //System.out.println("startnummer "+z+" probieren für "+k);
                for (int j = z - 2; j <= z + 2; j++) {
                    if (j < 1) {
                        j = 1;
                    }
                    if (j == z) {
                        j++;
                    }
                    if (j >= startnummern.length) {
                        break;
                    }
                    for (Kapelle l : kapellenMitAbhaengigkeit.get(k)) {
                        if (l.equals(startnummern[j])) {
                            abstandNichtAusreichend = true;
                            break;
                        }
                    }
                    if (abstandNichtAusreichend) {
                        break;
                    }
                }
            }
            if (!abstandNichtAusreichend) {
                zugeteilt = true;
                startnummern[z] = k;
                vergebeneStNr.add(z);
            } else {
                if (attempt_counter > 200) {
                    System.out.println("\nEs wurden bereits 200 Versuche für eine Startnummer für"
                        + " " + k.getBez() + " getätigt, Abbruch folgt.");
                    return false;
                }
            }
        }
        return true;
    }

    private void ermittleStartnummerII(Kapelle k) {
        zugeteilt = false;
        while (!zugeteilt) {
            if (erzeugeStartnummer(k)) {
                zugeteilt = true;
                startnummern[z] = k;
                vergebeneStNr.add(z);
            }
        }
    }

    private boolean erzeugeStartnummer(Kapelle k) {
        z = generator.nextInt(k.getStNrDif() + 1) + k.getFrStNr();
        for (int i : vergebeneStNr) {
            if (z == i) {
                return false;
            }
        }
        return true;
    }

    private void zeigeStartnummernAn() {
        System.out.println("Startreihenfolge für die Marschmusikbewertung 2018");
        System.out.println();
        for (int i = 1; i < startnummern.length; i++) {
            System.out.println(i + ". " + startnummern[i].getBez());
        }
    }
}