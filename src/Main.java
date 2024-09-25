import cocktails.BaseCocktail;
import cocktails.FastCocktail;
import cocktails.Ingredient;
import mylogging.ExcMsgLog;
import mystructures.MyArrayList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    static BaseCocktail generateFastCocktail() {
        return new FastCocktail(
                new Ingredient("ice", 0, (int) (Math.random() * 100)),
                new Ingredient("vodka", 38.5, (int) (Math.random() * 200))
        );
    }

    public static void main(String[] args) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        int n = 0;  // номер лог файла

        /*
        *      Эксперименты  для  ArrayList
        *                   |
        *                   V
        * */

        MyArrayList listik = new MyArrayList();

        for (int size = 10; n < 5; n++, size *= 10) {
            listik.clear();
            ExcMsgLog log = new ExcMsgLog(n);
            log.writeFine("Start programm: " + LocalDateTime.now().format(formatter));

            for (int j = 0; j < size; j++) {
                listik.add(generateFastCocktail(), log);
            }

            for (int j = 0; j < (int) (size * 0.1); j++) {
                listik.remove((int) (Math.random() * listik.size()), log);
            }

            listik.logInfo(log);
            log.writeFine("Finish programm: " + LocalDateTime.now().format(formatter));
        }
    }
}