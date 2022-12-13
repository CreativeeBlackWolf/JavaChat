package chat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.regex.Pattern;

import chat.Shared.AuthencationResponse;
import chat.Shared.ServerEvent;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPhoneNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Security.DH;
import chat.Shared.Security.RSA;
import chat.Shared.Utils.KeyConverter;
import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;

public class ClientHandler {
    // Никнейм должен быть:
    // от 6 до 27 символов в длину
    // не должен иметь "." или "_" как первый или последний символ
    // не должен содержать "__", "..", "._", "_."
    // не должен содержать иные символы, кроме латинского алфавита, чисел и "." "_"
    private static final Pattern NICKNAME_RULES = Pattern.compile("^(?=.{6,27}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");

    protected String username;
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
        do { this.username = authenticateUser(); } while (username == null);
    }

    public void startListening() {
        broadcast(ServerEvent.USER_JOINED.name());
        broadcast(username);
        sendClientsList();
        try {
            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    disconnect();
                    break;
                }

                String message = security.decrypt(clientData);
                
                if (message.startsWith(":clients")) {
                    broadcast(ServerEvent.COMMAND_EXECUTED.name());
                    sendClientsList();
                }
                else {
                    broadcast(ServerEvent.MESSAGE_RECIEVED.name());
                    broadcast(username + ": " + message);
                }
            }
        } catch (Exception e) {
            disconnect();
        }
    }

    private String authenticateUser() throws IOException {
        String encryptedTypeOfAuth = socketReader.readLine();
        String decryptedTypeOfAuth = security.decrypt(encryptedTypeOfAuth);
        String encryptedUsername = socketReader.readLine();
        String decryptedUsername = security.decrypt(encryptedUsername);
        String encryptedPassword = socketReader.readLine();
        String decryptedPassword = security.decrypt(encryptedPassword);
        
        if (decryptedTypeOfAuth.equals("LOG_ME_IN")) {
            if (NICKNAME_RULES.matcher(decryptedUsername).matches()) {
                if (!clients.containsKey(decryptedUsername)) {
                    if (auth.isUserRegistered(decryptedUsername)) {
                        if (auth.authenticate(decryptedPassword, decryptedUsername)) {
                            sendEncrypted(AuthencationResponse.LOGIN_SUCCESS.name());
                            return decryptedUsername;
                        }
                        else {
                            sendEncrypted(AuthencationResponse.INVALID_PASSWORD.name());
                            return null;
                        }
                    }
                    else {
                        sendEncrypted(AuthencationResponse.INVALID_USERNAME.name());
                        return null;
                    }
                }
                else {
                    sendEncrypted(AuthencationResponse.ALREADY_LOGGED_IN.name());
                    return null;
                }
            }
            else {
                sendEncrypted(AuthencationResponse.INVALID_USERNAME.name());
                return null;
            }
        } else if (decryptedTypeOfAuth.equals("REGISTER_ME")) {
            // sendEncrypted(AuthencationResponse.REGISTER_PROCESS.name());
            String encryptedName = socketReader.readLine();
            String encryptedLastName = socketReader.readLine();
            String name = security.decrypt(encryptedName);
            String lastName = security.decrypt(encryptedLastName);
            
            try {
                String encryptedNumber = socketReader.readLine();
                Number number = new Number(security.decrypt(encryptedNumber));
                String check = auth.checkUnique(decryptedUsername, number.convertToStandard());
                if (!check.equals("CHECK_SUCCESSFULL")) {
                    sendEncrypted(AuthencationResponse.valueOf(check).name());
                    return null;
                }

                User user = new User(decryptedUsername, 
                                    name, 
                                    lastName, 
                                    "Ayo, i'm new there!", 
                                    decryptedPassword, 
                                    number);
                auth.registerUser(user);
                sendEncrypted(AuthencationResponse.REGISTERED.name());
                return decryptedUsername;
            } catch (InvalidNameException 
                    | InvalidPasswordException 
                    | IOException
                    | InvalidPhoneNumberException e) {
                e.printStackTrace();
                sendEncrypted("OOPS: server just got an exception! Please try again or contact developers.");
                return null;
            }
        } else {
            sendEncrypted("Authencation type " + decryptedTypeOfAuth + " unknown.");
            disconnect();
            return null;
        }
    }

    private void sendClientsList() {
        String message = "";
        for (String username : clients.keySet()) {
            message += username + " ";
        }
        sendEncrypted(ServerEvent.CLIENTS_LIST_RECIEVED.name());
        sendEncrypted(message);
    }

    private void disconnect() {
        clients.remove(username);
        broadcast(ServerEvent.USER_DISCONNECTED.name());
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
        send(KeyConverter.keyToString(security.getPublicKey()));
        send(security.encrypt("лъягушка", security.getPrivateKey()));
        hell.setReceiverPublicKey((PublicKey) KeyConverter.stringToKey(socketReader.readLine(), "EC", false));
        send(KeyConverter.keyToString(hell.getPublickey()));
        send(hell.encrypt(KeyConverter.keyToString(security.getPrivateKey())));
    }
}
