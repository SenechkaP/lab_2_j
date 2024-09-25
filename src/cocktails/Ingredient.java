package cocktails;

public class Ingredient {
    private String name;
    private double strength;
    private int amount;

    public Ingredient(String name, double strength, int amount) {
        this.name = name;
        this.strength = strength;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s грамм %s (крепость = %s)", amount, name, strength);
    }

    public String toRecipeFormat() {
        return String.format("%s грамм %s", amount, name);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }
}
