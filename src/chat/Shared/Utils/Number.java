package chat.Shared.Utils;
import chat.Shared.Exceptions.InvalidPhoneNumberException;


public class Number {
    private String number;

    public Number(String number) throws InvalidPhoneNumberException {
        this.number = setNumber(number);
    }

    public Number() {
        
    }

    public String setNumber(String number) throws InvalidPhoneNumberException {
        if (!(number.startsWith("+7")) && !(number.startsWith("8"))) {
            throw new InvalidPhoneNumberException("Номер должен начинаться с \"+7\" или с \"8\".");
        }
        if(number.replaceAll("[- +)(\\d]", "").length() != 0){
            throw new InvalidPhoneNumberException("В номере имеются недопустимые символы");
        }
        if (number.replaceAll("\\D", "").length() != 11){
            throw new InvalidPhoneNumberException("Номер должен содержать 11 цифр.");
        }

        this.number = number;
        return number;
    }

    public String getNumber() {
        return number;
    }

    /** Приводит номер к стандарту +X (XXX) XXX-XX-XX
     * @return стандартизированный номер в виде строки
     */
    public String convertToStandard(){
        if (number.startsWith("8")){
            number = number.replaceFirst("8", "7");
        }
        number = number.replaceAll("\\D", "");
        number = number.replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d+)", "+$1 ($2) $3-$4-$5");

        return number;
    }
}