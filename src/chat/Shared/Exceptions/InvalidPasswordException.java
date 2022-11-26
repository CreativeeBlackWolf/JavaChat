package src.chat.Shared.Exceptions;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(String erorMessage){
        super(erorMessage);
    }
}
