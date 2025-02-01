package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreateProjectInterface {
    private static JTextField emailField;
    private static JLabel statusLabel;
    
    private static final String[] questions = {"NAME", "DESCRIPTION", "LINK"};
    private static final List<String> givenData = new ArrayList<>();
    JFrame frame;
    
    public CreateProjectInterface(String title) {
        frame = new JFrame(title);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(350, 150);
        
        emailField = new JTextField(20);
        JButton sendButton = new JButton("Send Email");
        statusLabel = new JLabel("");
        
        sendButton.addActionListener(e -> {
            givenData.add(emailField.getText());
        });
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Recipient Email:"));
        panel.add(emailField);
        panel.add(sendButton);
        panel.add(statusLabel);
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    
    public static void beginQuestionsForProject() {
        var project = new CreateProjectInterface("");
        for (String question : questions) {
            
            project.frame.setName(question);
            
            while (emailField.getText().isEmpty()) {};
            
            givenData.add(emailField.getText());
            emailField.setText("");
        }
    }
    
    public static void main(String[] args) {
        beginQuestionsForProject();
    }
}
