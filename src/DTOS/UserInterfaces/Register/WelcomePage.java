package DTOS.UserInterfaces.Register;

import DTOS.EXTRA_Links;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomePage {
    JFrame frame;
    
    public WelcomePage(String img, String name, String des) {
        // Main Part - Frame
        frame = new JFrame("Welcome Page");
        frame.setVisible(true);
        frame.setSize(1200, 600); // Increased frame size
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);
        
        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 10, 1150, 530); // Adjusted bounds to fit the larger frame
        frame.add(attach1);
        
        boolean flag = true;
        if (!img.equals("https://avatars.githubusercontent.com/u/154756433?v=4&size=64")) flag = false;
        // Example of web image URL
        
        CircleImagePanel webImagePanel = new CircleImagePanel(img, flag);
        webImagePanel.setBounds(500, 50, 200, 200); // Increased size and positioned higher
        attach1.add(webImagePanel);
        
        
        // Text label saying "Welcome, username!"
        JLabel welcomeLabel = new JLabel("Welcome, " + name + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font and size
        welcomeLabel.setBounds(500, 270, 200, 50); // Position the label close to the image
        attach1.add(welcomeLabel);
        
        
        // Add info button to the bottom right corner
        JButton infoButton = new JButton();
        try {
            Image img2 = ImageIO.read(new URL("https://www.iconsdb.com/icons/preview/blue/info-xxl.png"));
            img2 = img2.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            infoButton.setIcon(new ImageIcon(img2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        infoButton.setBounds(1080, 465, 64, 64); // Adjust the size and position
        infoButton.setContentAreaFilled(false);
        infoButton.setFocusPainted(false);
        infoButton.setBorderPainted(false);
        attach1.add(infoButton);
        
        
        infoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            
            }
        });
    }
}



class CircleImagePanel extends JPanel {
    private BufferedImage image;
    
    public CircleImagePanel(String imagePath, boolean isWeb) {
        try {
            if (isWeb) {
                // Load image from URL
                URL imgUrl = new URL(imagePath);
                image = javax.imageio.ImageIO.read(imgUrl);
            } else {
                File returnVal = new File(imagePath);
                image = ImageIO.read(returnVal);
            }
        } catch (MalformedURLException e) {
            System.err.println("Error: Invalid URL " + imagePath);
        } catch (IOException e) {
            System.err.println("Error: Unable to load image " + imagePath);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw a circle
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE); // Background color for the circle
        g2d.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        
        // Clip the area to a circle
        g2d.setClip(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        
        // Draw the image scaled to fit the circle
        if (image != null) {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Image not loaded.");
        }
    }
    
    @Override
    public boolean isOpaque() {
        return false;
    }
}
