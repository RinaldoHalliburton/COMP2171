package gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.util.ArrayList;

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
	private JButton backButton, linkButton, unlinkButton;
	private String bikeStation;
	private ArrayList<String> bikesInStation;

	public BikeStationUI(JFrame previousFrame) {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.bikeservice = new BikeService();
		this.selectBikeUI = previousFrame;
		this.userService = new UserService();
		this.bikesInStation = new ArrayList<String>();
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
		currentStation = station;

		if (lst == null || lst.isEmpty()) {
			System.out.println("No bikes found.");
			return;
		}

		// Clear existing rows before loading new ones
		tableModel.setRowCount(0);

		for (String bike : lst) {
			String[] tableColumn = bike.split("-", 2);
			bikesInStation.add(tableColumn[0]);

			if (tableColumn.length == 2) { // Prevents ArrayIndexOutOfBoundsException
				SwingUtilities.invokeLater(() -> {
					tableModel.addRow(new Object[] { tableColumn[0], tableColumn[1] });
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
		int acceptCharge = JOptionPane.showConfirmDialog(null, "The charge is 35JMD/minute. Do you accept?", "Payment",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (userService.getUserisLinked() == 0 && acceptCharge == JOptionPane.YES_OPTION) {

			String bikeID = JOptionPane.showInputDialog(null, "Enter Bike ID:", "Link a Bike",
					JOptionPane.QUESTION_MESSAGE);
			if (bikeID == null || bikeID.trim().isEmpty()) {
				return;
			}
			if (bikesInStation.contains(bikeID)) {
				bikeservice.inUse(bikeID);
				JOptionPane.showMessageDialog(null, "✅ Bike Linked Successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					if (tableModel.getValueAt(i, 0).equals(bikeID)) { // Check if ID matches
						tableModel.removeRow(i);
						return; // Exit after removing the row
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Bike not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

		}

	}

	private void handleUnlink() {
		if (bikeservice.getlinkedBike() != null) {
			boolean value = bikeservice.notInUse(currentStation);
			loadTable(currentStation);
			if (value) {
				JOptionPane.showMessageDialog(null, "Bike sucessfully unlinked", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Link a bike first", "No bike linked", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}

}
