public class FastCocktail extends BaseCocktail {
    private Ingredient vodka;

    public FastCocktail(Ingredient ice, Ingredient vodka) {
        super(ice);
        this.vodka = vodka;
        setTotalAmount(getTotalAmount() + vodka.getAmount());
        updateRecipe(vodka);
        calculateStrength();
    }

    @Override
    public String cookInstruction() {
        return super.cookInstruction() + ", " + Action.POUR.getDescription() + " водку";
    }

    private void updateRecipe(Ingredient vodka) {
        setRecipe(getRecipe() + ", " + vodka.toRecipeFormat());
    }

    private void calculateStrength() {
        double newStrength = (vodka.getStrength() * vodka.getAmount()) / getTotalAmount();
        setStrength(newStrength);
    }
}
