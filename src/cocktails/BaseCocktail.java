package cocktails;

public class BaseCocktail {
    private Ingredient ice;
    private String recipe;
    private double strength;
    private int totalAmount;

    public BaseCocktail(Ingredient ice) {
        this.ice = ice;
        this.recipe = ice.toRecipeFormat();
        this.strength = 0;
        this.totalAmount = ice.getAmount();
    }

    public String cookInstruction() {
        return Action.ADD.getDescription() + " лёд";
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    protected void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    protected void setStrength(double strength) {
        this.strength = strength;
    }

    public String getRecipe() {
        return recipe;
    }

    public double getStrength() {
        return strength;
    }

    public int getIceAmount() {
        return ice.getAmount();
    }

    public void setIceAmount(int amount) {
        ice.setAmount(amount);
        setTotalAmount(amount);
        setRecipe(ice.toRecipeFormat());
    }
}
