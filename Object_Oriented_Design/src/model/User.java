package model;

public class User {
	private String fname;
	private String lname;
	private int id;
	private String email;
	private String password;
	private String lastBike;

	public User(int id, String fname, String lname, String email, String password) {
		this.fname = fname;
		this.lname = lname;
		this.id = id;
		this.email = email;
		this.password = password;

	}

	// public User(int id, String fname, String lname, String email, String
	// lastBike) {
	/*
	 * this.fname = fname; this.lname = lname; this.id = id + ""; this.email =
	 * email; this.lastBike = lastBike;
	 * 
	 * }
	 */

	// Getter methods for each field
	public String getLastBike() {
		return lastBike;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
