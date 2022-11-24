package chatLogger;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public void WriteLog(String text){
        try(FileWriter fileWriter = new FileWriter("log.txt", true)){
            fileWriter.write(text + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
