package chat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.regex.Pattern;

import chat.Shared.AuthencationResponce;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Security.DH;
import chat.Shared.Security.RSA;
import chat.Shared.Utils.KeyConverter;
import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;

public class ClientHandler {
    private static final Pattern NICKNAME_RULES = Pattern.compile("\\w+");

    protected final String username;
    private final PrintWriter socketWriter;
    private final BufferedReader socketReader;
    private final Authenticator auth;
    protected final Socket clientSocket;
    private final Map<String, ClientHandler> clients;
    private final RSA security;
    private static final DH hell = new DH();


    public ClientHandler(Socket clientSocket, Map<String, ClientHandler> clients, Authenticator auth, RSA security) throws IOException {
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clients = clients;
        this.clientSocket = clientSocket;
        this.auth = auth;
        this.security = security;

        exchangeKeys();
        this.username = authenticateUser();
    }

    public void startListening() {
        broadcast("SERVER: " + username + " has joined chatroom!");
        try {
            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    disconnect();
                    break;
                }

                String message = security.decrypt(clientData);
                
                if (message.startsWith(":clients")) {
                    sendClientsList();
                }
                else {
                    broadcast(username + ": " + message);
                }
            }
        } catch (Exception e) {
            disconnect();
        }
    }

    private String authenticateUser() throws IOException {
        while (true) {
            String encryptedUsername = socketReader.readLine();
            String decryptedUsername = security.decrypt(encryptedUsername);
            String encryptedPassword = socketReader.readLine();
            String decryptedPassword = security.decrypt(encryptedPassword);
            if (NICKNAME_RULES.matcher(decryptedUsername).matches()) {
                if (!clients.containsKey(decryptedUsername)) {
                    if (auth.isUserRegistered(decryptedUsername)) {
                        if (auth.authenticate(decryptedPassword, decryptedUsername)) {
                            sendEncrypted(AuthencationResponce.LOGIN_SUCCESS.name());
                            return decryptedUsername;
                        }
                        else {
                            sendEncrypted(AuthencationResponce.INVALID_PASSWORD.name());
                        }
                    }
                    else {
                        sendEncrypted(AuthencationResponce.REGISTER_PROCESS.name());
                        String encryptedName = socketReader.readLine();
                        String encryptedLastName = socketReader.readLine();
                        String name = security.decrypt(encryptedName);
                        String lastName = security.decrypt(encryptedLastName);
                        
                        try {
                            String encryptedNumber = socketReader.readLine();
                            Number number = new Number(security.decrypt(encryptedNumber));
                            User user = new User(decryptedUsername, 
                                                name, 
                                                lastName, 
                                                "Ayo, i'm new there!", 
                                                decryptedPassword, 
                                                number);
                            auth.registerUser(user);
                            sendEncrypted(AuthencationResponce.REGISTERED.name());
                            return decryptedUsername;
                        } catch (InvalidNameException 
                                | InvalidPasswordException 
                                | IOException
                                | InvalidNumberException e) {
                            e.printStackTrace();
                            sendEncrypted("OOPS: server just got an exception! Please try again or contact developers.");
                        }
                    }
                }
                else {
                    sendEncrypted(AuthencationResponce.ALREADY_LOGGED_IN.name());
                }
            }
            else {
                sendEncrypted(AuthencationResponce.INVALID_USERNAME.name());
            }
        }
    }

    private void sendClientsList() {
        String message = "";
        for (String username : clients.keySet()) {
            message += "\t" + username;
        }
        sendEncrypted(message);
    }

    private void disconnect() {
        clients.remove(username);
        broadcast(username + " has disconnected.");
    }

    private void broadcast(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            if (clientHandler != this) {
                clientHandler.sendEncrypted(message);
            }
        }
    }

    private void send(String data) {
        try {
            socketWriter.println(data);
            socketWriter.flush();
        } catch (Exception e) {
            disconnect();
        }
    }

    protected void sendEncrypted(String data) {
        try {
            socketWriter.println(security.encrypt(data));
            socketWriter.flush();
        } catch (Exception e) {
            disconnect();
        }
    }

    private void exchangeKeys() throws IOException {
        send(KeyConverter.keyToString(hell.getPublickey()));
        hell.setReceiverPublicKey((PublicKey) KeyConverter.stringToKey(socketReader.readLine(), "EC", false));
        send(hell.encrypt(KeyConverter.keyToString(security.getPublicKey())));
        send(hell.encrypt(KeyConverter.keyToString(security.getPrivateKey())));
    }
}
