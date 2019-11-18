package ai;

import logic.Controller;
import structure.Card;

public class SimpleAI implements IPerson {
	
	// PROPERTIES
	private final Controller controller;
	
	// CONSTRUCTOR
	public SimpleAI(Controller controller) {
		this.controller = controller;
	}
	
	// METHODS
	@Override
	public void askParentMove(Card[] table, Card cardAlreadyTaken) {
		final int choice = (int)(Math.random() * table.length);
		Card chosenCard = table[choice];
		
		if (chosenCard.equals(cardAlreadyTaken)) {
			if (choice < 5) {
				chosenCard = table[choice+1];
			} else {
				chosenCard = table[choice-1];
			}
		}
		
		controller.delayedAICardAction(chosenCard, 3);
	}
	
	@Override
	public void askFirstMove(Card[] hand, Card[] table) {
		boolean foundMatch = false;
		for (Card cardFromHand : hand) {
			if (cardFromHand.checkCanBeMatched(table)) {
				foundMatch = true;
				for (Card cardFromTable : table) {
					if (cardFromHand.month == cardFromTable.month) {
						controller.delayedAICardAction(cardFromHand, 2);
						controller.delayedAICardAction(cardFromTable, 3);
						break;
					}
				}
				break;
			}
		}
		if (!foundMatch) {
			controller.delayedAICardAction(hand[0], 2);
			controller.delayedAIDumpAction();
		}
	}

	@Override
	public void askSecondMove(Card fromPile, Card[] table) {
		if (fromPile.checkCanBeMatched(table)) {
			for (Card card : table) {
				if (card.month == fromPile.month) {
					controller.delayedAICardAction(card, 3);
					break;
				}
			}
		} else {
			controller.delayedAIDumpAction();
		}
	}

	@Override
	public void askKoikoiMove(Card[] hand, Card[] table) {
		final boolean answer = false;
		controller.delayedAIKoikoiAction(answer);
	}
}
