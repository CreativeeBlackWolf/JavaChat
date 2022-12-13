package chat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import chat.Shared.ServerEvent;
import chat.Shared.Security.RSA;

import javax.swing.*;

public class ResponsePrinter implements Runnable {
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
            while (true) {
                String event = security.decrypt(socketReader.readLine());
                ServerEvent serverEvent = ServerEvent.valueOf(event);
                String line = security.decrypt(socketReader.readLine());
                if (chatArea != null) {
                    if (serverEvent == ServerEvent.USER_JOINED) {
                        onlineUsersArea.append(line);
                        chatArea.append("SERVER: " + line + " has joined chatroom!\n");
                    } else if (serverEvent == ServerEvent.MESSAGE_RECIEVED) {
                        chatArea.append(line + "\n");
                    } else if (serverEvent == ServerEvent.CLIENTS_LIST_RECIEVED) {
                        onlineUsersArea.selectAll();
                        onlineUsersArea.replaceSelection(null);
                        for (String client : line.split(" ")) {
                            onlineUsersArea.append(client + "\n");
                        }
                    } else if (serverEvent == ServerEvent.USER_DISCONNECTED) {
                        chatArea.append("SERVER: " + line + " has disconnected.\n");
                    }
                }
                else {
                    if (line != null) {
                        System.out.println(line);
                    } else {
                        System.err.println("Server disconnected :(");
                        // sys.exit необходим, так как в случае break'a главный поток останется работать.
                        System.exit(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() throws IOException {
        return socketReader.readLine();
    }
}
