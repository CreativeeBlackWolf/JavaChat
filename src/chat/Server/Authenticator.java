package chat.Server;

import chat.Shared.DatabaseFields;
import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Utils.User;
import chat.Shared.Security.BCrypt;

public class Authenticator {
    
    private final UserDatabaseWorker db;
    private final BCrypt BCrypt;

    public Authenticator(UserDatabaseWorker db) {
        this.db = db;
        this.BCrypt = new BCrypt();
    }

    public boolean authenticate(String decryptedPassword, String username) {
        String hashedPassword = db.getParam(DatabaseFields.password, username);
        return BCrypt.compare(decryptedPassword, hashedPassword);
    }

    public String checkUnique(String username, String number) {
        return db.checkUnique(username, number);
    }

    public void registerUser(User user) {
        user.setHashedPassword(user.getPassword());
        db.addUser(user);
    }

    public boolean isUserRegistered(String username) {
        return db.userExists(username);
    }

    public void close() {
        db.close();
    }

}
