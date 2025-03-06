package object_oriented_design;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

	// Method to hash the password
	private String hashPassword(String password) throws NoSuchAlgorithmException {
		// Get an instance of the SHA-256 message digest algorithm
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		// Perform the hashing
		byte[] hashedBytes = digest.digest(password.getBytes());

		// Convert the byte array into a hexadecimal string representation
		StringBuilder hexString = new StringBuilder();
		for (byte b : hashedBytes) {
			hexString.append(String.format("%02x", b));
		}

		return hexString.toString();
	}

	public String toHash(String password) {
		try {
			String hashedPassword = hashPassword(password);
			return hashedPassword;
		} catch (NoSuchAlgorithmException e) {

		}
		return password;
	}

	public boolean compareHash(String password, String hashed) {
		if (toHash(password).equals(hashed)) {
			return true;
		} else {
			return false;
		}
	}
}
