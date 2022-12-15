package chat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JTextArea;

import chat.Shared.ServerEvent;
import chat.Shared.Security.RSA;

public class ResponsePrinter implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ResponsePrinter.class);
    private BufferedReader socketReader;
    private RSA security;
    private JTextArea chatArea;
    private JTextArea onlineUsersArea;
    
    public ResponsePrinter(Socket input, RSA security) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream()));
        this.security = security;
    }

    public ResponsePrinter(Socket input) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream())); 
    }

    public ResponsePrinter(Socket input, RSA security, JTextArea chatArea, JTextArea onlineUsersArea) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream()));
        this.security = security;
        this.chatArea = chatArea;
        this.onlineUsersArea = onlineUsersArea;
    }

    @Override
    public void run() {
        try {
            logger.info("Started ResponsePrinter thread. Listening for messages...");
            while (true) {
                String encryptedEvent = socketReader.readLine();

                if (encryptedEvent == null) {
                    logger.info("Server disconnected :(");
                    System.exit(0);
                }

                String event = security.decrypt(encryptedEvent);
                ServerEvent serverEvent = ServerEvent.valueOf(event);
                String line = security.decrypt(socketReader.readLine());
                if (chatArea != null) {
                    if (serverEvent == ServerEvent.USER_JOINED) {
                        onlineUsersArea.append(line + "\n");
                        chatArea.append("SERVER: " + line + " has joined chatroom!\n");
                    } else if (serverEvent == ServerEvent.MESSAGE_RECEIEVED) {
                        chatArea.append(line + "\n");
                    } else if (serverEvent == ServerEvent.CLIENTS_LIST_RECEIEVED) {
                        onlineUsersArea.selectAll();
                        onlineUsersArea.replaceSelection(null);
                        for (String client : line.split(" ")) {
                            onlineUsersArea.append(client + "\n");
                        }
                        chatArea.append("Список участников обновлён\n");
                    } else if (serverEvent == ServerEvent.USER_DISCONNECTED) {
                        chatArea.append("SERVER: " + line + " has disconnected.\n");
                    } else if (serverEvent == ServerEvent.COMMAND_EXECUTED) {
                        chatArea.append("Команда выполнена --" + line + "\n");
                    } else if (serverEvent == ServerEvent.COMMAND_WROTE_WRONG) {
                        chatArea.append("Команда написана неправильно --" + line + "\n");
                    } else if (serverEvent == ServerEvent.USER_PROFILE_RECEIEVED) {
                        chatArea.append("Профиль пользователя: " + line + "\n");
                    } else if (serverEvent == ServerEvent.SERVER_ERROR) {
                        chatArea.append("SERVER ERROR: " + line + "\n");
                    } else {
                        logger.warn("Got unhandled server event: " + serverEvent.name());
                        chatArea.append(event + " -- " + line + "\n");
                    }
                }
                else {
                    if (line != null) {
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /** Читает незашифрованную строку сервера
     * (используется только для первичного обмена ключами)
     * @return Строку сервера
     * @throws IOException
     */
    public String readLine() throws IOException {
        return socketReader.readLine();
    }

    /** Читает и расшифровывает строку сервера
     * @return Расшифрованную строку сервера
     * @throws IOException
     */
    public String readEncryptedLine() throws IOException {
        if (security != null) {
            return security.decrypt(socketReader.readLine());
        } else {
            return null;
        }
    }
}
