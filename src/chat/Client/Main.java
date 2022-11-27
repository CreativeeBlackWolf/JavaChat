package chat.Client;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import chat.Shared.Ecryption.PasswordHash;
import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidNumberException;
import chat.Shared.Exceptions.InvalidPasswordException;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Number number = new Number();
        User user;
        try {
            user = new User("Jason",
                            "Voorheese",
                            "69",
                            "Test1test",
                            number);
        } catch (InvalidNameException | InvalidPasswordException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            input.close();
            throw new InvalidObjectException("Объект пользователя инициализирован неверно."); 
        }
        
        while (user.number.getNumber() == null) {
            try {
                System.out.print("Введите номер телефона: ");
                user.number.setNumber(input.nextLine());
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

        input.close();

        try {
            System.out.println(PasswordHash.getPasswordHash(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
