package lab5.view;

import javax.swing.*;

public class View extends JFrame {
    ViewPanel panel;

    public View(int[] xValues, int[] yValues, String title) {
        panel = new ViewPanel(xValues, yValues);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(title);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
