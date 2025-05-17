package Chat;

import DTOS.EXTRA_Links;
import Database.Functionality.User;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Chat_Interface {
    private final JFrame frame;
    private final String ENCODED_MESSAGE = "#ER86Yt42//EmilutCuParulCretBagaPulaInCotet";
    
    public Chat_Interface(ChatServer serverChannel, boolean ownerOfServer, String IPAddress,
                          String projectName, JFrame projectInterface, JButton joinButton, String username) {
        if (ownerOfServer) {
            new Thread(serverChannel::start).start();
        }
        
        // Main Part - Frame
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setAlwaysOnTop(true);
        
        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 10, 950, 940);
        frame.add(attach1);
        
        JLabel attach2 = new JLabel();
        attach2.setBackground(Color.GRAY);
        attach2.setOpaque(true);
        attach2.setBounds(900, 0, 50, 900);
        attach1.add(attach2);


        // Input Provider
        JTextField nameField = new JTextField();
        nameField.setBackground(Color.GRAY);
        nameField.setForeground(Color.BLACK);
        nameField.setFont(new Font("Arial", Font.BOLD, 25));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setOpaque(true);
        nameField.setText("text...");
        nameField.setBounds(-2, 892, 902, 50);  // Set bounds for nameField
        attach1.add(nameField);


        // Input Sender
        JLabel iconLabel = new JLabel();
        iconLabel.setBackground(Color.GRAY);
        iconLabel.setForeground(Color.BLACK);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 40));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setText("^");
        iconLabel.setOpaque(true);
        iconLabel.setBounds(900, 890, 50, 50);
        attach1.add(iconLabel);


        // Description Text Area
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("What are you waiting for? Start the conversation!");
        descriptionArea.setBackground(Color.LIGHT_GRAY);
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 25));
        descriptionArea.setOpaque(true);
        descriptionArea.setFocusable(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);


        //Scroll pane
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 900, 900);
        attach1.add(scrollPane);


        //clear button
        JButton clearButton = new JButton("X");
        clearButton.setBounds(900, 20, 50, 50);
        clearButton.setBackground(Color.RED);
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.getDisabledSelectedIcon();
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        attach1.add(clearButton);


        //exit button
        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(880, 80, 90, 35);
        exitButton.setBackground(Color.RED);
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.getDisabledSelectedIcon();
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        attach1.add(exitButton);


        //online count displayer
        JLabel statusButton = new JLabel("-");
        statusButton.setBounds(900, 140, 50, 35);
        statusButton.setBackground(Color.GREEN);
        statusButton.setFont(new Font("Arial", Font.BOLD, 15));
        statusButton.setOpaque(true);
        attach1.add(statusButton);

        //configuring interface
        attach1.setComponentZOrder(scrollPane, 0);
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(clearButton, 0);
        attach1.setComponentZOrder(exitButton, 0);
        attach1.setComponentZOrder(statusButton, 0);
        attach1.revalidate();
        attach1.repaint();

        //Joined user.
        Client client = new Client(descriptionArea, username, frame, IPAddress, projectInterface);


        //Places a countdown on sending messages so that the server doesn't crack.
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                if (nameField.getText().trim().isEmpty()) return;
                
                try {
                    if (iconLabel.isEnabled()) {
                        client.sendMessageToServer(nameField.getText(), ownerOfServer);
                        nameField.setText("");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                
                if (iconLabel.isEnabled()) {
                    iconLabel.setEnabled(false);
                    Timer timer = new Timer(1500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            iconLabel.setEnabled(true);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });


        //This closes a client and based on his status on the chat, it does additional things.
        Consumer<Client> closeClient = c -> {
            projectInterface.setState(Frame.NORMAL);
            
            if (ownerOfServer) {
                UserTasks.closeAdmin(client, ENCODED_MESSAGE, projectName);
            } else {
                UserTasks.closeVisitor(frame, projectName);
            }
            client.close();
            frame.dispose();
        };


        //we start the closing process of client(if pressing exit)
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              if (e.getButton() != 1) return;
              closeClient.accept(client);
            }
        });

        //we start the closing process of client(if exiting directly)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeClient.accept(client);
            }
        });


        //If someone posts a link, the users can access it by clicking on it directly from the chat screen
        descriptionArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                
                try {
                    int line = descriptionArea.getLineOfOffset(descriptionArea.getCaretPosition());
                    String link = descriptionArea.getText().split("\\R")[line].split(": ")[1];
                    
                    //Unsafe (No exc. handling)
                    EXTRA_Links.accessLink(link);
                } catch (BadLocationException e2) {
                    e2.printStackTrace();
                }
            }
        });


        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("text...");
                }
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                nameField.setText("");
            }
        });
        
        clearButton.addActionListener(e -> {
            descriptionArea.setText("");
        });
        
        //online displayer
        Runnable runnable = () -> {
            while (true) {
                UserTasks.displayOnlineUsersCount(statusButton, projectName);
            }
        };
        new Thread(runnable).start();
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                joinButton.setEnabled(true);
            }
        });
    }

    protected static class UserTasks {

        private static MysqlDataSource ds = new MysqlDataSource();
        private static Properties props = new Properties();
        static {
            PropertiesManager.loadStorefrontProps(props, new JFrame());
            ds.setServerName("localhost");
            ds.setPortNumber(3306);
            ds.setUser(props.getProperty("user"));
            ds.setPassword(props.getProperty("pass"));
        }

        //closes all the participant users(including admin), deleteing in the same time the server.
        public static void closeAdmin(Client client, String ENCODED_MESSAGE, String projectName) {
            String removeQuery = "DELETE FROM servers.locations WHERE name = ?";
            try {
                client.sendMessageToServer(ENCODED_MESSAGE, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (Connection connection = ds.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
                preparedStatement.setString(1, projectName);
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //closes just the user that wants to exit, the server will still be online until the admin will exit
        public static void closeVisitor(JFrame frame, String projectName) {
            String updateQuery = "UPDATE servers.locations SET onlineCount = onlineCount - 1 WHERE name = ? AND onlineCount > 0";

            try (Connection connection = ds.getConnection();
                 PreparedStatement psUpdate = connection.prepareStatement(updateQuery)) {

                psUpdate.setString(1, projectName);
                psUpdate.executeUpdate();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred while updating the online count!", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        }

        //displays how many users are there online in the chat server.
        public static void displayOnlineUsersCount(JLabel statusButton, String projectName) {
            try {
                TimeUnit.SECONDS.sleep(2);

                String selectQuery = "SELECT onlineCount FROM servers.locations WHERE name = ?";

                try (Connection connection = ds.getConnection();
                     PreparedStatement psSelect = connection.prepareStatement(selectQuery)) {

                    psSelect.setString(1, projectName);
                    ResultSet resultSet = psSelect.executeQuery();

                    if (resultSet.next()) {
                        int onlineCount = resultSet.getInt(1);
                        statusButton.setText(onlineCount+"");
                    }

                } catch (SQLException ex) {
                    System.err.println("Error fetching onlineCount: " + ex.getMessage());
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
