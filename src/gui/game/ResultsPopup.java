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
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import logic.Controller;
import utils.Resolution;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public class ResultsPopup extends JPanel {
	
	// PROPERTIES
	private final Controller controller;
	private final int frameWidth = 400;
	private final int frameHeight = 280;
	
	private final String plr1;
	private final String plr2;
	private final String score1;
	private final String score2;
	private final String titleLabel;
	
	// CONSTRUCTOR
	public ResultsPopup(Controller controller, Resolution res, String plr1, String plr2, int score1, int score2) {
		this.controller = controller;
		
		if (plr1.length() > 10) this.plr1 = plr1.substring(0, 10).concat("..."); else this.plr1 = plr1;
		if (plr2.length() > 10) this.plr2 = plr2.substring(0, 10).concat("..."); else this.plr2 = plr2;
		
		if (score1 > score2) titleLabel = this.plr1 + " " + ResourceLoader.localize("results_label_win");
			else if (score1 < score2) titleLabel = plr2 + " " + ResourceLoader.localize("results_label_win");
			else titleLabel = ResourceLoader.localize("results_label_tie");
		
		this.score1 = Integer.toString(score1) + " " + ResourceLoader.localize("gameapp_points_suffix");
		this.score2 = Integer.toString(score2) + " " + ResourceLoader.localize("gameapp_points_suffix");
		
		this.setPreferredSize(new Dimension(frameWidth, frameHeight));
		this.setLayout(new BorderLayout());
		
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
		this.setBounds((res.screenWidth - frameWidth)/2, (res.screenHeight - frameHeight)/2, frameWidth, frameHeight);
		
		initLayout();
	}

	private void initLayout() {
		
		final JPanel padding = new JPanel();
		padding.setBorder(new EmptyBorder(20, 20, 20, 20));
		padding.setLayout(new BorderLayout());
		this.add(padding);
		
		final JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());
		padding.add(wrapper);
		
		// top
		final JLabel topBar = new JLabel(titleLabel);
		wrapper.add(topBar, BorderLayout.NORTH);
		topBar.setFont(new Font("Helvetica", 40, 40));
		topBar.setHorizontalAlignment(JLabel.CENTER); 
		topBar.setPreferredSize(new Dimension(0, 40));
		
		// bottom
		final JPanel buttonContainer = new JPanel();
		wrapper.add(buttonContainer , BorderLayout.SOUTH);
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		
		final JButton finishGame = new JButton(ResourceLoader.localize("results_finish"));
		finishGame.setPreferredSize(new Dimension(100, 40));
		
		buttonContainer.add(Box.createHorizontalGlue());
		buttonContainer.add(finishGame);
		buttonContainer.add(Box.createHorizontalGlue());
		
		// center
		final JPanel innerPadding = new JPanel();
		innerPadding.setBorder(new EmptyBorder(20, 0, 10, 0));
		innerPadding.setLayout(new BorderLayout());
		wrapper.add(innerPadding, BorderLayout.CENTER);
		
		final JPanel innerWrapper = new JPanel();
		innerWrapper.setLayout(new BoxLayout(innerWrapper, BoxLayout.X_AXIS));
		innerPadding.add(innerWrapper);
		
		final JPanel leftHalf = new JPanel();
		leftHalf.setLayout(new BorderLayout());
		innerWrapper.add(leftHalf);
		
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(1,100));
		innerWrapper.add(sep);
		
		final JPanel rightHalf = new JPanel();
		rightHalf.setLayout(new BorderLayout());
		innerWrapper.add(rightHalf);
		
		final JLabel player1name = new JLabel(plr1);
		leftHalf.add(player1name, BorderLayout.NORTH);
		player1name.setHorizontalAlignment(JLabel.CENTER);
		player1name.setFont(new Font("Helvetica", 30, 30));
		final JLabel player1points = new JLabel(score1);
		leftHalf.add(player1points, BorderLayout.CENTER);
		player1points.setHorizontalAlignment(JLabel.CENTER);
		player1points.setFont(new Font("Helvetica", 20, 20));
		
		final JLabel player2name = new JLabel(plr2);
		rightHalf.add(player2name, BorderLayout.NORTH);
		player2name.setHorizontalAlignment(JLabel.CENTER);
		player2name.setFont(new Font("Helvetica", 30, 30));
		final JLabel player2points = new JLabel(score2);
		rightHalf.add(player2points, BorderLayout.CENTER);
		player2points.setHorizontalAlignment(JLabel.CENTER);
		player2points.setFont(new Font("Helvetica", 20, 20));
		
		// listeners
		finishGame.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.eventResultsClosed();
			}
		});
	}
}
