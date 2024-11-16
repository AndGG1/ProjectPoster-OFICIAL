package DTOS.UserInterfaces.Register;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
        attach1.setBounds(18, 10, 1164, 530); // Adjusted bounds to fit the larger frame
        frame.add(attach1);
        
        if (!img.equals("https://avatars.githubusercontent.com/u/154756433?v=4&size=64")) {
            // Placeholder for circle image with icon
            CircleImagePanel imagePanel = new CircleImagePanel("/path/to/your/image.png", false);
            imagePanel.setBounds(500, 50, 200, 200); // Increased size and positioned higher
            attach1.add(imagePanel);
        } else {
            // Example of web image URL
            CircleImagePanel webImagePanel = new CircleImagePanel(img, true);
            webImagePanel.setBounds(500, 50, 200, 200); // Increased size and positioned higher
            attach1.add(webImagePanel);
        }
        
        // Text label saying "Welcome, username!"
        JLabel welcomeLabel = new JLabel("Welcome, " + name + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font and size
        welcomeLabel.setBounds(500, 270, 200, 50); // Position the label close to the image
        attach1.add(welcomeLabel);
    }
    
    public static void main(String[] args) {
        new WelcomePage("https://avatars.githubusercontent.com/u/154756433?v=4&size=64", "username", "description");
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
                // Load image from local file
                URL imgUrl = getClass().getResource(imagePath);
                if (imgUrl != null) {
                    image = javax.imageio.ImageIO.read(imgUrl);
                } else {
                    System.err.println("Error: Unable to find image at " + imagePath);
                }
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
