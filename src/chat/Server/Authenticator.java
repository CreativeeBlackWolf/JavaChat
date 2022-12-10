package chat.Server;

import chat.Shared.DatabaseFields;
import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Utils.User;

public class Authenticator {
    
    private UserDatabaseWorker db;

    public Authenticator() {
        db = new UserDatabaseWorker();
    }

    public boolean authenticate(String decryptedPassword, String username) {
        String password = db.getParam(DatabaseFields.password, username);
        return password.equals(decryptedPassword);
    }

    public String checkUnique(String number) {
        return db.checkUnique(number);
    }

    public void registerUser(User user) {
        db.addUser(user);
    }

    public boolean isUserRegistered(String username) {
        return db.userExists(username);
    }

}
