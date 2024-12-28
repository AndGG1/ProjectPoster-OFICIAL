import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    
    public static void main(String[] args) {
        String to = "andrei.greblaru@gmail.com";
        
        String from = "greblaru@yahoo.com";
        String password = "Oravita1983";
        
        String host = "smtp.mail.yahoo.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(from);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Case");
            message.setText("Hi! I hope this works... !!!PLS WORK!!!");
            
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Message sent successfully to: " + to);
    }
}
