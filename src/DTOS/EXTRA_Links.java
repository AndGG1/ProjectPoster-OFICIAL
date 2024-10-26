package DTOS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//Really Specific Order!
//DO NOT ALTER!

// 0 = Open Password Eye | 1 = Closed Password Eye
public final class EXTRA_Links {

    private static ArrayList<ImageIcon> icons = new ArrayList<>();

    public static void doIt(String s) {
        Image img = null;
        try {
            img = ImageIO.read(new URL(s));
        } catch (IOException e) {
            //Not handling
        } finally {
            assert img != null;
            icons.add(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        }
    }

    public static ImageIcon getPassOpenEye() {
        //doIt("https://cdn-icons-png.flaticon.com/128/709/709724.png");
        return icons.get(0);
    }

    public static ImageIcon getPassClosedEye() {
        //doIt("https://cdn-icons-png.flaticon.com/128/9055/9055153.png");
        return icons.get(1);
    }


    //Link Validation
    public static boolean checkAbilityToCreate(String link)  {
        if (link.startsWith("https://") || link.startsWith("http://")) {
            return valid_Hyper_Text_Transfer_Protocol(link);
        }
        return false;
    }

    public static boolean valid_Hyper_Text_Transfer_Protocol(String httpToCheck) {

        try {
            URL url = new URL(httpToCheck);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }

        } catch (IOException e) {
            //TODO - handle
        }

        return false;
    }
}
