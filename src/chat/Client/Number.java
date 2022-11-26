package src.chat.Client;
import src.chat.Shared.Exceptions.InvalidNumberException;


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

        int count = 0;

        for(int i = 0; i < number.length(); i++)
        {
            if(Character.isDigit(number.charAt(i)))
                count++;
        }

        if (count != 11){
            throw new InvalidNumberException("В номере не должно быть больше или меньше 11-и цифр");
        }

        this.number = number;
        return number;
    }

    public String getNumber() {
        return number;
    }

    // метод, приводящий номер к стандарту +7 (xxx) xxx-xx-xx
    public String ConvertToStandard(){
        if (!number.contains("+")){
            number = number.replaceFirst("8", "+7");
        }
        number = number.replaceAll(" ", "");
        number = number.replaceAll("\\p{P}", "");
        number = number.replaceAll("-", "");
        number = number.replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d+)", "$1 ($2) $3-$4-$5");

        return number;
    }
}

