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

import model.Admin;
import model.Report;
import model.User;
import service.UserService;

public class Database {

	private Connection conn;

	// Constructor: Connect to MySQL database
	public Database() {
		String url = "jdbc:mysql://localhost:3306/RAB";
		String username = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database connection failed!");
		}
	}

	// ----------------------- USER CRUD -----------------------

	public User getUserByID(int ID) throws SQLException {
		String query = "SELECT userID, userFirstName, userLastName, userEmail, userPassword, hasPayment, isLinked FROM User WHERE userID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, ID);
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				new UserService().setSessionPayment(result.getInt(6));
				new UserService().setUserisLinked(result.getInt(7));
				return new User(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
						result.getString(5));
			}
		}
		return null;
	}

	public ArrayList<User> getAllUsers() throws SQLException {
		String query = "SELECT userID, userFirstName, userLastName, userEmail, lastBikeID FROM user";
		ArrayList<User> userList = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				userList.add(new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4),
						result.getString(5)));
			}
		}

		return userList;
	}

	public boolean insertUser(User user) {
		String query = "INSERT INTO user (userID, userFirstName, userLastName, userEmail, userPassword) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, Integer.parseInt(user.getId()));
			stmt.setString(2, user.getFname());
			stmt.setString(3, user.getLname());
			stmt.setString(4, user.getEmail());
			stmt.setString(5, user.getPassword());
			return stmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			if (e.getErrorCode() == 1062) {
				System.out.println("Error: Duplicate entry. User ID or Email already exists.");
			} else {
				System.out.println("SQL Integrity Error: " + e.getMessage());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteUser(int userId) {
		String deleteQuery = "DELETE FROM user WHERE userID = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
			preparedStatement.setInt(1, userId);
			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ----------------------- ADMIN CRUD -----------------------

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

	// ----------------------- PAYMENT CRUD -----------------------

	public ArrayList<String> fetchPaymentMethod(int userID) {
		ArrayList<String> paymentList = new ArrayList<>();
		String query = "SELECT payment.cardName, payment.cardNumber FROM user "
				+ "JOIN payment ON user.userID = payment.userID WHERE user.userID = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String cardName = rs.getString("cardName");
				String cardNumber = rs.getString("cardNumber");
				paymentList.add(cardName + "-" + cardNumber);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return paymentList;
	}

	public boolean insertPaymentMethod(String name, int id, String cardNumber, String cvv, String date) {
		String checkDuplicateQuery = "SELECT COUNT(*) FROM payment WHERE userID = ? AND cardNumber = ?";
		String insertQuery = "INSERT INTO payment (cardName, cardNumber, cvv, expDate, userID) VALUES (?, ?, ?, ?, ?)";
		String updateUserQuery = "UPDATE user SET hasPayment = 1 WHERE userID = ?";

		try (PreparedStatement checkStmt = conn.prepareStatement(checkDuplicateQuery)) {
			checkStmt.setInt(1, id);
			checkStmt.setString(2, cardNumber);
			ResultSet rs = checkStmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
			stmt.setString(1, name);
			stmt.setString(2, cardNumber);
			stmt.setString(3, cvv);
			stmt.setString(4, date);
			stmt.setInt(5, id);

			if (stmt.executeUpdate() > 0) {
				try (PreparedStatement updateStmt = conn.prepareStatement(updateUserQuery)) {
					updateStmt.setInt(1, id);
					return updateStmt.executeUpdate() > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deletePaymentMethod(String name, String cardNumber, int id) {
		String deleteQuery = "DELETE FROM payment WHERE userID = ? AND cardName = ? AND cardNumber = ?";
		String checkQuery = "SELECT COUNT(*) FROM payment WHERE userID = ?";
		String updateUserQuery = "UPDATE user SET hasPayment = 0 WHERE userID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, cardNumber);

			if (stmt.executeUpdate() > 0) {
				try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
					checkStmt.setInt(1, id);
					ResultSet rs = checkStmt.executeQuery();

					if (rs.next() && rs.getInt(1) == 0) {
						try (PreparedStatement updateStmt = conn.prepareStatement(updateUserQuery)) {
							updateStmt.setInt(1, id);
							new UserService().setSessionPayment(0);
							return updateStmt.executeUpdate() > 0;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ----------------------- BIKE OPERATIONS -----------------------

	public Integer fetchLastUserBike(int userID) {
		String query = "SELECT lastBikeID FROM user WHERE userID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userID);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int bikeID = rs.getInt("lastBikeID");
				if (!rs.wasNull()) {
					return bikeID;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // No bikeID found or user doesn't exist
	}

	public ArrayList<String> fetchBikes(String station) {
		ArrayList<String> bikeList = new ArrayList<>();
		String query = "SELECT bikeID, rating FROM bike WHERE station = ? AND inUse = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, station);
			stmt.setInt(2, 0);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String bikeID = String.valueOf(rs.getInt("bikeID"));
				String rating = rs.getString("rating");
				bikeList.add(bikeID + "-" + rating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bikeList;
	}

	public boolean linkBike(String id, int userID) {
		int intID;
		try {
			intID = Integer.parseInt(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String updateBikeQuery = "UPDATE bike SET inUse = 1, station = ? WHERE bikeID = ?";
		String updateUserQuery = "UPDATE user SET isLinked = 1, lastBikeID = ? WHERE userID = ?";
		String insertHistoryQuery = "INSERT INTO history (userID, bikeID, linkTime) VALUES (?, ?, ?)";

		try (PreparedStatement updateBikeStmt = conn.prepareStatement(updateBikeQuery);
				PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
				PreparedStatement insertHistoryStmt = conn.prepareStatement(insertHistoryQuery)) {

			updateBikeStmt.setString(1, null);
			updateBikeStmt.setInt(2, intID);

			if (updateBikeStmt.executeUpdate() > 0) {
				updateUserStmt.setInt(1, intID);
				updateUserStmt.setInt(2, userID);
				updateUserStmt.executeUpdate();

				insertHistoryStmt.setInt(1, userID);
				insertHistoryStmt.setInt(2, intID);
				insertHistoryStmt.setString(3, currentTime);
				insertHistoryStmt.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean unlinkBike(String bikeID, String currentStation, int userID) {
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String updateBikeQuery = "UPDATE bike SET inUse = 0, station = ? WHERE bikeID = ?";
		String updateUserQuery = "UPDATE user SET isLinked = 0 WHERE userID = ?";
		String updateHistoryQuery = "UPDATE history SET unlinkTime = ? WHERE userID = ? AND bikeID = ? AND unlinkTime IS NULL";

		try (PreparedStatement updateBikeStmt = conn.prepareStatement(updateBikeQuery);
				PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
				PreparedStatement updateHistoryStmt = conn.prepareStatement(updateHistoryQuery)) {

			updateBikeStmt.setString(1, currentStation);
			updateBikeStmt.setInt(2, Integer.parseInt(bikeID));

			if (updateBikeStmt.executeUpdate() > 0) {
				updateUserStmt.setInt(1, userID);
				updateUserStmt.executeUpdate();

				updateHistoryStmt.setString(1, currentTime);
				updateHistoryStmt.setInt(2, userID);
				updateHistoryStmt.setInt(3, Integer.parseInt(bikeID));
				updateHistoryStmt.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean rateBike(int bikeID, int userID, char ratingLetter) {
		// Step 1: Convert letter to numeric score
		double score = switch (Character.toUpperCase(ratingLetter)) {
		case 'A' -> 5.0;
		case 'B' -> 4.0;
		case 'C' -> 3.0;
		case 'D' -> 2.0;
		case 'F' -> 1.0;
		default -> -1;
		};

		if (score == -1) {
			System.out.println("Invalid rating letter.");
			return false;
		}

		String upsertQuery = """
				    INSERT INTO bike_rating (bikeID, userID, ratingLetter)
				    VALUES (?, ?, ?)
				    ON DUPLICATE KEY UPDATE ratingLetter = VALUES(ratingLetter)
				""";

		String averageQuery = """
				    SELECT AVG(CASE ratingLetter
				        WHEN 'A' THEN 5
				        WHEN 'B' THEN 4
				        WHEN 'C' THEN 3
				        WHEN 'D' THEN 2
				        WHEN 'F' THEN 1
				    END) AS avgRating
				    FROM bike_rating
				    WHERE bikeID = ?
				""";

		String updateBikeQuery = "UPDATE bike SET rating = ? WHERE bikeID = ?";

		try (PreparedStatement rateStmt = conn.prepareStatement(upsertQuery);
				PreparedStatement avgStmt = conn.prepareStatement(averageQuery);
				PreparedStatement updateStmt = conn.prepareStatement(updateBikeQuery)) {
			// Step 2: Insert/Update user rating
			rateStmt.setInt(1, bikeID);
			rateStmt.setInt(2, userID);
			rateStmt.setString(3, String.valueOf(ratingLetter).toUpperCase());
			rateStmt.executeUpdate();

			// Step 3: Recalculate average
			avgStmt.setInt(1, bikeID);
			ResultSet rs = avgStmt.executeQuery();

			if (rs.next()) {
				double avg = rs.getDouble("avgRating");

				// Step 4: Update bike's rating field
				// Convert numeric average to letter grade
				String letterGrade;
				if (avg >= 4.5) {
					letterGrade = "A";
				} else if (avg >= 3.5) {
					letterGrade = "B";
				} else if (avg >= 2.5) {
					letterGrade = "C";
				} else if (avg >= 1.5) {
					letterGrade = "D";
				} else {
					letterGrade = "F";
				}

				updateStmt.setString(1, letterGrade);

				updateStmt.setInt(2, bikeID);
				updateStmt.executeUpdate();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// ----------------------- REPORT CRUD -----------------------

	public boolean insertReport(int userID, String subject, String bikeID, String date, String time, String incident) {
		String insertQuery = "INSERT INTO report (userID, subject, bikeID, date, time, incident) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
			stmt.setInt(1, userID);
			stmt.setString(2, subject);
			stmt.setString(3, bikeID);
			stmt.setString(4, date);
			stmt.setString(5, time);
			stmt.setString(6, incident);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ----------------------- REPORT CRUD -----------------------

	public ArrayList<Report> getPendingReports() {
		ArrayList<Report> reportList = new ArrayList<>();

		String query = "SELECT * FROM report WHERE status = 0";

		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Report report = new Report(rs.getInt("reportID"), rs.getInt("userID"), rs.getString("subject"),
						rs.getString("bikeID"), rs.getString("date"), rs.getString("time"), rs.getString("incident"),
						rs.getInt("status"));
				reportList.add(report);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reportList;
	}

	public boolean handleReport(int reportID) {
		String updateQuery = "UPDATE report SET status = 1 WHERE reportID = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
			preparedStatement.setInt(1, reportID);
			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
