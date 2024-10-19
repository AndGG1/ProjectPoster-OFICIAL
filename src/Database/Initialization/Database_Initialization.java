package Database.Initialization;

import com.mysql.cj.jdbc.DatabaseMetaData;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database_Initialization {

    private static String USE_SCHEMA = "USE storefront";
    private static int MYSQL_DB_NOT_FOUND = 1049;

    public static void main(String[] args) {

        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser(System.getenv("MYSQLUSER"));
        ds.setPassword(System.getenv("MYSQLPASS"));

        try (Connection conn = ds.getConnection()) {

            DatabaseMetaData metaData = (DatabaseMetaData) conn.getMetaData();
            System.out.println(metaData.getSQLStateType());
            System.out.println(metaData.getConnection());

            if (!checkSchema(conn)) {
                System.out.println("storefront schema does not exist!");
                setUpSchema(conn);
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

    private static void setUpSchema(Connection conn) throws SQLException {

        String createSchema = "CREATE SCHEMA storefront";

        String createUser = """
                CREATE TABLE storefront.user (
                user_id int NOT NULL AUTOINCREMENT,
                user_name text NOT NULL,
                user_pass text NOT NULL,
                user_link text,
                user_img text,
                user_description,
                PRIMARY KEY (user_id)
                )
                """;

        try (Statement st = conn.createStatement()) {

            System.out.println("Creating storefront db.");
            st.execute(createSchema);
            if (checkSchema(conn)) {
                st.execute(createUser);
                System.out.println("Successfully created The User!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
