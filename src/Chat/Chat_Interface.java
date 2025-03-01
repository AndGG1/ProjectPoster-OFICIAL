package Chat;

import DTOS.EXTRA_Links;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Chat_Interface {
    private final JFrame frame;
    private final static List<String> apis;
    
    static {
        try {
            apis = Files.readAllLines(Path.of("API_KEYS"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        new Chat_Interface();
    }
    
    public Chat_Interface() {
        // Main Part - Frame
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);
        
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
        Client client = new Client(nameField);
        
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
        
        
        attach1.setComponentZOrder(scrollPane, 0);
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(clearButton, 0);
        attach1.revalidate();
        attach1.repaint();
        
        
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                try {
                    client.sendMessageToServer(nameField, client.getSocket());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
    }
}
