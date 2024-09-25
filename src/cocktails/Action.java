package cocktails;

public enum Action {
    POUR("налить"),
    STIR("размешать"),
    SHAKE("взболтать"),
    ADD("добавить");

    private final String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}