package lab5;

import lab5.view.View;

import java.util.Map;

public class Controller {
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void run() {
        Map<Integer, int[]> arrayListRes = model.getArrayListData();
        Map<Integer, int[]> hashMapRes = model.getHashMapData();

        String[] graphTitlesArr = {"addTotalTime", "addMedianTime", "removeTotalTime", "removeMedianTime"};
        String[] graphTitlesHash = {"putTotalTime", "putMedianTime", "replaceTotalTime", "replaceMedianTime"};

        // for arrayListRes
        for (int i = 0; i < 4; i++) {
            int[] xValues = new int[5];
            int[] yValues = new int[5];

            for (int j = 0, n = 10; j < 5; j++, n *= 10) {
                xValues[j] = n;
                yValues[j] = arrayListRes.get(n)[i];
            }

            View view = new View(xValues, yValues, "ArrayList  " + graphTitlesArr[i]);
            view.setVisible(true);
        }

//         for hashMapRes
        for (int i = 0; i < 4; i++) {
            int[] xValues = new int[5];
            int[] yValues = new int[5];

            for (int j = 0, n = 10; j < 5; j++, n *= 10) {
                xValues[j] = n;
                yValues[j] = hashMapRes.get(n)[i];
            }

            View view = new View(xValues, yValues, "HashMap  " + graphTitlesHash[i]);
            view.setVisible(true);
        }
    }
}
