package ai;

import logic.Controller;
import structure.Card;

public class AnalysisAI implements IPerson {
	
	// PROPERTIES
	private final Controller controller;
	
	// CONSTRUCTOR
	public AnalysisAI(Controller controller) {
		this.controller = controller;
	}

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
		Card cardHand = null;
		Card cardTable = null;
		int maxPoints = 0;
		for (Card cardFromHand : hand) {
			if (cardFromHand.checkCanBeMatched(table)) {
				foundMatch = true;
				for (Card cardFromTable : table) {
					if (cardFromHand.month == cardFromTable.month && (cardFromHand.value + cardFromTable.value) > maxPoints) {
						cardHand = cardFromHand;
						cardTable = cardFromTable;
						maxPoints = (cardFromHand.value + cardFromTable.value);
					}
				}
			}
		}
		if (foundMatch) {
			controller.delayedAICardAction(cardHand, 2);
			controller.delayedAICardAction(cardTable, 3);
		} else {
			controller.delayedAICardAction(hand[0], 2);
			controller.delayedAIDumpAction();
		}
	}

	@Override
	public void askSecondMove(Card fromPile, Card[] table) {
		if (fromPile.checkCanBeMatched(table)) {
			Card chosenCard = null; // will be overwritten
			int maxPoints = 0;
			for (Card card : table) {
				if (card.month == fromPile.month && fromPile.value > maxPoints) {
					maxPoints = fromPile.value;
					chosenCard = card;
				}
			}
			controller.delayedAICardAction(chosenCard, 3);
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
