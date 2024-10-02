package MVC;

import cocktails.BaseCocktail;
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

            switch (command) {
                case 0:
                    view.displayText("Программа завершена, данные сохранены.");
                    running = false;
                    break;
                case 1:
                    List<String> lines;

                    try {
                        lines = model.readLines();
                    } catch (IOException e) {
                        view.displayText("Что-то пошло не так...\n\n");
                        break;
                    }

                    for (String line : lines) {
                        String[] cocktail = line.split(";");
                        view.displayText("ID = " + cocktail[0] + "  type = " + cocktail[1] + ": " + cocktail[2] + "\n");
                    }
                    view.displayText("\n");

                    break;
                case 2:
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

                    boolean added = false;

                    try {
                        added = model.addCocktail(cocktail);
                    } catch (IOException e) {
                        view.displayText("Произошла ошибка, попробуйте ещё раз.\n\n");
                    }

                    if (added) {
                        view.displayText("Запись о коктейле успешно добавлена.\n\n");
                    }

                    break;
                default:
                    view.displayText("Нет такой команды.\n\n");
            }
        }
    }
}
