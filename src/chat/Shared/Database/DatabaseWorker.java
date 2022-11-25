package chat.Shared.Database;

import java.sql.*;


public class DatabaseWorker {
    private static Connection connection;
    private static ResultSet result;
    private static Statement stmt;
    
    public DatabaseWorker() {
    
    }

    public DatabaseWorker(String dbPath) throws SQLException {
        Connect(dbPath);
    }

    public boolean Connect(String dbPath) throws SQLException {
        if (connection != null) {
            if (!(connection.isClosed())) {
                // should throw ConnectionNotClosedException
            }
        }
        connection = null;
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        stmt = connection.createStatement();

        return connection.isValid(0);
    }

    public boolean TableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        result = meta.getTables(null, null, tableName, new String[] {"TABLE"});
        
        return result.next();
    }

    public boolean CreateTable(String tableName) {
        return false;
    }

    public boolean Close() throws SQLException {
        connection.close();
        stmt.close();
        result.close();

        return connection.isClosed() && stmt.isClosed() && result.isClosed();
    }

}
