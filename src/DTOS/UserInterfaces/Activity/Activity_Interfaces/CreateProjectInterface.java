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

    private final String[] questions = {"NAME", "DESCRIPTION", "LINK"};
    private final List<String> givenData = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private JFrame frame;
    private JTextField inputField;
    private static final Properties props = new Properties();

    public CreateProjectInterface(String title, String owner) {
        initializeProperties();
        setupUI(title, owner);
    }

    private void initializeProperties() {
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            showError("Failed to load properties file.");
        }
    }

    private void setupUI(String title, String owner) {
        frame = new JFrame(title);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(350, 150);

        inputField = new JTextField(20);
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> processInput(owner));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Input:"));
        panel.add(inputField);
        panel.add(nextButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void processInput(String owner) {
        String input = inputField.getText();

        if (isInvalidInput(input)) return;

        givenData.add(input);
        inputField.setText("");
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.length) {
            frame.setTitle(questions[currentQuestionIndex]);
        } else {
            saveProjectData(owner);
            frame.dispose();
        }
    }

    private boolean isInvalidInput(String input) {
        if (currentQuestionIndex == 0 && input.length() < 3) {
            showError("Project name is too short!");
            return true;
        }
        if (currentQuestionIndex == 1 && input.isEmpty()) {
            showError("Please insert a description!");
            return true;
        }
        if (currentQuestionIndex == 2 && !EXTRA_Links.checkAbilityToCreate(input)) {
            showError("Invalid URL! Please enter a valid link.");
            return true;
        }
        return false;
    }

    private void saveProjectData(String owner) {
        String name = givenData.get(0);
        String description = givenData.get(1);
        String link = givenData.get(2);

        try (Connection conn = getDatabaseConnection()) {
            String query = "INSERT INTO projects.section" + name.charAt(0) +
                    " (project_name, project_link, project_description, project_owner) VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, link);
                ps.setString(3, description);
                ps.setString(4, owner);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private Connection getDatabaseConnection() throws SQLException {
        var ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        return ds.getConnection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}