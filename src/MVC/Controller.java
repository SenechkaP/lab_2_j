package MVC;

import cocktails.BaseCocktail;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

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

    private JSONObject getUser() {
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

        JSONObject user = getUser();

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

                    boolean added = model.addCocktail(cocktail);
                    if (added) {
                        view.displayText("Запись о коктейле успешно добавлена.\n\n");
                    } else {
                        view.displayText("Произошла ошибка, попробуйте ещё раз.\n\n");
                    }
                    break;
                default:
                    view.displayText("Нет такой команды.\n\n");
            }
        }
    }
}
