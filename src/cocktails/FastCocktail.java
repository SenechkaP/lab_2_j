package cocktails;

public class FastCocktail extends BaseCocktail {
    private Ingredient vodka;

    public FastCocktail(Ingredient ice, Ingredient vodka) {
        super(ice);
        this.vodka = vodka;
        setTotalAmount(getTotalAmount() + vodka.getAmount());
        createRecipe(vodka);
        calculateStrength();
    }

    @Override
    public String cookInstruction() {
        return super.cookInstruction() + ", " + Action.POUR.getDescription() + " водку";
    }

    private void createRecipe(Ingredient vodka) {
        setRecipe(super.getRecipe() + ", " + vodka.toRecipeFormat());
    }

    private void calculateStrength() {
        double newStrength = (vodka.getStrength() * vodka.getAmount()) / getTotalAmount();
        setStrength(newStrength);
    }

    public int getVodkaAmount() {
        return vodka.getAmount();
    }

    public void setVodkaAmount(int amount) {
        vodka.setAmount(amount);
        setTotalAmount(getTotalAmount() + amount);
        String recipe = getRecipe();
        String[] recipeParts = recipe.split(",");
        String[] vodkaParts = recipeParts[1].split(" ");
        vodkaParts[1] = "" + amount;

        StringBuilder newRecipe = new StringBuilder(recipeParts[0]);
        newRecipe.append(",");
        for (int i = 1; i < vodkaParts.length; i++) {
            newRecipe.append(" ").append(vodkaParts[i]);
        }

        setRecipe(newRecipe.toString());
        calculateStrength();
    }

    public double getVodkaStrength() {
        return vodka.getStrength();
    }

    public void setVodkaStrength(double strength) {
        vodka.setStrength(strength);
        String recipe = getRecipe();
        String[] recipeParts = recipe.split(",");
        String[] vodkaParts = recipeParts[1].split(" ");
        vodkaParts[6] = strength + ")";

        StringBuilder newRecipe = new StringBuilder(recipeParts[0]);
        newRecipe.append(",");
        for (int i = 1; i < vodkaParts.length; i++) {
            newRecipe.append(" ").append(vodkaParts[i]);
        }

        setRecipe(newRecipe.toString());
        calculateStrength();
    }
}
