package chat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import chat.Shared.Security.RSA;

public class ResponsePrinter implements Runnable {
    private BufferedReader socketReader;
    private RSA security;
    
    public ResponsePrinter(Socket input, RSA security) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream()));
        this.security = security;
    }

    public ResponsePrinter(Socket input) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream())); 
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = socketReader.readLine();
                if (line != null) {
                    System.out.println(security.decrypt(line));
                }
                else {
                    System.err.println("Server disconnected :(");
                    // sys.exit необходим, так как в случае break'a главный поток останется работать.
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
