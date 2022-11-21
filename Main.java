import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Scanner;
import Exceptions.InvalidNameException;
import Exceptions.InvalidNumberException;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Number number = new Number();
        User user;
        try {
            user = new User("Jason",
                            "Voorheese",
                            "69",
                            "java1", 
                            number);
        } catch (InvalidNameException e1) {
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
                System.out.println(user.getStatusMessage());
            }
            catch (InvalidNumberException e) {
                System.out.println(e);
            }
        }

        input.close();
    }
}
