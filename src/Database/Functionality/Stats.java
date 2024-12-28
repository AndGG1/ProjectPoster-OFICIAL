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
import java.util.Scanner;

public class Stats {
    //TODO: Upgrade (more features)
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("INDEX: ") || line.contains("POS: ")) {
                System.out.println(line.split(": ")[1] + ": " + getIndex(line.split(": ")[1]));
            } else if (line.contains("SHOW ALL: ") || line.contains("USERS: ") || line.contains("PEEK ALL: ")) {
                showStats(Integer.parseInt(line.split(": ")[1]));
            } else if (line.contains("DELETE ALL: ")) {
                if (line.split(": ")[1].split(" - ")[0].equals(props.getProperty("pass")) && line.split(": ")[1].contains(" - ")) {
                    deleteAll(1, Boolean.parseBoolean(line.split(" - ")[1]));
                }
            }
        }
    }
    
    static MysqlDataSource ds;
    static int id;
    static Properties props = new Properties();
    static Properties props2 = new Properties();
    
    static {
        try {
            final var path = Path.of("storefront.properties");
            id = Integer.parseInt(String.valueOf(Files.lines(path).toArray()[3].toString().charAt(5)));
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
    
    private static void showUser(boolean delete, boolean show, String... username) throws SQLException {
        
        try (var sessionFactory = Persistence
                .createEntityManagerFactory("storefront" + props2.getProperty(Startup_Sign.serializeObject(username[0])));
        EntityManager em = sessionFactory.createEntityManager()) {
            
            var transaction = em.getTransaction();
            transaction.begin();
            
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
    
    private static void deleteAll(int index, boolean showAll) throws SQLException, IOException, InterruptedException {
        try (var sessionFactory = Persistence.createEntityManagerFactory("storefront" + index);
        EntityManager em = sessionFactory.createEntityManager()) {
            
            var transaction = em.getTransaction();
            transaction.begin();
                TypedQuery<User> query = em.createQuery("DELETE FROM storefront" + index + ".user", User.class);
                query.executeUpdate();
                transaction.commit();
            transaction.commit();
        } finally {
            System.out.println("Deletion worked successfully");
            if (showAll) showStats(1);
        }
    }
    
    private static String getIndex(String username) {
        return props2.getProperty(Startup_Sign.serializeObject(username));
    }
}
