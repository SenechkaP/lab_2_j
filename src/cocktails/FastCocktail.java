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

    @Override
    public void setIceAmount(int amount) {
        String recipe = getRecipe();
        super.setIceAmount(amount);
        setTotalAmount(getTotalAmount() + vodka.getAmount());
        String[] recipeParts = recipe.split(",");
        String[] iceParts = recipeParts[0].split(" ");
        iceParts[0] = "" + amount;

        StringBuilder newRecipe = new StringBuilder(iceParts[0]);

        for (int i = 1; i < iceParts.length; i++) {
            newRecipe.append(" ").append(iceParts[i]);
        }

        for (int i = 1; i < recipeParts.length; i++) {
            newRecipe.append(",").append(recipeParts[i]);
        }

        setRecipe(newRecipe.toString());
        calculateStrength();
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

        StringBuilder newRecipe = new StringBuilder(recipeParts[0] + ",");

        for (int i = 1; i < vodkaParts.length; i++) {
            newRecipe.append(" ").append(vodkaParts[i]);
        }

        for (int i = 2; i < recipeParts.length; i++) {
            newRecipe.append(",").append(recipeParts[i]);
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

        StringBuilder newRecipe = new StringBuilder(recipeParts[0] + ",");

        for (int i = 1; i < vodkaParts.length; i++) {
            newRecipe.append(" ").append(vodkaParts[i]);
        }

        for (int i = 2; i < recipeParts.length; i++) {
            newRecipe.append(",").append(recipeParts[i]);
        }

        setRecipe(newRecipe.toString());
        calculateStrength();
    }
}
