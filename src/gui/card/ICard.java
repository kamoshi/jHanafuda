package gui.card;

import structure.Card;

public interface ICard {
	
	/**
	 * Get the card instance that this GUI element represents.
	 * @return	The {@link Card} instance.
	 */
	public abstract Card getCard();
	
	/**
	 * Sets the GUI representation of a card face up.
	 */
	public abstract void setFaceup();
	
}
