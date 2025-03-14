package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Admin;
import model.User;
import service.BikeService;
import service.UserService;

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
			// System.out.println("✅ Connected to database!");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("❌ Database connection failed!");
		}

	}

	// Execute a SELECT query and return ResultSet
	/*
	 * public ResultSet executeQuery(String query) throws SQLException { Statement
	 * stmt = conn.createStatement(); return stmt.executeQuery(query); }
	 */

	// Get user by ID
	public User getUserByID(int ID) throws SQLException {
		String query = "SELECT userID, userFirstName, userLastName, userEmail, userPassword, hasPayment, isLinked FROM User WHERE userID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, ID);
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				new UserService().setSessionPayment(Integer.parseInt(result.getString(6)));
				new UserService().setUserisLinked(result.getInt(7));
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

	public ArrayList<String> fetchPaymentMethod(int userID) {
		ArrayList<String> paymentList = new ArrayList<String>();

		String query = "SELECT payment.cardName, payment.cardNumber FROM user "
				+ "JOIN payment ON user.userID = payment.userID " + "WHERE user.userID = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userID);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String firstName = rs.getString("cardName");
				String cardNumber = rs.getString("cardNumber");
				paymentList.add(firstName + "-" + cardNumber);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error fetching data from database", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return paymentList;
	}

	public ArrayList<String> fetchBikes(String station) {
		ArrayList<String> bikeList = new ArrayList<>();

		String query = "SELECT bikeID, rating FROM bike WHERE station = ? AND inUse = ?;";

		try (PreparedStatement stmt = conn.prepareStatement(query)) { // Use PreparedStatement
			stmt.setString(1, station);
			stmt.setInt(2, 0);

			try (ResultSet rs = stmt.executeQuery()) { // Execute query correctly
				while (rs.next()) {
					String bikeID = String.valueOf(rs.getInt("bikeID"));
					String rating = rs.getString("rating");
					bikeList.add(bikeID + "-" + rating);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error fetching data from database", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return bikeList;
	}

	public void deletePaymentMethod(String name, String cardNumber, int id) {
		String deleteQuery = "DELETE FROM payment " + "WHERE userID = ? AND cardName = ? AND cardNumber = ?";

		String checkQuery = "SELECT COUNT(*) FROM payment WHERE userID = ?";
		String updateUserQuery = "UPDATE user SET hasPayment = 0 WHERE userID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, cardNumber); // Exact match for security

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("✅ Successfully deleted " + rowsAffected + " payment record(s).");

				try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
					checkStmt.setInt(1, id);
					ResultSet rs = checkStmt.executeQuery();

					if (rs.next() && rs.getInt(1) == 0) { // No more payments left
						try (PreparedStatement updateStmt = conn.prepareStatement(updateUserQuery)) {
							updateStmt.setInt(1, id);
							int updateRows = updateStmt.executeUpdate();
							new UserService().setSessionPayment(0);
							if (updateRows > 0) {
								System.out.println("✅ `hasPayment` set to 0 for user ID: " + id);
							}
						}
					}
				}
			} else {
				System.out.println("⚠️ No matching payment records found for deletion.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("❌ Error deleting payment record: " + e.getMessage());
		}
	}

	public void insertPaymentMethod(String name, int id, String cardNumber, String cvv, String date) {
		String checkDuplicateQuery = "SELECT COUNT(*) FROM payment WHERE userID = ? AND cardNumber = ?";
		String insertQuery = "INSERT INTO payment (cardName, cardNumber, cvv, expDate, userID) VALUES (?, ?, ?, ?, ?)";
		String updateUserQuery = "UPDATE user SET hasPayment = 1 WHERE userID = ?";

		try (PreparedStatement checkStmt = conn.prepareStatement(checkDuplicateQuery)) {
			checkStmt.setInt(1, id);
			checkStmt.setString(2, cardNumber);

			ResultSet rs = checkStmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) { // If card number already exists for the user
				JOptionPane.showMessageDialog(null, "❌ Duplicate Card Number! This card is already registered.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return; // Stop execution, do not insert
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ Database Error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// If no duplicate, proceed with the insert
		try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
			stmt.setString(1, name);
			stmt.setString(2, cardNumber);
			stmt.setString(3, cvv);
			stmt.setString(4, date);
			stmt.setInt(5, id);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				JOptionPane.showMessageDialog(null, "✅ Payment Method Added Successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);

				// ✅ Set Session Payment after successful insert
				new UserService().setSessionPayment(id);

				try (PreparedStatement updateStmt = conn.prepareStatement(updateUserQuery)) {
					updateStmt.setInt(1, id);
					int updateRows = updateStmt.executeUpdate();
					if (updateRows > 0) {
						System.out.println("✅ `hasPayment` set to 1 for user ID: " + id);
					}
				}

				System.out.println("✅ Payment Added: " + id + " - " + cardNumber);
			} else {
				JOptionPane.showMessageDialog(null, "⚠️ Failed to Add Payment!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ Database Error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void linkBike(String id) {
		int intID;

		try {
			intID = Integer.parseInt(id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "❌ Enter a valid Integer: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get the current user ID (assuming UserService manages sessions)
		int userID = new UserService().getSessionID();

		// Get current timestamp
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		// SQL queries
		String updateBikeQuery = "UPDATE bike SET inUse = 1, station = ? WHERE bikeID = ?";
		String updateUserQuery = "UPDATE user SET isLinked = 1 WHERE userID = ?";
		String insertHistoryQuery = "INSERT INTO history (userID, bikeID, linkTime) VALUES (?, ?, ?)";

		try (PreparedStatement updateBikeStmt = conn.prepareStatement(updateBikeQuery);
				PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
				PreparedStatement insertHistoryStmt = conn.prepareStatement(insertHistoryQuery)) {
			// Update bike table (set inUse = 1)

			updateBikeStmt.setString(1, null);
			updateBikeStmt.setInt(2, intID);
			int bikeUpdatedRows = updateBikeStmt.executeUpdate();

			if (bikeUpdatedRows > 0) {
				// Update user table (set isLinked = 1)
				new UserService().setUserisLinked(1);
				updateUserStmt.setInt(1, userID);
				updateUserStmt.executeUpdate();

				// Insert into history table (log linking event)
				insertHistoryStmt.setInt(1, userID);
				insertHistoryStmt.setInt(2, intID);
				insertHistoryStmt.setString(3, currentTime);
				insertHistoryStmt.executeUpdate();

				JOptionPane.showMessageDialog(null, "✅ Bike " + intID + " linked successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "❌ No bike found with ID: " + intID, "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ Error updating database: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void unlinkBike(String bikeID, String currentStation) {
		// Get current user ID (assuming UserService manages sessions)
		int userID = new UserService().getSessionID();

		// Get current timestamp
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		// SQL queries
		String updateBikeQuery = "UPDATE bike SET inUse = 0, station = ? WHERE bikeID = ?";
		String updateUserQuery = "UPDATE user SET isLinked = 0 WHERE userID = ?";
		String updateHistoryQuery = "UPDATE history SET unlinkTime = ? WHERE userID = ? AND bikeID = ? AND unlinkTime IS NULL";

		try (PreparedStatement updateBikeStmt = conn.prepareStatement(updateBikeQuery);
				PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
				PreparedStatement updateHistoryStmt = conn.prepareStatement(updateHistoryQuery)) {
			// Update bike table (set inUse = 0)
			updateBikeStmt.setString(1, currentStation);
			updateBikeStmt.setInt(2, Integer.parseInt(bikeID));
			int bikeUpdatedRows = updateBikeStmt.executeUpdate();

			if (bikeUpdatedRows > 0) {
				// Update user table (set isLinked = 0)
				new UserService().setUserisLinked(0);
				new BikeService().setlinkedBiketoNull();
				updateUserStmt.setInt(1, userID);
				updateUserStmt.executeUpdate();

				// Update history table (set unlinkTime for the last linked record)
				updateHistoryStmt.setString(1, currentTime);
				updateHistoryStmt.setInt(2, userID);
				updateHistoryStmt.setInt(3, Integer.parseInt(bikeID));
				updateHistoryStmt.executeUpdate();

				// Notify user of success
				JOptionPane.showMessageDialog(null, "✅ Bike " + bikeID + " unlinked successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);

				// Update user status to reflect the unlinking
				new UserService().setUserisLinked(0);
			} else {
				JOptionPane.showMessageDialog(null, "❌ No bike found with ID: " + bikeID, "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ Error updating database: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void deleteUser(int userId) {
		String deleteQuery = "DELETE FROM user WHERE userID = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
			preparedStatement.setInt(1, userId);

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User with ID " + userId + " deleted successfully.");
				JOptionPane.showMessageDialog(null, "Account sucessfully deleted", "Account Deleted",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error deleting user: " + e.getMessage());
		}
	}
}
