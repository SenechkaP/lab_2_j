package lab5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Model {
    public Map<Integer, int[]> getArrayListData() {

        Map<Integer, int[]> data = new HashMap<>();

        for (int i = 0, n = 10; i < 5; i++, n *= 10) {
            try (BufferedReader br = new BufferedReader(new FileReader("src/logs/ArrayList_log.txt." + i))) {
                String line;
                int flag = 0;
                int[] array = new int[4];

                while ((line = br.readLine()) != null) {
                    if (flag >= 4) {
                        break;
                    }

                    String[] splitLine = line.split(" ");

                    if (Objects.equals(splitLine[1], "removeTotalCount:")) {
                        continue;
                    }

                    if (Objects.equals(splitLine[1], "addTotalTime:") || flag > 0) {
                        array[flag] = (int) Double.parseDouble(splitLine[2]);
                        flag++;
                    }
                }

                data.put(n, array);
            } catch (IOException e) {
                System.out.println("problem with reading");
            }
        }
        return data;
    }

    public Map<Integer, int[]> getHashMapData() {

        Map<Integer, int[]> data = new HashMap<>();

        for (int i = 5, n = 10; i < 10; i++, n *= 10) {
            try (BufferedReader br = new BufferedReader(new FileReader("src/logs/HashMap_log.txt." + i))) {
                String line;
                int flag = 0;
                int[] array = new int[4];

                while ((line = br.readLine()) != null) {
                    if (flag >= 4) {
                        break;
                    }

                    String[] splitLine = line.split(" ");

                    if (Objects.equals(splitLine[1], "replaceTotalCount:")) {
                        continue;
                    }

                    if (Objects.equals(splitLine[1], "putTotalTime:") || flag > 0) {
                        array[flag] = (int) Double.parseDouble(splitLine[2]);
                        flag++;
                    }
                }

                data.put(n, array);
            } catch (IOException e) {
                System.out.println("problem with reading");
            }
        }
        return data;
    }
}
