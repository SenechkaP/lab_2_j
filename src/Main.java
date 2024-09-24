import mylogging.ExcMsgLog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        MyArrayList<Integer> listik = new MyArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        try {

            ExcMsgLog log = new ExcMsgLog(3);

            log.write("Start programm: " + LocalDateTime.now().format(formatter));

            listik.add(5, log);
            listik.add(6, log);
            listik.add(7, log);
            listik.add(8, log);

            listik.remove(3, log);
            listik.remove(1, log);

            listik.add(12, log);

            listik.logInfo(log);

            log.write("Finish programm: " + LocalDateTime.now().format(formatter));

        } catch (IOException e) {
            System.out.println("problem");
        }

    }
}