import Exceptions.InvalidNumberException;


public class Number {
    private String number;

    public Number(String number) throws InvalidNumberException {
        this.number = setNumber(number);
    }

    public Number() {

    }

    public String setNumber(String number) throws InvalidNumberException {
        if ((!(number.startsWith("+7")) && number.length() == 13) || 
            (!(number.startsWith("8")) && number.length() == 12)) {
            throw new InvalidNumberException("Номер должен начинаться с \"+7\" или с \"8\".");
        }
        this.number = number;
        return number;
    }

    public String getNumber() {
        return number;
    }

    // метод, приводящий номер к стандарту +7 (xxx) xxx-xx-xx
    public String ConvertToStandard(){
        if (number.contains("+") == false){
            number = number.replaceFirst("8", "+7");
        }
        number = number.replaceAll(" ", "");
        number = number.replaceAll("\\p{P}", "");
        number = number.replaceAll("-", "");

        char[] z = number.toCharArray();
        number = "";
        for (int k = 0; k < z.length; k++) {
            number += z[k];
            if (k == 1 && z[2] != '(') {
                number += " (";
            }
            if (k == 4 && z[6] != ')') {
                number += ") ";
            }
            if (k == 7 && z[10] != '-') {
                number += "-";
            }
            if (k == 9 && z[10] != '-') {
                number += "-";
            }
        }
        return number;
    }
}

