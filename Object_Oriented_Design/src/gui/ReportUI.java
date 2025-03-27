package gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import service.ReportService;

public class ReportUI extends JFrame {

	private JButton backButton, submitButton;
	private JTextField subjectField, bikeField;
	private JTextArea incidentTextArea;
	private JPanel mainPanel;
	private BaseFrame baseframe;
	private JFrame frame;
	private JFrame mainMenuFrame;
	private JComboBox<String> monthComboBox, dayComboBox, hourComboBox, minuteComboBox, amPmComboBox;
	private JComboBox<Integer> yearComboBox;
	private ReportService reportService;

	public ReportUI(JFrame previousFrame) {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.reportService = new ReportService();
		this.mainMenuFrame = previousFrame;
		setupUI();
		submitButton.addActionListener(e -> {
			try {
				handleSubmit();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});
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

		submitButton = new JButton("Submit");
		submitButton.setBounds(265, 600, 100, 30);
		mainPanel.add(submitButton);

		JLabel subjectLabel = new JLabel("Enter subject:");
		subjectLabel.setBounds(60, 140, 120, 30);
		subjectLabel.setForeground(Color.white);
		mainPanel.add(subjectLabel);

		subjectField = new JTextField();
		subjectField.setBounds(190, 140, 150, 30);
		mainPanel.add(subjectField);

		JLabel bikeLabel = new JLabel("Bike ID:");
		bikeLabel.setBounds(60, 200, 120, 30);
		bikeLabel.setForeground(Color.white);
		mainPanel.add(bikeLabel);

		bikeField = new JTextField();
		bikeField.setBounds(190, 200, 150, 30);
		mainPanel.add(bikeField);

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setBounds(60, 260, 120, 30);
		dateLabel.setForeground(Color.white);
		mainPanel.add(dateLabel);

		// Month ComboBox
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		monthComboBox = new JComboBox<>(months);
		monthComboBox.setBounds(100, 260, 70, 30);
		mainPanel.add(monthComboBox);

		// Day ComboBox
		dayComboBox = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayComboBox.addItem(String.format("%02d", i));
		}
		dayComboBox.setBounds(180, 260, 70, 30);
		mainPanel.add(dayComboBox);

		// Year ComboBox (Current Year to 5 Years Back)
		yearComboBox = new JComboBox<>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = currentYear; i >= currentYear - 2; i--) {
			yearComboBox.addItem(i);
		}
		yearComboBox.setBounds(260, 260, 80, 30);
		mainPanel.add(yearComboBox);

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setBounds(60, 320, 120, 30);
		timeLabel.setForeground(Color.white);
		mainPanel.add(timeLabel);

		// Hour ComboBox (12-hour format)
		hourComboBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			hourComboBox.addItem(String.format("%02d", i));
		}
		hourComboBox.setBounds(100, 320, 70, 30);
		mainPanel.add(hourComboBox);

		// Minute ComboBox
		minuteComboBox = new JComboBox<>();
		for (int i = 0; i < 60; i++) {
			minuteComboBox.addItem(String.format("%02d", i));
		}
		minuteComboBox.setBounds(180, 320, 70, 30);
		mainPanel.add(minuteComboBox);

		// AM/PM ComboBox
		String[] amPmOptions = { "AM", "PM" };
		amPmComboBox = new JComboBox<>(amPmOptions);
		amPmComboBox.setBounds(260, 320, 70, 30);
		mainPanel.add(amPmComboBox);

		JLabel incidentLabel = new JLabel("Incident:");
		incidentLabel.setBounds(60, 380, 120, 30);
		incidentLabel.setForeground(Color.white);
		mainPanel.add(incidentLabel);

		// Incident TextArea
		incidentTextArea = new JTextArea("Describe the incident here...");
		incidentTextArea.setLineWrap(true);
		incidentTextArea.setWrapStyleWord(true);

		// Focus listener for placeholder behavior
		incidentTextArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (incidentTextArea.getText().equals("Describe the incident here...")) {
					incidentTextArea.setText("");
					incidentTextArea.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (incidentTextArea.getText().trim().isEmpty()) {
					incidentTextArea.setText("Describe the incident here...");
					incidentTextArea.setForeground(Color.GRAY);
				}
			}
		});

		JScrollPane incidentScrollPane = new JScrollPane(incidentTextArea);
		incidentScrollPane.setBounds(60, 420, 300, 160);
		mainPanel.add(incidentScrollPane);

		// Border setup
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10);
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel);
		frame.setVisible(true);
	}

	private void handleSubmit() {
		// Collect data from fields
		String subject = subjectField.getText();

		String bikeID = bikeField.getText();

		String month = (String) monthComboBox.getSelectedItem();
		String day = (String) dayComboBox.getSelectedItem();
		int year = (int) yearComboBox.getSelectedItem();
		String date = month + " " + day + ", " + year;

		String hour = (String) hourComboBox.getSelectedItem();
		String minute = (String) minuteComboBox.getSelectedItem();
		String amPm = (String) amPmComboBox.getSelectedItem();
		String time = hour + ":" + minute + "" + amPm;

		String incidentDescription = incidentTextArea.getText();
		if (subject.isEmpty() || bikeID.isEmpty() || incidentDescription.isEmpty()
				|| incidentDescription.equals("Describe the incident here...")) {

			JOptionPane.showMessageDialog(null, "Fill out all fields", "Empty Fields", JOptionPane.INFORMATION_MESSAGE);

		} else {

			boolean value = reportService.addReport(subject, bikeID, date, time, incidentDescription);
			if (value) {
				JOptionPane.showMessageDialog(null, "Report submitted.", "Success", JOptionPane.INFORMATION_MESSAGE);
				subjectField.setText("");
				bikeField.setText("");
				incidentTextArea.setText("");
			} else {
				JOptionPane.showMessageDialog(null, "Report submission failed.", "Failure",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

	}

	private void handleBack() {
		frame.dispose();
		mainMenuFrame.setVisible(true);
	}

}
