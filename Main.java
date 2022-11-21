import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        User statham = new User("Jason",
                            "Voorheese",
                            49,
                            "java1", 
                            "WHATS A CHAKACHA");
        statham.message();
        Number number1 = new Number("89533470922");
        number1.standard();
    }
}
