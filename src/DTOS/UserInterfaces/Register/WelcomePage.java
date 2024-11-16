package DTOS.UserInterfaces.Register;

import javax.swing.*;
import java.awt.*;

public class WelcomePage {
    JFrame frame;
    
    public WelcomePage() {
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
    }
}
