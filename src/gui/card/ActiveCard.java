package gui.card;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import logic.Controller;
import structure.Card;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public class ActiveCard extends JButton implements ICard {
	
	// PROPERTIES
	@SuppressWarnings("unused")
	private final Controller controller;
	private final Card card;
	@SuppressWarnings("unused")
	private final int context;
	private final int width;
	private final int height;
	
	private boolean faceup;
	
	// CONSTRUCTOR
	public ActiveCard(Controller controller, Card card, int width, int height, boolean faceup, int context) {
		this.controller = controller;
		this.card = card;
		this.context = context;
		this.width = width;
		this.height = height;
		this.faceup = faceup;
		
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		
		// Image loading ------------------
		String location;
		if (faceup) location = "cards/" + ResourceLoader.getCardSet() + "/" + card.name().toLowerCase() + ".png";
		else location = "cards/cardback.png";

		this.setIcon(ResourceLoader.requestImage(location, width, height));
		
		this.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.eventCardPressed(card, context);
			}
		});
	}
	
	// METHODS
	@Override
	public Card getCard() {
		return this.card;
	}
	
	@Override
	public void setFaceup() {
		if (this.faceup == false ) {
			this.setIcon(ResourceLoader.requestImage("cards/" + ResourceLoader.getCardSet() + "/" + card.name().toLowerCase() + ".png", width, height));
			this.faceup = true;
		}
	}
}
