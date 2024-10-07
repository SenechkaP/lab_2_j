package MVC;

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
import java.util.List;

public class Controller {
    private final Model model;
    private final View view;
    private ExcMsgLog log;
    private ConfigReader configReader;
    private boolean debugMode = false;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        try {
            this.log = new ExcMsgLog("src/logs/lab4_results", true);
            this.configReader = new ConfigReader();
        } catch (IOException e) {
            view.displayText("Не удалось создать экземпляр объекта ExcMsgLog для записи информации в лог файл\n\n");
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void checkPassword(JSONObject user) {
        String userName = (String) user.get("name");
        String password_hash = (String) user.get("password_hash");

        while (true) {
            view.displayText(userName + ", введите ваш пароль: ");
            String password = view.getLine();

            if (!BCrypt.checkpw(password, password_hash)) {
                view.displayText("Неверный пароль для пользователя " + userName + ". Попробуйте ещё раз.\n\n");
                continue;
            }
            break;
        }
    }


    private JSONObject getUser() throws IOException, ParseException {
        JSONObject user;
        while (true) {
            view.displayText("Введите имя пользователя: ");
            String userName = view.getLine();

            user = model.findUser(userName);

            if (user == null) {
                view.displayText("Пользователя с таким именем не существует. Попробуйте ещё раз.\n\n");
                continue;
            }

            break;
        }
        return user;
    }

    private List<String> getLines() {
        try {
            return model.readLines();
        } catch (IOException e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + "Не удалось получить информацию из файла");
            }
            view.displayText("Не удалось получить информацию из файла\n\n");
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
            view.displayText("Функция readLines отработала без исключений\n");
        } catch (IOException e) {
            log.writeSevere("Функция readLines выбросила исключение IOException");
            view.displayText("Функция readLines выбросила исключение IOException\n");
        }

        try {
            model.addCocktail(new FancyCocktail(
                    new Ingredient("ice", 0, 100),
                    new Ingredient("vodka", 40.5, 100),
                    new Ingredient("juice", 0, 100),
                    new Ingredient("tequila", 40.5, 100)
            ));
            log.writeInfo("Функция addCocktail отработала без исключений");
            view.displayText("Функция addCocktail отработала без исключений\n");
        } catch (IOException e) {
            log.writeSevere("Функция addCocktail выбросила исключение IOException");
            view.displayText("Функция addCocktail выбросила исключение IOException\n");
        }

        try {
            model.updateLines(lines);
            log.writeInfo("Функция updateLines отработала без исключений");
            view.displayText("Функция updateLines отработала без исключений\n");
        } catch (IOException e) {
            log.writeSevere("Функция updateLines выбросила исключение IOException");
            view.displayText("Функция updateLines выбросила исключение IOException\n");
        } catch (NullPointerException e) {
            log.writeSevere("Функция updateLines выбросила исключение NullPointerException");
            view.displayText("Функция updateLines выбросила исключение NullPointerException\n");
        }

