package gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import model.User;
import service.UserService;

public class ManageUsersUI extends JFrame {

	private BaseFrame baseframe;
	private JFrame frame;

	private JTable table;
	private DefaultTableModel tableModel;
	private JButton deleteButton, backButton, viewButton;
	private JTextField IDField;
	private JPanel mainPanel;
	private JFrame mainMenuFrame;
	private UserService userService;
	private ArrayList<User> userList;

	public ManageUsersUI(JFrame previousFrame) throws SQLException {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.userService = new UserService();
		mainMenuFrame = previousFrame;
		setupUI();
		loadTable();
		backButton.addActionListener(e -> {
			try {
				handleBack();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		deleteButton.addActionListener(e -> {
			try {
				handleDelete();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		viewButton.addActionListener(e -> {
			try {
				handleView();
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

		deleteButton = new JButton("Delete User");
		deleteButton.setBounds(20, 600, 110, 30);
		mainPanel.add(deleteButton);

		viewButton = new JButton("View User");
		viewButton.setBounds(265, 600, 100, 30);
		mainPanel.add(viewButton);

		JLabel IDLabel = new JLabel("Enter ID:");
		IDLabel.setBounds(60, 455, 120, 30);
		IDLabel.setForeground(Color.white);
		mainPanel.add(IDLabel);

		IDField = new JTextField();
		IDField.setBounds(150, 455, 150, 30);
		mainPanel.add(IDField);

		// Create table and scroll pane
		tableModel = new DefaultTableModel(new Object[] { "User ID", "Name" }, 0);
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 85, 350, 340);
		mainPanel.add(scrollPane);

		// Border setup
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10);
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel);
		frame.setVisible(true);

	}

	private void loadTable() throws SQLException {

		userList = userService.getUsers();

		if (userList == null || userList.isEmpty()) {
			return;
		}

		tableModel.setRowCount(0);

		for (User user : userList) {
			SwingUtilities.invokeLater(() -> {
				tableModel.addRow(new Object[] { user.getId(), user.getFname() + " " + user.getLname() });
			});

		}
	}

	private void handleBack() {
		frame.dispose();
		mainMenuFrame.setVisible(true);
	}

	private void handleDelete() throws SQLException {
		int selectedRow = table.getSelectedRow();
		boolean returnedVal = false;
		int userID = -1;

		if (selectedRow == -1) {
			String user = IDField.getText();
			if (user == null || user.trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please select a row or enter a User ID.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				userID = Integer.parseInt(user.trim());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid User ID format.", "Error", JOptionPane.ERROR_MESSAGE);
				IDField.setText("");
				return;
			}

			returnedVal = userService.removeUser(userID);
		} else {

			String id = (String) tableModel.getValueAt(selectedRow, 0);
			try {
				userID = Integer.parseInt(id.trim());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid User ID in table.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			returnedVal = userService.removeUser(userID);
		}

		if (returnedVal) {
			JOptionPane.showMessageDialog(null, "User Deleted Successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			IDField.setText("");
			if (selectedRow >= 0) {
				tableModel.removeRow(selectedRow);
			}
			loadTable();
		} else {
			JOptionPane.showMessageDialog(null, "User Not Deleted", "Failure", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void handleView() {
		int selectedRow = table.getSelectedRow();
		User userObject;

		if (selectedRow == -1) {
			String userIdText = IDField.getText().trim();
			if (userIdText.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please select a row or enter a User ID.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int userId = Integer.parseInt(userIdText);
				userObject = findUserByID(userList, userId);
				if (userObject == null) {
					JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
					IDField.setText("");
					return;
				}

				IDField.setText("");
				new ViewUserUI(userObject, this.frame);
				frame.setVisible(false);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid User ID format.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			Object idValue = tableModel.getValueAt(selectedRow, 0);
			String id = idValue != null ? idValue.toString() : "";

			int userId = Integer.parseInt(id);

			userObject = findUserByID(userList, userId);

			if (userObject == null) {
				IDField.setText("");
				JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			IDField.setText("");
			new ViewUserUI(userObject, this.frame);
			frame.setVisible(false);
		}
	}

	private User findUserByID(ArrayList<User> users, int userID) {
		for (User user : users) {
			if (user.getId() == userID) {
				return user;
			}
		}
		return null;
	}

}
