package bag.numbering;

public class Kapelle {

    private String bez;
    private int mNr;
    private int frStNr;
    private int spStNr;
    private boolean active;

    Kapelle(String bez, int mNr, int frStNr, int spStNr) {
        this.bez = bez;
        this.mNr = mNr;
        this.frStNr = frStNr;
        this.spStNr = spStNr;
        this.active = true;
    }

    public Kapelle(String bez, int mNr, int frStNr, int spStNr, boolean active) {
        this.bez = bez;
        this.mNr = mNr;
        this.frStNr = frStNr;
        this.spStNr = spStNr;
        this.active = active;
    }

    public void setBez(String bez) {
        this.bez = bez;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
                ", active=" + active +
                '}';
    }
}
