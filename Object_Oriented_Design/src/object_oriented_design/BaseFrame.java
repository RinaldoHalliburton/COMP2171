package object_oriented_design;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class BaseFrame extends JFrame {

	public JFrame Frame(JFrame frame) {

		// Create a new JFrame
		frame = new JFrame("Rent-A-Bike UWI");

		// Set up the frame properties
		frame.setUndecorated(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the frame to be centered on the screen
		frame.setLocationRelativeTo(null);
		frame.setBounds(555, 50, 400, 710);
		// frame.setResizable(false);

		JScrollPane scrollPane = new JScrollPane(getContentPane());
		frame.add(scrollPane);

		return frame;

	}

}
