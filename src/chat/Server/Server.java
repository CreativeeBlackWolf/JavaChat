package chat.Server;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        final int PORT = 2727;

        ServerSocket chatServer = null;

        try {
            chatServer = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
        // TODO auth

        

        while (chatServer != null) {
            try {
                Socket clientSocket = chatServer.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
