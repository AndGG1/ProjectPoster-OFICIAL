package Database.Functionality;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class Stats {
    static MysqlDataSource ds;
    static int id;
    
    static {
        try {
            id = Integer.parseInt(String.valueOf(Files.lines(Path.of("storefront.properties")).toArray()[2].toString().charAt(5)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser("And_GG");  // Make sure these properties are set correctly
        ds.setPassword("ER86Yt42");
    }
    
    public static void showStats() throws IOException, SQLException {
        String query = "SELECT * FROM storefront%s.user";
        
        Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        for (int i = 1; i <= id; i++) {
            System.out.println("DATABASE " + i + ": ");
            
            String currQ = query.formatted(i);
            ResultSet rs = st.executeQuery(currQ);   // Ensure ResultSet is properly managed
            ResultSetMetaData md = rs.getMetaData();
            
            System.out.println("size = " + md.getColumnCount()/3);
            
            while (rs.next()) {
                System.out.println("-".repeat(20));
                System.out.println("ID: " + rs.getInt(1));
                System.out.println("NAME: " + rs.getString("user_name"));
                System.out.println("PASS: " + rs.getString("user_pass"));
                System.out.println("LINK: " + rs.getString("user_link"));
            }
            System.out.println("-".repeat(20));
        }
    }
    
    public static void showUser(String username) throws SQLException {
        
        String query = "SELECT * FROM storefront%s.user WHERE user_name = '%s'";
        Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        
        for (int i = 1; i <= id; i++) {
            String currQ = query.formatted(id, username);
            if (st.executeQuery(currQ) != null) {
                ResultSet rs = st.executeQuery(currQ);   // Ensure ResultSet is properly managed
                rs.next();
                
                System.out.println("-".repeat(20));
                System.out.println("ID: " + rs.getInt(1));
                System.out.println("NAME: " + rs.getString("user_name"));
                System.out.println("PASS: " + rs.getString("user_pass"));
                System.out.println("LINK: " + rs.getString("user_link"));
                System.out.println("-".repeat(20));
            } else System.out.println(username + " not found in DB " + id);
        }
    }
}
