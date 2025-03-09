package service;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import db.Database;
import model.User;
import security.PasswordHasher;

public class UserService {

	private Database db;
	private PasswordHasher hasher;

	public UserService() {
		this.db = new Database();
		this.hasher = new PasswordHasher();
	}

	public boolean authenticateUser(String id, String password) {
		int userID;
		try {
			userID = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid User ID format!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try {
			User user = db.getUserByID(userID);
			if (user != null && hasher.compareHash(password, user.getPassword())) {
				return true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public boolean addUser(User user) throws SQLException {
		boolean addSuccess = db.insertUser(user);
		if (addSuccess) {

			JOptionPane.showMessageDialog(null, "User registered successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);

			return true;
		} else {
			JOptionPane.showMessageDialog(null, "User registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
}
