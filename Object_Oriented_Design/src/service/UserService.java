package service;

import java.sql.SQLException;

import db.Database;
import model.User;
import security.PasswordHasher;

public class UserService {

	private Database db;
	private PasswordHasher hasher;
	public static int userSessionID;
	public static int userSessionPayment;
	private static int useriSLinked;
	private static String userSessionPassword;

	public UserService() {
		this.db = new Database();
		this.hasher = new PasswordHasher();
	}

	public boolean authenticateUser(String id, String password) {
		int userID;
		try {
			userID = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}

		try {
			User user = db.getUserByID(userID);
			if (user != null && hasher.compareHash(password, user.getPassword())) {
				UserService.userSessionPassword = user.getPassword();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addUser(User user) {
		boolean addSuccess = db.insertUser(user);
		return addSuccess;
	}

	public boolean removeUser() {
		return db.deleteUser(userSessionID);
	}

	public void setSessionID(int id) {
		UserService.userSessionID = id;
	}

	public int getSessionID() {
		return UserService.userSessionID;
	}

	public void setSessionPayment(int value) {
		UserService.userSessionPayment = value;
	}

	public int getSessionPayment() {
		return UserService.userSessionPayment;
	}

	public void setUserisLinked(int useriSLinked) {
		UserService.useriSLinked = useriSLinked;
	}

	public int getUserisLinked() {
		return UserService.useriSLinked;
	}

	public String getUserSessionPassword() {
		return UserService.userSessionPassword;
	}
}
