public class Kapelle {

    private String bez;
    private int mNr;
    private int frStNr;
    private int spStNr;

    Kapelle(String bez, int mNr, int frStNr, int spStNr) {
        this.bez = bez;
        this.mNr = mNr;
        this.frStNr = frStNr;
        this.spStNr = spStNr;
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

    public boolean equals(Object obj) {
        if (obj instanceof Kapelle) {
            Kapelle kap = (Kapelle) obj;
            return this.mNr == kap.mNr;
        }
        return false;
    }

    public String toString() {
        return this.bez + " - " + "Frueheste StNr.: " + this.frStNr + " / " + "Spaeteste StNr.: "
            + this.spStNr;
    }
}
