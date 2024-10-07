package cocktails;

public class FancyCocktail extends FastCocktail {
    private Ingredient juice;
    private Ingredient tequila;

    public FancyCocktail(Ingredient ice, Ingredient vodka, Ingredient juice, Ingredient tequila) {
        super(ice, vodka);
        this.juice = juice;
        this.tequila = tequila;
        createRecipe(juice, tequila);
        setTotalAmount(getTotalAmount() + tequila.getAmount() + juice.getAmount());
        calculateStrength();
    }

    @Override
    public String cookInstruction() {
        return super.cookInstruction() + ", " + Action.POUR.getDescription() + " текилу, " +
                Action.STIR.getDescription() + ", " + Action.POUR.getDescription() + " сок, " +
                Action.SHAKE.getDescription();
    }

    private void createRecipe(Ingredient juice, Ingredient tequila) {
        setRecipe(getRecipe() + ", " + juice.toRecipeFormat() + ", " + tequila.toRecipeFormat());
    }

    private void calculateStrength() {
        double newStrength = getStrength() + (tequila.getStrength() * tequila.getAmount()) / getTotalAmount();
        setStrength(newStrength);
    }

    public int getJuiceAmount() {
        return juice.getAmount();
    }

    public void setJuiceAmount(int amount) {
        juice.setAmount(amount);
        setTotalAmount(getTotalAmount() + amount);

        String recipe = getRecipe();
        String[] recipeParts = recipe.split(",");
        String[] juiceParts = recipeParts[2].split(" ");
        juiceParts[1] = "" + amount;

        StringBuilder newRecipe = new StringBuilder(recipeParts[0] + "," + recipeParts[1] + ",");

        for (int i = 1; i < juiceParts.length; i++) {
            newRecipe.append(" ").append(juiceParts[i]);
        }

        for (int i = 3; i < recipeParts.length; i++) {
            newRecipe.append(",").append(recipeParts[i]);
        }

        setRecipe(newRecipe.toString());

        calculateStrength();
    }

    public int getTequilaAmount() {
        return tequila.getAmount();
    }

    public void setTequilaAmount(int amount) {
        tequila.setAmount(amount);
        setTotalAmount(getTotalAmount() + amount);

        String recipe = getRecipe();
        String[] recipeParts = recipe.split(",");
        String[] tequilaParts = recipeParts[3].split(" ");
        tequilaParts[1] = "" + amount;

        StringBuilder newRecipe = new StringBuilder(recipeParts[0] + "," + recipeParts[1] + "," + recipeParts[2] + ",");

        for (int i = 1; i < tequilaParts.length; i++) {
            newRecipe.append(" ").append(tequilaParts[i]);
        }

        setRecipe(newRecipe.toString());

        calculateStrength();
    }

    public double getTequilaStrength() {
        return tequila.getStrength();
    }

    public void setTequilaStrength(double strength) {
        tequila.setStrength(strength);

        String recipe = getRecipe();
        String[] recipeParts = recipe.split(",");
        String[] tequilaParts = recipeParts[3].split(" ");
        tequilaParts[6] = strength + ")";

        StringBuilder newRecipe = new StringBuilder(recipeParts[0] + "," + recipeParts[1] + "," + recipeParts[2] + ",");

        for (int i = 1; i < tequilaParts.length; i++) {
            newRecipe.append(" ").append(tequilaParts[i]);
        }

        setRecipe(newRecipe.toString());
        calculateStrength();
    }
}
