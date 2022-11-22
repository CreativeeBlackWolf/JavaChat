package Exceptions;

public class InvalidNameException extends Exception{
    public InvalidNameException(String errorMessage) {
        super(errorMessage);
    }
}
