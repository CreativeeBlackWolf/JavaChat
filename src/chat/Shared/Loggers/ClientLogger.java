package chat.Shared.Loggers;
import java.io.IOException;
import java.util.logging.*;

public class ClientLogger {
    private static final Logger log = Logger.getLogger(ClientLogger.class.getName());
    public static void client_logging(Exception e) throws IOException {

        Handler ch = new ConsoleHandler();
        log.addHandler(ch);
        log.log(Level.INFO, "", e);

        Handler fh = new FileHandler("ClientLog", true);
        log.addHandler(fh);
        log.log(Level.INFO, "", e);

    }
}
