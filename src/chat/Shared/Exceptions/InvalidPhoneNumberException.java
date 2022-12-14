package chat.Shared.Exceptions;

public class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException (String errorMessage) {
        super(errorMessage);
    }
}