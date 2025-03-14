package gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import service.BikeService;
import service.UserService;

public class BikeStationUI extends JFrame {

	private JFrame frame, selectBikeUI;
	private BaseFrame baseframe;
	private JTable table;
	private BikeService bikeservice;
	private DefaultTableModel tableModel;
	private JPanel mainPanel;
	private UserService userService;
	private String currentStation;
	JButton backButton, linkButton, unlinkButton;

	public BikeStationUI(JFrame previousFrame) {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.bikeservice = new BikeService();
		this.selectBikeUI = previousFrame;
		this.userService = new UserService();
		setupUI();
		// loadTable();
		backButton.addActionListener(e -> {
			try {
				handleBack();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		linkButton.addActionListener(e -> {
			try {
				handleLink();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		unlinkButton.addActionListener(e -> {
			try {
				handleUnlink();
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

		linkButton = new JButton("Link bike");
		linkButton.setBounds(59, 500, 100, 30);
		mainPanel.add(linkButton);

		unlinkButton = new JButton("Unlink bike");
		unlinkButton.setBounds(230, 500, 100, 30);
		mainPanel.add(unlinkButton);

		// Create table and scroll pane
		tableModel = new DefaultTableModel(new Object[] { "Bike ID", "Bike Rating" }, 0);
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 85, 350, 400);
		mainPanel.add(scrollPane);

		// Border setup
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10);
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel);
		frame.setVisible(true);
	}

	public void loadTable(String station) {

		ArrayList<String> lst = bikeservice.getBikes(station);
		lst.sort(Comparator.comparingInt(s -> Integer.parseInt(s.split("-")[0])));
		currentStation = station;

		if (lst == null || lst.isEmpty()) {
			System.out.println("No bikes found.");
			return;
		}

		// Clear existing rows before loading new ones
		tableModel.setRowCount(0);

		for (String bike : lst) {
			String[] parts = bike.split("-", 2);

			if (parts.length == 2) { // Prevents ArrayIndexOutOfBoundsException
				SwingUtilities.invokeLater(() -> {
					tableModel.addRow(new Object[] { parts[0], parts[1] });
				});
			} else {
				System.out.println("⚠️ Skipping invalid entry: " + bike);
			}
		}
	}

	private void handleBack() {
		frame.dispose();
		selectBikeUI.setVisible(true);

	}

	private void handleLink() {
		if (userService.getUserisLinked() == 1) {
			JOptionPane.showMessageDialog(null, "Unlink bike before linking another", "Unlink bike",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if (userService.getUserisLinked() == 0) {

			String bikeID = JOptionPane.showInputDialog(null, "Enter Bike ID:", "Link a Bikes",
					JOptionPane.QUESTION_MESSAGE);
			if (bikeID == null || bikeID.trim().isEmpty()) {
				return;
			}
			bikeservice.inUse(bikeID);
			loadTable(currentStation);

		}

	}

	private void handleUnlink() {
		if (bikeservice.getlinkedBike() != null) {
			bikeservice.notInUse(currentStation);
			loadTable(currentStation);
		}
	}

}
