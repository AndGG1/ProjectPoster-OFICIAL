package DTOS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SignInView {

    public SignInView() {

        //Main Part - Frame
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);

        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 10, 949, 940);
        frame.add(attach1);

        TrapezeLabel attach2 = new TrapezeLabel();
        attach2.setBackground(Color.GRAY);
        attach2.setForeground(Color.BLACK);
        attach2.setBounds(350, 0, 300, 50);
        attach1.add(attach2);


        //Profile Picture
        JLabel iconLabel = new JLabel();
        iconLabel.setBackground(Color.GRAY);
        iconLabel.setForeground(Color.BLACK);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 50));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setText("+");
        iconLabel.setOpaque(true);
        iconLabel.setBounds(75, 75, 200, 250);
        attach1.add(iconLabel);

        JLabel attach_Icon = new JLabel();
        attach_Icon.setBackground(Color.DARK_GRAY);
        attach_Icon.setOpaque(true);
        attach_Icon.setBounds(80, 80, 210, 260);
        attach1.add(attach_Icon);


        //Description Text Area
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBackground(Color.GRAY);
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 25));
        descriptionArea.setOpaque(true);
        descriptionArea.setText("Tell me about yourself...");
        descriptionArea.setFocusable(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);


        JLabel attach_Description = new JLabel();
        attach_Description.setBackground(Color.DARK_GRAY);
        attach_Description.setOpaque(true);
        attach_Description.setBounds(355, 80, 560, 260);
        attach1.add(attach_Description);

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(350, 75, 550, 250);
        attach1.add(scrollPane);


        //Name Container
        JLabel attach_Name = new JLabel();
        attach_Name.setBackground(Color.GRAY);
        attach_Name.setForeground(Color.BLACK);
        attach_Name.setFont(new Font("Arial", Font.BOLD, 25));
        attach_Name.setHorizontalAlignment(JLabel.CENTER);
        attach_Name.setText("NAME:");
        attach_Name.setOpaque(true);
        attach_Name.setBounds(25, 400, 200, 50); // Corrected bounds
        attach1.add(attach_Name);

        JTextField nameField = new JTextField();
        nameField.setBackground(Color.GRAY);
        nameField.setForeground(Color.BLACK);
        nameField.setFont(new Font("Arial", Font.BOLD, 25));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setOpaque(true);
        nameField.setText("John");
        nameField.setBounds(225, 400, 700, 50); // Added bounds for nameField
        attach1.add(nameField);

        JLabel attach_Name2 = new JLabel();
        attach_Name2.setBackground(Color.DARK_GRAY);
        attach_Name2.setOpaque(true);
        attach_Name2.setBounds(30, 410, 900, 50);
        attach1.add(attach_Name2);



        //Password Container
        JLabel attach_Pass = new JLabel();
        attach_Pass.setBackground(Color.GRAY);
        attach_Pass.setForeground(Color.BLACK);
        attach_Pass.setFont(new Font("Arial", Font.BOLD, 25));
        attach_Pass.setHorizontalAlignment(JLabel.CENTER);
        attach_Pass.setText("PASSWORD:");
        attach_Pass.setOpaque(true);
        attach_Pass.setBounds(25, 500, 200, 50); // Corrected bounds
        attach1.add(attach_Pass);

        JPasswordField passField = new JPasswordField();
        passField.setBackground(Color.GRAY);
        passField.setForeground(Color.BLACK);
        passField.setFont(new Font("Arial", Font.BOLD, 25));
        passField.setHorizontalAlignment(JTextField.CENTER);
        passField.setOpaque(true);
        passField.setText("12345");
        passField.setBounds(225, 500, 650, 50); // Added bounds for nameField
        attach1.add(passField);

        JLabel attach_Pass2 = new JLabel();
        attach_Pass2.setBackground(Color.DARK_GRAY);
        attach_Pass2.setOpaque(true);
        attach_Pass2.setBounds(30, 510, 850, 50);
        attach1.add(attach_Pass2);

        JButton passButton = new JButton(EXTRA.getPassEye());
        passButton.setBackground(Color.GRAY);
        passButton.setContentAreaFilled(false);
        passButton.setBorderPainted(false);
        passButton.setFocusPainted(false);
        passButton.setBounds(890, 505, 50, 50);
        attach1.add(passButton);



        //Revalidation Process
        attach1.setComponentZOrder(scrollPane, 0);
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(attach_Name, 0);
        attach1.revalidate();
        attach1.repaint();


        //Icon Label Listeners
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == 1) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    fileChooser.showOpenDialog(frame);

                    File returnVal = fileChooser.getSelectedFile();

                    if (returnVal.exists()) {

                        try {
                            Image img = ImageIO.read(new File(returnVal.getPath()));
                            if (img != null) {
                                img = img.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                                iconLabel.setIcon(new ImageIcon(img));

                            } else {

                                Image img2 = ImageIO.read(new URL("https://avatars.githubusercontent.com/u/154756433?v=4&size=64"));
                                img2 = img2.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                                iconLabel.setIcon(new ImageIcon(img2));
                            }
                            iconLabel.setText("");

                        } catch (IOException ex) {
                            //Not handling
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                iconLabel.setBounds(80, 80, 200, 250);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                iconLabel.setBounds(75, 75, 200, 250);
            }
        });


        //Description Label Listeners
        final boolean[] flag = {true};
        descriptionArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                scrollPane.setBounds(355, 80, 550, 250);

                if (flag[0]) {
                    descriptionArea.setText("");
                    flag[0] = false;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                scrollPane.setBounds(350, 75, 550, 250);

                if (descriptionArea.getText().isEmpty() || descriptionArea.getText().isBlank()) {
                    descriptionArea.setText("Tell me about yourself...");
                    flag[0] = true;
                }
            }
        });


        //Name Field Label Listeners
        nameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nameField.setBounds(227, 405, 700, 50);
                attach_Name.setBounds(27, 405, 200, 50);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nameField.setBounds(225, 400, 700, 50);
                attach_Name.setBounds(25, 400, 200, 50);
            }
        });

        final boolean[] exampleOn = new boolean[1];
        exampleOn[0] = true;
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (exampleOn[0]) {
                    nameField.setText("");
                    exampleOn[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String focus = nameField.getText();
                if (focus.isEmpty() || focus.isBlank()) {
                    nameField.setText("John");
                    exampleOn[0] = true;
                }
            }
        });



        //Pass Field Label Listeners
        passField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                passField.setBounds(227, 505, 650, 50);
                attach_Pass.setBounds(27, 505, 200, 50);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passField.setBounds(225, 500, 650, 50);
                attach_Pass.setBounds(25, 500, 200, 50);
            }
        });

        final boolean[] exampleOn2 = new boolean[1];
        exampleOn2[0] = true;
        passField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (exampleOn2[0]) {
                    passField.setText("");
                    passField.setEchoChar('•');
                    exampleOn2[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String focus = passField.getText();
                if (focus.isEmpty() || focus.isBlank()) {
                    passField.setText("12345");
                    passField.setEchoChar((char) 0);
                    exampleOn2[0] = true;
                }
            }
        });

        final boolean[] isNormalChar = new boolean[1];
        passButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return; //only left-click allowed

                if (!isNormalChar[0]) {
                    passField.setEchoChar((char) 0);
                    isNormalChar[0] = true;
                } else {
                    passField.setEchoChar('•');
                    isNormalChar[0] = false;
                }
            }
        });
    }



    public static class TrapezeLabel extends JLabel {

        public TrapezeLabel() {
            super("");
            super.setFont(new Font("Arial", Font.BOLD, 25));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int[] xPoints = {0, getWidth(), getWidth()-50, 50};
            int[] yPoints = {0, 0, getHeight(), getHeight()};
            Polygon trapeze = new Polygon(xPoints, yPoints, 4);

            g2.setColor(getBackground());
            g2.fill(trapeze);

            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();
            g2.drawString("Sign-In", x-45, y);

            super.paintComponent(g);
        }
    }
}
