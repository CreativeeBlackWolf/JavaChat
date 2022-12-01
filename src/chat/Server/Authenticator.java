package chat.Server;

import chat.Shared.DatabaseFields;
import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Utils.User;

public class Authenticator {
    
    private UserDatabaseWorker db;

    public Authenticator() {
        db = new UserDatabaseWorker();
    }

    public boolean Authenticate(String decryptedPassword, String username) {
        String password = db.GetParam(DatabaseFields.password, username);
        return password.equals(decryptedPassword);
    }

    public void RegisterUser(User user) {
        db.AddUser(user);
    }

    public boolean IsUserRegistered(String username) {
        return db.UserExists(username);
    }

}
