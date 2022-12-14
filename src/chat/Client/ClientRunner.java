package chat.Client;

import java.io.IOException;
import java.net.UnknownHostException;

import chat.Shared.AuthencationResponse;
import chat.Shared.Utils.PhoneNumber;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Exceptions.InvalidPhoneNumberException;
import chat.Shared.Exceptions.ServerVerifyException;
import chat.Shared.Utils.DefaultUser;

public class ClientRunner {
    public static void main(String[] args) throws UnknownHostException, IOException, ServerVerifyException {
        Client client = new Client(Config.HOST, Config.PORT);
        AuthencationResponse authResponse = null;

        do {
            System.out.println("Вы хотите войти (1) или зарегистрироватся (2)? (введите число):");
            int answer = Integer.parseInt(client.consoleReader.readLine());
            if (answer == 1) {
                client.clientWriter.println(client.security.encrypt("LOG_ME_IN"));
                client.clientWriter.flush();
            }  else if (answer == 2) {
                client.clientWriter.println(client.security.encrypt("REGISTER_ME"));
                client.clientWriter.flush();
            } else {
                continue;
            }
            client.user = new DefaultUser(); 
            System.out.print("Введите имя пользователя: ");
            client.user.setUsername(client.consoleReader.readLine());
            while (client.user.getPassword() == null) {
                System.out.print("Введите пароль: ");
                try {
                    client.user.setPassword(client.consoleReader.readPassword());
                } catch (InvalidPasswordException e1) {
                    System.out.println(e1);
                }
            }

            client.clientWriter.println(client.security.encrypt(client.user.getUsername()));
            client.clientWriter.println(client.security.encrypt(client.user.getPassword()));
            client.clientWriter.flush();

            if (answer == 2) {
                do {
                    try {
                        client.user.number = new PhoneNumber();
                        System.out.print("Введите своё имя: ");
                        client.user.setName(client.consoleReader.readLine());
                        System.out.print("Введите свою фамилию: ");
                        client.user.setLastName(client.consoleReader.readLine());
                        System.out.print("Введите номер телефона: ");
                        client.user.number.setNumber(client.consoleReader.readLine());
                        client.clientWriter.println(client.security.encrypt(client.user.getName()));
                        client.clientWriter.println(client.security.encrypt(client.user.getLastName()));
                        client.clientWriter.println(client.security.encrypt(client.user.number.convertToStandard()));
                        client.clientWriter.flush();
                        authResponse = AuthencationResponse.valueOf(client.security.decrypt(client.securedPrinter.readLine()));
                        System.err.println(authResponse.name());
                    } catch (InvalidPhoneNumberException | InvalidNameException e) {
                        System.out.println(e);
                    }
                } while (client.user.number.getNumber() == null ||
                         client.user.getName() == null ||
                         client.user.getLastName() == null);
                if (authResponse == AuthencationResponse.PHONE_NUMBER_EXISTS) {
                    System.err.println("Процесс регистрации прерван, повторите попытку.");
                }
            } else {
                authResponse = AuthencationResponse.valueOf(client.security.decrypt(client.securedPrinter.readLine()));
                System.err.println(authResponse.name());
            }

        } while (authResponse != AuthencationResponse.LOGIN_SUCCESS &&
                 authResponse != AuthencationResponse.REGISTERED);
        
        new Thread(client.securedPrinter).start();

        while (true) {
            String message = client.consoleReader.readLine();
            if (!message.equals("")) {
                client.clientWriter.println(client.security.encrypt(message));
                client.clientWriter.flush();
            }
        }
    }
}
