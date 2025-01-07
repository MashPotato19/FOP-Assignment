import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Handles email notifications for the to-do list application.
 */
public class EmailNotiSystem {
   /**
     * Sends an email notification.
     *
     * @param senderEmail    the sender's email address
     * @param senderPassword the sender's email password
     * @param subject        the subject of the email
     * @param messageText    the content of the email
     * @param recipientEmail the recipient's email address
     */
    public static void sendEmail(String senderEmail, String senderPassword, String subject, String messageText, String recipientEmail) {
        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Email session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);

            // Send the email
            Transport.send(message);
            System.out.println("Reminder email sent successfully to " + recipientEmail);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
    
}

