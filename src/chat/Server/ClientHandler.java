package chat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import chat.Shared.AuthencationResponse;
import chat.Shared.DatabaseFields;
import chat.Shared.ServerEvent;
import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Exceptions.InvalidPhoneNumberException;
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
    private final UserDatabaseWorker userDB;


    public ClientHandler(Socket clientSocket, Map<String, ClientHandler> clients, RSA security, UserDatabaseWorker userDB) throws IOException {
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clients = clients;
        this.clientSocket = clientSocket;
        this.userDB = userDB;
        this.auth = new Authenticator(userDB);
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
                int messageArgsLength = message.split(" ").length;
                
                if (message.startsWith(":clients")) {
                    sendClientsList();
                } else if (message.toLowerCase().startsWith(":userprofile")) {
                    if (messageArgsLength == 2) {
                        sendProfileInfo(message.split(" ")[1]);
                    } else {
                        sendEncrypted(ServerEvent.COMMAND_WROTE_WRONG.name());
                        sendEncrypted("Команда должна принимать только 1 аргумент: username");
                    }
                } else if (message.toLowerCase().startsWith(":changestatus")) {
                    if (messageArgsLength >= 2) {
                        changeStatusMessage(
                            String.join(" ", 
                                Arrays.copyOfRange(message.split(" "), 1, messageArgsLength)
                            )
                        );
                    } else {
                        sendEncrypted(ServerEvent.COMMAND_WROTE_WRONG.name());
                        sendEncrypted("Команда должна принимать аргумент statusMessage");
                    }
                } else {
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
                        }
                    }
                    else {
                        sendEncrypted(AuthencationResponse.INVALID_USERNAME.name());
                    }
                }
                else {
                    sendEncrypted(AuthencationResponse.ALREADY_LOGGED_IN.name());
                }
            }
            else {
                sendEncrypted(AuthencationResponse.INVALID_USERNAME.name());
            }
        } else if (decryptedTypeOfAuth.equals("REGISTER_ME")) {
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
                } else {
                    User user = new User(decryptedUsername, 
                                        name, 
                                        lastName, 
                                        "Ayo, i'm new there!", 
                                        decryptedPassword, 
                                        number);
                    auth.registerUser(user);
                    sendEncrypted(AuthencationResponse.REGISTERED.name());
                    return decryptedUsername;
                }      
            } catch (InvalidNameException 
                    | InvalidPasswordException 
                    | IOException
                    | InvalidPhoneNumberException e) {
                e.printStackTrace();
                sendEncrypted("OOPS: server just got an exception! Please try again or contact developers.");
            }
        } else {
            sendEncrypted("Authencation type " + decryptedTypeOfAuth + " unknown.");
            disconnect();
        }
        return null;
    }

    private void sendProfileInfo(String username) {
        String message;
        if (!userDB.userExists(username)) {
            sendEncrypted(ServerEvent.COMMAND_WROTE_WRONG.name());
            sendEncrypted("Профиль с именем пользователя `" + username + "` не найден.");
            return;
        }
        String profileUsername = userDB.getParam(DatabaseFields.username, username);
        String profileName = userDB.getParam(DatabaseFields.name, username);
        String profileLastName = userDB.getParam(DatabaseFields.last_name, username);
        String profileStatusMessage = userDB.getParam(DatabaseFields.status_message, username);
        String profilePhoneNumber = userDB.getParam(DatabaseFields.phone_number, username);
        message = String.format("%s | %s | %s | %s | %s", 
                                profileUsername,
                                profileName,
                                profileLastName,
                                profileStatusMessage,
                                profilePhoneNumber);
        sendEncrypted(ServerEvent.USER_PROFILE_RECIEVED.name());
        sendEncrypted(message);
    }

    private void changeStatusMessage(String statusMessage) {
        if (userDB.setParam(DatabaseFields.status_message, username, statusMessage)) {
            sendEncrypted(ServerEvent.COMMAND_EXECUTED.name());
            sendEncrypted("Статус успешно изменён!");
        } else {
            sendEncrypted(ServerEvent.SERVER_ERROR.name());
            sendEncrypted("Произошла ошибка при обработке команды...");
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
        broadcast(username);
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
