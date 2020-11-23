public class Cocktail extends Food{

    private String drink;
    private String fruit;

    public Cocktail(String _drink, String _fruit) {
        super("Cocktail");
        drink = _drink;
        fruit = _fruit;
    }

    public Cocktail(String _drink) {
        super("Cocktail");
        drink = _drink;
        fruit = null;
    }

    public void consume() {
        System.out.println(this + " drunk");
    }

    public int calculateCalories() {
        return (fruit == null ? 0 : 100) + 100 ;
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Food)) return false; // Шаг 1
        if (name==null || ((Food)arg0).name==null) return false; // Шаг 2
        return name.equals(((Food)arg0).name) &&
                drink.equals(((Cocktail)arg0).drink) &&
                (fruit==null && ((Cocktail)arg0).fruit == null ||
                        fruit!=null && ((Cocktail)arg0).fruit != null && fruit.equals(((Cocktail)arg0).fruit)); // Шаг 3
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    @Override
    public String toString()
    {
        return name + " with " + drink + (fruit == null ? "" : " and " + fruit);
    }
}
