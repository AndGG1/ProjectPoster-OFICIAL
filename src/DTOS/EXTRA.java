package DTOS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

//Really Specific Order!
//DO NOT ALTER!
public final class EXTRA {

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
}