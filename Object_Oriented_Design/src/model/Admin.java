package model;

public class Admin {
	private int id;
	private String fname;
	private String lname;
	private String password;

	public Admin(int id, String fname, String lname, String password) {
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.password = password;

	}

	public String getPassword() {
		return this.password;
	}

}
