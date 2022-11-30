package chat.Shared.Loggers;

import java.io.IOException;
import java.util.logging.*;

public class ServerLogger {
    private static final Logger log = Logger.getLogger(ServerLogger.class.getName());
    public static void server_logging(Exception e) throws IOException {

        Handler ch = new ConsoleHandler();
        log.addHandler(ch);
        log.log(Level.INFO, "", e);

        Handler fh = new FileHandler("ServerLog", true);
        log.addHandler(fh);
        log.log(Level.INFO, "", e);

    }
}
