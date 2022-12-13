package chat.Server;

import chat.Shared.DatabaseFields;
import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Utils.User;

public class Authenticator {
    
    private final UserDatabaseWorker db;

    public Authenticator(UserDatabaseWorker db) {
        this.db = db;
    }

    public boolean authenticate(String decryptedPassword, String username) {
        String password = db.getParam(DatabaseFields.password, username);
        return password.equals(decryptedPassword);
    }

    public String checkUnique(String username, String number) {
        return db.checkUnique(username, number);
    }

    public void registerUser(User user) {
        db.addUser(user);
    }

    public boolean isUserRegistered(String username) {
        return db.userExists(username);
    }

    public void close() {
        db.close();
    }

}