        log.writeFine("Finish autotests: " + LocalDateTime.now().format(formatter));
    }

    public void run() {
        try {
            model.updateID();
        } catch (IOException e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось обновить информацию о содержании файла");
            }
            view.displayText("Не удалось обновить информацию о содержании файла.");
            return;
        }

        JSONObject user = null;
        try {
            user = getUser();
        } catch (IOException | ParseException e) {
            if (debugMode) {
                log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось найти пользователя");
            }
            view.displayText("Не удалось найти пользователя\n\n");
            return;
        }

        checkPassword(user);

        view.displayGreeting((String) user.get("name"));
        view.displayText("\n");

        if (Boolean.parseBoolean(configReader.getProperty("runautotests"))) {
            runAutotests();
            view.displayText("\n");
        }

        boolean running = true;

        while (running) {
            view.displayMenu((String) user.get("right"));
            view.displayText("\n");
            int command = 0;

            try {
                command = Integer.parseInt(view.getLine());
            } catch (NumberFormatException e) {
                if (debugMode) {
                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён номер команды");
                }
                view.displayText("Номер команды - целое число! Попробуйте ещё раз.\n\n");
                continue;
            }

            List<String> lines = getLines();

            switch (command) {
                case 0:
                    view.displayText("Программа завершена, данные сохранены.");
                    running = false;
                    break;
                case 1: {
                    for (String line : lines) {
                        String[] cocktail = line.split(";");
                        view.displayText("ID = " + cocktail[0] + "  type = " + cocktail[1] + ": " + cocktail[2] + "\n");
                    }
                    view.displayText("\n");

                    break;
                }
                case 2: {
                    BaseCocktail cocktail = null;
                    int variant = 1;

                    view.displayCocktailVarieties();
                    try {
                        variant = Integer.parseInt(view.getLine());
                    } catch (NumberFormatException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён номер коктейля");
                        }
                        view.displayText("Номер варианта - целое число! Попробуйте ещё раз.\n\n");
                        break;
                    }

                    boolean shouldBreak = false;

                    switch (variant) {
                        case 1:
                            try {
                                cocktail = view.inputBaseCocktail();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о коктейле");
                                }
                                view.displayText("Число грамм - целое число! Попробуйте ещё раз.\n\n");
                                shouldBreak = true;
                                break;
                            }
                        case 2:
                            try {
                                cocktail = view.inputFastCocktail();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о коктейле");
                                }
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                shouldBreak = true;
                                break;
                            }
                        case 3:
                            try {
                                cocktail = view.inputFancyCocktail();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о коктейле");
                                }
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                shouldBreak = true;
                                break;
                            }
                        default:
                            view.displayText("Такого варианта коктейля нет.\n\n");
                            shouldBreak = true;
                    }

                    if (shouldBreak) {
                        break;
                    }

                    String added_str = null;

                    try {
                        added_str = model.addCocktail(cocktail);
                        lines.add(added_str);
                    } catch (IOException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось добавить коктейль");
                        }
                        view.displayText("Не удалось добавить коктейль.\n\n");
                    }

                    if (added_str != null) {
                        view.displayText("Запись о коктейле успешно добавлена.\n\n");
                    }

                    break;
                }
                case 3: {
                    view.displayText("Введите ID элемента, информацию о котором вы хотите обновить: ");

                    int id;

                    try {
                        id = Integer.parseInt(view.getLine());
                        if (id < 0 || id >= model.getSize()) {
                            throw new IllegalArgumentException();
                        }
                    } catch (NumberFormatException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён ID");
                        }
                        view.displayText("ID - целое число! Попробуйте ещё раз.\n\n");
                        break;
                    } catch (IllegalArgumentException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Нет такого ID");
                        }
                        view.displayText("Нет такого ID. Попробуйте сначала вывести все записи и посмотреть, какие ID доступны.\n\n");
                        break;
                    }

                    String[] lineParts = lines.get(id).split(";");
                    String cocktailType = lineParts[1];
                    String[] ingredients = lineParts[2].split(",");  // size = 1 or 2 or 4

                    String cocktailStr = "";
                    view.displayText("Если вы не хотите менять какой-то из параметров какого-то ингредиента,\nто в таком случае введите любое отрицательное число\n\n");

                    switch (cocktailType) {
                        case "BaseCocktail": {
                            try {
                                /*
                                 * ingredients[0].split(" "):
                                 * ["500", "грамм", "ice", "(крепость", "=", "0.0)"]
                                 * */
                                BaseCocktail cocktail = view.inputBaseCocktail();
                                if (cocktail.getIceAmount() < 0) {
                                    int newIceAmount = Integer.parseInt(ingredients[0].split(" ")[0]);
                                    cocktail.setIceAmount(newIceAmount);
                                }

                                cocktailStr = id + ";" + cocktail.getClass().getSimpleName() + ";" + cocktail.getRecipe();
                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о коктейле");
                                }
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                break;
                            }
                        }
                        case "FastCocktail": {
                            try {
                                /*
                                 * ingredients[0].split(" "):
                                 * ["230", "грамм", "ice", "(крепость", "=", "0.0)"]
                                 * ingredients[1].split(" "):
                                 * ["", "105", "грамм", "vodka", "(крепость", "=", "34.5)"]
                                 * */
                                FastCocktail fastCocktail = view.inputFastCocktail();
                                if (fastCocktail.getIceAmount() < 0) {
                                    int newIceAmount = Integer.parseInt(ingredients[0].split(" ")[0]);
                                    fastCocktail.setIceAmount(newIceAmount);
                                }
                                if (fastCocktail.getVodkaAmount() < 0) {
                                    int newVodkaAmount = Integer.parseInt(ingredients[1].split(" ")[1]);
                                    fastCocktail.setVodkaAmount(newVodkaAmount);
                                }
                                if (fastCocktail.getVodkaStrength() < 0) {
                                    String vodkaStrengthStr = ingredients[1].split(" ")[6];
                                    vodkaStrengthStr = vodkaStrengthStr.substring(0, vodkaStrengthStr.length() - 1);
                                    double newVodkaStrength = Double.parseDouble(vodkaStrengthStr);
                                    fastCocktail.setVodkaStrength(newVodkaStrength);
                                }

                                cocktailStr = id + ";" + fastCocktail.getClass().getSimpleName() + ";" + fastCocktail.getRecipe();
                                //cocktail = fastCocktail;

                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о коктейле");
                                }
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                break;
                            }
                        }
                        case "FancyCocktail": {
                            try {
                                /*
                                 * ingredients[0].split(" "):
                                 * ["230", "грамм", "ice", "(крепость", "=", "0.0)"]
                                 * ingredients[1].split(" "):
                                 * ["", "105", "грамм", "vodka", "(крепость", "=", "34.5)"]
                                 * ingredients[2].split(" "):
                                 * ["", "456", "грамм", "juice", "(крепость", "=", "0.0)"]
                                 * ingredients[3].split(" "):
                                 * ["", "105", "грамм", "tequila", "(крепость", "=", "30.5)"]
                                 * */

                                FancyCocktail fancyCocktail = view.inputFancyCocktail();
                                if (fancyCocktail.getIceAmount() < 0) {
                                    int newIceAmount = Integer.parseInt(ingredients[0].split(" ")[0]);
                                    fancyCocktail.setIceAmount(newIceAmount);
                                }
                                if (fancyCocktail.getVodkaAmount() < 0) {
                                    int newVodkaAmount = Integer.parseInt(ingredients[1].split(" ")[1]);
                                    fancyCocktail.setVodkaAmount(newVodkaAmount);
                                }
                                if (fancyCocktail.getVodkaStrength() < 0) {
                                    String vodkaStrengthStr = ingredients[1].split(" ")[6];
                                    vodkaStrengthStr = vodkaStrengthStr.substring(0, vodkaStrengthStr.length() - 1);
                                    double newVodkaStrength = Double.parseDouble(vodkaStrengthStr);
                                    fancyCocktail.setVodkaStrength(newVodkaStrength);
                                }
                                if (fancyCocktail.getJuiceAmount() < 0) {
                                    int newJuiceAmount = Integer.parseInt(ingredients[2].split(" ")[1]);
                                    fancyCocktail.setJuiceAmount(newJuiceAmount);
                                }
                                if (fancyCocktail.getTequilaAmount() < 0) {
                                    int newTequilaAmount = Integer.parseInt(ingredients[3].split(" ")[1]);
                                    fancyCocktail.setTequilaAmount(newTequilaAmount);
                                }
                                if (fancyCocktail.getTequilaStrength() < 0) {
                                    String tequilaStrengthStr = ingredients[3].split(" ")[6];
                                    tequilaStrengthStr = tequilaStrengthStr.substring(0, tequilaStrengthStr.length() - 1);
                                    double newTequilaStrength = Double.parseDouble(tequilaStrengthStr);
                                    fancyCocktail.setTequilaStrength(newTequilaStrength);
                                }

                                cocktailStr = id + ";" + fancyCocktail.getClass().getSimpleName() + ";" + fancyCocktail.getRecipe();
                                //cocktail = fastCocktail;

                                break;
                            } catch (NumberFormatException e) {
                                if (debugMode) {
                                    log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введена информация о коктейле");
                                }
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                break;
                            }
                        }
                    }

