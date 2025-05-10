package Database.Initialization;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ChatDatabase_Initialization {
    
    private static final String USE_SCHEMA = "USE servers";
    private static final int MYSQL_DB_NOT_FOUND = 1049;
    
    public static void main(String[] args) throws IOException {
        
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        try (Connection conn = ds.getConnection()) {
            if (!schemaExists(conn)) {
                createSchema(conn);
                System.out.println("PROJECT SCHEMA finished successfully! :)");
            } else {
                System.out.println("PROJECT SCHEMA already exists! :(");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void createSchema(Connection connection) {
        try {
            
            // Create database if not exists
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS servers";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createDatabaseQuery);
            }
            
            String query = "CREATE TABLE servers.locations (" +
                    """
                    id INT NOT NULL AUTO_INCREMENT,
                    name TEXT NOT NULL,
                    ip_address TEXT NOT NULL,
                    port INT NOT NULL,
                    users TEXT NOT NULL,
                    onlineCount INT NOT NULL,
                    PRIMARY KEY (id)
                    )
                    """;
            
            // Create tables
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }
    
    private static boolean schemaExists(Connection connection) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(USE_SCHEMA);
        } catch (SQLException e) {
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            
            if (connection.getMetaData().getDatabaseProductName().equals("MySQL") && e.getErrorCode() == MYSQL_DB_NOT_FOUND) {
                return false;
            } else {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
