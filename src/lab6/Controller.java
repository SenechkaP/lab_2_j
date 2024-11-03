package lab6;

import MVC.Model;
import cocktails.BaseCocktail;
import cocktails.FancyCocktail;
import cocktails.FastCocktail;
import cocktails.Ingredient;
import config.ConfigReader;
import mylogging.ExcMsgLog;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Controller implements ViewListener {
    private String right = "";
    private final Model model;
    private final View view;
    private ExcMsgLog log;
    private ConfigReader configReader;
    private boolean debugMode = false;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.view.setListener(this);
        try {
            this.log = new ExcMsgLog("src/logs/lab4_results", true);
            this.configReader = new ConfigReader();
        } catch (IOException e) {
            view.displayError("Не удалось создать экземпляр объекта ExcMsgLog для записи информации в лог файл");
        }
    }

    @Override
    public void onShowAllCocktails() {
        ArrayList<String> cocktails = (ArrayList<String>) getLines();
        if (cocktails != null) {
            view.displayCocktailList(cocktails, () -> view.showMainMenu(""));
        } else {
            view.displayError("Ошибка получения данных о коктейлях.");
        }
    }

    @Override
    public void onAddCocktail(BaseCocktail cocktail) throws IOException {
        model.addCocktail(cocktail);
    }

    @Override
    public void onUpdateCocktail(int id) throws IllegalArgumentException {
        if (id < 0 || id >= model.getSize()) {
            throw new IllegalArgumentException();
        }

        ArrayList<String> cocktails = (ArrayList<String>) getLines();
        String[] lineParts = cocktails.get(id).split(";");
        String cocktailType = lineParts[1];
        String[] ingredients = lineParts[2].split(",");

        switch (cocktailType) {
            case "BaseCocktail": {
                BaseCocktail cocktail = new BaseCocktail(
                        new Ingredient("ice", 0, Integer.parseInt(ingredients[0].split(" ")[0]))
                );
                view.showBaseCocktailUpdate(id, cocktail);
                break;
            }
            case "FastCocktail": {
                String vodkaStrengthStr = ingredients[1].split(" ")[6];
                vodkaStrengthStr = vodkaStrengthStr.substring(0, vodkaStrengthStr.length() - 1);
                double vodkaStrength = Double.parseDouble(vodkaStrengthStr);
                FastCocktail cocktail = new FastCocktail(
                        new Ingredient("ice", 0, Integer.parseInt(ingredients[0].split(" ")[0])),
                        new Ingredient("vodka", vodkaStrength, Integer.parseInt(ingredients[1].split(" ")[1]))
                );
                view.showFastCocktailUpdate(id, cocktail);
                break;
            }
            case "FancyCocktail": {
                String vodkaStrengthStr = ingredients[1].split(" ")[6];
                vodkaStrengthStr = vodkaStrengthStr.substring(0, vodkaStrengthStr.length() - 1);
                double vodkaStrength = Double.parseDouble(vodkaStrengthStr);

                String tequilaStrengthStr = ingredients[3].split(" ")[6];
                tequilaStrengthStr = tequilaStrengthStr.substring(0, tequilaStrengthStr.length() - 1);
                double tequilaStrength = Double.parseDouble(tequilaStrengthStr);

                FancyCocktail cocktail = new FancyCocktail(
                        new Ingredient("ice", 0, Integer.parseInt(ingredients[0].split(" ")[0])),
                        new Ingredient("vodka", vodkaStrength, Integer.parseInt(ingredients[1].split(" ")[1])),
                        new Ingredient("juice", 0, Integer.parseInt(ingredients[2].split(" ")[1])),
                        new Ingredient("tequila", tequilaStrength, Integer.parseInt(ingredients[3].split(" ")[1]))
                );
                view.showFancyCocktailUpdate(id, cocktail);
                break;
            }
        }
    }

    @Override
    public void onUpdateCocktail(int id, BaseCocktail cocktail) throws IOException {
        ArrayList<String> cocktails = (ArrayList<String>) getLines();
        String cocktailStr = id + ";" + cocktail.getClass().getSimpleName() + ";" + cocktail.getRecipe();
        cocktails.set(id, cocktailStr);
        model.updateLines(cocktails);
    }

    @Override
    public void onDeleteCocktail(int id) throws IOException, IllegalArgumentException {
        ArrayList<String> cocktails = (ArrayList<String>) getLines();

        if (id < 0 || id >= cocktails.size()) {
            throw new IllegalArgumentException();
        }

        if (id != cocktails.size() - 1) {
            for (int i = id + 1; i < cocktails.size(); i++) {
                String[] lineParts = cocktails.get(i).split(";");
                lineParts[0] = "" + (i - 1);
                String newLine = lineParts[0] + ";" + lineParts[1] + ";" + lineParts[2];
                cocktails.set(i, newLine);
            }
        }

        cocktails.remove(id);
        model.updateLines(cocktails);
    }

    @Override
    public void onTurnOnDebugMode() {
        configReader.setProperty("debugmode", "true");
        debugMode = true;
        view.displayText("Режим отладки включён");
    }

    @Override
    public void logSevere(String text) {
        if (debugMode) {
            log.writeSevere(LocalDateTime.now().format(formatter) + " " + text);
        }
    }

    @Override
    public void onRunAutotests() {
        runAutotests();
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void checkPassword(JSONObject user, Runnable onSuccess) {
        String userName = (String) user.get("name");
        String passwordHash = (String) user.get("password_hash");

        view.displayText(userName + ", введите ваш пароль: ");
        view.getLine(password -> {
            if (BCrypt.checkpw(password, passwordHash)) {
                onSuccess.run();
            } else {
                view.displayError("Неверный пароль для пользователя " + userName + ". Попробуйте ещё раз.");
                checkPassword(user, onSuccess);
            }
        });
    }


    private void getUser(Consumer<JSONObject> userCallback) {
        view.displayText("Введите имя пользователя: ");
        view.getLine(userName -> {
            try {
                JSONObject user = model.findUser(userName);
                if (user == null) {
                    view.displayError("Пользователя с таким именем не существует. Попробуйте ещё раз.");
                    getUser(userCallback);
                } else {
                    view.displayError("");
                    userCallback.accept(user);
                }
            } catch (IOException | ParseException e) {
                view.displayError("Не удалось найти пользователя");
            }
        });
    }

    private List<String> getLines() {
        try {
            return model.readLines();
        } catch (IOException e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + "Не удалось получить информацию из файла");
            }
            view.displayError("Не удалось получить информацию из файла");
            return null;
        }
    }

    private void runAutotests() {
        log.writeFine("Start autotests: " + LocalDateTime.now().format(formatter));

        List<String> lines = List.of(
                "0;FastCocktail;500 грамм ice (крепость = 0.0), 300 грамм vodka (крепость = 43.5)",
                "1;FancyCocktail;100 грамм ice (крепость = 0.0), 200 грамм vodka (крепость = 34.2), 241 грамм juice (крепость = 0.0), 300 грамм tequila (крепость = 23.4)"
        );

        try {
            lines = model.readLines();
            log.writeInfo("Функция readLines отработала без исключений");
            view.displayText("Функция readLines отработала без исключений");
        } catch (IOException e) {
            log.writeSevere("Функция readLines выбросила исключение IOException");
            view.displayText("Функция readLines выбросила исключение IOException");
        }

        try {
            model.addCocktail(new FancyCocktail(
                    new Ingredient("ice", 0, 100),
                    new Ingredient("vodka", 40.5, 100),
                    new Ingredient("juice", 0, 100),
                    new Ingredient("tequila", 40.5, 100)
            ));
            log.writeInfo("Функция addCocktail отработала без исключений");
            view.displayText("Функция addCocktail отработала без исключений");
        } catch (IOException e) {
            log.writeSevere("Функция addCocktail выбросила исключение IOException");
            view.displayText("Функция addCocktail выбросила исключение IOException");
        }

        try {
            model.updateLines(lines);
            log.writeInfo("Функция updateLines отработала без исключений");
            view.displayText("Функция updateLines отработала без исключений");
        } catch (IOException e) {
            log.writeSevere("Функция updateLines выбросила исключение IOException");
            view.displayText("Функция updateLines выбросила исключение IOException");
        } catch (NullPointerException e) {
            log.writeSevere("Функция updateLines выбросила исключение NullPointerException");
            view.displayText("Функция updateLines выбросила исключение NullPointerException");
        }

        log.writeFine("Finish autotests: " + LocalDateTime.now().format(formatter));
    }

    public void run() {
        view.show();
        try {
            model.updateID();
        } catch (IOException e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось обновить информацию о содержании файла");
            }
            view.displayError("Не удалось обновить информацию о содержании файла.");
            return;
        }

        getUser(user -> {
            checkPassword(user, () -> {
                right = (String) user.get("right");
                view.displayGreeting((String) user.get("name"));
                view.showMainMenu(right);
            });
        });
    }
}
