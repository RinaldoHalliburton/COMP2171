package object_oriented_design;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailVerificationService {
	private static final String SENDER_EMAIL = "rentabikeuwi@gmail.com"; // Replace with your email
	private static final String SENDER_PASSWORD = "qkmg omcx ccsv eicl"; // Use an app password for security

	// Generate a 6-digit random token
	public static String generateToken() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	// Send verification email with the token
	public String sendVerificationEmail(String recipientEmail) {
		String host = "smtp.gmail.com"; // SMTP server for Gmail

		// Email properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Create session with authentication
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
			}
		});

		try {
			// Generate the token
			String token = generateToken();

			// Create the email message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(SENDER_EMAIL));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject("Email Verification Code");
			message.setText("Your email verification code is: " + token);

			// Send the email
			Transport.send(message);
			System.out.println("✅ Verification email sent to: " + recipientEmail);
			return token;
		} catch (MessagingException e) {
			System.out.println("❌ Failed to send email: " + e.getMessage());
			return null;
		}
	}

	/*
	 * public static void main(String[] args) { String recipient =
	 * "rinaldohalliburton7917@gmail.com"; // Replace with user's email boolean
	 * success = sendVerificationEmail(recipient);
	 * 
	 * if (success) { System.out.println("✅ Email sent successfully!"); } else {
	 * System.out.println("❌ Email failed to send."); } }
	 */
}
