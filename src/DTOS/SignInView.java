package DTOS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        frame.setResizable(true);
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
        });


        //Description Text Area
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBackground(Color.GRAY);
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 25));
        descriptionArea.setOpaque(true);
        descriptionArea.setText("...");
        descriptionArea.setFocusable(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setBounds(350, 75, 550, 250);
        attach1.add(scrollPane);

        attach1.revalidate();
        attach1.repaint();
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
