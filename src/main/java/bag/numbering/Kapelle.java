package bag.numbering;

import java.util.Objects;

public class Kapelle {

    private String bez;
    private int mNr;
    private int frStNr;
    private int spStNr;
    private boolean active;

    public Kapelle() {
        this.mNr = -1;
    }

    public Kapelle(String bez) {
        this.bez = bez;
        this.mNr = -1;
        this.active = true;
    }

    public Kapelle(String bez, int mNr) {
        this.bez = bez;
        this.mNr = mNr;
        this.active = true;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kapelle kapelle = (Kapelle) o;
        return mNr == kapelle.mNr &&
                frStNr == kapelle.frStNr &&
                spStNr == kapelle.spStNr &&
                active == kapelle.active &&
                Objects.equals(bez, kapelle.bez);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bez, mNr, frStNr, spStNr, active);
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
