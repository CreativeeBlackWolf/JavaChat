package chat.Shared.Exceptions;

public class ConnectionNotClosedException extends Exception{
    public ConnectionNotClosedException(String errorMessage) {
        super(errorMessage);
    }
}
