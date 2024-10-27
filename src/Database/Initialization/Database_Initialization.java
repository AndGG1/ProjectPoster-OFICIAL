package Database.Initialization;

import com.mysql.cj.jdbc.DatabaseMetaData;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database_Initialization {

    private static String USE_SCHEMA = "USE storefront";
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

            DatabaseMetaData metaData = (DatabaseMetaData) conn.getMetaData();
            System.out.println(metaData.getSQLStateType());
            System.out.println(metaData.getConnection());

            if (!checkSchema(conn)) {
                System.out.println("storefront schema does not exist! :)");
                setUpSchema(conn);
            } else System.out.println("The Schema does already exist! :(");
            
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

    private static void setUpSchema(Connection conn) throws SQLException {
        String createSchema = "CREATE SCHEMA storefront";

        String createUser = """
            CREATE TABLE storefront.user (
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
            } else System.out.println("User already exists as a Table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
