package Database.Functionality;

import Database.Functionality.Startup.Startup_Sign;
import Database.Initialization.ChatDatabase_Initialization;
import Database.Initialization.Database_Initialization;
import Database.Initialization.ProjectDatabase_Initialization;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

    public class Stats {
        // TODO: Upgrade (more features)
        private static Scanner scanner = null;

        public static void main(String[] args) throws SQLException, IOException, InterruptedException {
            scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("INDEX: ") || line.contains("POS: ")) {
                    String key = line.split(": ")[1];
                    System.out.println(key + ": " + getIndex(key));

                } else if (line.contains("SHOW ALL: ") || line.contains("USERS: ") || line.contains("PEEK ALL: ")) {
                    int id = Integer.parseInt(line.split(": ")[1]);
                    showStats(id);

                } else if (line.contains("DELETE ALL: ")) {
                    String[] parts = line.split(": ")[1].split(" - ");

                    if (parts[0].equals(props.getProperty("pass")) && line.split(": ")[1].contains(" - ")) {
                        int id = Integer.parseInt(props.get("id") + "");
                        boolean flag = Boolean.parseBoolean(parts[1]);
                        deleteAll(id, flag);

                        props2.forEach((name, key) -> {
                            if (key.equals(id)) props2.remove(name);
                        });
                    }

                } else if (line.contains("SHOW USER: ")) {
                    String[] parts = line.replace("SHOW USER: ", "").split(" - ");

                    boolean shouldDelete = Boolean.parseBoolean(parts[0]);
                    boolean shouldShow = Boolean.parseBoolean(parts[1]);
                    String[] names = parts[2].split(", ");

                    showUser(shouldDelete, shouldShow, names);

                } else if (line.equals("MAINTAIN") || line.contains("MANAGE") || line.contains("KEEP TRACK")) {
                    keepTrack();

                } else if (line.contains("MAINTAIN USERS")) {
                    keepTrackOfUsersJoining(Path.of("users.properties"));

                } else if (line.contains("MONITOR DATABASE")) {
                    Database_Initialization.main(new String[] {});

                } else if (line.contains("CLOSE")) {
                    break;

                } else if (line.equals("INIT DB")) {
                    Database_Initialization.main(new String[] {});
                    ChatDatabase_Initialization.main(new String[] {});
                    ProjectDatabase_Initialization.main(new String[] {});
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
    
    private static void showUser(boolean delete, boolean show, String[] username) {
        
        try (var sessionFactory = Persistence
                .createEntityManagerFactory("storefront" + props2.getProperty(Startup_Sign.serializeObject(username[0])));
        EntityManager em = sessionFactory.createEntityManager()) {
            
            var transaction = em.getTransaction();
            transaction.begin();
            
            TypedQuery<User> query;
            for (String name : username) {
                query = em.createQuery("SELECT u FROM User u WHERE u.username LIKE ?1", User.class);
                query.setParameter(1, name);
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
                em.createQuery("DELETE FROM User").executeUpdate();
                transaction.commit();
        } finally {
            System.out.println("Deletion worked successfully");
            if (showAll) showStats(1);
        }
    }
    
    private static String getIndex(String username) {
        return props2.getProperty(Startup_Sign.serializeObject(username));
    }
    
    private static void keepTrack() throws IOException, InterruptedException {
        System.out.println("Started keeping track!");
        System.out.println("-".repeat(30));
        var watcherService = FileSystems.getDefault().newWatchService();
        WatchKey watchKey = Paths.get(".").register(watcherService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        
        while (true) {
            watchKey = watcherService.take();
            
            watchKey.pollEvents().forEach(event -> {
                if (event.kind() != StandardWatchEventKinds.OVERFLOW) {
                    System.out.println("EVENT KIND: " + event.kind());
                    System.out.println("EVENT CONTEXT: " + event.context());
                    System.out.println("EVENT OBJ: " + event);
                    System.out.println("HAPPENED AT: " + LocalDateTime.now() + "\n");
                }
            });
            System.out.println();
            watchKey.reset();
        }
    }
    
    public static void keepTrackOfUsersJoining(Path users) throws InterruptedException, IOException {
        System.out.println("Started keeping track of users!");
        System.out.println("-".repeat(30));
        
        FileTime oldTime = Files.getLastModifiedTime(users);
        
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            
            FileTime currentFileTime = Files.getLastModifiedTime(users);
            if (!currentFileTime.equals(oldTime)) {
                List<String> lines = Files.readAllLines(users);
                String curr = String.join("\r", lines);
                
                System.out.println("Modification happened in users.properties!");
                System.out.println("New user/s:");
                System.out.println(curr);
                
                oldTime = currentFileTime;
            }
        }
    }
}
