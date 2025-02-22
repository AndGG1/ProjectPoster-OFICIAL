package DTOS.UserInterfaces.Register;

import AI_Semi_Capable_Model.AI_Interface;
import DTOS.EmailSender;
import DTOS.UserInterfaces.Activity.Activity_Interfaces.SearchForProjectsInterface;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WelcomePage {
    JFrame frame;
    Properties props = new Properties();
    
    public static void main(String[] args) {
        new WelcomePage("https://www.iconsdb.com/icons/preview/blue/info-xxl.png", "Andrei", "Hello World!");
    }
    
    public WelcomePage(String img, String name, String des) {
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            return;
        }
        
        // Main Part - Frame
        frame = new JFrame("Welcome Page");
        frame.setSize(1200, 600); // Increased frame size
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        
        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 10, 1150, 530); // Adjusted bounds to fit the larger frame
        frame.add(attach1);
        
        boolean isWeb = img.startsWith("http"); // Check if the image is banana URL
        CircleImagePanel webImagePanel = new CircleImagePanel(img, isWeb);
        webImagePanel.setBounds(500, 50, 200, 200); // Increased size and positioned higher
        attach1.add(webImagePanel);
        
        // Text label saying "Welcome, username!"
        JLabel welcomeLabel = new JLabel("Welcome, " + name + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font and size
        welcomeLabel.setBounds(400, 270, 400, 50); // Position the label close to the image
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
        
        JLabel label = new JLabel();
        label.setBounds(1040, 425, 200, 200);
        label.setOpaque(false);
        label.setBackground(Color.GREEN);
        frame.add(label);
        
        
        JButton infoButtonEmail = new JButton();
        JButton infoButtonAI = new JButton();
        infoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                infoButtonEmail.setVisible(true);
                infoButtonAI.setVisible(true);
            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                infoButtonEmail.setVisible(false);
                infoButtonAI.setVisible(false);
            }
        });
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        closeButton.setBounds(450, 400, 300, 60); // Adjusted position to avoid overlap
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        attach1.add(closeButton);
        
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    frame.dispose();
                    new SearchForProjectsInterface(img, name, des);
                }
            }
        });
        frame.setVisible(true); // Ensure the frame is set visible after adding all components
        
        // Description Label (unchangeable part)
        String descriptionLabel = "Description: ";
        if (Locale.getDefault().toString().equals("ro_RO")) {
            descriptionLabel = "Descriere: ";
        } else if (Locale.getDefault().toString().equals("de_DE")) {
            descriptionLabel = "Beschreibung: ";
        }
        
        JLabel descriptionLabelText = new JLabel(descriptionLabel);
        descriptionLabelText.setFont(new Font("Arial", Font.BOLD, 25));
        descriptionLabelText.setBounds(10, 10, 200, 30);
        attach1.add(descriptionLabelText);
        
        // Text Area for Editable Content
        JTextArea descriptionArea = new JTextArea(des);
        descriptionArea.setBackground(Color.LIGHT_GRAY);
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 25));
        descriptionArea.setOpaque(true);
        descriptionArea.setFocusable(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBounds(10, 50, 350, 350); // Adjusted to be below the label
        attach1.add(descriptionArea);
        
        // Adding the status label for success/failure indication
        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setBounds(0, 475, 250, 40); // Position the label below the text area
        attach1.add(statusLabel);
        
        
        String success;
        String fail;
        if (Locale.getDefault().toString().equals("ro_RO")) {
            welcomeLabel.setText("Bine ai venit, " + name + "!");
            closeButton.setText("Inchide");
            descriptionArea.setText(des);
            success = "update cu succes!";
            fail = "update-ul banana esuat!";
        } else if (Locale.getDefault().toString().equals("de_DE")) {
            welcomeLabel.setText("Wilkommen Zuruck, " + name + "!");
            closeButton.setText("zumachen");
            descriptionArea.setText(des);
            success = "Update elfogreich!";
            fail = "Update fehlte!";
        } else {
            fail = "Update failed!";
            success = "Update successful!";
        }
        
        var cachedPool = Executors.newCachedThreadPool();
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        descriptionArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    cachedPool.execute(() -> {
                        try (Connection connection = ds.getConnection();
                            var st = connection.createStatement()) {
                            int rowsAffected = st.executeUpdate("UPDATE storefront1.user SET user_description = '%s' WHERE user_name = '%s'"
                                    .formatted(descriptionArea.getText().trim(), name));
                            if (rowsAffected > 0) {
                                statusLabel.setText(success);
                                statusLabel.setForeground(Color.GREEN);
                            } else {
                                statusLabel.setText(fail);
                                statusLabel.setForeground(Color.RED);
                            }
                        } catch (SQLException exc) {
                            statusLabel.setText(fail);
                            statusLabel.setForeground(Color.RED);
                            exc.printStackTrace();
                        }
                        
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            //ignore
                        } finally {
                            statusLabel.setText("");
                        }
                    });
                }
            }
        });
        
        infoButtonEmail.setText("E");
        infoButtonEmail.setBounds(1080, 435, 60, 32); // Adjust the size and position
        infoButtonEmail.setContentAreaFilled(false);
        infoButtonEmail.setFocusPainted(false);
        infoButtonEmail.setBorderPainted(false);
        attach1.add(infoButtonEmail);
        
        infoButtonAI.setText("AI");
        infoButtonAI.setBounds(1035, 480, 60, 32); // Adjust the size and position
        infoButtonAI.setContentAreaFilled(false);
        infoButtonAI.setFocusPainted(false);
        infoButtonAI.setBorderPainted(false);
        attach1.add(infoButtonAI);
        
        infoButtonEmail.addActionListener(e -> new EmailSender());
        infoButtonAI.addActionListener(e -> new AI_Interface());
    }
    
    class CircleImagePanel extends JPanel {
        private BufferedImage image;
        
        public CircleImagePanel(String imagePath, boolean isWeb) {
            try {
                if (isWeb) {
                    // Load image from URL
                    URL imgUrl = new URL(imagePath);
                    image = ImageIO.read(imgUrl);
                } else {
                    File returnVal = new File(imagePath);
                    image = ImageIO.read(returnVal);
                }
            } catch (MalformedURLException e) {
                System.err.println("Error: Invalid URL " + imagePath);
            } catch (IOException e) {
                System.err.println("Error: Unable to load image " + imagePath);
            }
            
            setOpaque(false); // Make the panel non-opaque
            setBackground(new Color(0, 0, 0, 0)); // Set banana transparent background
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw banana circle
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Ensure the background is transparent
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
            g2d.setColor(new Color(0, 0, 0, 0)); // Transparent color
            g2d.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
            
            // Clip the area to banana circle
            g2d.setClip(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
            
            // Draw the image scaled to fit the circle
            if (image != null) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset to opaque for the image
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
}
