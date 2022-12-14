package chat.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.Shared.DatabaseFields;
import chat.Shared.Database.UserDatabaseWorker;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Utils.SecuredUser;
import chat.Shared.Security.BCrypt;


public class Authenticator {
    
    private final Logger logger = LoggerFactory.getLogger(Authenticator.class);
    
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

    public void registerUser(SecuredUser user) {
        try {
            user.setPassword(user.getPassword());
            db.addUser(user);
        } catch (InvalidPasswordException e) {
            logger.error(null, e);
        }
    }

    public boolean isUserRegistered(String username) {
        return db.userExists(username);
    }

    public void close() {
        db.close();
    }

}
