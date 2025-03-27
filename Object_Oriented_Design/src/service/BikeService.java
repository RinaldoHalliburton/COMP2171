package service;

import java.util.ArrayList;

import db.Database;

public class BikeService {

	private Database db;
	private UserService us;
	private static String linkedBike = null;
	private int userID;
	private static String previousBike = null;

	public BikeService() {
		this.db = new Database();
		this.us = new UserService();
		this.userID = us.getSessionID();
		BikeService.linkedBike = getBikeLastBike();
	}

	public ArrayList<String> getBikes(String station) {
		return db.fetchBikes(station);

	}

	private String getBikeLastBike() {
		Integer bike = db.fetchLastUserBike(userID);
		if (bike == null) {
			return null;
		}
		return bike + "";
	}

	public boolean inUse(String bikeID) {
		BikeService.linkedBike = bikeID;
		BikeService.previousBike = BikeService.linkedBike;
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

	public boolean bikeRating(char rating) {
		int previousBike;
		if (!(getBikeLastBike() == null)) {
			previousBike = Integer.parseInt(getBikeLastBike());
			return db.rateBike(previousBike, this.userID, rating);
		}
		return false;

	}

}
