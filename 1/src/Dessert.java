public class Dessert extends Food{


     private String component1;
     private String component2;

    public Dessert(String _component1, String _component2)
    {
        super("Dessert");
        component1 = _component1;
        component2 =_component2;
    }

    public Dessert(String _component1)
    {
        super("Dessert");
        component1 = _component1;
        component2 = null;
    }

    public void consume() {
        System.out.println(this + " eaten");
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Food)) return false; // Шаг 1
        if (name==null || ((Food)arg0).name==null) return false; // Шаг 2
        return name.equals(((Food)arg0).name) &&
                component1.equals(((Dessert)arg0).component1) &&
                (component2 == null && ((Dessert)arg0).component2 == null
                        || component2 != null && ((Dessert)arg0).component2 != null && component2.equals(((Dessert)arg0).component2)); // Шаг 3
    }

    public int calculateCalories()
    {
        return  (component2 == null ? 0 : 100) + 200;
    }

    public String getComponent1() {
        return component1;
    }

    public void setComponent1(String component1) {
        this.component1 = component1;
    }

    public String getComponent2() {
        return component2;
    }

    public void setComponent2(String component2) {
        this.component2 = component2;
    }

    @Override
    public String toString ()
    {
        return name + " with " + component1 + (component2 == null ? "" : " and " + component2);
    }
}
