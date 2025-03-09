package model;

public class User {
	private String fname;
	private String lname;
	private String id;
	private String email;
	private String password;

	public User(String id, String fname, String lname, String email, String password) {
		this.fname = fname;
		this.lname = lname;
		this.id = id;
		this.email = email;
		this.password = password;
	}

	// Getter and Setter methods for each field
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