//                    String cocktailStr = id + ";" + cocktail.getClass().getSimpleName() + ";" + cocktail.getRecipe();

                    lines.set(id, cocktailStr);
                    try {
                        model.updateLines(lines);
                        view.displayText("Информация успешно обновлена\n\n");
                    } catch (IOException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось записать изменения в файл");
                        }
                        view.displayText("Не удалось записать изменения в файл\n\n");
                    }

                    break;
                }
                case 4: {
                    view.displayText("Введите ID элемента, информацию о котором вы хотите удалить: ");

                    int id;

                    try {
                        id = Integer.parseInt(view.getLine());
                        if (id < 0 || id >= model.getSize()) {
                            throw new IllegalArgumentException();
                        }
                    } catch (NumberFormatException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Некорректно введён ID");
                        }
                        view.displayText("ID - целое число! Попробуйте ещё раз.\n\n");
                        break;
                    } catch (IllegalArgumentException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Нет такого ID");
                        }
                        view.displayText("Нет такого ID. Попробуйте сначала вывести все записи и посмотреть, какие ID доступны.\n\n");
                        break;
                    }

                    if (id != lines.size() - 1) {
                        for (int i = id + 1; i < lines.size(); i++) {
                            String[] lineParts = lines.get(i).split(";");
                            lineParts[0] = "" + (i - 1);
                            String newLine = lineParts[0] + ";" + lineParts[1] + ";" + lineParts[2];
                            lines.set(i, newLine);
                        }
                    }

                    lines.remove(id);
                    try {
                        model.updateLines(lines);
                        view.displayText("Информация успешно удалена\n\n");
                    } catch (IOException e) {
                        if (debugMode) {
                            log.writeSevere(LocalDateTime.now().format(formatter) + " Не удалось записать изменения в файл");
                        }
                        view.displayText("Не удалось записать изменения в файл\n\n");
                    }

                    break;
                }
                case 5: {
                    if (!debugMode) {
                        configReader.setProperty("debugmode", "true");
                        debugMode = true;
                        view.displayText("Режим отладки включен\n\n");
                    }
                    break;
                }
                case 6: {
                    if (user.get("right") != "root") {
                        continue;
                    }
                    runAutotests();
                    break;
                }
                default:
                    view.displayText("Нет такой команды.\n\n");
            }
        }
    }
}
