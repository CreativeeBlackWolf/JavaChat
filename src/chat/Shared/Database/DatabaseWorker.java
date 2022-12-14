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
        connect(dbPath);
    }

    public boolean connect(String dbPath) throws SQLException, ConnectionNotClosedException {
        if (connection != null) {
            if (!(connection.isClosed())) {
                throw new ConnectionNotClosedException("Already connected to database.");
            }
        }
        connection = null;
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        return connection.isValid(0);
    }

    /** Проверяет, существует ли таблица в базе.
     * @param tableName имя таблицы
     * @return {@code true} если таблица существует, {@code false}, если нет
     * @throws SQLException
     */
    public boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet result = meta.getTables(null, null, tableName, new String[] {"TABLE"});
        
        return result.next();
    }

    public boolean close() throws SQLException {
        connection.close();

        return connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

}
