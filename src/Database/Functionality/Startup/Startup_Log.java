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
    
    private static Properties props = new Properties();
    private static Properties props2 = new Properties();
    private static User user;
    
    public static boolean searchUser(String username) {
        try {
            props.load(Files.newInputStream(Path.of("users.properties")));
            return props.getProperty(username) != null;
        } catch (IOException e) {
            e.printStackTrace();  // Print stack trace for better debugging
            return false;
        }
    }
    
    public static boolean checkUser(String username, String password) {
        // Getting the props. needed to access the Server in a secure way.
        try {
            props2.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        // Ensuring the database name is correctly retrieved
        String databaseName = "storefront" + props.getProperty(username);
        if (databaseName == null) {
            System.out.println("Database name not found for username: " + username);
            return false;
        }
        
        String query = String.format("SELECT * FROM %s.user WHERE user_name='%s' AND user_pass='%s'", databaseName, username, password);
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser(props2.getProperty("user"));  // Make sure these properties are set correctly
        ds.setPassword(props2.getProperty("pass"));
        
        try (Connection conn = ds.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {  // Ensure ResultSet is properly managed
            
            // Process the ResultSet
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
                return false;  // No user found with the given username and password
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static User getUser() {
        System.out.println(user.getImg() + "-");
        return user;
    }
}
