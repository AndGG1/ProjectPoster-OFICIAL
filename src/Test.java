import DTOS.EXTRA_Links;
import DTOS.UserInterfaces.Register.SignInView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class Test {
    
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        
        try {
            EXTRA_Links.doIt("https://cdn-icons-png.flaticon.com/128/709/709724.png");
            EXTRA_Links.doIt("https://cdn-icons-png.flaticon.com/128/9055/9055153.png");
            EXTRA_Links.doIt("https://www.iconsdb.com/icons/preview/blue/info-xxl.png");
            
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            if (dimension.width >= 2000 && dimension.height >= 1000) {
                new SignInView();
            } else JOptionPane.showMessageDialog(new JFrame(), "Your screen is too small for this application! We are sorry!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "An error has occurred while trying to run the application. Restart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
