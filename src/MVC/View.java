package MVC;

import cocktails.BaseCocktail;
import cocktails.FancyCocktail;
import cocktails.FastCocktail;
import cocktails.Ingredient;

import java.util.Objects;
import java.util.Scanner;

public class View {
    private Scanner scanner = new Scanner(System.in);

    public void close() {
        scanner.close();
    }

    public void displayGreeting(String userName) {
        System.out.println("Добро пожаловать, " + userName);
    }

    public void displayText(String text) {
        System.out.print(text);
    }

    public String getLine() {
        return scanner.nextLine();
    }

    public int getInt() {
        return scanner.nextInt();
    }

    public void displayMenu(String right) {
        if (Objects.equals(right, "user")) {
            System.out.print(
                    """
                            Выйти из программы......................0
                            Прочитать все записи о коктейлях........1
                            Добавить новую запись о коктейле........2
                            Удалить запись о коктейле...............3
                            Обновить запись о коктейле..............4
                            """
            );
        } else if (Objects.equals(right, "root")) {
            System.out.print(
                    """
                            Выйти из программы....................0
                            Прочитать все записи о коктейлях......1
                            Добавить новую запись о коктейле......2
                            Удалить запись о коктейле.............3
                            Обновить запись о коктейле............4
                            Включить режим отладки................5
                            Запустить автотесты...................6
                            """
            );
        }
    }

    public void displayCocktailVarieties() {
        System.out.print(
                """
                        Выберите разновидность коктейля, запись
                        о котором вы хотите создать:
                        
                        BaseCocktail..........................1
                        FastCocktail..........................2
                        FancyCocktail.........................3
                        """
        );
    }

    public BaseCocktail inputBaseCocktail() {
        displayText("Введите целое число грамм льда: ");
        int ice_grams = Integer.parseInt(getLine());
        return new BaseCocktail(new Ingredient("ice", 0, ice_grams));
    }

    public BaseCocktail inputFastCocktail() {
        displayText("Введите целое число грамм льда: ");
        int ice_grams = Integer.parseInt(getLine());
        displayText("Введите целое число грамм водки: ");
        int vodka_grams = Integer.parseInt(getLine());
        displayText("Введите крепость водки (может быть дробным): ");
        double vodka_strength = Double.parseDouble(getLine());
        return new FastCocktail(
                new Ingredient("ice", 0, ice_grams),
                new Ingredient("vodka", vodka_strength, vodka_grams)
        );
    }

    public BaseCocktail inputFancyCocktail() {
        displayText("Введите целое число грамм льда: ");
        int ice_grams = Integer.parseInt(getLine());
        displayText("Введите целое число грамм водки: ");
        int vodka_grams = Integer.parseInt(getLine());
        displayText("Введите крепость водки (может быть дробным): ");
        double vodka_strength = Double.parseDouble(getLine());
        displayText("Введите целое число грамм текилы: ");
        int tequila_grams = Integer.parseInt(getLine());
        displayText("Введите крепость текилы (может быть дробным): ");
        double tequila_strength = Double.parseDouble(getLine());
        displayText("Введите целое число грамм сока: ");
        int juice_grams = Integer.parseInt(getLine());
        return new FancyCocktail(
                new Ingredient("ice", 0, ice_grams),
                new Ingredient("vodka", vodka_strength, vodka_grams),
                new Ingredient("juice", 0, juice_grams),
                new Ingredient("tequila", tequila_strength, tequila_grams)
        );
    }
}
