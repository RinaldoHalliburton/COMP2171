package object_oriented_design;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Database {

	private Connection conn;

	// Constructor: Connect to MySQL database
	public Database() {
		String url = "jdbc:mysql://localhost:3306/RAB";
		String username = System.getenv("DB_USER"); // Use a proper env variable
		String password = System.getenv("DB_PASSWORD");

		try {
			// Load the MySQL JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("✅ Connected to database!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("❌ Database connection failed!");
		}
	}

	// Close database connection
	public void closeDB() {
		try {
			if (conn != null) {
				conn.close();
				System.out.println("🔌 Database connection closed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Execute a SELECT query and return ResultSet
	public ResultSet executeQuery(String query) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(query);
	}

	// Get user by ID
	public User getUserByID(int ID) throws SQLException {
		String query = "SELECT userID, userFirstName, userLastName, userEmail, userPassword FROM User WHERE userID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, ID);
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				return new User(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
						result.getString(5));
			}
		}
		return null;
	}

	// Get admin by ID
	public Admin getAdminByID(int ID) throws SQLException {
		String query = "SELECT adminID, adminFirstName, adminLastName, adminEmail, adminPassword FROM Admin WHERE adminID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, ID);
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				return new Admin(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
						result.getString(5));
			}
		}
		return null;
	}

	// Insert a new user
	public boolean insertUser(User user) {
		String query = "INSERT INTO user (userID, userFirstName, userLastName, userEmail, userPassword) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, Integer.parseInt(user.getId()));
			stmt.setString(2, user.getFname());
			stmt.setString(3, user.getLname());
			stmt.setString(4, user.getEmail());
			stmt.setString(5, user.getPassword());

			// Execute update and return true if insertion is successful
			return stmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// Catch MySQL duplicate entry error (Error Code 1062)
			if (e.getErrorCode() == 1062) {
				System.out.println("❌ Error: Duplicate entry. User ID or Email already exists.");
				JOptionPane.showMessageDialog(null, "❌ Error: Duplicate entry. User ID or Email already exists.",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				System.out.println("❌ SQL Integrity Error: " + e.getMessage());
				JOptionPane.showMessageDialog(null, "❌ SQL Integrity Error", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "❌ Database Error", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("❌ Database Error: " + e.getMessage());
		}
		return false; // Return false if insertion fails due to a duplicate or SQL error
	}
}
