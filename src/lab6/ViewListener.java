package lab6;

import cocktails.BaseCocktail;

import java.io.IOException;

public interface ViewListener {
    void onShowAllCocktails();

    void onAddCocktail(BaseCocktail cocktail) throws IOException;

    void onUpdateCocktail(int id) throws IllegalArgumentException;

    void onUpdateCocktail(int id, BaseCocktail cocktail) throws IOException;

    void onDeleteCocktail(int id) throws IOException, IllegalArgumentException;

    void onTurnOnDebugMode();

    void logSevere(String text);

    void onRunAutotests();
}
