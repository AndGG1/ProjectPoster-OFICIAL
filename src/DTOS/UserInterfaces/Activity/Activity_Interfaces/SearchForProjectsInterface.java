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

public class SearchForProjectsInterface {
    private final JFrame frame;
    private static Properties props = new Properties();
    
    public static void main(String[] args) {
        new SearchForProjectsInterface("https://www.iconsdb.com/icons/preview/blue/info-xxl.png", "X", "X");
    }
    
    public SearchForProjectsInterface(String img, String name, String description) {
        // Main Part - Frame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame = new JFrame("Search for Projects");
        frame.setVisible(true);
        frame.setSize(dimension.width, dimension.height);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        
        
        try {
            props.load(Files.newInputStream(Path.of("storefront.properties"), StandardOpenOption.READ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Attach1 Label
        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 50, dimension.width - 51, dimension.height - 100);
        frame.add(attach1);
        
        
        // Search Label
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 25));
        searchLabel.setBounds(20, 10, 100, 30);
        frame.add(searchLabel);
        
        // Search TextField
        JTextField searchTextField = new JTextField();
        searchTextField.setBackground(Color.LIGHT_GRAY);
        searchTextField.setForeground(Color.BLACK);
        searchTextField.setFont(new Font("Arial", Font.BOLD, 25));
        searchTextField.setHorizontalAlignment(JTextField.CENTER);
        searchTextField.setBounds(130, 10, 1500, 30); // Adjust width to 400 and position closer to search label
        frame.add(searchTextField);
        
        // Search Button
        JButton searchButton = new JButton("^");
        searchButton.setFont(new Font("Arial", Font.BOLD, 25));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.setBounds(1660, 10, 30, 30); // Initial positioning
        frame.add(searchButton);
        
        
        // Description Text Area
        JTextArea descriptionArea = new JTextArea();
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
        scrollPane.setBounds(0, 0, dimension.width - 51, dimension.height - 100);
        attach1.add(scrollPane);
        
        //profile details
        boolean isWeb = img.startsWith("http"); // Check if the image is banana URL
        CircleImagePanel webImagePanel = new CircleImagePanel(img, isWeb);
        webImagePanel.setBounds(2500, 5, 40, 40); // Increased size and positioned higher
        frame.add(webImagePanel);
        
        JButton createProjectButton = new JButton("+");
        createProjectButton.setBackground(Color.LIGHT_GRAY);
        createProjectButton.setFont(new Font("Arial", Font.BOLD, 20));
        createProjectButton.setBounds(1500, 5, 50, 40);
        createProjectButton.setBorderPainted(false);
        createProjectButton.setFocusPainted(false);
        frame.add(createProjectButton);
        
        JButton removeProjectButton = new JButton("-");
        removeProjectButton.setBackground(Color.LIGHT_GRAY);
        removeProjectButton.setFont(new Font("Arial", Font.BOLD, 20));
        removeProjectButton.setBounds(750, 5, 50, 40);
        removeProjectButton.setBorderPainted(false);
        removeProjectButton.setFocusPainted(false);
        frame.add(removeProjectButton);
        
        Runnable compResize = () -> {
            Dimension newSize = frame.getSize();
            
            // Adjust search label size and position
            searchLabel.setBounds(20, 10, newSize.width / 10, 30);
            
            // Adjust search text field size and position
            searchTextField.setBounds(130, 10, newSize.width / 2, 30);
            
            // Adjust search button size and position
            searchButton.setBounds(searchTextField.getX() + searchTextField.getWidth() + 10, 10, newSize.width / 50, 30);
            
            // Adjust attach1 size and position
            attach1.setBounds(18, 50, newSize.width - 51, newSize.height - 100);
            
            scrollPane.setBounds(0, 0, newSize.width - 51, newSize.height - 100);
            
            // Adjust profile image size and position relative to the frame width
            webImagePanel.setBounds(newSize.width - 140, 5, 40, 40);
            
            // Adjust create project button size and position relative to the frame width
            createProjectButton.setBounds(newSize.width - 300, 5, 50, 40);
            
            // Adjust remove project button size and position relative to the frame width
            removeProjectButton.setBounds(newSize.width - 370, 5, 50, 40);
        };
        
        frame.repaint();
        compResize.run();
        descriptionArea.setText("Start searching for projects!");
        
        if (Locale.getDefault().equals(new Locale("ro", "RO"))) {
                searchLabel.setText("Cautare:");
            } else if (Locale.getDefault().equals(new Locale("de", "DE"))) {
                searchLabel.setText("Suchen:");
        }
        
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                compResize.run();
            }
        });
        
        
        
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                getProjects(searchTextField.getText().charAt(0), searchTextField.getText(), descriptionArea);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setFont(new Font("Arial", Font.BOLD, 29));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setFont(new Font("Arial", Font.BOLD, 25));
            }
        });
        
        createProjectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                new CreateProjectInterface("Create Project: ", name);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                createProjectButton.setFont(new Font("Arial", Font.BOLD, 25));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                createProjectButton.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });
        
        removeProjectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                new RemoveProjectInterface("Remove Project: ", name);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                removeProjectButton.setFont(new Font("Arial", Font.BOLD, 30));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                removeProjectButton.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });
        
        descriptionArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                if (!descriptionArea.isEnabled()) return;
                
                try {
                    int line = descriptionArea.getLineOfOffset(descriptionArea.getCaretPosition());
                    String projectLine = descriptionArea.getText().split("\\R")[line];
                    
                    String name = projectLine.split(", ")[0].split("Project")[1]; name = name.substring(1);
                    String description = projectLine.split(", ")[2];
                    String link = projectLine.split(", ")[1];
                    new ProjectInterface(name, "unknown", description.substring(0, description.length()-1), link, descriptionArea);
                    
                    descriptionArea.setEnabled(false);
                } catch (BadLocationException e2) {
                    //ignore
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    
    private final Executor cachedThreadPool;
    List<List<Object>> projects;
    {
        cachedThreadPool = Executors.newCachedThreadPool();
        projects = Collections.synchronizedList(new ArrayList<>());
    }
    
    private void getProjects(char startingLetter, String wholeWord, JTextArea searchTextField) {
        String query = "SELECT project_id, project_name, project_link, project_description FROM projects.section" + startingLetter;
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setUser(props.getProperty("user"));
        ds.setPassword(props.getProperty("pass"));
        
        cachedThreadPool.execute(() -> {
            try (Connection conn = ds.getConnection();
                Statement st = conn.createStatement()) {
                ResultSet resultSet = st.executeQuery(query);
                
                while (resultSet.next()) {
                    List<Object> row = new ArrayList<>();
                    
                    row.add(resultSet.getInt("project_id"));
                    row.add(resultSet.getString("project_name"));
                    row.add(resultSet.getString("project_link"));
                    row.add(resultSet.getString("project_description"));
                    
                    double percentage = Search_Feature.similarity(wholeWord, row.get(1) + "");
                    
                    if (percentage >= 33) {
                        row.add(percentage);
                        projects.add(row);
                    }
                }
                
                if (projects.isEmpty() || searchTextField.getText().isEmpty()) {
                    searchTextField.setText("No results returned from the search!");
                    return;
                }
                searchTextField.setText("");
                
                Collections.sort(projects, new Comparator<List<Object>>() {
                    @Override
                    public int compare(List<Object> o1, List<Object> o2) {
                        return Double.compare((double) o1.get(o1.size() - 1), (double) o2.get(o2.size() - 1));
                    }
                });
                Collections.reverse(projects);
                
                for (var row : projects) {
                    row.remove(0);
                    row.remove(row.size() - 1);
                    searchTextField.append("Project: " + row + "\n");
                }
                
                projects.clear();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Search failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class CircleImagePanel extends JPanel {
    private BufferedImage image;
    
    public CircleImagePanel(String imagePath, boolean isWeb) {
        try {
            if (isWeb) {
                // Load image from URL
                URL imgUrl = new URL(imagePath);
                image = ImageIO.read(imgUrl);
            } else {
                File returnVal = new File(imagePath);
                image = ImageIO.read(returnVal);
            }
        } catch (MalformedURLException e) {
            System.err.println("Error: Invalid URL " + imagePath);
        } catch (IOException e) {
            System.err.println("Error: Unable to load image " + imagePath);
        }
        
        setOpaque(false); // Make the panel non-opaque
        setBackground(new Color(0, 0, 0, 0)); // Set banana transparent background
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw banana circle
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Ensure the background is transparent
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent color
        g2d.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        
        // Clip the area to banana circle
        g2d.setClip(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        
        // Draw the image scaled to fit the circle
        if (image != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset to opaque for the image
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
