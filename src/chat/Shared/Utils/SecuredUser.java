package chat.Shared.Utils;

import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Security.BCrypt;
import chat.Shared.Utils.ABC.User;

public class SecuredUser extends User {

    private String password;

    public SecuredUser(String username, String name, String lastName, String statusMessage, String password,
            PhoneNumber number) throws InvalidNameException, InvalidPasswordException {
        super(username, name, lastName, statusMessage, password, number);
    }

    public SecuredUser() {
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String setPassword(String password) throws InvalidPasswordException {
        BCrypt crypto = new BCrypt();
        return this.password = crypto.hash(10, password);
    }
    
}
