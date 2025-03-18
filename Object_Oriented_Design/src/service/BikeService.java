package service;

import java.util.ArrayList;

import db.Database;

public class BikeService {

	private Database db;
	private UserService us;
	private static String linkedBike = null;
	private int userID;

	public BikeService() {
		this.db = new Database();
		this.us = new UserService();
		this.userID = us.getSessionID();
	}

	public ArrayList<String> getBikes(String station) {
		return db.fetchBikes(station);

	}

	public boolean inUse(String bikeID) {
		BikeService.linkedBike = bikeID;
		boolean value = db.linkBike(bikeID, userID);
		us.setUserisLinked(1);
		return value;
	}

	public boolean notInUse(String currentStation) {
		boolean val = db.unlinkBike(BikeService.linkedBike, currentStation, userID);
		us.setUserisLinked(0);
		setlinkedBiketoNull();
		return val;
	}

	public String getlinkedBike() {
		return linkedBike;
	}

	public void setlinkedBiketoNull() {
		linkedBike = null;
	}

}
