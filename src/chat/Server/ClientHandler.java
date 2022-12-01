package chat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Pattern;

import chat.Shared.AuthencationResponce;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Utils.User;
import chat.Shared.Utils.Number;

public class ClientHandler {
    private static final Pattern NICKNAME_RULES = Pattern.compile("\\w+");

    protected final String username;
    private final PrintWriter socketWriter;
    private final BufferedReader socketReader;
    private final Authenticator auth;
    protected final Socket clientSocket;
    private final Map<String, ClientHandler> clients;


    public ClientHandler(Socket clientSocket, Map<String, ClientHandler> clients, Authenticator auth) throws IOException {
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clients = clients;
        this.clientSocket = clientSocket;
        this.auth = auth;

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
                
                if (message.startsWith(":clients")) {
                    SendClientsList();
                }
                else {
                    Broadcast(username + ": " + message);
                }
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
                if (!clients.containsKey(encryptedUsername)) {
                    if (auth.IsUserRegistered(encryptedUsername)) {
                        if (auth.Authenticate(encryptedPassword, encryptedUsername)) {
                            Send(AuthencationResponce.LOGIN_SUCCESS.name());
                            return encryptedUsername;
                        }
                        else {
                            Send(AuthencationResponce.INVALID_PASSWORD.name());
                        }
                    }
                    else {
                        Send(AuthencationResponce.REGISTER_PROCESS.name());
                        String name = socketReader.readLine();
                        String lastName = socketReader.readLine();
                        
                        try {
                            Number number = new Number(socketReader.readLine());
                            // TODO replace to decrypted
                            User user = new User(encryptedUsername, 
                                                name, 
                                                lastName, 
                                                "Ayo, i'm new there!", 
                                                encryptedPassword, 
                                                number);
                            auth.RegisterUser(user);
                            Send(AuthencationResponce.REGISTERED.name());
                            return encryptedUsername;
                        } catch (InvalidNameException 
                                | InvalidPasswordException 
                                | IOException
                                | InvalidNumberException e) {
                            e.printStackTrace();
                            Send("OOPS: server just got an exception! Please try again or contact developers.");
                        }
                    }
                }
                else {
                    Send(AuthencationResponce.ALREADY_LOGGED_IN.name());
                }
            }
            else {
                // TODO send encrypted
                Send(AuthencationResponce.INVALID_USERNAME.name());
            }
        }
    }

    private void SendClientsList() {
        String message = "";
        for (String username : clients.keySet()) {
            message += "\t" + username;
        }
        Send(message);
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

    protected void Send(String data) {
        try {
            socketWriter.println(data);
            socketWriter.flush();
        } catch (Exception e) {
            Disconnect();
        }
    }
}
