package DTOS.UserInterfaces.Register;

import DTOS.EXTRA_Links;
import Database.Functionality.Startup.Startup_Log;
import Database.Functionality.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInView {
    private final JFrame frame;

    public LogInView() {
        
        //Resource Bundle
        //Locale.setDefault(new Locale("de", "DE"));
        ResourceBundle rb = null;
        if (Locale.getDefault().toString().equals("ro_RO") || Locale.getDefault().toString().equals("de_DE")) {
            rb = ResourceBundle.getBundle("BasicText", Locale.getDefault());
        }
        
        //Main Part - Frame
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);

        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 10, 949, 430);
        frame.add(attach1);

        TrapezeLabel attach2 = new TrapezeLabel();
        attach2.setBackground(Color.GRAY);
        attach2.setForeground(Color.BLACK);
        attach2.setBounds(350, 0, 300, 50);
        attach1.add(attach2);



        //Name Container
        JLabel attach_Name = new JLabel();
        attach_Name.setBackground(Color.GRAY);
        attach_Name.setForeground(Color.BLACK);
        attach_Name.setFont(new Font("Arial", Font.BOLD, 25));
        attach_Name.setHorizontalAlignment(JLabel.CENTER);
        attach_Name.setText("NAME:");
        attach_Name.setOpaque(true);
        attach_Name.setBounds(25, 100, 200, 50); // Corrected bounds
        attach1.add(attach_Name);

        JTextField nameField = new JTextField();
        nameField.setBackground(Color.GRAY);
        nameField.setForeground(Color.BLACK);
        nameField.setFont(new Font("Arial", Font.BOLD, 25));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setOpaque(true);
        nameField.setText("John");
        nameField.setBounds(225, 100, 700, 50); // Added bounds for nameField
        attach1.add(nameField);

        JLabel attach_Name2 = new JLabel();
        attach_Name2.setBackground(Color.DARK_GRAY);
        attach_Name2.setOpaque(true);
        attach_Name2.setBounds(30, 110, 900, 50);
        attach1.add(attach_Name2);



        //Password Container
        JLabel attach_Pass = new JLabel();
        attach_Pass.setBackground(Color.GRAY);
        attach_Pass.setForeground(Color.BLACK);
        attach_Pass.setFont(new Font("Arial", Font.BOLD, 25));
        attach_Pass.setHorizontalAlignment(JLabel.CENTER);
        attach_Pass.setText("PASSWORD:");
        attach_Pass.setOpaque(true);
        attach_Pass.setBounds(25, 200, 200, 50); // Corrected bounds
        attach1.add(attach_Pass);

        JPasswordField passField = new JPasswordField();
        passField.setBackground(Color.GRAY);
        passField.setForeground(Color.BLACK);
        passField.setFont(new Font("Arial", Font.BOLD, 25));
        passField.setHorizontalAlignment(JTextField.CENTER);
        passField.setOpaque(true);
        passField.setBounds(225, 200, 650, 50); // Added bounds for nameField
        attach1.add(passField);

        JLabel attach_Pass2 = new JLabel();
        attach_Pass2.setBackground(Color.DARK_GRAY);
        attach_Pass2.setOpaque(true);
        attach_Pass2.setBounds(30, 210, 850, 50);
        attach1.add(attach_Pass2);

        JButton passButton = new JButton(EXTRA_Links.getPassClosedEye());
        passButton.setBackground(Color.GRAY);
        passButton.setContentAreaFilled(false);
        passButton.setBorderPainted(false);
        passButton.setFocusPainted(false);
        passButton.setBounds(890, 205, 50, 50);
        attach1.add(passButton);



        //Confirm Button
        JButton confirmButton = new JButton();
        confirmButton.setBackground(Color.GRAY);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setHorizontalAlignment(JButton.CENTER);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 25));
        confirmButton.setText("Log In");
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setBounds(370, 320, 250, 50);
        attach1.add(confirmButton);

        JLabel attach_ConfirmButton = new JLabel();
        attach_ConfirmButton.setBackground(Color.DARK_GRAY);
        attach_ConfirmButton.setOpaque(true);
        attach_ConfirmButton.setBounds(375, 330, 250, 50);
        attach1.add(attach_ConfirmButton);
        
        
        
        //Swap_Interface_Button
        JButton swapButton = new JButton();
        swapButton.setForeground(Color.BLACK);
        swapButton.setHorizontalAlignment(JButton.CENTER);
        swapButton.setFont(new Font("Arial", Font.BOLD, 35));
        swapButton.setText("<");
        swapButton.setContentAreaFilled(false);
        swapButton.setBorderPainted(false);
        swapButton.setFocusPainted(false);
        swapButton.setBounds(300, 0, 75, 75);
        attach1.add(swapButton);
        
        
        
        //Internationalization
        if (rb != null) {
            attach_Name.setText(rb.getString("name"));
            attach_Pass.setText(rb.getString("password"));
            //attach2.setText(rb.getString("sign"));
            confirmButton.setText(rb.getString("sign"));
        }



        //Revalidation Process
        Consistency cons = SignInView.getCons();
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(attach_Name, 0);
        attach1.revalidate();
        attach1.repaint();



        //Name Field Label Listeners
        nameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nameField.setBounds(227, 105, 700, 50);
                attach_Name.setBounds(27, 105, 200, 50);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nameField.setBounds(225, 100, 700, 50);
                attach_Name.setBounds(25, 100, 200, 50);
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
                passField.setBounds(227, 205, 650, 50);
                attach_Pass.setBounds(27, 205, 200, 50);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passField.setBounds(225, 200, 650, 50);
                attach_Pass.setBounds(25, 200, 200, 50);
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
        
        
        
        //Swap Button Listeners
        swapButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                
                //does the SignIn View exist? true => set it visible, else, create a new one
                //this boosts the performance
                if (!cons.isSign()) {
                    cons.setSignIn(new SignInView());
                    cons.setSign(true);
                } else cons.getSignIn().getSignFrame().setVisible(true);
            }
        });
        
        
        
        confirmButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked (MouseEvent e) {
                    Runnable myRUnnable = () -> {
                        boolean validUsername = Startup_Log.searchUser(nameField.getText());
                        boolean validPass = validUsername ? Startup_Log.checkUser(nameField.getText(), String.valueOf(passField.getPassword())) : false;
                        System.out.println(validPass);
                        
                        if (!validUsername && !validPass) {
                            attach_Name.setBackground(Color.RED);
                            attach_Pass.setBackground(Color.RED);
                            return;
                        } else if (!validPass) {
                            attach_Name.setBackground(Color.GRAY);
                            attach_Pass.setBackground(Color.RED);
                            return;
                        }
                        attach_Name.setBackground(Color.GRAY);
                        attach_Pass.setBackground(Color.GRAY);
                        frame.dispose();
                        
                        User user = Startup_Log.getUser();
                        System.out.println(user + "-");
                        System.out.println(user.getImg());
                        new WelcomePage(user.getImg(), user.getUsername(), user.getDescription());
                    };
                    Thread newThread = new Thread(myRUnnable);
                    newThread.start();
                }
        });
        
        
        
        //Frame Closed?
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cons.setLog(false);
                cons.setLogIn(null);
                
                if (cons.isSign()) cons.getSignIn().getSignFrame().setVisible(true);
            }
        });
    }
    
    public Frame getFrame() {
        return frame;
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
            g2.drawString("Log-In", x-45, y);

            super.paintComponent(g);
        }
    }
}