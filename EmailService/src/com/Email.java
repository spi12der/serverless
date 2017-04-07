package com;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.json.simple.JSONObject;

public class Email {
	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	final String SMTP_PORT = "465";
	@SuppressWarnings("unchecked")
	public JSONObject SendEmail(JSONObject message){
			JSONObject response = new JSONObject();
		  	// Get a Properties object
		     Properties props = System.getProperties();
		     try{
			     String SMTPHost = (String)message.get("smtp_host");
			     final String username = (String)message.get("username");
			     final String password = (String)message.get("password");
			     props.setProperty("mail.smtp.host", SMTPHost);
			     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			     props.setProperty("mail.smtp.socketFactory.fallback", "false");
			     props.setProperty("mail.smtp.port", SMTP_PORT);
			     props.setProperty("mail.smtp.socketFactory.port", SMTP_PORT);
			     props.put("mail.smtp.auth", "true");
			     props.put("mail.debug", "true");
			     props.put("mail.store.protocol", "pop3");
			     props.put("mail.transport.protocol", "smtp");
			     Session session = Session.getDefaultInstance(props, 
			                          new Authenticator(){
			                             protected PasswordAuthentication getPasswordAuthentication() {
			                                return new PasswordAuthentication(username, password);
			                             }});
	
			   // -- Create a new message --
			     Message msg = new MimeMessage(session);
	
			  // -- Set the FROM and TO fields --
			     msg.setFrom(new InternetAddress((String)message.get("sender")));
			     msg.setRecipients(Message.RecipientType.TO, 
			                      InternetAddress.parse((String)message.get("receiver"),false));
			     msg.setSubject((String)message.get("subject"));
			     msg.setText((String)message.get("message"));
			     msg.setSentDate(new Date());
			     Transport.send(msg);
			     System.out.println("Message sent.");
			     response.put("status", "1");
			     response.put("message", "Message Sent Successful");
		  }
		  catch (MessagingException e){ 
			  System.out.println("Error in sending, cause: " + e);
			  response.put("status", "0");
			  response.put("message", "Message Sent Unsuccessful");
		  }
	      return response;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String [] args) {  
		Email e = new Email();
		JSONObject test = new JSONObject();
		test.put("smtp_host", "students.iiit.ac.in");
		test.put("username", "gaurav.agarwal@students.iiit.ac.in");
		test.put("password", "*******");
		test.put("sender", "gaurav.agarwal@students.iiit.ac.in");
		test.put("receiver", "rohit.dayama@students.iiit.ac.in");
		test.put("subject", "Love Letter");
		test.put("message", "Bokacoda Randi");
		e.SendEmail(test);
	}
}
