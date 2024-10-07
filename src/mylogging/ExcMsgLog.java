package mylogging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ExcMsgLog {
    private static Logger logger;
    private int exception_count = 0;

    public ExcMsgLog(String logFilePath, boolean append) throws IOException {
        LogManager.getLogManager().readConfiguration(ExcMsgLog.class.getResourceAsStream("logging.properties"));
        logger = Logger.getLogger(ExcMsgLog.class.getName());

        FileHandler fileHandler = new FileHandler(logFilePath, append);
        logger.addHandler(fileHandler);
    }

    public void writeFine(String msg) {
        logger.fine(msg);
    }

    public void writeInfo(String msg) {
        logger.info(msg);
    }

    public void writeSevere(String msg) {
        logger.severe(msg);
        exception_count += 1;
    }

    public int getException_count() {
        return exception_count;
    }
}
