package chat.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientHandlerRunnable implements Runnable {

    private final Socket clientSocket;
    private final Map<String, ClientHandler> clients;

    public ClientHandlerRunnable(Socket clientSocket, Map<String, ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        // TODO authencation
    }

    @Override
    public void run() {
        try {
            ClientHandler newClient = new ClientHandler(clientSocket, clients);
            clients.put(newClient.username, newClient);
            newClient.StartListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
