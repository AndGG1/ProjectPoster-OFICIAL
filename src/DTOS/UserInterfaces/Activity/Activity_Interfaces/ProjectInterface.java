package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import Chat.ChatServer;
import Chat.Chat_Interface;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class ProjectInterface {
    static Properties props = new Properties();
    ChatServer serverChannel = new ChatServer();

    JFrame frame;
    JButton joinChatButton;

    public ProjectInterface(String projectName, String ownerName, String description, String link, JTextArea area, String username) throws IOException {
        ResourceBundle rb = null;
        if (Locale.getDefault().toString().equals("ro_RO") || Locale.getDefault().toString().equals("de_DE")) {
            rb = ResourceBundle.getBundle("BasicText", Locale.getDefault());
        }

        // Generate a random color (orange, cyan, or yellow)
        Color[] colors = {Color.ORANGE, Color.CYAN, Color.YELLOW};
        Color randomColor = colors[new Random().nextInt(colors.length)];

        // Calculate the word count of the description
        int wordCount = description.split("\\s+").length;

        // Create the frame
        frame = new JFrame("Project Interface");
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create the project name label
        JLabel nameLabel = new JLabel(projectName + " (" + ownerName + ")");
        nameLabel.setBounds(200, 20, 600, 50); // Centered at the top
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(randomColor); // Assign random highlight color
        frame.add(nameLabel);

        // Create the description label
        JLabel descriptionLabel = new JLabel("<html>" + description + "</html>");
        descriptionLabel.setBounds(150, 80, 700, 600); // Starts from the top and stretches down
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setOpaque(true);
        descriptionLabel.setBackground(randomColor); // Assign random highlight color
        frame.add(descriptionLabel);

        // Create the word count label
        JLabel wordCountLabel = new JLabel("Words: " + wordCount);
        wordCountLabel.setBounds(650, 680, 200, 30); // Below the description label, on the right side
        wordCountLabel.setHorizontalAlignment(JLabel.CENTER);
        wordCountLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        wordCountLabel.setOpaque(true);
        wordCountLabel.setBackground(randomColor); // Assign random highlight color
        frame.add(wordCountLabel);

        // Create the link header label
        JLabel linkHeaderLabel = new JLabel("LINK:");
        linkHeaderLabel.setBounds(350, 750, 100, 30);
        linkHeaderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        linkHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
        linkHeaderLabel.setOpaque(true);
        linkHeaderLabel.setBackground(randomColor); // Assign random highlight color
        frame.add(linkHeaderLabel);

        // Create the hyperlink label
        JLabel linkLabel = new JLabel("<html><a href=''>" + link + "</a></html>");
        linkLabel.setBounds(450, 750, 200, 30);
        linkLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        linkLabel.setForeground(Color.BLUE);
        linkLabel.setHorizontalAlignment(JLabel.CENTER);
        linkLabel.setOpaque(true);
        linkLabel.setBackground(randomColor); // Assign random highlight color
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(link));
                } catch (Exception ex) {
                    //ignore
                }
            }
        });
        frame.add(linkLabel);

        // Create the Join Chat button
        joinChatButton = new JButton("Join Chat");
        joinChatButton.setBounds(800, 900, 150, 40); // Bottom-right corner
        joinChatButton.setFont(new Font("Arial", Font.BOLD, 14));
        joinChatButton.setBackground(randomColor); // Assign random highlight color
        joinChatButton.setForeground(Color.BLACK);
        joinChatButton.setFocusPainted(false); // Remove the blue aura when clicked
        joinChatButton.setBorderPainted(false); // Remove default borders

        if (rb != null) {
            joinChatButton.setText(rb.getString("join"));
        }

        // Add an action listener for the button
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));

        joinChatButton.addActionListener(e -> {
            joinChatButton.setEnabled(false);
            frame.setState(Frame.ICONIFIED);

            try (Connection connection = ds.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT id, name, ip_address, port, users, onlineCount FROM servers.locations WHERE name = ?"
                 )) {

                statement.setString(1, projectName);
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) {
                    handleNewServerEntry(connection, projectName, username);
                } else if (!resultSet.getString(5).contains(username)) {
                    handleExistingServerUpdate(connection, resultSet, projectName, username);
                } else {
                    frame.setState(Frame.NORMAL);
                    joinChatButton.setEnabled(true);
                }
            } catch (SQLException | UnknownHostException ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred while joining the chat!", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        });

        frame.add(joinChatButton);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                area.setEnabled(true);
            }
        });
    }

    private void handleExistingServerUpdate(Connection connection, ResultSet resultSet, String projectName, String username) throws SQLException {
        try (PreparedStatement psUpdate = connection.prepareStatement(
                "UPDATE servers.locations SET ip_address = ?, port = ?, users = ?, onlineCount = ? WHERE name = ?"
        )) {
            psUpdate.setString(1, resultSet.getString(3));
            psUpdate.setInt(2, resultSet.getInt(4));
            psUpdate.setString(3, resultSet.getString(5) + ", " + username);
            psUpdate.setInt(4, resultSet.getInt(6) + 1);
            psUpdate.setString(5, projectName);
            psUpdate.executeUpdate();
        }

        new Chat_Interface(serverChannel, false, resultSet.getString(3), projectName, frame, joinChatButton, username);
    }

    private void handleNewServerEntry(Connection connection, String projectName, String username) throws SQLException, UnknownHostException {
        String IP_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        new Chat_Interface(serverChannel, true, IP_ADDRESS, projectName, frame, joinChatButton, username);

        try (PreparedStatement psInsert = connection.prepareStatement(
                "INSERT INTO servers.locations (name, ip_address, port, users, onlineCount) VALUES (?, ?, ?, ?, ?)"
        )) {
            psInsert.setString(1, projectName);
            psInsert.setString(2, IP_ADDRESS);
            psInsert.setInt(3, 5000);
            psInsert.setString(4, username);
            psInsert.setInt(5, 1);
            psInsert.executeUpdate();
        }
    }

    public static void main(String[] args) throws IOException {
        // Example usage
        new ProjectInterface("Amazing Project", "Andrei",
                "This is a detailed and captivating project description that will now be displayed over multiple lines. "
                        + "The description can go on and on to test how it dynamically adjusts in the label.",
                "https://example.com", new JTextArea(), "Andrei" + new Random().nextInt(1, 1000));
    }
}
