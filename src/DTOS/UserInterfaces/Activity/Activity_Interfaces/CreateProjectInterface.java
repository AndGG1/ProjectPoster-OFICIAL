package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CreateProjectInterface {
    private static JTextField inputField;
    private static JLabel statusLabel;
    
    private static final String[] questions = {"NAME", "DESCRIPTION", "LINK"};
    private static final List<String> givenData = new ArrayList<>();
    private static int currentQuestionIndex = 0;
    JFrame frame;
    private static Properties props = new Properties();
    
    
    public CreateProjectInterface(String title) {
        frame = new JFrame(title);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(350, 150);
        
        final var path = Path.of("users.properties");
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        inputField = new JTextField(20);
        JButton nextButton = new JButton("Next");
        statusLabel = new JLabel("");
        
        nextButton.addActionListener(e -> {
            givenData.add(inputField.getText());
            inputField.setText("");
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                frame.setTitle(questions[currentQuestionIndex]);
            } else {
                handleProjectData(givenData, frame);
            }
        });
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Input:"));
        panel.add(inputField);
        panel.add(nextButton);
        panel.add(statusLabel);
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    
    public static void beginQuestionsForProject() {
        var project = new CreateProjectInterface(questions[currentQuestionIndex]);
        
        while (currentQuestionIndex < questions.length) {
            // Waiting for input
        }
    }
    
    public static void handleProjectData(List<String> data, JFrame frame) {
        String name = data.get(0);
        String description = data.get(1);
        String link = data.get(2);
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        String query = "INSERT INTO projects.section" + name.charAt(0) + " (project_name, project_link, project_description) VALUES (?, ?, ?)";
        try (Connection conn = ds.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, link);
            ps.setString(3, description);
            ps.addBatch();
            
            ps.executeBatch();
            ps.clearBatch();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        beginQuestionsForProject();
    }
}
