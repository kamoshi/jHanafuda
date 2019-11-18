package gui.game;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Resolution;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {
	
	// PROPERTIES
	private final JLabel name = new JLabel("NAME");
	private final JLabel points = new JLabel("POINTS");
	
	// CONSTRUCTOR
	public PlayerPanel (Resolution res, boolean reverse) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createVerticalGlue());
		
		name.setFont(new Font("sans-serif", Font.PLAIN, res.topMargin/3));
		name.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		points.setFont(new Font("sans-serif", Font.PLAIN, res.topMargin/4));
		points.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		if (reverse) {
			this.add(points);
			this.add(name);
		} else {
			this.add(name);
			this.add(points);
		}
		
		points.add(Box.createVerticalGlue());
	}
	
	/**
	 * Updates player name displayed.
	 * @param name	The name to display.
	 */
	public void updateName(String string) {
		name.setText(string);
	}
	/**
	 * Updates points displayed for player.
	 * @param points	The number of points to display.
	 */
	public void updatePoints(int integer) {
		points.setText(Integer.toString(integer) + " " + ResourceLoader.localize("gameapp_points_suffix"));
	}
}
