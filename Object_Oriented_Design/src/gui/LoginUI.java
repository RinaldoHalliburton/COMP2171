package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import service.AdminService;
import service.LoginService;
import service.UserService;

public class LoginUI extends JFrame {

	private JTextField idField;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JCheckBox adminCheckBox;
	private JButton loginButton;
	private JButton signupButton;
	private LoginService loginService;
	private JPanel mainPanel;
	private JFrame frame;
	private BaseFrame baseframe;
	private SignupUI signupUI;
	private UserService userService;
	private AdminService adminService;

	public LoginUI(UserService userService, AdminService adminService) {
		this.loginService = new LoginService(userService, adminService);
		this.frame = new JFrame();
		this.baseframe = new BaseFrame();
		this.userService = userService;
		this.adminService = adminService;
		// Initialize UI components (idText, passwordText, loginButton, etc.)
		setupUI();

		// Add login functionality to the buttonS
		loginButton.addActionListener(e -> {
			try {
				handleLogin();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		signupButton.addActionListener(e -> {
			try {
				handleSignup();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
	}

	private void setupUI() {

		frame = baseframe.Frame(this.frame);

		// Set the frame layout to BorderLayout to place the GIF at the top
		frame.setLayout(new BorderLayout());

		// Load the animated GIF as an icon
		ImageIcon icon = new ImageIcon("landingpic.gif");
		JLabel bannerLabel = new JLabel(icon);

		// Set the preferred size of the GIF
		bannerLabel.setPreferredSize(new Dimension(400, 200));

		// Set the alignment of the GIF to be centered
		bannerLabel.setHorizontalAlignment(JLabel.CENTER);

		// Add the bannerLabel to the NORTH of the frame (Banner section)
		frame.add(bannerLabel, BorderLayout.NORTH);

		frame.setUndecorated(false);

		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(0, 0, 245, 168));
		mainPanel.setLayout(null);

		nameLabel = new JLabel("RENT-A-BIKE");
		nameLabel.setBounds(150, 30, 100, 25);
		Font font = new Font("Arial", Font.BOLD, 15);
		nameLabel.setFont(font);
		nameLabel.setForeground(Color.white);

		adminCheckBox = new JCheckBox("Admin");
		adminCheckBox.setBounds(140, 110, 100, 25);
		adminCheckBox.setBackground(new Color(0, 0, 245, 0));
		adminCheckBox.setForeground(Color.BLACK);

		idField = new JTextField();
		idLabel = new JLabel("UWI ID:");
		idLabel.setForeground(Color.white);
		idLabel.setBounds(70, 190, 100, 25);
		idField.setBounds(171, 190, 170, 25);

		passwordField = new JPasswordField();
		passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(Color.white);
		passwordLabel.setBounds(70, 240, 100, 25);
		passwordField.setBounds(171, 240, 170, 25);

		loginButton = new JButton("Login");
		loginButton.setBounds(150, 340, 100, 25);

		signupButton = new JButton("Sign Up");
		signupButton.setBounds(150, 390, 100, 25);

		mainPanel.add(nameLabel);
		mainPanel.add(adminCheckBox);
		mainPanel.add(idField);
		mainPanel.add(passwordField);
		mainPanel.add(passwordLabel);
		mainPanel.add(idLabel);
		mainPanel.add(loginButton);
		mainPanel.add(signupButton);

		frame.add(mainPanel, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	private void handleLogin() throws HeadlessException, SQLException {
		String id = idField.getText().trim();
		String password = new String(passwordField.getPassword());
		int idInt = 0;
		boolean isAdmin = adminCheckBox.isSelected();
		try {
			idInt = Integer.parseInt(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (id.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both ID and password.", "Input Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (loginService.login(id, password, isAdmin)) {
			if (isAdmin) {
				// Open Admin page
				idField.setText("");
				passwordField.setText("");
				isAdmin = false;
				frame.dispose();
				new MainMenuUI_Admin();
			} else {
				// Open User page
				idField.setText("");
				passwordField.setText("");
				frame.dispose();
				userService.setSessionID(idInt);
				new MainMenuUI_User();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Account does not exist", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	private void handleSignup() throws HeadlessException, SQLException {

		frame.setVisible(false);
		signupUI = new SignupUI(frame);
		return;

	}

}
