package bag.numbering;

import java.util.Objects;
import java.util.UUID;

public class Kapelle {
    private String id;
    private String bez;
    private int mNr;
    private int frStNr;
    private int spStNr;
    private boolean active;

    public Kapelle() {
        this.id = UUID.randomUUID().toString();
    }

    public Kapelle(String bez, int mNr) {
        this.id = UUID.randomUUID().toString();
        this.bez = bez;
        this.mNr = mNr;
        this.active = true;
    }

    public Kapelle(String bez, int mNr, int frStNr, int spStNr) {
        this.id = UUID.randomUUID().toString();
        this.bez = bez;
        this.mNr = mNr;
        this.frStNr = frStNr;
        this.spStNr = spStNr;
        this.active = true;
    }

    public Kapelle(Kapelle capToCopy) {
        //omitted since we copy the id here
        //this();
        this.bez = capToCopy.getBez();
        this.mNr = capToCopy.getMNr();
        this.frStNr = capToCopy.getFrStNr();
        this.spStNr = capToCopy.getSpStNr();
        this.active = capToCopy.isActive();
        this.setId(capToCopy.getId());
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
        return id.equals(kapelle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
