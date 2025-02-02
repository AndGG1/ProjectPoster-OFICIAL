package DTOS.UserInterfaces.Activity.Activity_Interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class SearchForProjectsInterface {
    private final JFrame frame;
    
    public static void main(String[] args) {
        new SearchForProjectsInterface();
    }
    
    public SearchForProjectsInterface() {
        // Main Part - Frame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame = new JFrame("Search for Projects");
        frame.setVisible(true);
        frame.setSize(dimension.width, dimension.height);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);
        
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
        
        
            if (Locale.getDefault().equals(new Locale("ro", "RO"))) {
                searchLabel.setText("Cautare:");
            } else if (Locale.getDefault().equals(new Locale("de", "DE"))) {
                searchLabel.setText("Suchen:");
            }
                
                // Resize Logic
                frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
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
                        
                        frame.repaint();
                    }
                });
        
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
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
    }
}
