package bag.numbering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

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

    public static ArrayList<Kapelle> deepCopyOfActiveOnes(ArrayList<Kapelle> allKapellen) {
        ArrayList<Kapelle> resultList = new ArrayList<>();
        for (Kapelle kap : allKapellen) {
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
        for(Kapelle dep : lastUpdatedDep) {
            dependencies.computeIfAbsent(dep, kl -> new ArrayList<>());
            if(!dependencies.get(dep).contains(lastUpdated)) {
                dependencies.get(dep).add(lastUpdated);
            }
        }
    }
}
