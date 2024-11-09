import DTOS.EXTRA_Links;
import DTOS.UserInterfaces.Register.SignInView;
import Database.Functionality.Stats;

import java.io.IOException;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws IOException, SQLException {
        //This is temporary until we will have the Server and Client,
        // so that when the client joins, the data gets loaded and then the interface, not both in the same time.
        EXTRA_Links.doIt("https://cdn-icons-png.flaticon.com/128/709/709724.png");
        EXTRA_Links.doIt("https://cdn-icons-png.flaticon.com/128/9055/9055153.png");
        
        Stats.showStats();
        new SignInView();
    }
}