package chat.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import chat.Shared.Security.RSA;

public class ClientHandlerRunnable implements Runnable {

    private final Socket clientSocket;
    private final Map<String, ClientHandler> clients;
    private final Authenticator auth;
    private final RSA security;

    public ClientHandlerRunnable(Socket clientSocket, Map<String, ClientHandler> clients, Authenticator auth, RSA security) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.auth = auth;
        this.security = security;
    }

    @Override
    public void run() {
        try {
            ClientHandler newClient = new ClientHandler(clientSocket, clients, auth, security);
            clients.put(newClient.username, newClient);
            newClient.startListening();
        } catch (IOException e) {
            System.err.println("Client disconnected unexpectedly.");
        }
    }
}
