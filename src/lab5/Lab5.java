package lab5;

public class Lab5 {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        controller.run();
    }
}
