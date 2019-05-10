package bag.numbering;

import java.util.ArrayList;

public class Kapelle {

    private String bez;
    private int mNr;
    private int frStNr;
    private int spStNr;
    private ArrayList<Kapelle> dependencies;

    Kapelle(String bez, int mNr, int frStNr, int spStNr) {
        this.bez = bez;
        this.mNr = mNr;
        this.frStNr = frStNr;
        this.spStNr = spStNr;
        this.dependencies = new ArrayList<>();
    }

    public Kapelle(String bez, int mNr, int frStNr, int spStNr, ArrayList<Kapelle> dependencies) {
        this.bez = bez;
        this.mNr = mNr;
        this.frStNr = frStNr;
        this.spStNr = spStNr;
        this.dependencies = dependencies;
    }

    public ArrayList<Kapelle> getDependencies() {
        return dependencies;
    }

    public void setDependencies(ArrayList<Kapelle> dependencies) {
        this.dependencies = dependencies;
    }

    public String getBez() {
        return this.bez;
    }

    public int getMNr() {
        return this.mNr;
    }

    public int getFrStNr() {
        return this.frStNr;
    }

    public int getSpStNr() {
        return this.spStNr;
    }

    public int getStNrDif() {
        return this.spStNr - this.frStNr;
    }

    public void setmNr(int mNr) {
        this.mNr = mNr;
    }

    public void setFrStNr(int frStNr) {
        this.frStNr = frStNr;
    }

    public void setSpStNr(int spStNr) {
        this.spStNr = spStNr;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Kapelle) {
            Kapelle kap = (Kapelle) obj;
            return this.mNr == kap.mNr;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Kapelle{" +
                "bez='" + bez + '\'' +
                ", mNr=" + mNr +
                ", frStNr=" + frStNr +
                ", spStNr=" + spStNr +
                ", dependencies=" + dependencies +
                '}';
    }
}
