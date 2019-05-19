package bag.numbering;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final String FILE_PATH = "./data.json";
    private static final Type KAPELLEN_TYPE = new TypeToken<List<Kapelle>>() {
    }.getType();

    public static String pruefeStNr(List<Kapelle> list) {
        //first count active participants to be able to set latest starting number
        long participants = list.stream().filter(Kapelle::isActive).count();

        boolean invalid = false;
        for (Kapelle k : list) {
            if (k.getFrStNr() <= 0)
                k.setFrStNr(1);
            if (k.getSpStNr() == 0)
                k.setSpStNr((int) participants);
            if (k.getSpStNr() >= participants)
                k.setSpStNr((int) participants);

            if (k.getStNrDif() < 0) {
                System.err.println(k.getBez() + " hat ungültigen Startnummernbereich (frühest"
                        + " möglich = " + k.getFrStNr() + " spätest möglich = " + k.getSpStNr() + ").");
                invalid = true;
            }
        }
        if (invalid) {
            System.err.println("\nInvalid starting range(s) - exiting program...");
            //System.exit(1);
            return "Mindestens eine Kapelle hat einen ungültigen Startnummernbereich";
        }
        return "";
    }

    /**
     * Checks for no self-dependencies and further more adds dependencies in both directions if they are not yet set
     *
     * @param map
     * @return an empty string in case of no error or an error description
     */
    public static String pruefeAbhaengigkeit(Map<Kapelle, ArrayList<Kapelle>> map) {
        boolean invalid = false;
        for (Kapelle k : map.keySet()) {
            for (Kapelle j : map.get(k)) {
                if (k.equals(j)) {
                    System.err.println(k.getBez() + " hat Abhängigkeit zu sich selbst.");
                    invalid = true;
                }
                /*
                //TODO this is no longer needed since we use keepDependenciesUpToDate already beforehand
                //TODO but check this afterwards
                //check for dependency in both directions:
                map.computeIfAbsent(j, k1 -> new ArrayList<>());
                if (!map.get(j).contains(k)) {
                    map.get(j).add(k);
                }
                */
            }
        }
        if (invalid) {
            System.err.println("\nInvalid dependence(s) detected - exiting program...");
            return "Mindestens eine Kapelle hat eine Abhängigkeit zu sich selbst";
            //System.exit(1); //exit the program if there is an invalid dependence
        }
        return "";
    }

    public static ArrayList<Kapelle> deepCopyOfActiveOnes(List<Kapelle> kapellen) {
        ArrayList<Kapelle> resultList = new ArrayList<>();
        for (Kapelle kap : kapellen) {
            if (kap.isActive())
                resultList.add(new Kapelle(kap.getBez(), kap.getMNr(), kap.getFrStNr(), kap.getSpStNr(), kap.isActive()));
        }
        return resultList;
    }

    public static void keepDependenciesUpToDate(Map<Kapelle, ArrayList<Kapelle>> dependencies, Kapelle lastUpdated) {
        ArrayList<Kapelle> lastUpdatedDep = dependencies.get(lastUpdated);

        for (Map.Entry<Kapelle, ArrayList<Kapelle>> entry : dependencies.entrySet()) {
            if (entry.getKey() != lastUpdated) {
                if (entry.getValue().contains(lastUpdated)) {
                    //check if last updated also contains entry.getKey
                    if (!lastUpdatedDep.contains(entry.getKey())) {
                        //delete it in entry.getValue
                        entry.getValue().remove(lastUpdated);
                    }
                }
            }
        }
        //now look in the other direction: see if every lastUpdatedDep is also in the other ones
        for (Kapelle dep : lastUpdatedDep) {
            dependencies.computeIfAbsent(dep, kl -> new ArrayList<>());
            if (!dependencies.get(dep).contains(lastUpdated)) {
                dependencies.get(dep).add(lastUpdated);
            }
        }
    }

    public static void writeToFile(List<Kapelle> data) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(FILE_PATH);
        gson.toJson(data, writer);
        writer.flush();
        writer.close();
    }

    public static ArrayList<Kapelle> readFromFile() throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(FILE_PATH);
        return gson.fromJson(reader, KAPELLEN_TYPE);
    }

    public List<Kapelle> getAllParticipantsOLD() {
        //Create all participants
        Kapelle goellersdorf = new Kapelle("Blasmusikkapelle Göllersdorf", 311, 1, 5); //Festzelt(2)
        Kapelle guntersdorf = new Kapelle("Trachtenkapelle Guntersdorf", 101);
        Kapelle hadres = new Kapelle("Dorfmusik Hadres im Pulkautal", 345);
        Kapelle hardegg = new Kapelle("Waldviertler Grenzlandkapelle Hardegg", 71);
        //Kapelle heldenberg = new Kapelle("Jugend-Radetzkykapelle Heldenberg", 999, 1, PARTICIPANTS);
        //Kapelle hollabrunn = new Kapelle("Stadtmusik Hollabrunn", 998, 1, PARTICIPANTS);
        Kapelle mailberg = new Kapelle("Weinviertler Hauerkapelle Mailberg", 340, 4, 4); //wirklich 4. Kapelle?
        Kapelle maissau = new Kapelle("Stadtmusik Maissau", 238, 1, 1);         //Gastkapelle
        Kapelle obermarkersdorf = new Kapelle("Musikkapelle Obermarkersdorf", 248);
        Kapelle pulkau = new Kapelle("Trachtenkapelle Pulkau", 187, 1, 6); //Festzelt(3)
        Kapelle ravelsbach = new Kapelle("Jugend-Deutschmeisterkapelle Ravelsbach", 338);
        Kapelle retz = new Kapelle("Stadtkapelle Retz", 278);
        Kapelle retzbach = new Kapelle("Trachtenkapelle Retzbach", 191);
        Kapelle roeschitz = new Kapelle("Musikverein Röschitz", 122);
        //Kapelle roseldorf = new Kapelle("Musikkapelle Roseldorf", 378, 1, PARTICIPANTS);
        Kapelle schmidatal = new Kapelle("Musikverein Schmidatal & Musikkapelle Roseldorf", 154); //2018 gemeinsam angetreten
        Kapelle theras = new Kapelle("Trachtenkapelle Theras", 245);
        Kapelle unterduernbach = new Kapelle("Musikverein Unterdürnbach", 997);
        Kapelle wullersdorf = new Kapelle("Jugend-Musikverein Wullersdorf", 435);
        Kapelle zellerndorf = new Kapelle("Musikkapelle Zellerndorf", 170, 1, 2); //Festzelt(1)
        Kapelle ziersdorf = new Kapelle("Trachtenkapelle Ziersdorf und Umgebung", 369, 1, 8);
        Kapelle kirchberg = new Kapelle("Musikverein Kirchberg am Wagram", 999);
        Kapelle angerberg_mariastein = new Kapelle("Bundesmusikkapelle Angerberg/Mariastein", 999, 10, 0);

        return new ArrayList<>(Arrays.asList(goellersdorf, guntersdorf, hadres,
                hardegg, mailberg, maissau, obermarkersdorf, pulkau, ravelsbach, retz, retzbach, roeschitz, schmidatal,
                theras, unterduernbach, wullersdorf, zellerndorf, ziersdorf, kirchberg, angerberg_mariastein));
    }
}
