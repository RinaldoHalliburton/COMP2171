package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class SelectBikeUI extends JFrame {
	private BaseFrame baseframe;
	private JFrame frame;
	private JButton backButton, backGate, humanities, medSci, assemblyHall, tent, sciTech, law, business, scoSci, union;
	JFrame mainMenuFrame;

	public SelectBikeUI(JFrame previousFrame) {

		this.baseframe = new BaseFrame();
		this.frame = baseframe.Frame(this);
		this.mainMenuFrame = previousFrame;
		setupUI();
		backButton.addActionListener(e -> {
			try {
				handleBack();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		backGate.addActionListener(e -> {
			try {
				handleStation("Back gate");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		humanities.addActionListener(e -> {
			try {
				handleStation("Humanities");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		medSci.addActionListener(e -> {
			try {
				handleStation("Medical science");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		assemblyHall.addActionListener(e -> {
			try {
				handleStation("Assembly hall");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		tent.addActionListener(e -> {
			try {
				handleStation("Tent");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		sciTech.addActionListener(e -> {
			try {
				handleStation("Science and Technology");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		law.addActionListener(e -> {
			try {
				handleStation("Law");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		business.addActionListener(e -> {
			try {
				handleStation("Business");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		scoSci.addActionListener(e -> {
			try {
				handleStation("Social science");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

		union.addActionListener(e -> {
			try {
				handleStation("Union");
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			}
		});

	}

	private void setupUI() {
		frame.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(0, 0, 245, 150)); // Inner background is white

		// Create borders
		Border outerBorder = BorderFactory.createLineBorder(new Color(211, 210, 214), 10); // Outer blue border (10px)
		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10); // Inner white space (10px)

		backButton = new JButton("Back");
		backButton.setBounds(20, 34, 100, 30);
		mainPanel.add(backButton);

		backGate = new JButton("Back gate");
		backGate.setBounds(59, 120, 100, 30);
		mainPanel.add(backGate);

		humanities = new JButton("Humanities");
		humanities.setBounds(59, 200, 100, 30);
		mainPanel.add(humanities);

		medSci = new JButton("Med. Sci.");
		medSci.setBounds(59, 280, 100, 30);
		mainPanel.add(medSci);

		assemblyHall = new JButton("Assembly Hall");
		assemblyHall.setBounds(59, 360, 112, 30);
		mainPanel.add(assemblyHall);

		tent = new JButton("Tent");
		tent.setBounds(59, 440, 100, 30);
		mainPanel.add(tent);

		law = new JButton("Law");
		law.setBounds(230, 120, 100, 30);
		mainPanel.add(law);

		sciTech = new JButton("Sci. Tech.");
		sciTech.setBounds(230, 200, 100, 30);
		mainPanel.add(sciTech);

		business = new JButton("Business");
		business.setBounds(230, 280, 100, 30);
		mainPanel.add(business);

		scoSci = new JButton("Sco. Sci.");
		scoSci.setBounds(230, 360, 100, 30);
		mainPanel.add(scoSci);

		union = new JButton("Union");
		union.setBounds(230, 440, 100, 30);
		mainPanel.add(union);

		// Combine borders
		mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private void handleBack() {
		frame.dispose();
		mainMenuFrame.setVisible(true);
	}

	public void handleStation(String station) {
		switch (station) {
		case "Back gate":
			new BikeStationUI(this.frame).loadTable("Back gate");
			frame.setVisible(false);
			break;
		case "Humanities":
			new BikeStationUI(this.frame).loadTable("Humanities");
			frame.setVisible(false);
			break;
		case "Medical science":
			new BikeStationUI(this.frame).loadTable("Medical science");
			frame.setVisible(false);
			break;
		case "Assembly hall":
			new BikeStationUI(this.frame).loadTable("Assembly hall");
			frame.setVisible(false);
			break;
		case "Tent":
			new BikeStationUI(this.frame).loadTable("Tent");
			frame.setVisible(false);
			break;
		case "Law":
			new BikeStationUI(this.frame).loadTable("Law");
			frame.setVisible(false);
			break;
		case "Science and Technology":
			new BikeStationUI(this.frame).loadTable("Science and Technology");
			frame.setVisible(false);
			break;
		case "Business":
			new BikeStationUI(this.frame).loadTable("Business");
			frame.setVisible(false);
			break;
		case "Social science":
			new BikeStationUI(this.frame).loadTable("Social science");
			frame.setVisible(false);
			break;
		case "Union":
			new BikeStationUI(this.frame).loadTable("Union");
			frame.setVisible(false);
			break;
		default:
			;
		}
	}

}
