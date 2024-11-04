package Database.Functionality.Startup;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Startup_Sign {
    private static Properties props = new Properties();
    
    public static void addUser(String username, String password, String link, String img, String description) {
        if (Startup_Log.searchUser(username)) return;
        
            try {
                props.load(Files.newInputStream(Path.of("storefront.properties"),
                        StandardOpenOption.READ));
            } catch (IOException e) {
                return;
            }
            
            String DATABASE_NAME = "storefront" + props.getProperty("id");
            String query = "INSERT INTO %s.user (user_name, user_pass, user_link, user_img, user_description) VALUES %s, %s, %s, %s, %s"
                    .formatted(DATABASE_NAME, username, password, link, img, description);
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword("pass");
        
        try (Connection conn = ds.getConnection();
             Statement st = conn.createStatement()) {
            
            st.executeUpdate(query);
            if (st.getUpdateCount() != 1) {
                System.out.println(("An unexpected error has occurred while adding User: " + username));
            }
        } catch (SQLException e) {
            return;
        }
    }
}
