package lab5.view;

import javax.swing.*;
import java.awt.*;

public class ViewPanel extends JPanel {
    private int[] xValues, yValues;

    ViewPanel(int[] xValues, int[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.setPreferredSize(new Dimension(1400, 700));
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        int initPosX = 50;
        int initPosY = 20;

        g2D.drawLine(initPosX, initPosY, initPosX, initPosY + 650);
        g2D.drawLine(initPosX, initPosY, initPosX - 5, initPosY + 10);
        g2D.drawLine(initPosX, initPosY, initPosX + 5, initPosY + 10);
        g2D.drawString("time, ms", initPosX + 10, initPosY + 5);

        g2D.drawLine(initPosX, initPosY + 650, initPosX + 1350, initPosY + 650);
        g2D.drawLine(initPosX + 1350, initPosY + 650, initPosX + 1340, initPosY + 645);
        g2D.drawLine(initPosX + 1350, initPosY + 650, initPosX + 1340, initPosY + 655);
        g2D.drawString("size", initPosX + 1335, initPosY + 640);

        int yMaxValue = -10000000;
        for (int i = 0; i < yValues.length; i++) {
            if (yValues[i] > yMaxValue) {
                yMaxValue = yValues[i];
            }
        }
        double yScale = (double) 650 / (2 * yMaxValue);

        int startPosX = initPosX;
        int startPosY = initPosY + 650;

        for (int i = 0; i < xValues.length; i++) {
            int res = (int) (initPosY + 650 - yScale * yValues[i]);

            g2D.setColor(Color.BLUE);
            g2D.drawLine(startPosX, startPosY, startPosX + 266, res);
            g2D.setColor(Color.BLACK);
            g2D.drawLine(initPosX - 5, res, initPosX + 5, res);
            g2D.drawString("" + yValues[i], initPosX - 40, res);
            g2D.drawLine(startPosX + 266, initPosY + 650 - 5, startPosX + 266, initPosY + 650 + 5);

            String xText = "" + xValues[i];  // one digit is ~ 16 pixels, so minus 4, because we need to move 1/2 of number

            g2D.drawString(xText, startPosX + 266 - xText.length() * 4, initPosY + 650 + 20);
            g2D.setColor(Color.LIGHT_GRAY);
            g2D.drawLine(initPosX, res, startPosX + 266, res);
            g2D.drawLine(startPosX + 266, initPosY + 650, startPosX + 266, res);

            startPosX += 266;
            startPosY = initPosY + 650 - (int) (yScale * yValues[i]);
        }
    }
}
