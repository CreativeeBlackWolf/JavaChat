package chat.Client;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Scanner;

import chat.Shared.Exceptions.InvalidNameException;
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

        input.close();
        new ChatMenu();
    }
}
