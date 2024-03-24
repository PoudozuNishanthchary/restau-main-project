import org.group.project.classes.FoodDrink;
import org.group.project.classes.Menu;
import org.group.project.test.generators.FoodDrinkGenerator;
import org.junit.jupiter.api.Test;

public class MenuTests {

    @Test
    public void testAddItemToMenu() {

        Menu menu = new Menu();
        FoodDrink foodItem1 = FoodDrinkGenerator.createFoodItem1();
        FoodDrink foodItem2 = FoodDrinkGenerator.createFoodItem2();
        FoodDrink foodItem3 = FoodDrinkGenerator.createFoodItem3();
        FoodDrink drinkItem1 = FoodDrinkGenerator.createDrinkItem1();
        FoodDrink drinkItem2 = FoodDrinkGenerator.createDrinkItem2();
        FoodDrink drinkItem3 = FoodDrinkGenerator.createDrinkItem3();

        menu.addItemToMenu(foodItem1);
        menu.addItemToMenu(foodItem2);
        menu.addItemToMenu(foodItem3);
        menu.addItemToMenu(drinkItem1);
        menu.addItemToMenu(drinkItem2);
        menu.addItemToMenu(drinkItem3);

        System.out.println(menu.getMenuOfItems());

    }


}