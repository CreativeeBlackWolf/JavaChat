package chat.Client;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Scanner;

import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Encryption.MessageEncryption;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Number number = new Number();
        User user;
        try {
            user = new User("not saqriphnix",
                            "Jason",
                            "Voorheese",
                            "69",
                            "Test1test",
                            number);
        } catch (InvalidNameException | InvalidPasswordException e1) {
            e1.printStackTrace();
            input.close();
            throw new InvalidObjectException("Объект пользователя инициализирован неверно."); 
        }

        while (user.number.getNumber() == null) {
            try {
                System.out.print("Введите номер телефона: ");
                user.number.setNumber(input.nextLine());
                UserDatabaseWorker worker = new UserDatabaseWorker();
                System.out.println(worker.UserExists(user.getUsername()));
                System.out.println(user.number.ConvertToStandard());
                System.out.println(user.getName());
                System.out.println(user.getLastName());
                System.out.println(user.getPassword());
                System.out.println(user.getStatusMessage());
            }
            catch (InvalidNumberException e) {
                System.out.println(e);
            }
        }

        MessageEncryption User1 = new MessageEncryption();
        MessageEncryption User2 = new MessageEncryption();
        String encryptMsg = User1.encrypt("Plain Text", User2.getPublicKey());
        System.out.println("Зашифрованное сообщение User-ом 1: " + encryptMsg);
        System.out.println("Расшифрованное сообщение User-ом 2: " + User2.decrypt(encryptMsg));

        input.close();
    }
}
