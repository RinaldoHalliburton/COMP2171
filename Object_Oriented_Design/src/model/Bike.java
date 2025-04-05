package model;

public class Bike {

	private int bikeID;
	private String rating;
	private boolean inUse;
	private String station;

	public Bike(int id, String rating, boolean inUse, String station) {
		this.bikeID = id;
		this.rating = rating;
		this.inUse = inUse;
		this.station = station;
	}

	public int getBikeID() {
		return bikeID;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

}
