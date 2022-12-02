package chat.Client;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Scanner;

import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Ecryption.PasswordHash;
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

        System.out.println(PasswordHash.getPasswordHash(user.getPassword(), PasswordHash.generateSalt()));
        input.close();
    }
}
