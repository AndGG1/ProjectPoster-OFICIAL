package DTOS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Really Specific Order!
//DO NOT MODIFY!

// 0 = Open Password Eye | 1 = Closed Password Eye | 2 = Info Button
public final class EXTRA_Links {
    
    private static final ArrayList<ImageIcon> icons = new ArrayList<>();
    
    public static void getLink(String s) {
        Image img = null;
        try {
            img = ImageIO.read(new URL(s));
        } catch (IOException e) {
            //not handling
        } finally {
            assert img != null;
            icons.add(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        }
    }
    
    public static ImageIcon getPassOpenEye() {
        //getLink("https://cdn-icons-png.flaticon.com/128/709/709724.png");
        return icons.get(0);
    }
    
    public static ImageIcon getPassClosedEye() {
        //getLink("https://cdn-icons-png.flaticon.com/128/9055/9055153.png");
        return icons.get(1);
    }
    
    public static ImageIcon getInfoButton() {
        //getLink("https://www.iconsdb.com/icons/preview/blue/info-xxl.png");
        return icons.get(2);
    }
    
    
    //Link Validation
    public static boolean checkAbilityToCreate(String link) {
        if (link.startsWith("http://") || link.startsWith("https://") && !link.equals("https://site.com/John")) {
            
            Pattern emailPattern = Pattern.compile
                    ("([\\w.-]+)\\.([\\w .-]){2,}/(.+)");
            String checkOn = link.split("//")[1];
            Matcher matcher = emailPattern.matcher(checkOn);
            
            if (matcher.find()) {
                try {
                    URI uri = new URI(link);
                    URL url = uri.toURL();
                } catch (Exception e) {
                    return false;
                }
                return valid_Hyper_Text_Transfer_Protocol(link);
            }
        }
        return false;
    }
    
    private static boolean valid_Hyper_Text_Transfer_Protocol(String httpToCheck) {
        
        try {
            URL url = new URL(httpToCheck);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return ContentValidator.isValidContent(connection);
            }
            
        } catch (IOException e) {
            return false;
        }
        
        return false;
    }
    
    private static class ContentValidator {
        public static boolean isValidContent(HttpURLConnection connection) {
            
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String dummy = "";
                while ((dummy = in.readLine()) != null) {
                    content.append(dummy);
                    if (content.toString().contains("page isn't available") ||
                            content.toString().contains("page not found")) {
                        return false;
                    }
                }
                in.close();
                
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
    
    public static void accessLink(String link) {
        URI uri;
        try {
            uri = new URI(link);
            
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e) {
                    //not handling
                }
            }
        } catch (URISyntaxException e) {
            //not handling
        }
    }
}