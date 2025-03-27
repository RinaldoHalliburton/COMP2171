package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import service.AdminService;
import service.UserService;

public class MainMenuUI_Admin extends JFrame {

	private BaseFrame baseframe;
	private JFrame frame;
	private JButton manageReportsButton, logoutButton, manageUserButton;

	public MainMenuUI_Admin() {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		setupUI();

		logoutButton.addActionListener(e -> {
			try {
				handleLogout();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		manageReportsButton.addActionListener(e -> {
			try {
				handleManageReport();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		manageUserButton.addActionListener(e -> {
			try {
				handleManageUser();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
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

		manageUserButton = new JButton("Manage Users");
		manageUserButton.setBounds(138, 200, 120, 30);
		mainPanel.add(manageUserButton);

		manageReportsButton = new JButton("Manage Reports");
		manageReportsButton.setBounds(133, 280, 130, 30);
		mainPanel.add(manageReportsButton);

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

	private void handleManageUser() throws SQLException {
		frame.setVisible(false);
		new ManageUsersUI(this.frame);

	}

	private void handleManageReport() throws SQLException {
		frame.setVisible(false);
		new ManageReportsUI(this.frame);

	}

}
