package service;

import java.util.ArrayList;

import db.Database;

public class PaymentService {

	private Database db;
	private UserService userService;
	private int id;

	public PaymentService() {
		db = new Database();
		userService = new UserService();

	}

	public ArrayList<String> getPaymentMethod() {
		this.id = userService.getSessionID();
		return db.fetchPaymentMethod(this.id);

	}

	public boolean removePaymentMethod(String name, String cardNumber) {
		this.id = userService.getSessionID();
		return db.deletePaymentMethod(name, cardNumber, id);
	}

	public boolean addPaymentMethod(String name, String cardNumber, String cvv, String date) {
		this.id = userService.getSessionID();
		boolean value = db.insertPaymentMethod(name, id, cardNumber, cvv, date);
		userService.setSessionPayment(1);
		return value;

	}

}
