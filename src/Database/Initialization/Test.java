package Database.Initialization;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class Test {
    
    public static void main(String[] args) throws IOException{

        
        reverse(new int[] {2, 1});
    }
    
    private static boolean executeSelect(Statement statement, String table,
                                         String columnName, String columnValue)
            throws SQLException {
        
        String query = "SELECT * FROM %s WHERE %s='%s'"
                .formatted(table, columnName, columnValue);
        var rs = statement.executeQuery(query);
        return rs != null;
    }
    
    private static boolean insertRecord(Statement st, String table, String[] names, String[] values)
            throws SQLException {
        
        String colNames = String.join(",", names);
        String colValues = String.join(",", values);
        String query = "INSERT INTO %s (%s) VALUES (%s)"
                .formatted(table, colNames, colValues); // using st.execute(x);
        System.out.println(query);
        int recordsIns = st.getUpdateCount();
        if (recordsIns > 0) {
            System.out.println(executeSelect(st, table, names[0], values[0]) + " is the winner!");
        }
        return recordsIns > 0;
    }
    
    private static boolean updateRecord(Statement st, String table, String originalName, String originalVal,
                                        String newName, String newVal) throws SQLException {
        
        String query = "UPDATE %s SET %s='%s' WHERE %s='%s'"
                .formatted(table, newName, newVal, originalName, originalVal);
        System.out.println(query);
        st.execute(query);
        int recordsUpdated = st.getUpdateCount();
        
        if (recordsUpdated > 0) {
            executeSelect(st, table, newName, newVal);
        }
        return recordsUpdated > 0;
    }
    
    private static void deleteArtistAlbum(Connection conn, Statement st,
                                          String artistName, String albumName) throws SQLException {
        
        System.out.println("AUTOCOMMIT = " + conn.getAutoCommit());
        conn.setAutoCommit(false);
        
        try {
            String deleteSongs = """
                    DELETE FROM music.songs WHERE album_id =
                    (SELECT ALBUM_ID from music.albums WHERE album_name = '%s')
                    """.formatted(albumName);
            int deletedSongs = st.executeUpdate(deleteSongs);
            System.out.printf("Deleted %s rows from music.songs%n", deletedSongs);
            
            st.addBatch(deleteSongs);
            st.addBatch(deleteSongs); // IK it's the same
            int[] results = st.executeBatch();
            
            //rest of code...
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        }
        conn.setAutoCommit(true);
    }
    
    private static void reverse(int[] arr) {
        int l = 0;
        int r = arr.length-1;
        while (l < r) {
            int temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            l++;
            r--;
        }
    }
}
