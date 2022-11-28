package chat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Pattern;

import chat.Shared.AuthencationResponce;

public class ClientHandler {
    private static final Pattern NICKNAME_RULES = Pattern.compile("\\w+");

    protected final String username;
    private final PrintWriter socketWriter;
    private final BufferedReader socketReader;
    protected final Socket clientSocket;
    private final Map<String, ClientHandler> clients;


    public ClientHandler(Socket clientSocket, Map<String, ClientHandler> clients) throws IOException {
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clients = clients;
        this.clientSocket = clientSocket;

        this.username = AuthenticateUser();
    }

    public void StartListening() {
        Broadcast("SERVER: " + username + " has joined chatroom!");
        try {
            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    Disconnect();
                    break;
                }

                // TODO should be decrypted
                String message = clientData;

                Broadcast(username + ": " + message);
            }
        } catch (Exception e) {
            Disconnect();
        }
    }

    private String AuthenticateUser() throws IOException {
        while (true) {
            String encryptedUsername = socketReader.readLine();
            // String decryptedUsername = decryptor.decryptString(encryptedUsername);
            String encryptedPassword = socketReader.readLine();
            // String decryptedPassword = decryptor.decryptString(encryptedPassword);
            if (NICKNAME_RULES.matcher(encryptedUsername).matches()) {
                // TODO replace to decryptedUsername when decryptor is ready
                return encryptedUsername;
            }
            else {
                // TODO send encrypted
                Send(AuthencationResponce.INVALID_USERNAME.name());
            }
        }
    }

    private void Disconnect() {
        clients.remove(username);
        Broadcast(username + " has disconnected.");
    }

    private void Broadcast(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            if (clientHandler != this) {
                // TODO send encrypted
                clientHandler.Send(message);
            }
        }
    }

    private void Send(String data) {
        try {
            socketWriter.print(data);
            socketWriter.flush();
        } catch (Exception e) {
            Disconnect();
        }
    }
}
