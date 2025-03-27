package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Report; // Assume you have a Report class with getters

public class ViewReportUI extends JFrame {

	private JButton backButton;
	private JPanel mainPanel;
	private JFrame frame;
	private BaseFrame baseframe;
	private JFrame previousFrame;
	private Report report;

	public ViewReportUI(Report report, JFrame previousFrame) {
		this.report = report;
		this.frame = new JFrame();
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this.frame);
		this.previousFrame = previousFrame;

		setupUI();

		backButton.addActionListener(e -> handleBack());
	}

	private void setupUI() {
		mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(0, 0, 245, 150));

		backButton = new JButton("Back");
		backButton.setBounds(20, 30, 100, 30);
		mainPanel.add(backButton);

		addLabel("Report ID:", String.valueOf(report.getReportID()), 160);
		addLabel("User ID:", String.valueOf(report.getUserID()), 200);
		addLabel("Subject:", report.getSubject(), 240);
		addLabel("Bike ID:", report.getBikeID(), 280);
		addLabel("Date:", report.getDate(), 320);
		addLabel("Time:", report.getTime(), 360);
		addLabel("Incident:", report.getIncident(), 400);

		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10);
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel);
		frame.setVisible(true);
	}

	private void addLabel(String labelText, String valueText, int yPos) {
		JLabel label = new JLabel(labelText);
		label.setBounds(60, yPos, 100, 30);
		label.setForeground(Color.white);
		mainPanel.add(label);

		JLabel value = new JLabel(valueText);
		value.setBounds(160, yPos, 300, 30);
		value.setForeground(Color.white);
		mainPanel.add(value);
	}

	private void handleBack() {
		frame.dispose();
		previousFrame.setVisible(true);
	}

}
