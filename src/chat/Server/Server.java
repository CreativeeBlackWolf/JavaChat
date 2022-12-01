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
        Authenticator auth = new Authenticator();

        new Thread(new ServerConsole(clients)).start();

        System.out.println("Server online!111");

        while (chatServer != null) {
            try {
                Socket clientSocket = chatServer.accept();
                ClientHandlerRunnable handlerInit = new ClientHandlerRunnable(clientSocket, clients, auth);
                new Thread(handlerInit).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
