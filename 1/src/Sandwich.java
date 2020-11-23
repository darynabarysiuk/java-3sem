public class Sandwich extends Food {

    private String filling1;
    private String filling2;

    public Sandwich(String _filling1, String _filling2) {
        super("Sandwich");
        filling1 = _filling1;
        filling2 = _filling2;
    }

    public Sandwich(String _filling1) {
        super("Sandwich");
        filling1 = _filling1;
        filling2 = null;
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Food)) return false; // Шаг 1
        if (name==null || ((Food)arg0).name==null) return false; // Шаг 2
        return name.equals(((Food)arg0).name) &&
                filling1.equals(((Sandwich)arg0).filling1) &&
                (((Sandwich)arg0).filling2 == null && filling2 == null ||
                        ((Sandwich)arg0).filling2 != null && filling2 != null && filling2.equals(((Sandwich)arg0).filling2)); // Шаг 3
    }

    public void consume() {
        System.out.println(this + " eaten");
    }

    public int calculateCalories() {
        return (filling2 == null ? 0 : 50) + 200;
    }

    public String getFilling2() {
        return filling2;
    }

    public void setFilling2(String filling2) {
        this.filling2 = filling2;
    }

    public String getFilling1() {
        return filling1;
    }

    public void setFilling1(String filling1) {
        this.filling1 = filling1;
    }

    @Override
    public String toString()
    {
        return name + " with " + filling1 + (filling2 == null ? "" : " and " + filling2);
    }
}
