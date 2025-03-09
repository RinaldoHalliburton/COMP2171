package main;

import javax.swing.SwingUtilities;

import gui.LoginUI;
import service.AdminService;
import service.UserService;

public class Main {

	public static void main(String[] args) {

		// Initialize the services
		UserService userService = new UserService();
		AdminService adminService = new AdminService();
		// BikeService bikeService = new BikeService();
		// IncidentService incidentService = new IncidentService();
		// PaymentService paymentService = new PaymentService();

		// Load data for bikes (you can add more methods to load other data as
		// necessary)
		// bikeService.loadBikesFromFile("file.txt");

		// Create the Login UI and pass the services
		SwingUtilities.invokeLater(() -> {
			new LoginUI(userService, adminService);
		});

	}

}
