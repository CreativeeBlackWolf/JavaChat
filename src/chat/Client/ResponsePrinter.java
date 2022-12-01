package chat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ResponsePrinter implements Runnable {
    private BufferedReader socketReader;
    
    public ResponsePrinter(Socket input) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream()));
        // TODO decryptor there
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = socketReader.readLine();
                if (line != null) {
                    System.out.println(line);
                }
                else {
                    System.err.println("Server disconnected :(");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() throws IOException {
        return socketReader.readLine();
    }
}
