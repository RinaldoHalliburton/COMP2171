package gui;

import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import service.BikeService;

public class RatingUI extends JFrame {

	private JPanel mainPanel;
	private BaseFrame baseframe;
	private JFrame frame;
	private JFrame mainMenuFrame;
	private JButton backButton, submitButton;
	private JComboBox<String> rateBox;
	private BikeService bikeService;

	public RatingUI(JFrame previousFrame) {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.mainMenuFrame = previousFrame;
		this.bikeService = new BikeService();
		setupUI();
		backButton.addActionListener(e -> {
			try {
				handleBack();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		submitButton.addActionListener(e -> {
			try {
				handleSubmit();
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

		submitButton = new JButton("Submit");
		submitButton.setBounds(150, 200, 100, 30);
		mainPanel.add(submitButton);

		JLabel rateLabel = new JLabel("Enter a rating for previous bike:");
		rateLabel.setBounds(60, 140, 200, 30);
		rateLabel.setForeground(Color.white);
		mainPanel.add(rateLabel);

		// Rate ComboBox
		String[] rate = { "A", "B", "C", "D", "E", "F" };
		rateBox = new JComboBox<>(rate);
		rateBox.setBounds(300, 140, 70, 30);
		mainPanel.add(rateBox);

		// Border setup
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10);
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel);
		frame.setVisible(true);
	}

	private void handleBack() {
		frame.dispose();
		mainMenuFrame.setVisible(true);

	}

	private void handleSubmit() {
		String selected = (String) rateBox.getSelectedItem();
		char rate = selected.charAt(0);
		boolean value = bikeService.bikeRating(rate);
		if (value) {
			JOptionPane.showMessageDialog(null, "Rating submitted.", "Success", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Use a bike before you can rate.", "Failure",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

	}

}
