package gui.card;

import logic.Controller;
import structure.Card;

public class CardFactory {
	
	// creation modes
	public enum Mode{
		Passive,
		Active
	}
	
	// Methods
	public ICard createCard(Mode mode, Controller controller, Card card, int cardWidth, int cardHeight, boolean faceup, int context) {
		switch (mode) {
			case Passive:	return new PassiveCard(card, cardWidth, cardHeight, faceup);
			case Active:	return new ActiveCard(controller, card, cardWidth, cardHeight, faceup, context);
			default:		throw new RuntimeException("Undefined type");
		}
	}
}
