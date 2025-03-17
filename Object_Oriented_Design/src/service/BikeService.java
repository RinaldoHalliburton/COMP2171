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

	public void inUse(String bikeID) {
		BikeService.linkedBike = bikeID;
		db.linkBike(bikeID, userID);
		us.setUserisLinked(1);
	}

	public void notInUse(String currentStation) {
		db.unlinkBike(BikeService.linkedBike, currentStation, userID);
		us.setUserisLinked(0);
		setlinkedBiketoNull();
	}

	public String getlinkedBike() {
		return linkedBike;
	}

	public void setlinkedBiketoNull() {
		linkedBike = null;
	}

}
