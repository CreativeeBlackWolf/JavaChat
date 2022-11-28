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
                    ExecuteCommand(line);
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ExecuteCommand(String command) {
        if (command.startsWith("!clients")) {
            PrintListOfClients();
        } else if (command.startsWith("!broadcast")) {
            Broadcast(command.substring("!broadcast ".length()));
        } 
        else {
            System.err.println("Unknown command, type !help to see all commmands.");
        }
    }

    private void PrintListOfClients() {
        System.out.println("Users online: " + clients.size());
        System.out.println("Users list:");
        for (String username : clients.keySet()) {
            System.out.println("\t" + username);
        }
    }

    private void Broadcast(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.Send("SERVER: " + message);
        }
    }
}
