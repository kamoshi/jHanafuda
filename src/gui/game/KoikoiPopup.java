package gui.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Controller;
import utils.Resolution;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public class KoikoiPopup extends JPanel {
	
	// PROPERTIES
	private final Controller controller;
	private final int frameWidth = 350;
	private final int frameHeight = 150;
	private final boolean active;
	
	private final JLabel infoLabel = new JLabel();
	
	// CONSTRUCTOR
	public KoikoiPopup(Controller controller, Resolution res, String plr, String yaku, int score, boolean active) {
		this.controller = controller;
		this.active = active;
		this.setPreferredSize(new Dimension(frameWidth, frameHeight));
		this.setLayout(new BorderLayout());
		
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
		this.setBounds((res.screenWidth - frameWidth)/2, (res.screenHeight - frameHeight)/2, frameWidth, frameHeight);
		infoLabel.setText(plr + ": " + ResourceLoader.localize("koikoi_" + yaku.toLowerCase()) + " - " + score);
		
		initLayout();
	}
	
	private void initLayout() {
		
		final JPanel padding = new JPanel();
		padding.setBorder(new EmptyBorder(10, 10, 10, 10));
		padding.setLayout(new BorderLayout());
		this.add(padding);
		
		final JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());
		padding.add(wrapper);
		
		// top
		final JLabel titleLabel = new JLabel(ResourceLoader.localize("koikoi_question"));
		wrapper.add(titleLabel, BorderLayout.NORTH);
		titleLabel.setPreferredSize(new Dimension(0, 40));
		titleLabel.setFont(new Font("Helvetica", 20, 20));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// bottom
		final JPanel buttonContainer = new JPanel();
		wrapper.add(buttonContainer , BorderLayout.SOUTH);
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		
		final JButton accept = new JButton(ResourceLoader.localize("koikoi_yes"));
		accept.setPreferredSize(new Dimension(200, 40));
		final JButton refuse = new JButton(ResourceLoader.localize("koikoi_no"));
		refuse.setPreferredSize(new Dimension(200, 40));
		
		accept.setEnabled(!active);
		refuse.setEnabled(!active);
		
		buttonContainer.add(accept);
		buttonContainer.add(Box.createHorizontalStrut(10));
		buttonContainer.add(refuse);
		
		
		// center
		infoLabel.setFont(new Font("Helvetica", 16, 16));
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		wrapper.add(infoLabel, BorderLayout.CENTER);
		
		
		// listeners
		accept.addActionListener( new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        controller.eventKoikoiAnswer(true);
		    }
		});
		refuse.addActionListener( new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        controller.eventKoikoiAnswer(false);
		    }
		});
		
	}
}