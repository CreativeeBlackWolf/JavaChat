package chat.Server;

import java.io.IOException;
import java.util.Map;

import chat.Shared.UserConsoleReader;
import chat.Shared.UserReader;
import chat.Shared.UserSystemInReader;

public class ServerConsole implements Runnable {
    private final Map<String, ClientHandler> clients;
    private final UserReader consoleReader;

    public ServerConsole(Map<String, ClientHandler> clients) {
        this.clients = clients;
        this.consoleReader = System.console() == null 
        ? new UserSystemInReader() : new UserConsoleReader();
    }

    @Override
    public void run() {
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
        if (command.startsWith("!clients")) {
            printListOfClients();
        } else if (command.startsWith("!broadcast")) {
            broadcast(command.substring("!broadcast ".length()));
        } 
        else {
            System.err.println("Unknown command, type !help to see all commmands.");
        }
    }

    private void printListOfClients() {
        System.out.println("Users online: " + clients.size());
        System.out.println("Users list:");
        for (String username : clients.keySet()) {
            System.out.println("\t" + username);
        }
    }

    private void broadcast(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.sendEncrypted("SERVER: " + message);
        }
    }
}
