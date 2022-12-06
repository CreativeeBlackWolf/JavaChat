package chat.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

import chat.Shared.AuthencationResponce;
import chat.Shared.UserConsoleReader;
import chat.Shared.UserReader;
import chat.Shared.UserSystemInReader;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Security.DH;
import chat.Shared.Security.RSA;
import chat.Shared.Utils.KeyConverter;
import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;


public class Client {
    public static void main(String[] args) throws IOException {
        User user = new User();
        DH hell = new DH();
        Socket clientSocket = new Socket("localhost", 2727);

        PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream());

        // инициализируем принтер без шифрования, для чтения полученных ключей от сервера
        ResponsePrinter exchangingPrinter = new ResponsePrinter(clientSocket);

        // отправляем свой публичный ключ серверу 
        clientWriter.println(KeyConverter.keyToString(hell.getPublickey()));
        clientWriter.flush();

        // считаем приватный ключ
        hell.setReceiverPublicKey(
            (PublicKey) KeyConverter.stringToKey(exchangingPrinter.readLine(), "EC", false)
        );

        // получаем зашифрованный публичный ключ сервера, и тут же расшифровываем его
        PublicKey serverPublicKey = (PublicKey) KeyConverter.stringToKey(
            hell.decrypt(exchangingPrinter.readLine()), "RSA", false);
        // получаем зашифрованный приватный ключ сервера, и тут же расшифровываем его
        PrivateKey serverPrivateKey = (PrivateKey) KeyConverter.stringToKey(
            hell.decrypt(exchangingPrinter.readLine()), "RSA", true);

        // применяем полученные ключи от сервера
        RSA security = new RSA(serverPublicKey, serverPrivateKey);
    
        // инициализируем принтер с шифрованием
        ResponsePrinter printer = new ResponsePrinter(clientSocket, security);

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

            clientWriter.println(security.encrypt(user.getUsername()));
            clientWriter.println(security.encrypt(user.getPassword()));
            clientWriter.flush();
            authResponse = AuthencationResponce.valueOf(security.decrypt(printer.readLine()));
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
                        clientWriter.println(security.encrypt(user.getName()));
                        clientWriter.println(security.encrypt(user.getLastName()));
                        clientWriter.println(security.encrypt(user.number.convertToStandard()));
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
                clientWriter.println(security.encrypt(message));
                clientWriter.flush();
            }
        }
    }
}
