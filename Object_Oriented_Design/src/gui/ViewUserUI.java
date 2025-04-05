package gui;

import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.User;
import service.PaymentService;

public class ViewUserUI extends JFrame {

	private PaymentService paymentService;
	private JButton backButton;
	private JPanel mainPanel;
	private BaseFrame baseframe;
	private JFrame frame;
	private JFrame usersMenuFrame;
	private User user;

	public ViewUserUI(User user, JFrame previousFrame) {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.paymentService = new PaymentService();
		this.user = user;
		this.usersMenuFrame = previousFrame;
		setupUI();
		backButton.addActionListener(e -> {
			try {
				handleBack();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});
	}

	private void setupUI() {
		mainPanel = new JPanel(null); // Use absolute positioning
		mainPanel.setBackground(new Color(0, 0, 245, 150));

		backButton = new JButton("Back");
		backButton.setBounds(20, 34, 100, 30);
		mainPanel.add(backButton);

		JLabel IDLabel = new JLabel("User ID:");
		IDLabel.setBounds(60, 160, 50, 30);
		IDLabel.setForeground(Color.white);
		mainPanel.add(IDLabel);

		JLabel ID = new JLabel(user.getId() + "");
		ID.setBounds(120, 160, 200, 30);
		ID.setForeground(Color.white);
		mainPanel.add(ID);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setBounds(60, 210, 120, 30);
		nameLabel.setForeground(Color.white);
		mainPanel.add(nameLabel);

		JLabel name = new JLabel(user.getFname() + " " + user.getLname());
		name.setBounds(120, 210, 120, 30);
		name.setForeground(Color.white);
		mainPanel.add(name);

		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setBounds(60, 260, 140, 30);
		emailLabel.setForeground(Color.white);
		mainPanel.add(emailLabel);

		JLabel email = new JLabel(user.getEmail());
		email.setBounds(120, 260, 200, 30);
		email.setForeground(Color.white);
		mainPanel.add(email);

		JLabel lastBikeLabel = new JLabel("Last Bike:");
		lastBikeLabel.setBounds(60, 310, 120, 30);
		lastBikeLabel.setForeground(Color.white);
		mainPanel.add(lastBikeLabel);

		JLabel lastBike = new JLabel(user.getLastBike());
		lastBike.setBounds(120, 310, 120, 30);
		lastBike.setForeground(Color.white);
		mainPanel.add(lastBike);

		// Border setup
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10);
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel);
		frame.setVisible(true);
	}

	private void handleBack() {
		frame.dispose();
		usersMenuFrame.setVisible(true);
	}

}
