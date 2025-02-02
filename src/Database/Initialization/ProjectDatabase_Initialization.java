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

public class ProjectDatabase_Initialization {
    private static final String USE_SCHEMA = "USE projects";
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
            connection.setAutoCommit(false);
            
            // Create database if not exists
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS projects";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createDatabaseQuery);
            }
            
            connection.setCatalog("projects");
            
            // Create tables
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                String tableName = "section" + ch;
                String query = "CREATE TABLE " + tableName + " (" +
                        "project_id INT NOT NULL AUTO_INCREMENT, " +
                        "project_name TEXT NOT NULL, " +
                        "project_link TEXT NOT NULL, " +
                        "project_description TEXT NOT NULL, " +
                        "PRIMARY KEY (project_id)" +
                        ")";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(query);
                }
            }
            
            connection.commit();
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
