package Database.Functionality;

import Database.Functionality.Startup.Startup_Sign;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Properties;

public class Stats {
    //TODO: Upgrade (more features)
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        // e.g showUser(true, true, "Bro3");
        showStats(0);
    }
    
    static MysqlDataSource ds;
    static int id;
    static Properties props = new Properties();
    static Properties props2 = new Properties();
    
    static {
        try {
            final var path = Path.of("storefront.properties");
            id = Integer.parseInt(String.valueOf(Files.lines(path).toArray()[2].toString().charAt(5)));
            props.load(Files.newInputStream(path));
            props2.load(Files.newInputStream(Path.of("users.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser(props.getProperty("user"));  // Make sure these properties are set correctly
        ds.setPassword(props.getProperty("pass"));
    }
    
    private static void showStats(int timeout) throws IOException, SQLException, InterruptedException {
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
                Thread.sleep(timeout* 1000L);
            }
            System.out.println("-".repeat(20));
        }
    }
    
    //TODO
    private static void showUser(boolean delete, boolean deleteAll, boolean show, String... username) throws SQLException {
        try (var sessionFactory = Persistence
                .createEntityManagerFactory("storefront" + props2.getProperty(Startup_Sign.serializeObject(username[0])));
        EntityManager em = sessionFactory.createEntityManager()) {
            
            var transaction = em.getTransaction();
            transaction.begin();
            
            if (deleteAll) {
                for (int i = 1; i <= Integer.parseInt(props.getProperty("id")); i++) {
                    TypedQuery<User> query = em.createQuery("DELETE FROM storefront" + i + ".user", User.class);
                    query.executeUpdate();
                    transaction.commit();
                }
                em.close();
                return;
            }
            
            TypedQuery<User> query;
            for (String name : username) {
                query = em.createQuery("SELECT u FROM User u WHERE u.username LIKE ?1", User.class);
                query.setParameter(1, "%"+name+"%");
                User user = query.getSingleResultOrNull();
                
                if (user != null) {
                    if (show) System.out.println(user);
                    if (delete) {
                        em.remove(user);
                        props2.remove(Startup_Sign.serializeObject(user.getUsername()));
                    }
                    
                } else System.out.println(name + " wasn't found!");
            }
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
