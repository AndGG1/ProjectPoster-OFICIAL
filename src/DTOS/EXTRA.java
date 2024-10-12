package DTOS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public final class EXTRA {

    private static ImageIcon imgIcon;

    public static void doIt(String s) {
        Image img = null;
        try {
            img = ImageIO.read(new URL(s));
        } catch (IOException e) {
            //Not handling
        } finally {
            assert img != null;
            imgIcon = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        }
    }

    public static ImageIcon getPassEye() {
        //doIt("https://cdn-icons-png.flaticon.com/128/709/709724.png");
        return imgIcon;
    }
}