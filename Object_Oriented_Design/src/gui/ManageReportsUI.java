package gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.SQLException;
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

import model.Report;
import service.ReportService;

public class ManageReportsUI extends JFrame {

	private JTable table;
	private DefaultTableModel tableModel;
	private JButton resolveButton, backButton, viewButton;
	private JPanel mainPanel;
	private BaseFrame baseframe;
	private JFrame frame;
	private JFrame mainMenuFrame;
	private ReportService reportService;
	ArrayList<Report> reportList;

	public ManageReportsUI(JFrame previousFrame) throws SQLException {
		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.reportService = new ReportService();
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
		resolveButton.addActionListener(e -> {
			try {
				handleResolve();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
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

		resolveButton = new JButton("Resolve");
		resolveButton.setBounds(265, 34, 100, 30);
		mainPanel.add(resolveButton);

		viewButton = new JButton("View");
		viewButton.setBounds(265, 600, 100, 30);
		mainPanel.add(viewButton);

		// Create table and scroll pane
		tableModel = new DefaultTableModel(new Object[] { "Case", "User ID", "Subject" }, 0);
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

	private void loadTable() throws SQLException {

		reportList = reportService.getReports();

		if (reportList == null || reportList.isEmpty()) {
			return;
		}

		// Clear existing rows before loading new ones
		tableModel.setRowCount(0);

		for (Report report : reportList) {
			SwingUtilities.invokeLater(() -> {
				tableModel.addRow(new Object[] { report.getReportID(), report.getUserID(), report.getSubject() });
			});

		}
	}

	private void handleBack() {
		frame.dispose();
		mainMenuFrame.setVisible(true);
	}

	private void handleResolve() throws SQLException {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {
			int caseId = (int) tableModel.getValueAt(selectedRow, 0);
			String pass = String.format("Case %s resolved.", caseId);
			String fail = String.format("Case %s was not resolved.", caseId);
			if (reportService.resolveReport(caseId))

			{
				JOptionPane.showMessageDialog(null, pass, "Success", JOptionPane.INFORMATION_MESSAGE);
				loadTable();
				return;
			} else {
				JOptionPane.showMessageDialog(null, fail, "Failed", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

	}

	public void handleView() {
		int selectedRow = table.getSelectedRow();
		Report report = null;
		if (selectedRow >= 0) {
			int caseId = (int) tableModel.getValueAt(selectedRow, 0);
			report = findReportByCase(reportList, caseId);
			if (report != null) {
				frame.setVisible(false);
				new ViewReportUI(report, this.frame);
			}

		} else {
			JOptionPane.showMessageDialog(frame, "Please select a row if present.");
		}

	}

	private Report findReportByCase(ArrayList<Report> reports, int caseId) {
		for (Report report : reports) {
			if (report.getReportID() == caseId) {
				return report;
			}
		}
		return null;
	}

}
