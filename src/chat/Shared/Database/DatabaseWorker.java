package chat.Shared.Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.Shared.Exceptions.ConnectionNotClosedException;


public class DatabaseWorker {
    private static Connection connection;
    

    public DatabaseWorker(String dbPath) throws SQLException, ConnectionNotClosedException {
        Connect(dbPath);
    }

    public boolean Connect(String dbPath) throws SQLException, ConnectionNotClosedException {
        if (connection != null) {
            if (!(connection.isClosed())) {
                throw new ConnectionNotClosedException("Already connected to database.");
            }
        }
        connection = null;
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        return connection.isValid(0);
    }

    public boolean TableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet result = meta.getTables(null, null, tableName, new String[] {"TABLE"});
        
        return result.next();
    }

    public boolean Close() throws SQLException {
        connection.close();

        return connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

}
