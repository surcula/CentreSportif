package services;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dto.Mail;
import org.apache.log4j.Logger;

/**
 * @author Renaud DIANA
 *
 */
public class MailSender {

	// Log4j	 
	private static final Logger	log	= Logger.getLogger(MailSender.class);
	
	public static boolean sendMail(Mail mail) {
		
		/* ********* attach **********
		// Set Subject: header field
        message.setSubject("This is the Subject Line!");

        // Create the message part 
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setText("This is message body");
        
        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        String filename = "file.txt";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        message.setContent(multipart );
        */
		boolean sendFlag = false;
		
		final String username = "your_account@gmail.com";
        final String password = "your_password";
		
		// Check nick and replyTo
		if(mail.getNick() == null || mail.getNick().equals(""))
        	mail.setNick(mail.getFrom());
        if(mail.getReplyTo() == null || mail.getReplyTo().equals(""))
        	mail.setReplyTo(mail.getFrom());
        
        // Debug
        for(String s : mail.getListTo())
        	log.debug("To: " + s);
        log.debug("Subject: " + mail.getSubject());
		log.debug("MsgBody: " + mail.getMsgBody());
		log.debug("From: " + mail.getFrom());
		log.debug("Nick: " + mail.getNick());
		log.debug("ReplyTo: " + mail.getReplyTo());
		
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); //TLS
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        try {        	
            Message msg = new MimeMessage(session);
            
            if(mail.isEncodeUTF8()) {
            	
	            msg.setHeader("Content-Type", "text/plain;charset=\"UTF-8\"");
	            msg.setHeader("Content-Transfert-Encoding", "8bit");
            }
            
			msg.setFrom(new InternetAddress(mail.getFrom(), mail.getNick()));
		 
        	for(String email : mail.getListTo()) {
            
            	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
                        
            msg.setReplyTo(new Address[]{new InternetAddress(mail.getReplyTo())});
            msg.setSubject(mail.getSubject());
            msg.setText(mail.getMsgBody());
            Transport.send(msg);
            
            sendFlag = true;
            
        } catch (AddressException e) {
        	
        	//e.printStackTrace();
            log.error("AddressException\n" + e.toString());
        } catch (MessagingException e) {
        	
        	//e.printStackTrace();
            log.error("MessagingException\n" + e.toString());
        } catch (UnsupportedEncodingException e) {
        	
			//e.printStackTrace();
			log.error("UnsupportedEncodingException\n" + e.toString());
		}
        
        return sendFlag;
	}
}