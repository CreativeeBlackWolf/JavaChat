package Database;

import java.sql.*;


public class DatabaseWorker {
    private static Connection connection;
    private static ResultSet result;
    private static Statement stmt;
    
    public DatabaseWorker() {
    
    }

    public boolean Connect(String dbPath) throws SQLException {
        if (!(connection.isClosed())) {
            // should throw ConnectionNotClosedException
        }
        connection = null;
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        return true;
    }

    public boolean Close() throws SQLException {
        connection.close();
        stmt.close();
        result.close();

        return true;
    }

}
