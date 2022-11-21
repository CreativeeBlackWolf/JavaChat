package Exceptions;

public class InvalidNumberException extends Exception {
    public InvalidNumberException (String errorMessage) {
        super(errorMessage);
    }
}