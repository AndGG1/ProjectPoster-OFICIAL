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
    private static final Properties props = new Properties();

    public RemoveProjectInterface(String title, String owner) {
        setupFrame(title);
        loadProperties();
        setupUI(owner);
    }

    private void setupFrame(String title) {
        frame = new JFrame(title);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(350, 150);
    }

    private void loadProperties() {
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            showError("An error occurred while loading properties.");
        }
    }

    private void setupUI(String owner) {
        inputField = new JTextField(20);
        JButton nextButton = new JButton("Next");
        statusLabel = new JLabel("");

        nextButton.addActionListener(e -> processProject(inputField.getText(), owner));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Input:"));
        panel.add(inputField);
        panel.add(nextButton);
        panel.add(statusLabel);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void processProject(String projectName, String owner) {
        if (projectName.length() < 3) {
            showError("Project name is too short!");
            return;
        }

        if (!isOwnerOfProject(projectName, owner)) {
            showError("Invalid project name or you are not the owner!");
            return;
        }

        removeProjectData(projectName, owner);
        frame.dispose();
    }

    private void removeProjectData(String projectName, String owner) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM projects.section" + projectName.charAt(0) + " WHERE project_name = ? AND project_owner = ?")) {

            ps.setString(1, projectName);
            ps.setString(2, owner);
            ps.executeUpdate();

            showMessage("Project " + projectName + " deleted successfully!", "Project Deleted");
        } catch (SQLException ignored) {
        }
    }

    private boolean isOwnerOfProject(String projectName, String owner) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM projects.section" + projectName.charAt(0) + " WHERE project_owner = ? AND project_name = ?")) {

            ps.setString(1, owner);
            ps.setString(2, projectName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ignored) {
        }
        return false;
    }

    private MysqlDataSource getDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        return ds;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}