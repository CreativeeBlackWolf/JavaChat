import java.io.IOException;
import java.util.Scanner;

import Exceptions.InvalidNumberException;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        User statham = new User("Jason",
                            "Voorheese",
                            49,
                            "java1", 
                            "WHATS A CHAKACHA");
        statham.message();
        Number number = new Number();
        while (number.getNumber() == null) {
            try {
                System.out.print("Введите номер телефона: ");
                number.setNumber(input.nextLine());
                System.out.println(number.ConvertToStandard());
            }
            catch (InvalidNumberException e) {
                System.out.println(e);
            }
        }

        input.close();
    }
}
