package service;

import java.sql.SQLException;
import java.util.ArrayList;

import db.Database;
import model.Report;

public class ReportService {
	private UserService userService;
	private Database db;

	public ReportService() {
		this.userService = new UserService();
		this.db = new Database();
	}

	public boolean addReport(String subject, String bike, String date, String time, String incident) {
		return db.insertReport(userService.getSessionID(), subject, bike, date, time, incident);
	}

	public ArrayList<Report> getReports() throws SQLException {
		return db.getPendingReports();
	}

	public boolean resolveReport(int reportID) {
		return db.handleReport(reportID);
	}

}
