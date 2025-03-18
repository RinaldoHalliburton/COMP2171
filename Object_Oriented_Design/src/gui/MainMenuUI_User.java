package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import security.PasswordHasher;
import service.AdminService;
import service.UserService;

public class MainMenuUI_User extends JFrame {

	private BaseFrame baseframe;
	private JFrame frame;
	private JButton logoutButton, paymentButton, selectBikeButton, deleteAccountButton, reportButton, rateButton;
	private UserService us;
	private PasswordHasher ps;

	public MainMenuUI_User() {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.us = new UserService();
		this.ps = new PasswordHasher();
		setupUI();

		logoutButton.addActionListener(e -> {
			try {
				handleLogout();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		paymentButton.addActionListener(e -> {
			try {
				handlePayment();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		selectBikeButton.addActionListener(e -> {
			try {
				handleSelectBike();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		deleteAccountButton.addActionListener(e -> {
			try {
				handleDelete();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

	}

	private void setupUI() {

		frame.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(0, 0, 245, 150)); // Inner background is white

		// Create borders
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10); // Outer blue border (10px)
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10); // Inner white space (10px)

		logoutButton = new JButton("Logout");
		logoutButton.setBounds(20, 34, 100, 30);
		mainPanel.add(logoutButton);

		paymentButton = new JButton("Payment");
		paymentButton.setBounds(140, 120, 100, 30);
		mainPanel.add(paymentButton);

		selectBikeButton = new JButton("Select Bike");
		selectBikeButton.setBounds(140, 200, 100, 30);
		mainPanel.add(selectBikeButton);

		rateButton = new JButton("Rate Bike");
		rateButton.setBounds(140, 280, 100, 30);
		mainPanel.add(rateButton);

		reportButton = new JButton("Report Incident");
		reportButton.setBounds(118, 360, 143, 30);
		mainPanel.add(reportButton);

		deleteAccountButton = new JButton("Delete Account");
		deleteAccountButton.setBounds(118, 440, 143, 30);
		mainPanel.add(deleteAccountButton);

		// Combine borders
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setVisible(true);

	}

	private void handleLogout() {

		frame.dispose();
		UserService userService = new UserService();
		AdminService adminService = new AdminService();
		SwingUtilities.invokeLater(() -> {
			new LoginUI(userService, adminService);
		});

	}

	private void handlePayment() {

		frame.setVisible(false);
		new PaymentUI(this.frame);

	}

	private void handleSelectBike() {

		if (us.getSessionPayment() == 0) {
			JOptionPane.showMessageDialog(null, "Add a payment first", "Payment Needed",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			frame.setVisible(false);
			new SelectBikeUI(this.frame);
		}

	}

	private void handleDelete() {
		String password = JOptionPane.showInputDialog(null, "Enter password", "Delete Account",
				JOptionPane.QUESTION_MESSAGE);
		if (password == null || password.trim().isEmpty()) {
			return;
		}

		boolean val = us.removeUser();

		if (ps.compareHash(password, us.getUserSessionPassword()) && val) {
			JOptionPane.showMessageDialog(null, "User deleted successfully.", "User Deleted",
					JOptionPane.INFORMATION_MESSAGE);
			frame.dispose();
			new LoginUI(new UserService(), new AdminService());
		} else {
			JOptionPane.showMessageDialog(null, "Password incorrect", "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

	}

}
