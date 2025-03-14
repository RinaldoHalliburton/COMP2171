package service;

import java.util.ArrayList;

import db.Database;

public class BikeService {

	private Database db;
	private UserService us;
	private static String linkedBike = null;

	public BikeService() {
		this.db = new Database();
		this.us = new UserService();
	}

	public ArrayList<String> getBikes(String station) {
		return db.fetchBikes(station);

	}

	public void inUse(String id) {
		BikeService.linkedBike = id;
		System.out.println(BikeService.linkedBike);
		db.linkBike(id);
	}

	public void notInUse(String currentStation) {
		db.unlinkBike(BikeService.linkedBike, currentStation);
	}

	public String getlinkedBike() {
		return linkedBike;
	}

	public void setlinkedBiketoNull() {
		linkedBike = null;
	}

}
