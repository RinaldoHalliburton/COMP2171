package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import service.SignupService;

public class SignupUI extends JFrame {
	private BaseFrame baseframe;
	private JFrame frame;
	private SignupService signupService;
	private JPanel mainPanel;
	private JTextField lastNameField;
	private JTextField firstNameField;
	private JTextField emailField;
	private JTextField idField;
	private JPasswordField passwordField;
	private JButton confirmButton;
	private JFrame loginFrame;

	public SignupUI(JFrame previousFrame) {
		this.frame = new JFrame();
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this.frame);
		this.signupService = new SignupService();
		this.mainPanel = new JPanel();
		this.loginFrame = previousFrame;
		setup();
		confirmButton.addActionListener(e -> {
			try {
				handleConfirm();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

	}

	private void setup() {

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

		JLabel nameLabel = new JLabel("Sign Up");
		nameLabel.setBounds(150, 20, 100, 25);
		Font font = new Font("Arial", Font.BOLD, 15);
		nameLabel.setFont(font);
		nameLabel.setForeground(Color.white);

		firstNameField = new JTextField();
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setForeground(Color.white);
		firstNameLabel.setBounds(70, 60, 100, 25);
		firstNameField.setBounds(171, 60, 170, 25);

		lastNameField = new JTextField();
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setForeground(Color.white);
		lastNameLabel.setBounds(70, 115, 100, 25);
		lastNameField.setBounds(171, 115, 170, 25);

		emailField = new JTextField();
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(Color.white);
		emailLabel.setBounds(70, 170, 100, 25);
		emailField.setBounds(171, 170, 170, 25);

		idField = new JTextField();
		JLabel idLabel = new JLabel("ID Number:");
		idLabel.setForeground(Color.white);
		idLabel.setBounds(70, 225, 100, 25);
		idField.setBounds(171, 225, 170, 25);

		passwordField = new JPasswordField();
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(Color.white);
		passwordLabel.setBounds(70, 280, 100, 25);
		passwordField.setBounds(171, 280, 170, 25);

		confirmButton = new JButton("Sign Up");
		confirmButton.setBounds(150, 400, 100, 25);

		mainPanel.add(nameLabel);
		mainPanel.add(emailField);

		mainPanel.add(lastNameLabel);
		mainPanel.add(lastNameField);

		mainPanel.add(firstNameLabel);
		mainPanel.add(firstNameField);

		mainPanel.add(emailLabel);
		mainPanel.add(idField);

		mainPanel.add(idLabel);
		mainPanel.add(passwordField);

		mainPanel.add(passwordLabel);
		mainPanel.add(confirmButton);

		frame.add(mainPanel, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	private void handleConfirm() throws SQLException {
		String nameF = firstNameField.getText().trim();
		String nameL = lastNameField.getText().trim();
		String id = idField.getText().trim();
		String password = new String(passwordField.getPassword());
		String email = emailField.getText().trim();
		if (nameF.isEmpty() || nameL.isEmpty() || id.isEmpty() || password.isEmpty() || email.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			Integer.parseInt(id);
			boolean isAdded = signupService.signup(nameF, nameL, id, password, email);
			if (isAdded) {
				firstNameField.setText("");
				lastNameField.setText("");
				idField.setText("");
				emailField.setText("");
				passwordField.setText("");
				this.frame.dispose();
				loginFrame.setVisible(true);

			}

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid User ID format!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

}
