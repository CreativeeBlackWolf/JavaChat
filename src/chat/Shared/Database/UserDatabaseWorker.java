package chat.Shared.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import chat.Shared.Utils.User;

public class UserDatabaseWorker {
    private static DatabaseWorker worker;
    private static Connection connection;

    public UserDatabaseWorker() {
        try {
            worker = new DatabaseWorker("./Users.db");
            connection = worker.getConnection();

            InitializeUsersDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void InitializeUsersDatabase() {
        try (Statement stmt = connection.createStatement()) {
            if (worker.TableExists("Users")) {
                return;
            }
            String statement = """
                CREATE TABLE "Users" (
                    "user_id"	INTEGER,
                    "username"	TEXT NOT NULL,
                    "name"	TEXT NOT NULL,
                    "last_name"	TEXT NOT NULL,
                    "password"	TEXT NOT NULL,
                    "status_message"	TEXT,
                    "phone_number"	TEXT NOT NULL UNIQUE,
                    "date_registered"	TEXT NOT NULL,
                    PRIMARY KEY("user_id" AUTOINCREMENT)
                );""";
            stmt.execute(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean AddUser(User user) {
        String statement = """
                INSERT INTO Users ("username", "name", "last_name", "password", "status_message", "phone_number", "date_registered")
                VALUES (?, ?, ?, ?, ?, ?, datetime("now", "localtime"));
                """;
        try (PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getStatusMessage());
            stmt.setString(6, user.number.ConvertToStandard());

            return stmt.execute();
        } catch (Exception e) {
            System.err.println("Cannot comply the statement at AddUser, an error has occured:");
            e.printStackTrace();
            return false;
        }
    }
}
