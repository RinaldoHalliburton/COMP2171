package gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import com.toedter.calendar.JDateChooser;

import service.PaymentService;

public class PaymentUI extends JFrame {

	private JTable table;
	private DefaultTableModel tableModel;
	private JButton deleteButton, backButton, addButton;
	private JTextField nameField, cardField, cvvField;
	private JDateChooser dateChooser;
	private JPanel mainPanel;
	private BaseFrame baseframe;
	private JFrame frame;
	private JFrame mainMenuFrame;
	private PaymentService paymentService;
	private JComboBox<String> monthComboBox;
	private JComboBox<Object> yearComboBox;

	public PaymentUI(JFrame previousFrame) {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.paymentService = new PaymentService();
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
			}
		});

		addButton.addActionListener(e -> {
			try {
				handleAdd();
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

		deleteButton = new JButton("Delete");
		deleteButton.setBounds(265, 34, 100, 30);
		mainPanel.add(deleteButton);

		addButton = new JButton("Add");
		addButton.setBounds(265, 600, 100, 30);
		mainPanel.add(addButton);

		JLabel nameLabel = new JLabel("Name on card:");
		nameLabel.setBounds(60, 230, 120, 30);
		nameLabel.setForeground(Color.white);
		mainPanel.add(nameLabel);

		nameField = new JTextField();
		nameField.setBounds(190, 230, 150, 30);
		mainPanel.add(nameField);

		JLabel cardLabel = new JLabel("Card number:");
		cardLabel.setBounds(60, 280, 120, 30);
		cardLabel.setForeground(Color.white);
		mainPanel.add(cardLabel);

		cardField = new JTextField();
		cardField.setBounds(190, 280, 150, 30);
		mainPanel.add(cardField);

		JLabel cvvLabel = new JLabel("CVV:");
		cvvLabel.setBounds(60, 330, 120, 30);
		cvvLabel.setForeground(Color.white);
		mainPanel.add(cvvLabel);

		cvvField = new JTextField();
		cvvField.setBounds(190, 330, 150, 30);
		mainPanel.add(cvvField);

		JLabel dateLabel = new JLabel("Expiry Date:");
		dateLabel.setBounds(60, 380, 120, 30);
		dateLabel.setForeground(Color.white);
		mainPanel.add(dateLabel);

		// Month ComboBox
		String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		monthComboBox = new JComboBox<>(months);
		monthComboBox.setBounds(190, 380, 70, 30);
		mainPanel.add(monthComboBox);

		// Year ComboBox (Current Year + Next 10 Years)
		yearComboBox = new JComboBox<>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = currentYear; i <= currentYear + 10; i++) {
			yearComboBox.addItem(i);
		}
		yearComboBox.setBounds(270, 380, 70, 30);
		mainPanel.add(yearComboBox);

		// Create table and scroll pane
		tableModel = new DefaultTableModel(new Object[] { "Name on Card", "Card Number" }, 0);
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 85, 350, 120);
		mainPanel.add(scrollPane);

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

	private void handleDelete() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a payment to delete.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String name = (String) tableModel.getValueAt(selectedRow, 0);
		String cardNumber = (String) tableModel.getValueAt(selectedRow, 1);

		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this payment?", "Confirm",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {

			boolean value = paymentService.removePaymentMethod(name, cardNumber);
			if (value) {
				JOptionPane.showMessageDialog(null, "✅ Payment Method Deleted Successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Payment Method deletion failed!", "Failure",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			tableModel.removeRow(selectedRow);
			loadTable();

		}
	}

	private void handleAdd() {

		String name = nameField.getText().trim();
		String cardNumber = cardField.getText().trim();
		String cvv = cvvField.getText().trim();
		String month = (String) monthComboBox.getSelectedItem();
		int year = (int) yearComboBox.getSelectedItem();
		String date = month + "/" + year;

		// Validate Input Fields
		if (name.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty()) {
			JOptionPane.showMessageDialog(null, "❌ Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!cardNumber.matches("\\d{13,19}")) { // Ensure card number is 13-19 digits
			JOptionPane.showMessageDialog(null, "❌ Invalid Card Number!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!cvv.matches("\\d{3,4}")) { // Ensure CVV is 3 or 4 digits
			JOptionPane.showMessageDialog(null, "❌ Invalid CVV!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		nameField.setText("");
		cardField.setText("");
		cvvField.setText("");
		boolean val = paymentService.addPaymentMethod(name, cardNumber, cvv, date);
		if (val) {
			JOptionPane.showMessageDialog(null, "✅ Payment Method Added Successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Payment Method not Added!", "Failure",
					JOptionPane.INFORMATION_MESSAGE);
		}
		loadTable();

	}

	private void loadTable() {

		ArrayList<String> lst = paymentService.getPaymentMethod();

		if (lst == null || lst.isEmpty()) {
			return;
		}

		// Clear existing rows before loading new ones
		tableModel.setRowCount(0);

		for (String payment : lst) {
			String[] parts = payment.split("-", 2);

			if (parts.length == 2) { // Prevents ArrayIndexOutOfBoundsException
				SwingUtilities.invokeLater(() -> {
					tableModel.addRow(new Object[] { parts[0], parts[1] });
				});
			}
		}
	}
}
