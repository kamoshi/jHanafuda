package structure;

import java.util.ArrayList;
import java.util.Collections;

public class Tabletop {
	
	// PROPERTIES
	private final ArrayList<Card> table;
	private final ArrayList<Card> pile;
	
	// CONSTRUCTOR
	public Tabletop() {
		int tableCapacity = 24;
		int pileCapacity = 48;
		this.table = new ArrayList<>(tableCapacity);
		this.pile = new ArrayList<>(pileCapacity);
	}
	
	// COMPLEX
	
	/**
	 * Prepares cards for the game
	 */
	public void prepareGame(Player plr1, Player plr2) {
		pilePopulate();
		pileShuffle();
		
		for (int i = 0; i < 4; i++) {
			plr1.deliverCardsToHand(pileTakeCards(2));
			plr2.deliverCardsToHand(pileTakeCards(2));
			deliverCardsToTable(pileTakeCards(2));
		}
	}
	
	/**
	 * Prepares cards for finding parent.
	 */
	public void prepareParentCheck() {
		pilePopulate();
		pileShuffle();
		deliverCardsToTable(pileTakeCards(8));
	}
	
	/**
	 * Clears all
	 */
	public void clearAll() {
		table.clear();
		pile.clear();
	}
	
	// BASIC
	public void pilePopulate() {
		for (Card card : Card.values()) {
			pile.add(card);
		}
	}
	public void pileShuffle() {
		Collections.shuffle(pile);
	}
	/**
	 * Takes n cards out of the pile.
	 * @param n	The amount of cards to take.
	 * @return	Array containing the cards.
	 */
	public Card[] pileTakeCards(int n) {
		Card[] cards = new Card[n];
		for (int i = 0; i < n; i++) {
			cards[i] = pile.get(pile.size()-(i+1));
			pile.remove(pile.size()-(i+1));
		}
		return cards;
	}
	
	// Table
	public void deliverCardsToTable(Card[] cards) {
		for (Card card : cards) {
			table.add(card);
		}
	}
	
	/**
	 * Returns all cards in Table in an array.
	 * @return	Array containing the cards.
	 */
	public Card[] getTableCards() {
		Card[] array = new Card[table.size()];
		int i = 0;
		for (Card card : table) {
			array[i] = card;
			i++;
		}
		return array;
	}
	
	public void tableTakeCard(Card card) {
		this.table.remove(card);
	}
	
	public Card getFirstCardPile() {
		return this.pile.get(pile.size()-1);
	}
}
