package service;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import model.User;
import security.EmailVerificationService;
import security.PasswordHasher;

public class SignupService {
	private UserService userservice;
	private User user;
	private PasswordHasher hasher;
	private EmailVerificationService email;

	public SignupService() {
		this.userservice = new UserService();
		this.hasher = new PasswordHasher();
		this.email = new EmailVerificationService();
	}

	public boolean isValidID(String password) {
		if (password.charAt(0) == '6' && password.charAt(1) == '2' && password.charAt(2) == '0'
				&& password.length() == 9) {
			return true;

		} else {
			return false;
		}

	}

	public boolean isValidEmail(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		boolean owner = false;
		// Compile the regex
		Pattern p = Pattern.compile(emailRegex);

		// send email to verify ownership
		String generatedtoken = this.email.sendVerificationEmail(email);

		// get token input
		String input = JOptionPane.showInputDialog(null, "Enter token sent to email:", "Input Box",
				JOptionPane.QUESTION_MESSAGE);

		// if email token = generated token
		if (input != null) {
			if (input.equals(generatedtoken)) {
				owner = true;
			}
		}

		// Check if email matches the pattern
		return email != null && p.matcher(email).matches() && owner;
	}

	public boolean isValidPassword(String password) {

		// Regex to check valid password.
		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		Matcher m = p.matcher(password);

		// Return if the password
		// matched the ReGex
		return m.matches();

	}

	public boolean signup(String firstname, String lastname, int id, String password, String email)
			throws SQLException {
		if (isValidID(id + "") && isValidPassword(password) && isValidEmail(email)) {
			password = hasher.toHash(password);
			user = new User(id, firstname, lastname, email, password);
			return userservice.addUser(user);

		} else {
			JOptionPane.showMessageDialog(null, "Invalid credentials: Enter valid details", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

}
