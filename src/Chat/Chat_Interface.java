package Chat;

import DTOS.EXTRA_Links;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Chat_Interface {
    private final JFrame frame;
    
    public Chat_Interface(SimpleServerChannel serverChannel, boolean ownerOfServer, String IPAddress, String projectName, JFrame projectInterface, JButton joinButton) {
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
        
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 900, 900);
        attach1.add(scrollPane);
        
        JButton clearButton = new JButton("X");
        clearButton.setBounds(900, 20, 50, 50);
        clearButton.setBackground(Color.RED);
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.getDisabledSelectedIcon();
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        attach1.add(clearButton);
        
        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(880, 80, 90, 35);
        exitButton.setBackground(Color.RED);
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.getDisabledSelectedIcon();
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        attach1.add(exitButton);
        
        JLabel statusButton = new JLabel("-");
        statusButton.setBounds(900, 140, 50, 35);
        statusButton.setBackground(Color.GREEN);
        statusButton.setFont(new Font("Arial", Font.BOLD, 15));
        statusButton.setOpaque(true);
        attach1.add(statusButton);
        
        Client client = new Client(descriptionArea, "Bot" + new Random().nextInt(1, 10), frame, IPAddress, projectInterface, statusButton);
        
        attach1.setComponentZOrder(scrollPane, 0);
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(clearButton, 0);
        attach1.setComponentZOrder(exitButton, 0);
        attach1.setComponentZOrder(statusButton, 0);
        attach1.revalidate();
        attach1.repaint();
        
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                if (nameField.getText().trim().isEmpty()) return;
                
                try {
                    if (iconLabel.isEnabled()) {
                        client.sendMessageToServer(nameField.getText(), ownerOfServer);
                        statusButton.setText(serverChannel.getClientChannels().size()+"");
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
        
        
        
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        String removeQuery = "DELETE FROM servers.locations WHERE name = ?";
        Consumer<Client> closeClient = c -> {
            projectInterface.setState(Frame.NORMAL);
            
            if (ownerOfServer) {
                try {
                    client.sendMessageToServer("#ER86Yt42//EmilutCuParulCretBagaPulaInCotet", true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                frame.dispose();
                client.close();
            }
            
            try (Connection connection = ds.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
                preparedStatement.setString(1, projectName);
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                
                closeClient.accept(client);
            }
        });
        
        
        
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeClient.accept(client);
            }
        });
        
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
        
        
            Runnable runnable = () -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
}
