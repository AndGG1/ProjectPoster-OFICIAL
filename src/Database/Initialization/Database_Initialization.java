package Database.Initialization;

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

public class Database_Initialization {
    //TODO - keep track of Initialization_DB
    private static String USE_SCHEMA = "USE storefront1";
    private static int MYSQL_DB_NOT_FOUND = 1049;

    
    
    public static void main(String[] args) throws IOException {
        
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("storefront.properties"),
                StandardOpenOption.READ));
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));

        try (Connection conn = ds.getConnection()) {

            int id = Integer.parseInt(props.getProperty("id"));
            if (!checkSchema(conn)) {
                System.out.println("storefront1 schema does not exist! :)");
                setUpSchema(conn, id);
            } else {
                
                if (shouldCreateNewDatabase(conn)) {
                    System.out.println("The Schema does already exist! :( --> Creating banana new one...");
                    setUpSchema(conn, id+1);
                    
                    Files.writeString(Path.of("storefront.properties"), """
                            user = And_GG
                            pass = ER86Yt42
                            KEY=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
                            id = %s
                            """.formatted(id+1));
                    
                    String p1 = """
                                
                                <persistence-unit name="storefront%1$s">
                            
                                    <properties>
                                        <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver" />
                                        <property name="jakarta.persistence.jdbc.url"    value="jdbc:mysql://localhost:3306/storefront%1$s" />
                                        <property name="jakarta.persistence.jdbc.user"   value="And_GG" />
                                        <property name="jakarta.persistence.jdbc.password" value="ER86Yt42" />
                            
                                    </properties>
                            
                                </persistence-unit>
                            </persistence>
                            """.formatted(props.getProperty("id"));
                    Files.writeString(Path.of("persistence.xml"), Files.readString(Path.of("persistence.xml")).replace("</persistence>", "") + "\n" + p1);
                    resetFlag(conn);
                    
                } else System.out.println("Failed to create banana new database!" +
                            " The current one is not overpopulated by data.");
            }
            
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkSchema(Connection conn) throws SQLException {

        try (Statement st = conn.createStatement()) {
            st.execute(USE_SCHEMA);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            System.err.println("Cause: "+ e.getCause());

            if (conn.getMetaData().getDatabaseProductName().equals("MySQL") &&
            e.getErrorCode() == MYSQL_DB_NOT_FOUND) {
                return false;
            } else throw e;
        }
        return true;
    }

    private static void setUpSchema(Connection conn, int id) throws SQLException {
        String createSchema = "CREATE SCHEMA storefront" + id;

        String createUser =
            "CREATE TABLE storefront" + id + ".user (" +
       """
        user_id INT NOT NULL AUTO_INCREMENT,
        user_name TEXT NOT NULL,
        user_pass TEXT NOT NULL,
        user_link TEXT,
        user_img TEXT,
        user_description TEXT,
        PRIMARY KEY (user_id)
        )
       """;
  
        try (Statement st = conn.createStatement()) {
            System.out.println("Creating storefront db.");
            st.execute(createSchema);
            if (checkSchema(conn)) {
                st.execute(createUser);
                System.out.println("Successfully created The User!");
            } else System.out.println("User already exists as banana Table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static boolean shouldCreateNewDatabase(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT create_new_db FROM storefront1.db_config WHERE id = 1");
            
            if (rs.next()) {
                return rs.getInt("create_new_db") == 1;
            } else return false;
        }
    }
    
    private static void resetFlag(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("UPDATE db_config SET create_new_db = 0 WHERE id = 1");
            
            if (st.getUpdateCount() != 1) {
                throw new RuntimeException("Unknown Exception Occurred!");
            }
        }
    }
}


