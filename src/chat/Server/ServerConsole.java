package chat.Server;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.Shared.ServerEvent;
import chat.Shared.UserConsoleReader;
import chat.Shared.UserReader;
import chat.Shared.UserSystemInReader;

public class ServerConsole implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ServerConsole.class);
    private final Map<String, ClientHandler> clients;
    private final UserReader consoleReader;

    public ServerConsole(Map<String, ClientHandler> clients) {
        this.clients = clients;
        this.consoleReader = System.console() == null 
        ? new UserSystemInReader() : new UserConsoleReader();
    }

    @Override
    public void run() {
        logger.info("Server console started.");
        while (true) {
            try {
                String line = consoleReader.readLine();
                if (line != null) {
                    executeCommand(line);
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCommand(String command) {
        if (command.startsWith("!clients ")) {
            printListOfClients();
        } else if (command.startsWith("!broadcast ")) {
            broadcast(command.substring("!broadcast ".length()));
        } else if (command.startsWith("!kick ")) {
            kick(command.substring("!kick ".length()));
        } else if (command.startsWith("!help")) {
            printHelp();
        } 
        else {
            System.err.println("Unknown command, type !help to see all commmands.");
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("!broadcast <message>");
        System.out.println("!kick <user>");
        System.out.println("!clients");
    }

    private void kick(String username) {
        try {
            ClientHandler client = clients.get(username);
            if (client!= null) {
                client.sendEncrypted(ServerEvent.MESSAGE_RECEIEVED.name());
                client.sendEncrypted("SERVER: You have been kicked!");
                client.clientSocket.close();
            }
        } catch (IOException e) {
            logger.error("Failed to kick client", e);
        }
    }

    private void printListOfClients() {
        System.out.println("Users online: " + clients.size());
        System.out.println("Users list:");
        for (String username : clients.keySet()) {
            System.out.println("\t" + username + "\n");
        }
    }

    private void broadcast(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.sendEncrypted(ServerEvent.MESSAGE_RECEIEVED.name());
            clientHandler.sendEncrypted("SERVER: " + message);
        }
    }
}
