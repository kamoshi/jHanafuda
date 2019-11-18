package gui.card;

import java.awt.Dimension;

import javax.swing.JLabel;

import structure.Card;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public class PassiveCard extends JLabel implements ICard {
	
	// PROPERTIES
	private final Card card;
	private final int width;
	private final int height;
	
	private boolean faceup;
	
	// CONSTRUCTOR
	public PassiveCard(Card card, int width, int height, boolean faceup) {
		this.card = card;
		this.width = width;
		this.height = height;
		this.faceup = faceup;
		
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		
		this.setOpaque(true);
		
		// Image loading ------------------
		String location;
		if (faceup) location = "cards/" + ResourceLoader.getCardSet() + "/" + card.name().toLowerCase() + ".png";
		else location = "cards/cardback.png";

		this.setIcon(ResourceLoader.requestImage(location, width, height));
	}

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
