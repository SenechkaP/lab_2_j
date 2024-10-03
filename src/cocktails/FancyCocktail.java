package cocktails;

public class FancyCocktail extends FastCocktail {
    private Ingredient juice;
    private Ingredient tequila;

    public FancyCocktail(Ingredient ice, Ingredient vodka, Ingredient juice, Ingredient tequila) {
        super(ice, vodka);
        this.juice = juice;
        this.tequila = tequila;
        updateRecipe(juice, tequila);
        setTotalAmount(getTotalAmount() + tequila.getAmount() + juice.getAmount());
        calculateStrength();
    }

    @Override
    public String cookInstruction() {
        return super.cookInstruction() + ", " + Action.POUR.getDescription() + " текилу, " +
                Action.STIR.getDescription() + ", " + Action.POUR.getDescription() + " сок, " +
                Action.SHAKE.getDescription();
    }

    private void updateRecipe(Ingredient juice, Ingredient tequila) {
        setRecipe(getRecipe() + ", " + juice.toRecipeFormat() + ", " + tequila.toRecipeFormat());
    }

    private void calculateStrength() {
        double newStrength = getStrength() + (tequila.getStrength() * tequila.getAmount()) / getTotalAmount();
        setStrength(newStrength);
    }

    public int getJuiceAmount() {
        return juice.getAmount();
    }

    public int getTequilaAmount() {
        return tequila.getAmount();
    }

    public double getTequilaStrength() {
        return tequila.getStrength();
    }
}
