package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import DTOS.UserInterfaces.Activity.Search_Feature;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Provides a GUI interface for searching and managing projects.
 */
public class SearchForProjectsInterface {
    private static final Properties props = new Properties();
    private final JFrame frame;
    private final Executor cachedThreadPool = Executors.newCachedThreadPool();
    private final List<List<Object>> projects = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        new SearchForProjectsInterface(
                "https://www.iconsdb.com/icons/preview/blue/info-xxl.png", "X", "X"
        );
    }

    public SearchForProjectsInterface(String img, String name, String description) {
        // Screen and frame setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Search for Projects");
        frame.setSize(screenSize.width, screenSize.height);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // Load properties file
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            showError("An error occurred while loading properties.");
        }

        // GUI Components
        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        frame.add(attach1);

        JLabel searchLabel = new JLabel(getLocalizedSearchText());
        searchLabel.setFont(new Font("Arial", Font.BOLD, 25));
        frame.add(searchLabel);

        JTextField searchTextField = new JTextField();
        setupTextField(searchTextField);

        JButton searchButton = createButton("^", 25, Color.LIGHT_GRAY);
        JButton createProjectButton = createButton("+", 20, Color.LIGHT_GRAY);
        JButton removeProjectButton = createButton("-", 20, Color.LIGHT_GRAY);

        JTextArea descriptionArea = createDescriptionArea();
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        setupScrollPane(scrollPane, attach1);

        // Profile image panel
        boolean isWebImage = img.startsWith("http");
        CircleImagePanel profileImagePanel = new CircleImagePanel(img, isWebImage);
        frame.add(profileImagePanel);

        // Add components to frame
        frame.add(searchTextField);
        frame.add(searchButton);
        frame.add(createProjectButton);
        frame.add(removeProjectButton);

        // Set initial layout
        Runnable layoutUpdater = () -> updateComponentLayout(
                frame, searchLabel, searchTextField, searchButton,
                attach1, scrollPane, profileImagePanel,
                createProjectButton, removeProjectButton
        );
        layoutUpdater.run();

        // Initial description
        descriptionArea.setText("Start searching for projects!");

        // Add resize listener
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutUpdater.run();
            }
        });

        // Add button listeners
        setupButtonListeners(
                searchButton, searchTextField, descriptionArea,
                createProjectButton, name, removeProjectButton
        );
        setupDescriptionAreaListener(descriptionArea);

        frame.setVisible(true);
    }

    /**
     * Returns the localized string for the "Search" label.
     */
    private String getLocalizedSearchText() {
        Locale locale = Locale.getDefault();
        if (locale.equals(new Locale("ro", "RO"))) {
            return "Cautare:";
        } else if (locale.equals(new Locale("de", "DE"))) {
            return "Suchen:";
        }
        return "Search:";
    }

    /**
     * Centralized method for showing error dialogs.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Setup the search text field.
     */
    private void setupTextField(JTextField textField) {
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setFont(new Font("Arial", Font.BOLD, 25));
        textField.setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Create a styled button.
     */
    private JButton createButton(String text, int fontSize, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Create and configure the description area.
     */
    private JTextArea createDescriptionArea() {
        JTextArea area = new JTextArea();
        area.setBackground(Color.LIGHT_GRAY);
        area.setForeground(Color.BLACK);
        area.setFont(new Font("Arial", Font.BOLD, 25));
        area.setOpaque(true);
        area.setFocusable(true);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        return area;
    }

    /**
     * Setup scroll pane for description area and add to parent.
     */
    private void setupScrollPane(JScrollPane scrollPane, JLabel parent) {
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        parent.add(scrollPane);
    }

    /**
     * Update the layout of all components based on the current frame size.
     */
    private void updateComponentLayout(
            JFrame frame, JLabel searchLabel, JTextField searchTextField, JButton searchButton,
            JLabel attach1, JScrollPane scrollPane, JPanel profileImagePanel,
            JButton createProjectButton, JButton removeProjectButton
    ) {
        Dimension size = frame.getSize();

        searchLabel.setBounds(20, 10, size.width / 10, 30);
        searchTextField.setBounds(130, 10, size.width / 2, 30);
        searchButton.setBounds(searchTextField.getX() + searchTextField.getWidth() + 10, 10, size.width / 50, 30);

        attach1.setBounds(18, 50, size.width - 51, size.height - 100);
        scrollPane.setBounds(0, 0, size.width - 51, size.height - 100);

        profileImagePanel.setBounds(size.width - 140, 5, 40, 40);
        createProjectButton.setBounds(size.width - 300, 5, 50, 40);
        removeProjectButton.setBounds(size.width - 370, 5, 50, 40);
    }

    /**
     * Attach listeners to buttons for consistent behavior.
     */
    private void setupButtonListeners(
            JButton searchButton, JTextField searchField, JTextArea resultsArea,
            JButton createButton, String userName, JButton removeButton
    ) {

        searchButton.setFont(new Font("Arial", Font.BOLD, 15));
        // Search Button
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && !searchField.getText().isEmpty()) {
                    getProjects(searchField.getText().charAt(0), searchField.getText(), resultsArea);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setFont(new Font("Arial", Font.BOLD, 19));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setFont(new Font("Arial", Font.BOLD, 15));
            }
        });

        // Create Project Button
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new CreateProjectInterface("Create Project: ", userName);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                createButton.setFont(new Font("Arial", Font.BOLD, 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                createButton.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });

        // Remove Project Button
        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new RemoveProjectInterface("Remove Project: ", userName);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                removeButton.setFont(new Font("Arial", Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeButton.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });
    }

    /**
     * Attach mouse listener to description area for project selection.
     */
    private void setupDescriptionAreaListener(JTextArea descriptionArea) {
        descriptionArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1 || !descriptionArea.isEnabled()) return;

                try {
                    int line = descriptionArea.getLineOfOffset(descriptionArea.getCaretPosition());
                    String projectLine = descriptionArea.getText().split("\\R")[line];
                    String[] parts = projectLine.split(", ");

                    String projectName = parts[0].split("Project")[1].trim();
                    String link = parts[1];
                    String desc = parts[2];
                    if (desc.endsWith("]")) desc = desc.substring(0, desc.length() - 1);

                    new ProjectInterface(projectName, "unknown", desc, link, descriptionArea, projectName);
                    descriptionArea.setEnabled(false);
                } catch (BadLocationException | IOException ex) {
                    // Ignore or handle error as needed
                }
            }
        });
    }

    /**
     * Fetch and display projects matching the search term.
     */
    private void getProjects(char startingLetter, String searchTerm, JTextArea resultsArea) {
        String query = "SELECT project_id, project_name, project_link, project_description FROM projects.section" + startingLetter;

        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));

        cachedThreadPool.execute(() -> {
            try (Connection conn = ds.getConnection(); Statement st = conn.createStatement()) {
                ResultSet resultSet = st.executeQuery(query);

                while (resultSet.next()) {
                    List<Object> row = new ArrayList<>();
                    row.add(resultSet.getInt("project_id"));
                    row.add(resultSet.getString("project_name"));
                    row.add(resultSet.getString("project_link"));
                    row.add(resultSet.getString("project_description"));

                    double similarity = Search_Feature.similarity(searchTerm, row.get(1).toString());
                    if (similarity >= 33) {
                        row.add(similarity);
                        projects.add(row);
                    }
                }

                if (projects.isEmpty() || searchTerm.isEmpty()) {
                    resultsArea.setText("No results returned from the search!");
                    return;
                }
                resultsArea.setText("");

                // Sort by similarity descending
                projects.sort((o1, o2) -> Double.compare(
                        (double) o2.get(o2.size() - 1),
                        (double) o1.get(o1.size() - 1)
                ));

                for (var row : projects) {
                    row.remove(0); // remove project_id
                    row.remove(row.size() - 1); // remove similarity
                    resultsArea.append("Project: " + row + "\n");
                }

                projects.clear();
            } catch (SQLException e) {
                showError("Search failed!");
            }
        });
    }
}

/**
 * Displays a circular image from a file path or web URL.
 */
class CircleImagePanel extends JPanel {
    private BufferedImage image;

    public CircleImagePanel(String imagePath, boolean isWeb) {
        try {
            if (isWeb) {
                image = ImageIO.read(new URL(imagePath));
            } else {
                image = ImageIO.read(new File(imagePath));
            }
        } catch (MalformedURLException e) {
            System.err.println("Error: Invalid URL " + imagePath);
        } catch (IOException e) {
            System.err.println("Error: Unable to load image " + imagePath);
        }
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw a circular clipped image
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));

        g2d.setClip(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));

        if (image != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Image not loaded.");
        }
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}