package model;

public class PaymentMethod {
	private String name;
	private String cardNumber;
	private String cvv;
	private String date;
	private String address;

	public PaymentMethod(String name, String cardNumber, String cvv, String date, String address) {
		this.name = name;
		this.cardNumber = cardNumber;
		this.cvv = cvv;
		this.date = date;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCvv() {
		return cvv;
	}

	public String getDate() {
		return date;
	}

	public String getAddress() {
		return address;
	}

}
