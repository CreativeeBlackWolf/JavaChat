package chat.Shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserSystemInReader implements UserReader {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public String readPassword() throws IOException {
        return reader.readLine();
    }
}
