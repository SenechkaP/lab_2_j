import MVC.Controller;
import MVC.Model;
import MVC.View;

public class Lab4 {
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(model, view);

        controller.run();
        view.close();
    }
}
