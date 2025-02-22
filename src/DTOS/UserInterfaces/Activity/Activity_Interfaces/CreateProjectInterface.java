package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import DTOS.EXTRA_Links;
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
    private JTextField inputField;
    private JLabel statusLabel;
    
    // Changed these to instance variables to reset state
    private final String[] questions = {"NAME", "DESCRIPTION", "LINK"};  // changed from static to final
    private final List<String> givenData = new ArrayList<>();  // changed from static to final
    private int currentQuestionIndex = 0;  // changed from static to instance variable
    private JFrame frame;
    private static Properties props = new Properties();
    
    public CreateProjectInterface(String title, String owner) {
        // Reset the state for each new instance
        givenData.clear();  // added to reset givenData
        currentQuestionIndex = 0;  // added to reset currentQuestionIndex
        
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
            if (currentQuestionIndex == 0 && inputField.getText().length() < 3) {
                JOptionPane.showMessageDialog(frame, "Name of Project too short",
                        "Invalid Project Name", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (currentQuestionIndex == 1 && inputField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please insert description!",
                        "Empty Description", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (currentQuestionIndex == 2 && !EXTRA_Links.checkAbilityToCreate(inputField.getText())) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid link!",
                        "Invalid URL", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            givenData.add(inputField.getText());
            inputField.setText("");
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                frame.setTitle(questions[currentQuestionIndex]);
            } else {
                handleProjectData(givenData, owner);
                frame.dispose();  // Dispose the frame after handling project data
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
    
//    public static void beginQuestionsForProject() {
  //      SwingUtilities.invokeLater(() -> new CreateProjectInterface(questions[0]));  // changed to pass the first question title
  //  }
    
    public void handleProjectData(List<String> data, String owner) {
        String name = data.get(0);
        String description = data.get(1);
        String link = data.get(2);
        
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        String query = "INSERT INTO projects.section" + name.charAt(0) + " (project_name, project_link, project_description, project_owner) VALUES (?, ?, ?, ?)";
        try (Connection conn = ds.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, link);
            ps.setString(3, description);
            ps.setString(4, owner);
            ps.addBatch();
            
            ps.executeBatch();
            ps.clearBatch();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
  //  public static void main(String[] args) {
  //      beginQuestionsForProject();
   // }
}
