package DTOS.UserInterfaces.Register;

import DTOS.EXTRA_Links;
import Database.Functionality.Startup.Startup_Log;
import Database.Functionality.Startup.Startup_Sign;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SignInView {
    private final JFrame frame;
    private static Consistency cons;

    public SignInView() {
        //Resource Bundle
        //Locale.setDefault(new Locale("de", "DE"));
        ResourceBundle rb = null;
        if (Locale.getDefault().toString().equals("ro_RO") || Locale.getDefault().toString().equals("de_DE")) {
            rb = ResourceBundle.getBundle("BasicText", Locale.getDefault());
        }
        
        //Main Part - Frame
        frame = new JFrame();
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
        descriptionArea.setFocusable(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        if (rb != null) {
            descriptionArea.setText(rb.getString("description"));
        }


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
        passField.setBounds(225, 500, 650, 50); // Added bounds for nameField
        attach1.add(passField);

        JLabel attach_Pass2 = new JLabel();
        attach_Pass2.setBackground(Color.DARK_GRAY);
        attach_Pass2.setOpaque(true);
        attach_Pass2.setBounds(30, 510, 850, 50);
        attach1.add(attach_Pass2);

        JButton passButton = new JButton(EXTRA_Links.getPassClosedEye());
        passButton.setBackground(Color.GRAY);
        passButton.setContentAreaFilled(false);
        passButton.setBorderPainted(false);
        passButton.setFocusPainted(false);
        passButton.setBounds(890, 505, 50, 50);
        attach1.add(passButton);



        //Name Container
        JLabel attach_Link = new JLabel();
        attach_Link.setBackground(Color.GRAY);
        attach_Link.setForeground(Color.BLACK);
        attach_Link.setFont(new Font("Arial", Font.BOLD, 25));
        attach_Link.setHorizontalAlignment(JLabel.CENTER);
        attach_Link.setText("LINK:");
        attach_Link.setOpaque(true);
        attach_Link.setBounds(25, 600, 200, 50); // Corrected bounds
        attach1.add(attach_Link);

        JTextField linkField = new JTextField();
        linkField.setBackground(Color.GRAY);
        linkField.setForeground(Color.BLACK);
        linkField.setFont(new Font("Arial", Font.BOLD, 25));
        linkField.setHorizontalAlignment(JTextField.CENTER);
        linkField.setOpaque(true);
        linkField.setText("https://site.com/John");
        linkField.setBounds(225, 600, 700, 50); // Added bounds for nameField
        attach1.add(linkField);

        JLabel attach_Link2 = new JLabel();
        attach_Link2.setBackground(Color.DARK_GRAY);
        attach_Link2.setOpaque(true);
        attach_Link2.setBounds(30, 610, 900, 50);
        attach1.add(attach_Link2);



        //Confirm Button
        JButton confirmButton = new JButton();
        confirmButton.setBackground(Color.GRAY);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setHorizontalAlignment(JButton.CENTER);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 25));
        confirmButton.setText("Sign Up");
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setBounds(370, 780, 250, 50);
        attach1.add(confirmButton);

        JLabel attach_ConfirmButton = new JLabel();
        attach_ConfirmButton.setBackground(Color.DARK_GRAY);
        attach_ConfirmButton.setOpaque(true);
        attach_ConfirmButton.setBounds(375, 790, 250, 50);
        attach1.add(attach_ConfirmButton);

        
        
        //Swap_Interface_Button
        JButton swapButton = new JButton();
        swapButton.setForeground(Color.BLACK);
        swapButton.setHorizontalAlignment(JButton.CENTER);
        swapButton.setFont(new Font("Arial", Font.BOLD, 35));
        swapButton.setText(">");
        swapButton.setContentAreaFilled(false);
        swapButton.setBorderPainted(false);
        swapButton.setFocusPainted(false);
        swapButton.setBounds(625, 0, 75, 75);
        attach1.add(swapButton);
        
        
        
        //Internationalization
        if (rb != null) {
            attach_Name.setText(rb.getString("name"));
            attach_Pass.setText(rb.getString("password"));
            attach_Link.setText(rb.getString("link"));
            //attach2.setText(rb.getString("sign"));
            confirmButton.setText(rb.getString("sign"));
        }
        
        

        //Revalidation Process
        cons = new Consistency();
        cons.setSignIn(this);
        attach1.setComponentZOrder(scrollPane, 0);
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(attach_Name, 0);
        attach1.setComponentZOrder(swapButton, 0);
        attach1.revalidate();
        attach1.repaint();



        //Icon Label Listeners
        final String[] imgSource = new String[1];
        System.out.println(imgSource[0]);
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == 1) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    int result = fileChooser.showOpenDialog(frame);
                    
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File returnVal = fileChooser.getSelectedFile();
                        if (returnVal.exists() && returnVal.isFile() && returnVal.canRead()) {
                            try {
                                Image img = ImageIO.read(returnVal);
                                if (img != null) {
                                    img = img.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                                    imgSource[0] = fileChooser.getSelectedFile().getPath();
                                    System.out.println(imgSource[0]);
                                    iconLabel.setIcon(new ImageIcon(img));
                                } else {
                                    Image img2 = ImageIO.read(new URL("https://avatars.githubusercontent.com/u/154756433?v=4&size=64"));
                                    img2 = img2.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                                    imgSource[0] = "https://avatars.githubusercontent.com/u/154756433?v=4&size=64";
                                    iconLabel.setIcon(new ImageIcon(img2));
                                }
                                iconLabel.setText("");
                            } catch (IOException e2) {
                                //do nothing
                            }
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
        ResourceBundle finalRb = rb;
        ResourceBundle finalRb1 = rb;
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
                    if (finalRb1 != null) {
                        descriptionArea.setText(finalRb1.getString("description"));
                    } else descriptionArea.setText("Tell me about yourself...");
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

        final boolean[] isNormalChar = new boolean[1];
        passButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return; //only left-click allowed

                if (!isNormalChar[0]) {
                    passField.setEchoChar((char) 0);
                    passButton.setIcon(EXTRA_Links.getPassOpenEye());
                    isNormalChar[0] = true;
                } else {
                    passField.setEchoChar('•');
                    passButton.setIcon(EXTRA_Links.getPassClosedEye());
                    isNormalChar[0] = false;
                }
            }
        });



        //Link Listeners
        attach_Link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EXTRA_Links.accessLink(linkField.getText());
            }
        });
        
        linkField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                linkField.setBounds(227, 605, 700, 50);
                attach_Link.setBounds(27, 605, 200, 50);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                linkField.setBounds(225, 600, 700, 50);
                attach_Link.setBounds(25, 600, 200, 50);
            }
        });

        final boolean[] exampleOn3 = new boolean[1];
        exampleOn3[0] = true;
        linkField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (exampleOn3[0]) {
                    linkField.setText("");
                    exampleOn3[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String focus = linkField.getText();
                if (focus.isEmpty() || focus.isBlank()) {
                    linkField.setText("https://site.com/John");
                    exampleOn3[0] = true;
                }
            }
        });



        //ConfirmButton Listeners
        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Runnable myRunnable = () -> {
                    boolean flag = false;
                    
                    if (nameField.getText().replaceAll(" ", "").length() < 3) {
                        flag = true;
                        attach_Name.setBackground(Color.RED);
                    } else attach_Name.setBackground(Color.GRAY);
                    
                    if (passField.getText().length() < 6) {
                        flag = true;
                        attach_Pass.setBackground(Color.RED);
                    } else attach_Pass.setBackground(Color.GRAY);
                    
                    //Needed to create banana variable as the if-statement was getting confusing by the method
                    boolean bool = EXTRA_Links.checkAbilityToCreate(linkField.getText());
                    if (!bool) {
                        flag = true;
                        attach_Link.setBackground(Color.RED);
                    } else attach_Link.setBackground(Color.GRAY);
                    
                    
                    if (flag) {
                        System.out.println("Ooops! Something Went Wrong!");
                        return;
                    }
                    synchronized (this) {
                        if (!Startup_Log.searchUser(nameField.getText())) {
                            Startup_Sign.addUser(nameField.getText(), String.valueOf(passField.getPassword()), linkField.getText(), imgSource[0], descriptionArea.getText());
                            System.out.println("Successfully Added User: " + nameField.getText());
                            
                            new WelcomePage(imgSource[0] == null ? "https://avatars.githubusercontent.com/u/154756433?v=4&size=64" : imgSource[0], nameField.getText(), descriptionArea.getText());
                            frame.dispose();
                        }
                    }
                };
                Thread newThread = new Thread(myRunnable);
                newThread.setUncaughtExceptionHandler((thread, exc) -> {
//                    System.out.println(newThread.getName() + " got banana problem!");
//                    System.out.println("Exc: " + exc);
                    newThread.interrupt();
                });
                newThread.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                confirmButton.setBounds(372, 785, 250, 50);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                confirmButton.setBounds(370, 780, 250, 50);
            }
        });
        
        
        
        //Swap Button Listeners
        swapButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                
                //does the LogIn View exist? true => set it visible, else, create banana new one
                //this boosts the performance
                if (!cons.isLog()) {
                    cons.setLogIn(new LogInView());
                    cons.setLog(true);
                } else cons.getLogIn().getFrame().setVisible(true);
            }
        });
        
        
        
        //Frame Closed?
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cons.setSign(false);
                cons.setSignIn(null);
                
                if (cons.isLog()) cons.getLogIn().getFrame().setVisible(true);
            }
        });
    }
    
    public JFrame getSignFrame() {
        return frame;
    }
    
    public static Consistency getCons() {
        return cons;
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
            g2.drawString("Sign-Up", x-45, y);

            super.paintComponent(g);
        }
    }
}
