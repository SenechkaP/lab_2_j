package lab6;

import cocktails.BaseCocktail;
import cocktails.FancyCocktail;
import cocktails.FastCocktail;
import cocktails.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class View {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final JLabel messageLabel;
    private final JLabel errorLabel;
    private ViewListener listener;
    private final JPanel labelPanel;

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    public void show() {
        frame.setVisible(true);
    }

    public View() {
        frame = new JFrame("лаб 6 дал??? ДАЛ! да было братишка");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        errorLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel = new JLabel("Начало программы", SwingConstants.CENTER);
        labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(2, 1));
        labelPanel.add(errorLabel);
        labelPanel.add(messageLabel);

        mainPanel.add(labelPanel, BorderLayout.NORTH);

        frame.add(mainPanel);
    }

    public void displayText(String text) {
        messageLabel.setText(text);

        frame.revalidate();
        frame.repaint();
    }

    public void displayError(String text) {
        errorLabel.setText(text);

        frame.revalidate();
        frame.repaint();
    }

    public void displayGreeting(String userName) {
        messageLabel.setText("Добро пожаловать, " + userName);

        frame.revalidate();
        frame.repaint();
    }

    public void getLine(Consumer<String> callback) {
        JPanel inputPanel = new JPanel();
        JTextField textInput = new JTextField(20);
        JButton submitButton = new JButton("Подтвердить");

        inputPanel.add(textInput);
        inputPanel.add(submitButton);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        submitButton.addActionListener(e -> {
            String result = textInput.getText();
            callback.accept(result);
            mainPanel.remove(inputPanel);
            mainPanel.revalidate();
        });
    }

    public void showMainMenu(String right) {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        JButton printCocktailsButton = new JButton("Вывести все записи о коктейлях");
        JButton addCocktailButton = new JButton("Добавить новую запись о коктейле");
        JButton updateCocktailButton = new JButton("Обновить запись о коктейле");
        JButton deleteCocktailButton = new JButton("Удалить запись о коктейле");
        JButton debugButton = new JButton("Включить режим отладки");
        JButton autotestsButton = new JButton("Запустить автотесты");
        menuPanel.add(printCocktailsButton);
        menuPanel.add(addCocktailButton);
        menuPanel.add(updateCocktailButton);
        menuPanel.add(deleteCocktailButton);
        menuPanel.add(debugButton);

        if (Objects.equals(right, "root")) {
            menuPanel.add(autotestsButton);
        }

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        printCocktailsButton.addActionListener(e -> {
            listener.onShowAllCocktails();
        });

        addCocktailButton.addActionListener(e -> showCocktailTypeMenu());
        updateCocktailButton.addActionListener(e -> showUpdateMenu());
        deleteCocktailButton.addActionListener(e -> showDeleteMenu());
        debugButton.addActionListener(e -> {
            listener.onTurnOnDebugMode();
        });
        autotestsButton.addActionListener(e -> {
            listener.onRunAutotests();
        });

        frame.revalidate();
        frame.repaint();
    }

    public void displayCocktailList(ArrayList<String> cocktails, Runnable onBackToMenu) {
        mainPanel.removeAll();
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setEditable(false);
        for (String line : cocktails) {
            String[] cocktail = line.split(";");
            textArea.append("ID = " + cocktail[0] + "  type = " + cocktail[1] + ": " + cocktail[2] + "\n");
        }
        mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Вернуться в главное меню");
        backButton.addActionListener(e -> onBackToMenu.run());
        mainPanel.add(backButton, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private void showUpdateMenu() {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        JTextField idInput = new JTextField(10);
        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        inputPanel.add(new JLabel("ID элемента, который нужно обновить:"));
        inputPanel.add(idInput);
        inputPanel.add(submitButton);
        inputPanel.add(backButton);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idInput.getText());

                listener.onUpdateCocktail(id);
            } catch (NumberFormatException ex) {
                displayError("Некорректно введён ID");
                listener.logSevere("Некорректно введён ID");
            } catch (IllegalArgumentException ex) {
                displayError("Нет такого ID. Вернитесь в главное меню и посмотрите доступные ID.");
                listener.logSevere("Некорректно введён ID");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }

    private void showDeleteMenu() {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        JTextField idInput = new JTextField(10);
        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        inputPanel.add(new JLabel("ID элемента, который нужно удалить:"));
        inputPanel.add(idInput);
        inputPanel.add(submitButton);
        inputPanel.add(backButton);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idInput.getText());

                listener.onDeleteCocktail(id);
                messageLabel.setText("Запись удалена.");
            } catch (NumberFormatException ex) {
                displayError("Некорректно введён ID");
                listener.logSevere("Некорректно введён ID");
            } catch (IllegalArgumentException ex) {
                displayError("Нет такого ID. Вернитесь в главное меню и посмотрите доступные ID.");
                listener.logSevere("Некорректно введён ID");
            } catch (IOException ex) {
                displayError("Не удалось удалить запись.");
                listener.logSevere("Не удалось удалить запись.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }

    private void showCocktailTypeMenu() {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("Выберите тип коктейля");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel typePanel = new JPanel();
        JButton baseCocktailButton = new JButton("BaseCocktail");
        JButton fastCocktailButton = new JButton("FastCocktail");
        JButton fancyCocktailButton = new JButton("FancyCocktail");

        typePanel.add(baseCocktailButton);
        typePanel.add(fastCocktailButton);
        typePanel.add(fancyCocktailButton);
        mainPanel.add(typePanel, BorderLayout.CENTER);

        baseCocktailButton.addActionListener(e -> showBaseCocktailInput());
        fastCocktailButton.addActionListener(e -> showFastCocktailInput());
        fancyCocktailButton.addActionListener(e -> showFancyCocktailInput());

        frame.revalidate();
        frame.repaint();
    }

    private void showBaseCocktailInput() {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        JTextField iceInput = new JTextField(10);
        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        inputPanel.add(new JLabel("Граммы льда:"));
        inputPanel.add(iceInput);
        inputPanel.add(submitButton);
        inputPanel.add(backButton);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int iceGrams = Integer.parseInt(iceInput.getText());
                if (iceGrams < 0) {
                    throw new NumberFormatException();
                }

                BaseCocktail cocktail = new BaseCocktail(new Ingredient("ice", 0, iceGrams));
                listener.onAddCocktail(cocktail);
                displayText("Запись о новом коктейле создана.");
            } catch (NumberFormatException ex) {
                displayError("Пожалуйста, введите корректные числовые значения.");
                listener.logSevere("Некорректно введены значения.");
            } catch (IOException ex) {
                displayError("Не удалось добавить коктейль.");
                listener.logSevere("Не удалось добавить коктейль.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }

    public void showBaseCocktailUpdate(int id, BaseCocktail currentCocktail) {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField iceInput = new JTextField(String.valueOf(currentCocktail.getIceAmount()), 10);

        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        iceInput.setMaximumSize(new Dimension(400, 30));

        inputPanel.add(new JLabel("Граммы льда:"));
        inputPanel.add(iceInput);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(submitButton);
        inputPanel.add(backButton);

        wrapperPanel.add(inputPanel);
        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int iceGrams = Integer.parseInt(iceInput.getText());

                BaseCocktail cocktail = new BaseCocktail(
                        new Ingredient("ice", 0, iceGrams)
                );
                listener.onUpdateCocktail(id, cocktail);
                messageLabel.setText("Запись о коктейле обновлена.");
            } catch (NumberFormatException ex) {
                displayError("Пожалуйста, введите корректные числовые значения.");
                listener.logSevere("Некорректно введены значения.");
            } catch (IOException ex) {
                displayError("Не удалось обновить запись.");
                listener.logSevere("Не удалось обновить запись.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }

    private void showFastCocktailInput() {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField iceInput = new JTextField(10);
        JTextField vodkaGramsInput = new JTextField(10);
        JTextField vodkaStrengthInput = new JTextField(10);

        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        iceInput.setMaximumSize(new Dimension(400, 30));
        vodkaGramsInput.setMaximumSize(new Dimension(400, 30));
        vodkaStrengthInput.setMaximumSize(new Dimension(400, 30));

        inputPanel.add(new JLabel("Граммы льда:"));
        inputPanel.add(iceInput);
        inputPanel.add(new JLabel("Граммы водки:"));
        inputPanel.add(vodkaGramsInput);
        inputPanel.add(new JLabel("Крепость водки:"));
        inputPanel.add(vodkaStrengthInput);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(submitButton);
        inputPanel.add(backButton);

        wrapperPanel.add(inputPanel);
        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int iceGrams = Integer.parseInt(iceInput.getText());
                int vodkaGrams = Integer.parseInt(vodkaGramsInput.getText());
                double vodkaStrength = Double.parseDouble(vodkaStrengthInput.getText());

                if (iceGrams < 0 || vodkaGrams < 0 || vodkaStrength < 0) {
                    throw new NumberFormatException();
                }

                BaseCocktail cocktail = new FastCocktail(
                        new Ingredient("ice", 0, iceGrams),
                        new Ingredient("vodka", vodkaStrength, vodkaGrams)
                );
                listener.onAddCocktail(cocktail);
                displayText("Запись о новом коктейле создана.");
            } catch (NumberFormatException ex) {
                displayError("Пожалуйста, введите корректные числовые значения.");
                listener.logSevere("Некорректно введены значения.");
            } catch (IOException ex) {
                displayError("Не удалось добавить коктейль.");
                listener.logSevere("Не удалось добавить коктейль.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }

    public void showFastCocktailUpdate(int id, FastCocktail currentCocktail) {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField iceInput = new JTextField(String.valueOf(currentCocktail.getIceAmount()), 10);
        JTextField vodkaGramsInput = new JTextField(String.valueOf(currentCocktail.getVodkaAmount()), 10);
        JTextField vodkaStrengthInput = new JTextField(String.valueOf(currentCocktail.getVodkaStrength()), 10);

        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        iceInput.setMaximumSize(new Dimension(400, 30));
        vodkaGramsInput.setMaximumSize(new Dimension(400, 30));
        vodkaStrengthInput.setMaximumSize(new Dimension(400, 30));

        inputPanel.add(new JLabel("Граммы льда:"));
        inputPanel.add(iceInput);
        inputPanel.add(new JLabel("Граммы водки:"));
        inputPanel.add(vodkaGramsInput);
        inputPanel.add(new JLabel("Крепость водки:"));
        inputPanel.add(vodkaStrengthInput);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(submitButton);
        inputPanel.add(backButton);

        wrapperPanel.add(inputPanel);
        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int iceGrams = Integer.parseInt(iceInput.getText());
                int vodkaGrams = Integer.parseInt(vodkaGramsInput.getText());
                double vodkaStrength = Double.parseDouble(vodkaStrengthInput.getText());

                BaseCocktail cocktail = new FastCocktail(
                        new Ingredient("ice", 0, iceGrams),
                        new Ingredient("vodka", vodkaStrength, vodkaGrams)
                );
                listener.onUpdateCocktail(id, cocktail);
                messageLabel.setText("Запись о коктейле обновлена.");
            } catch (NumberFormatException ex) {
                displayError("Пожалуйста, введите корректные числовые значения.");
                listener.logSevere("Некорректно введены значения.");
            } catch (IOException ex) {
                displayError("Не удалось обновить запись.");
                listener.logSevere("Не удалось обновить запись.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }


    private void showFancyCocktailInput() {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField iceInput = new JTextField(10);
        JTextField juiceInput = new JTextField(10);
        JTextField vodkaGramsInput = new JTextField(10);
        JTextField vodkaStrengthInput = new JTextField(10);
        JTextField tequilaGramsInput = new JTextField(10);
        JTextField tequilaStrengthInput = new JTextField(10);

        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        iceInput.setMaximumSize(new Dimension(400, 30));
        juiceInput.setMaximumSize(new Dimension(400, 30));
        vodkaGramsInput.setMaximumSize(new Dimension(400, 30));
        vodkaStrengthInput.setMaximumSize(new Dimension(400, 30));
        tequilaGramsInput.setMaximumSize(new Dimension(400, 30));
        tequilaStrengthInput.setMaximumSize(new Dimension(400, 30));

        inputPanel.add(new JLabel("Граммы льда:"));
        inputPanel.add(iceInput);
        inputPanel.add(new JLabel("Граммы сока:"));
        inputPanel.add(juiceInput);
        inputPanel.add(new JLabel("Граммы водки:"));
        inputPanel.add(vodkaGramsInput);
        inputPanel.add(new JLabel("Крепость водки:"));
        inputPanel.add(vodkaStrengthInput);
        inputPanel.add(new JLabel("Граммы текилы:"));
        inputPanel.add(tequilaGramsInput);
        inputPanel.add(new JLabel("Крепость текилы:"));
        inputPanel.add(tequilaStrengthInput);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(submitButton);
        inputPanel.add(backButton);

        wrapperPanel.add(inputPanel);
        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int iceGrams = Integer.parseInt(iceInput.getText());
                int juiceGrams = Integer.parseInt(juiceInput.getText());
                int vodkaGrams = Integer.parseInt(vodkaGramsInput.getText());
                double vodkaStrength = Double.parseDouble(vodkaStrengthInput.getText());
                int tequilaGrams = Integer.parseInt(tequilaGramsInput.getText());
                double tequilaStrength = Double.parseDouble(tequilaStrengthInput.getText());

                if (iceGrams < 0 || juiceGrams < 0 || vodkaGrams < 0 || vodkaStrength < 0 || tequilaGrams < 0 || tequilaStrength < 0) {
                    throw new NumberFormatException();
                }

                BaseCocktail cocktail = new FancyCocktail(
                        new Ingredient("ice", 0, iceGrams),
                        new Ingredient("vodka", vodkaStrength, vodkaGrams),
                        new Ingredient("juice", 0, juiceGrams),
                        new Ingredient("tequila", tequilaStrength, tequilaGrams)
                );
                listener.onAddCocktail(cocktail);
                displayText("Запись о новом коктейле создана.");
            } catch (NumberFormatException ex) {
                displayError("Пожалуйста, введите корректные числовые значения.");
                listener.logSevere("Некорректно введены значения.");
            } catch (IOException ex) {
                displayError("Не удалось добавить коктейль.");
                listener.logSevere("Не удалось добавить коктейль.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }

    public void showFancyCocktailUpdate(int id, FancyCocktail currentCocktail) {
        mainPanel.removeAll();
        errorLabel.setText("");
        messageLabel.setText("");
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField iceInput = new JTextField(String.valueOf(currentCocktail.getIceAmount()), 10);
        JTextField juiceInput = new JTextField(String.valueOf(currentCocktail.getJuiceAmount()), 10);
        JTextField vodkaGramsInput = new JTextField(String.valueOf(currentCocktail.getVodkaAmount()), 10);
        JTextField vodkaStrengthInput = new JTextField(String.valueOf(currentCocktail.getVodkaStrength()), 10);
        JTextField tequilaGramsInput = new JTextField(String.valueOf(currentCocktail.getTequilaAmount()), 10);
        JTextField tequilaStrengthInput = new JTextField(String.valueOf(currentCocktail.getTequilaStrength()), 10);

        JButton submitButton = new JButton("Подтвердить");
        JButton backButton = new JButton("Вернуться в главное меню");

        iceInput.setMaximumSize(new Dimension(400, 30));
        juiceInput.setMaximumSize(new Dimension(400, 30));
        vodkaGramsInput.setMaximumSize(new Dimension(400, 30));
        vodkaStrengthInput.setMaximumSize(new Dimension(400, 30));
        tequilaGramsInput.setMaximumSize(new Dimension(400, 30));
        tequilaStrengthInput.setMaximumSize(new Dimension(400, 30));

        inputPanel.add(new JLabel("Граммы льда:"));
        inputPanel.add(iceInput);
        inputPanel.add(new JLabel("Граммы сока:"));
        inputPanel.add(juiceInput);
        inputPanel.add(new JLabel("Граммы водки:"));
        inputPanel.add(vodkaGramsInput);
        inputPanel.add(new JLabel("Крепость водки:"));
        inputPanel.add(vodkaStrengthInput);
        inputPanel.add(new JLabel("Граммы текилы:"));
        inputPanel.add(tequilaGramsInput);
        inputPanel.add(new JLabel("Крепость текилы:"));
        inputPanel.add(tequilaStrengthInput);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(submitButton);
        inputPanel.add(backButton);

        wrapperPanel.add(inputPanel);
        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            try {
                int iceGrams = Integer.parseInt(iceInput.getText());
                int juiceGrams = Integer.parseInt(juiceInput.getText());
                int vodkaGrams = Integer.parseInt(vodkaGramsInput.getText());
                double vodkaStrength = Double.parseDouble(vodkaStrengthInput.getText());
                int tequilaGrams = Integer.parseInt(tequilaGramsInput.getText());
                double tequilaStrength = Double.parseDouble(tequilaStrengthInput.getText());

                BaseCocktail cocktail = new FancyCocktail(
                        new Ingredient("ice", 0, iceGrams),
                        new Ingredient("vodka", vodkaStrength, vodkaGrams),
                        new Ingredient("juice", 0, juiceGrams),
                        new Ingredient("tequila", tequilaStrength, tequilaGrams)
                );
                listener.onUpdateCocktail(id, cocktail);
                messageLabel.setText("Запись о коктейле обновлена.");
            } catch (NumberFormatException ex) {
                displayError("Пожалуйста, введите корректные числовые значения.");
                listener.logSevere("Некорректно введены значения.");
            } catch (IOException ex) {
                displayError("Не удалось обновить запись.");
                listener.logSevere("Не удалось обновить запись.");
            }
        });

        backButton.addActionListener(e -> showMainMenu(""));

        frame.revalidate();
        frame.repaint();
    }
}
