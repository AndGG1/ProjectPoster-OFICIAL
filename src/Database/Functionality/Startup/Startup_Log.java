package Database.Functionality.Startup;

import Database.Functionality.User;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Startup_Log {
    private static Properties userProperties = new Properties();
    private static Properties serverProperties = new Properties();
    private static User user;
    
    public static boolean searchUser(String username) {
        try {
            userProperties.load(Files.newInputStream(Path.of("users.properties"), StandardOpenOption.READ));
            return userProperties.getProperty(Startup_Sign.serializeObject(username)) != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean checkUser(String username, String password) {
        try {
            serverProperties.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        String databaseName = "storefront" + userProperties.getProperty(Startup_Sign.serializeObject(username));
        if (databaseName == null) {
            System.out.println("Database name not found for username: " + username);
            return false;
        }
        
        String query = String.format("SELECT * FROM %s.user WHERE user_name='%s' AND user_pass='%s'", databaseName, username, password);
        System.out.println("Query: " + query);
        
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(serverProperties.getProperty("user")); // Ensure these properties match persistence.xml
        ds.setPassword(serverProperties.getProperty("pass"));
        
        try (Connection conn = ds.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            if (rs.next()) {
                user = new User(
                        rs.getString("user_name"),
                        rs.getString("user_pass"),
                        rs.getString("user_link"),
                        rs.getString("user_img"),
                        rs.getString("user_description")
                );
                System.out.println(user);
                System.out.println("User found: " + username);
                return true;
            } else {
                System.out.println("No user found with the given username and password.");
                return false;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static User getUser() {
        return user;
    }
}