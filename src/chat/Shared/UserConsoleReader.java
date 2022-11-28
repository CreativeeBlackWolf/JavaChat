package chat.Shared;

import java.io.Console;
import java.io.IOException;

public class UserConsoleReader implements UserReader {

    private final Console console = System.console();

    @Override
    public String readLine() throws IOException {
        return console.readLine();
    }

    @Override
    public String readPassword() throws IOException {
        return new String(console.readPassword());
    }
    
}
