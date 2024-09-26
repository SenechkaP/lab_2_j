package mystructures;

import java.util.ArrayList;
import java.util.Comparator;

public class Calculations {
    public static float getMedian(ArrayList<Long> array) {
        int size = array.size();
        array.sort(Comparator.naturalOrder());
        if (size % 2 == 0) {
            return (float) (array.get((size - 1) / 2) + array.get(size / 2)) / 2;
        }
        return array.get(size / 2);
    }
}
