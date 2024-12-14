package Database.Functionality.Startup;

import Database.Functionality.User;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Startup_Log {
    
    //private static Properties props = new Properties();
    private static Properties props2 = new Properties();
    private static User user;
    private static String dbID = "none";
    
    public static boolean searchUser(String username) {
        try {
//            props.load(Files.newInputStream(Path.of("users.dat")));
//            return props.getProperty(username) != null;
            
            
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Path.of("users.dat")))) {
                while (true) {
                    try {
                        obj user = (obj) ois.readObject();
                        System.out.println(user.name());
                        if (user.name().equals(username)) {
                            dbID = String.valueOf(user.value());
                            return true;
                        }
                    } catch (IOException  | ClassNotFoundException e) {
                        break;
                    }
                }
        }
            return false;
        } catch (IOException e) {
            e.printStackTrace();  // Print stack trace for better debugging
            return false;
        }
    }
    
    public static boolean checkUser(String username, String password) {
        try {
            props2.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        String databaseName = "storefront" + dbID;
        if (databaseName == null) {
            System.out.println("Database name not found for username: " + username);
            return false;
        }
        
        // Print debug information
        System.out.println("Database Name: " + databaseName);
        System.out.println("User: " + props2.getProperty("user"));
        System.out.println("Password: " + props2.getProperty("pass"));
        
        String query = String.format("SELECT * FROM %s.user WHERE user_name='%s' AND user_pass='%s'", databaseName, username, password);
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props2.getProperty("user"));
        ds.setPassword(props2.getProperty("pass"));
        
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
