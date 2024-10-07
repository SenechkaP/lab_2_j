import cocktails.BaseCocktail;
import cocktails.FastCocktail;
import cocktails.Ingredient;
import mylogging.ExcMsgLog;
import mystructures.MyArrayList;
import mystructures.MyHashMap;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lab3 {
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

        MyArrayList listik;

        for (int size = 10; n < 5; n++, size *= 10) {
            listik = new MyArrayList();
            ExcMsgLog log = new ExcMsgLog("src/logs/ArrayList_log.txt." + n, false);
            log.writeFine("Start programm: " + LocalDateTime.now().format(formatter));

            for (int j = 0; j < size; j++) {
                listik.add(generateFastCocktail(), log);
            }

            for (int j = 0; j < (int) (size * 0.1); j++) {
                listik.remove((int) (Math.random() * listik.size()), log);
            }

            listik.logInfo(log);
            log.writeInfo("Amount of exceptions: " + log.getException_count());
            log.writeFine("Finish programm: " + LocalDateTime.now().format(formatter));
        }

        /*
         *      Эксперименты  для  HashMap
         *                   |
         *                   V
         * */

        MyHashMap hashik;

        for (int size = 10; n < 10; n++, size *= 10) {
            hashik = new MyHashMap();
            ExcMsgLog log = new ExcMsgLog("src/logs/HashMap_log.txt." + n, false);
            log.writeFine("Start programm: " + LocalDateTime.now().format(formatter));

            for (int j = 0; j < size; j++) {
                hashik.put(generateFastCocktail(), log);
            }

            for (int j = 0; j < (int) (size * 0.1); j++) {
                hashik.replace((int) (Math.random() * hashik.size()), generateFastCocktail(), log);
            }

            hashik.logInfo(log);
            log.writeInfo("Amount of exceptions: " + log.getException_count());
            log.writeFine("Finish programm: " + LocalDateTime.now().format(formatter));
        }
    }
}