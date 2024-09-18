public class Main {
    public static void main(String[] args) {
        Ingredient ice = new Ingredient("лёд", 0, 250);
        Ingredient vodka = new Ingredient("водка", 0.4, 100);
        Ingredient juice = new Ingredient("сок", 0, 200);
        Ingredient tequila = new Ingredient("текила", 0.3, 100);

        BaseCocktail cocktail = new BaseCocktail(ice);

        System.out.println(cocktail.cookInstruction());

        FastCocktail cocktail1 = new FastCocktail(ice, vodka);

        System.out.println(cocktail1.getStrength());

        FancyCocktail f3 = new FancyCocktail(ice, vodka, juice, tequila);
        System.out.println(f3.getRecipe());
        System.out.println(f3.cookInstruction());
        System.out.println(f3.getStrength());
    }
}