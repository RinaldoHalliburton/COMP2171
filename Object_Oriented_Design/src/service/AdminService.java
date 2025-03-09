package service;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import db.Database;
import model.Admin;
import security.PasswordHasher;

public class AdminService {

	private Database db;
	private PasswordHasher hasher;

	public AdminService() {
		this.db = new Database();
		this.hasher = new PasswordHasher();
	}

	public boolean authenticateAdmin(String id, String password) throws SQLException {

		int adminID;
		try {
			adminID = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid Admin ID format!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try {
			Admin admin = db.getAdminByID(adminID);
			if (admin != null && hasher.compareHash(password, admin.getPassword())) {
				return true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;

	}

	public void addAdmin(Admin admin) {

	}

}
