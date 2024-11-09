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
    private static Properties props2 = new Properties();
    
    public static void addUser(String username, String password, String link, String img, String description) {
        if (Startup_Log.searchUser(username)) return;
        
        final var path = Path.of("users.properties");
        try {
                props.load(Files.newInputStream(Path.of("storefront.properties"),
                        StandardOpenOption.READ));
                props2.load(Files.newInputStream(path));
            } catch (IOException e) {
                return;
            }
            
            String DATABASE_NAME = "storefront" + props.getProperty("id");
            String query = String.format("INSERT INTO %s.user (user_name, user_pass, user_link, user_img, user_description) VALUES ('%s', '%s', '%s', '%s', '%s')", DATABASE_NAME, username, password, link, img, description);
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        try (Connection conn = ds.getConnection();
             Statement st = conn.createStatement()) {
            
            Files.writeString(path, "\n%s=%s".formatted(username, props.getProperty("id")), StandardOpenOption.APPEND);
            
            st.executeUpdate(query);
            if (st.getUpdateCount() != 1) {
                System.out.println(("An unexpected error has occurred while adding User: " + username));
            }
        } catch (SQLException | IOException e) {
            //do nothing
        }
    }
}
