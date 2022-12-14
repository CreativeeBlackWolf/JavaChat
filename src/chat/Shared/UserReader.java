package chat.Shared;

import java.io.IOException;

public interface UserReader {
    String readLine() throws IOException;
    String readPassword() throws IOException;
}
