package chat.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Security.RSA;

public class ClientHandlerRunnable implements Runnable {

    private final Socket clientSocket;
    private final Map<String, ClientHandler> clients;
    private final RSA security;
    private final UserDatabaseWorker userDB;

    public ClientHandlerRunnable(Socket clientSocket, Map<String, ClientHandler> clients, RSA security, UserDatabaseWorker userDB) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.security = security;
        this.userDB = userDB;
    }

    @Override
    public void run() {
        try {
            ClientHandler newClient = new ClientHandler(clientSocket, clients, security, userDB);
            clients.put(newClient.username, newClient);
            newClient.startListening();
        } catch (IOException e) {
            System.err.println("Client disconnected unexpectedly.");
        }
    }
}
