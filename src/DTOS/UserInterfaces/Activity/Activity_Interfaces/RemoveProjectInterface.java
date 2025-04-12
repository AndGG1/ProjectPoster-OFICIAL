package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.Properties;

public class RemoveProjectInterface {
    private JTextField inputField;
    private JLabel statusLabel;
    private JFrame frame;
    private static Properties props = new Properties();
    
    public RemoveProjectInterface(String title, String owner) {
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
            if (inputField.getText().length() < 3) {
                JOptionPane.showMessageDialog(frame, "Name of Project too short",
                        "Invalid Project Name", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isOwnerOfProject(inputField.getText(), owner)) {
                JOptionPane.showMessageDialog(frame, "Invalid Project Name or you aren't the project's current owner!",
                        "Wrong Project!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            handleProjectData(inputField.getText(), owner);
            frame.dispose();  // Dispose the frame after handling project data
        });
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Input:"));
        panel.add(inputField);
        panel.add(nextButton);
        panel.add(statusLabel);
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    
    private void handleProjectData(String projectName, String owner) {
            var ds = new MysqlDataSource();
            ds.setServerName("localhost");
            ds.setPortNumber(3306);
            ds.setUser(props.getProperty("user"));
            ds.setPassword(props.getProperty("pass"));
            
            String query = "DELETE FROM projects.section" + projectName.charAt(0) + " WHERE project_name = ? AND project_owner = ?";
            try (Connection connection = ds.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, projectName);
                ps.setString(2, owner);
                ps.executeUpdate();
                
                JOptionPane.showMessageDialog(frame, "You have successfully deleted project: " + projectName,
                        "Project Deleted", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                //ignore
            }
    }
    
    private boolean isOwnerOfProject(String projectName, String owner) {
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        String query = "SELECT * FROM projects.section" + projectName.charAt(0) + " WHERE project_owner = ? AND project_name = ?";
        try (Connection connection = ds.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, owner);
            ps.setString(2, projectName);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            //ignore
        }
        return false;
    }

}
