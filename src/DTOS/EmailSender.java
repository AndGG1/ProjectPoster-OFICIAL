package DTOS;

import org.apache.commons.validator.routines.EmailValidator;

import java.awt.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.util.Properties;

public class EmailSender {
    
    private static JTextField emailField;
    private static JLabel statusLabel;
    
    public static void main(String[] args) throws MessagingException {
        sendEmail("andrei.greblaru@gmail.com");
    }
    
    public EmailSender() {
        String from = "your_email";
        String pass = "your_password";
        String serverHost = "your_host";
        
        JFrame frame = new JFrame("Email Sender");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(350, 150);
        
        emailField = new JTextField(20);
        JButton sendButton = new JButton("Send Email");
        statusLabel = new JLabel("");
        
        sendButton.addActionListener(e -> {
            String to = emailField.getText();
            if (!isSafeEmail(to)) {
                emailField.setBackground(Color.RED);
                statusLabel.setText("Invalid email address.");
                return;
            }
            try {
                sendEmail(to);
                statusLabel.setText("Message sent successfully!");
                emailField.setBackground(Color.WHITE);
            } catch (MessagingException ex) {
                emailField.setBackground(Color.RED);
                statusLabel.setText("Failed to send email.");
            }
        });
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Recipient Email:"));
        panel.add(emailField);
        panel.add(sendButton);
        panel.add(statusLabel);
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    
    private static boolean isSafeEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    
    private static void sendEmail(String to) throws MessagingException {
        String password = "ER86Yt42"; // Use app-specific password if 2FA is enabled
        String host = "smtp.gmail.com";
        String from = "andrei.greblaru@gmail.com";
        
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Using TLS
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.user", "username");
        properties.put("mail.smtp.password", "password");
        properties.put("mail.smtp.starttls.enable", true); // Enable TLS
        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Email");
            message.setText("This is banana test email to verify SMTP configuration.");
            
            Transport.send(message, message.getAllRecipients());
            System.out.println("Sent message successfully....");
        } catch (AuthenticationFailedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
