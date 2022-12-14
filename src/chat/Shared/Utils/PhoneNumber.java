package chat.Shared.Utils;
import chat.Shared.Exceptions.InvalidPhoneNumberException;


public class PhoneNumber {
    private String number;

    public PhoneNumber(String number) throws InvalidPhoneNumberException {
        this.number = setNumber(number);
    }

    public PhoneNumber() {
        
    }

    public String setNumber(String number) throws InvalidPhoneNumberException {
        if(number.replaceAll("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", "").length() != 0){
            throw new InvalidPhoneNumberException("Неправильный номер телефона.");
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