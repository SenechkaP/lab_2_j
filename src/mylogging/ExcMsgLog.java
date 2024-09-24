package mylogging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ExcMsgLog {
    private static ArrayList <Exception> excList;
    private static Logger logger;

    public ExcMsgLog(int n) throws IOException {
        excList = new ArrayList<>();

        LogManager.getLogManager().readConfiguration(ExcMsgLog.class.getResourceAsStream("logging.properties"));
        logger = Logger.getLogger(ExcMsgLog.class.getName());

        FileHandler fileHandler = new FileHandler("src/mylogging/err_log.txt." + n, true);
//        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    public void write(String msg) {
        logger.fine(msg);
    }

    public void writeLog(String msg) {
        logger.info(msg);
    }
}
