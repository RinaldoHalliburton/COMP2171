package model;

public class Report {
	private int reportID;
	private int userID;
	private String subject;
	private String bikeID;
	private String date;
	private String time;
	private String incident;
	private int status;

	public Report(int reportID, int userID, String subject, String bikeID, String date, String time, String incident,
			int status) {
		this.reportID = reportID;
		this.userID = userID;
		this.subject = subject;
		this.bikeID = bikeID;
		this.date = date;
		this.time = time;
		this.incident = incident;
		this.status = status;
	}

	public int getReportID() {
		return reportID;
	}

	public int getUserID() {
		return userID;
	}

	public String getSubject() {
		return subject;
	}

	public String getBikeID() {
		return bikeID;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getIncident() {
		return incident;
	}

	public void setIncident(String incident) {
		this.incident = incident;
	}

	public int isStatus() {
		return status;
	}

}
