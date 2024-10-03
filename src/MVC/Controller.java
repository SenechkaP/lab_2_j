package MVC;

import cocktails.BaseCocktail;
import cocktails.FancyCocktail;
import cocktails.FastCocktail;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;

public class Controller {
    private Model model;
    private View view;

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
            view.displayText("Что-то пошло не так...\n\n");
            return null;
        }
    }

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        try {
            model.updateID();
        } catch (IOException e) {
            view.displayText("Не удалось обновить информацию о содержании файла.");
            return;
        }

        JSONObject user = null;
        try {
            user = getUser();
        } catch (IOException | ParseException e) {
            view.displayText("Что-то пошло не так...\n\n");
        }

        checkPassword(user);

        view.displayGreeting((String) user.get("name"));
        view.displayText("\n");

        boolean running = true;

        while (running) {
            view.displayMenu((String) user.get("right"));
            view.displayText("\n");
            int command = 0;

            try {
                command = Integer.parseInt(view.getLine());
            } catch (NumberFormatException e) {
                view.displayText("Номер команды - целое число! Попробуйте ещё раз.\n\n");
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
                                view.displayText("Число грамм - целое число! Попробуйте ещё раз.\n\n");
                                shouldBreak = true;
                                break;
                            }
                        case 2:
                            try {
                                cocktail = view.inputFastCocktail();
                                break;
                            } catch (NumberFormatException e) {
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                shouldBreak = true;
                                break;
                            }
                        case 3:
                            try {
                                cocktail = view.inputFancyCocktail();
                                break;
                            } catch (NumberFormatException e) {
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
                        view.displayText("Произошла ошибка, попробуйте ещё раз.\n\n");
                    }

                    if (added_str == null) {
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
                        view.displayText("ID - целое число! Попробуйте ещё раз.\n\n");
                        break;
                    } catch (IllegalArgumentException e) {
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
                                    int newJuiceAmount = Integer.parseInt(ingredients[2].split(" ")[2]);
                                    fancyCocktail.setJuiceAmount(newJuiceAmount);
                                }
                                if (fancyCocktail.getTequilaAmount() < 0) {
                                    int newTequilaAmount = Integer.parseInt(ingredients[3].split(" ")[2]);
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
                                view.displayText("Что-то было введено неверно. Попробуйте ещё раз.\n\n");
                                break;
                            }
                        }
                    }

//                    String cocktailStr = id + ";" + cocktail.getClass().getSimpleName() + ";" + cocktail.getRecipe();

                    lines.set(id, cocktailStr);
                    try {
                        model.updateLines(lines);
                    } catch (IOException e) {
                        view.displayText("Не удалось записать изменения в файл\n\n");
                    }

                    break;
                }
                default:
                    view.displayText("Нет такой команды.\n\n");
            }
        }
    }
}
