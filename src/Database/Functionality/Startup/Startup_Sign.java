package Database.Functionality.Startup;

import com.mysql.cj.jdbc.MysqlDataSource;
import Database.Functionality.Startup.obj;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Startup_Sign {
    private static Properties props = new Properties();
    private static Properties props2 = new Properties();
    
    public static void addUser(String username, String password, String link, String img, String description) {
        if (Startup_Log.searchUser(username)) return;
        
        final var path = Path.of("users.dat");
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
            props2.load(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        String DATABASE_NAME = "storefront" + props.getProperty("id");
        String query = "INSERT INTO " + DATABASE_NAME + ".user (user_name, user_pass, user_link, user_img, user_description) VALUES (?, ?, ?, ?, ?)";
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        try (Connection conn = ds.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, link);
            ps.setString(4, img);
            ps.setString(5, description);
            ps.addBatch();
            
            ps.executeBatch();
            ps.clearBatch();
            
            appendUser(new obj(username, props.getProperty("id")));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void appendUser(obj user) throws IOException {
        boolean append = Files.exists(Path.of("users.dat"));
        try (FileOutputStream fos = new FileOutputStream("users.dat", true);
             ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            oos.writeObject(user);
            oos.flush();
        }
    }
    
    private static class AppendObjectOutputStream extends ObjectOutputStream {
        public AppendObjectOutputStream(FileOutputStream fos) throws IOException {
            super(fos);
        }
        
        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // Avoid writing the header
        }
    }
}
