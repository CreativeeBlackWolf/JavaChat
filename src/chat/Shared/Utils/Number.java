package chat.Shared.Utils;
import chat.Shared.Exceptions.InvalidNumberException;


public class Number {
    private String number;

    public Number(String number) throws InvalidNumberException {
        this.number = setNumber(number);
    }

    public Number() {
        
    }

    public String setNumber(String number) throws InvalidNumberException {
        if (!(number.startsWith("+7")) && !(number.startsWith("8"))) {
            throw new InvalidNumberException("Номер должен начинаться с \"+7\" или с \"8\".");
        }
        if (number.replaceAll("\\D", "").length() != 11){
            throw new InvalidNumberException("Номер должен содержать 11 цифр.");
        }

        this.number = number;
        return number;
    }

    public String getNumber() {
        return number;
    }

    // метод, приводящий номер к стандарту +7 (xxx) xxx-xx-xx
    public String convertToStandard(){
        if (number.startsWith("8")){
            number = number.replaceFirst("8", "7");
        }
        number = number.replaceAll("\\D", "");
        number = number.replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d+)", "+$1 ($2) $3-$4-$5");

        return number;
    }
}