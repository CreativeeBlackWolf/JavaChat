package chat.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import chat.Shared.AuthencationResponce;
import chat.Shared.UserConsoleReader;
import chat.Shared.UserReader;
import chat.Shared.UserSystemInReader;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;


public class Client {
    public static void main(String[] args) throws IOException {
        User user = new User();
        Socket clientSocket = new Socket("localhost", 2727);

        PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream());

        ResponsePrinter printer = new ResponsePrinter(clientSocket);
        UserReader consoleReader = System.console() == null ? new UserSystemInReader() : new UserConsoleReader();
        
        AuthencationResponce authResponse;

        do {
            System.out.print("Введите имя пользователя: ");
            user.setUsername(consoleReader.readLine());
            while (user.getPassword() == null) {
                System.out.print("Введите пароль: ");
                try {
                    user.setPassword(consoleReader.readPassword());
                } catch (InvalidPasswordException e1) {
                    System.out.println(e1);
                }
            }

            clientWriter.println(user.getUsername());
            clientWriter.println(user.getPassword());
            clientWriter.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            authResponse = AuthencationResponce.valueOf(printer.readLine());
            System.err.println(authResponse.name());
            if (authResponse == AuthencationResponce.REGISTER_PROCESS) {
                do {
                    try {
                        user.number = new Number();
                        System.out.print("Введите своё имя: ");
                        user.setName(consoleReader.readLine());
                        System.out.print("Введите свою фамилию: ");
                        user.setLastName(consoleReader.readLine());
                        System.out.print("Введите номер телефона: ");
                        user.number.setNumber(consoleReader.readLine());
                        clientWriter.println(user.getName());
                        clientWriter.println(user.getLastName());
                        clientWriter.println(user.number.ConvertToStandard());
                        clientWriter.flush();
                    } catch (InvalidNumberException | InvalidNameException e) {
                        System.out.println(e);
                    }
                } while (user.number.getNumber() == null ||
                         user.getName() == null ||
                         user.getLastName() == null);
            }
    
        } while (authResponse != AuthencationResponce.LOGIN_SUCCESS &&
                 authResponse != AuthencationResponce.REGISTERED &&
                 authResponse != AuthencationResponce.REGISTER_PROCESS);
        
        new Thread(printer).start();

        while (true) {
            String message = consoleReader.readLine();
            if (!message.equals("")) {
                clientWriter.println(message);
                clientWriter.flush();
            }
        }
    }
}
