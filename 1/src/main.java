import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

class Main
{
    // Главный метод главного класса
    public static void main(String[]args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Food[] breakfast = new Food[20];
        int itemsSoFar = 0;
        int i = 0;
        boolean sorted = false;
        boolean calories = false;
        if(args[i].charAt(0) =='-')
        {
            if(args[i].compareTo("-sorted") == 0)
            {
                sorted = true;
                ++i;
            }
            else
            {
                if(args[i].compareTo("-calories") == 0)
                {
                    calories = true;
                    ++i;
                }
            }

            if((calories || sorted) && args[i].charAt(0) =='-' )
            {
                if(args[i].compareTo("-sorted") == 0)
                {
                    sorted = true;
                    ++i;
                }
                else
                {
                    if(args[i].compareTo("-calories") == 0)
                    {
                        calories = true;
                        ++i;
                    }
                }
            }
        }

        for (; i < args.length && itemsSoFar < 20; ++i) {
            String[] parts = args[i].split("/");
            try {
                Class myClass = Class.forName(parts[0]);
                if (parts.length == 3) {
                    Constructor constructor = myClass.getConstructor(String.class, String.class);
                    breakfast[itemsSoFar] = (Food) constructor.newInstance(parts[1], parts[2]);
                } else if (parts.length == 2) {
                    Constructor constructor = myClass.getConstructor(String.class);
                    breakfast[itemsSoFar] = (Food) constructor.newInstance(parts[1]);
                } else {
                    Constructor constructor = myClass.getConstructor();
                    breakfast[itemsSoFar] = (Food) constructor.newInstance();
                }
                ++itemsSoFar;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                System.out.println(parts[0] + " cannot be added");
            }
        }
        Food f = breakfast[0];
        int equals = 0;
        for (Food item : breakfast) {
            if (item != null)
                if(item.equals(f))
                {
                    ++equals;
                }
            else
                break;
        }
        System.out.println("Number of type " + f + " = " + equals);
        if(calories)
        {
            int sum = 0;
            for (Food item : breakfast) {
                if(item != null) {
                    sum = sum + item.calculateCalories();
                }
                else
                    break;
            }
            System.out.println("Calories in breakfast = " + sum);
        }
        if(sorted)
        {
            Arrays.sort(breakfast,new FoodComparator());
            System.out.println("Breakfast is sorted!");
        }
        // Перебор всех элементов массива
        for (Food item : breakfast){
            if (item != null)
                // Если элемент не null – употребить продукт
                item.consume();
            else
                // Если дошли до элемента null – значит достигли конца
                // списка продуктов, ведь 20 элементов в массиве было
                // выделено с запасом, и они могут быть не
                // использованы все
                break;
        }

        System.out.println("Good luck!");

    }
}
