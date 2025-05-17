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
//        sendEmail("andrei.greblaru@gmail.com");
        new EmailSender();
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
        final String host = "server73.romania-webhosting.com";
        final String port = "465";
        final String username = "...";
        final String password = "...";
        final String fromAddress = "...";

        String toAddress = to;
        String subject = "Test Email via Custom SMTP (Java)";
        String body = "This is a test message sent using Jakarta Mail and custom server settings.";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true"); // Important for port 465


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setText(body);

            System.out.println("Attempting to send email to " + toAddress + " via " + host + ":" + port + "...");
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException mex) {
            System.err.println("Failed to send email. Error: " + mex.getMessage());
            mex.printStackTrace();
            Exception nextEx = mex.getNextException();
            while (nextEx != null) {
                System.err.println("Caused by:");
                nextEx.printStackTrace();
                if (nextEx instanceof MessagingException) {
                    nextEx = ((MessagingException) nextEx).getNextException();
                } else if (nextEx instanceof Exception){
                    // To handle cases where the nested exception isn't MessagingException but has its own cause
                    Throwable cause = nextEx.getCause();
                    nextEx = (cause instanceof Exception) ? (Exception) cause : null;
                }
                else {
                    nextEx = null;
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
