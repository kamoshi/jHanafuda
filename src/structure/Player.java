package structure;

import java.util.ArrayList;

public class Player {
	
	// PROPERTIES
	private final String name;
	private int points;
	private int passedYaku;
	private boolean calledKoiKoi;
	
	private final ArrayList<Card> hand			= new ArrayList<>(8);
	private final ArrayList<Card> collected		= new ArrayList<>(24);
	
	// CONSTRUCTOR
	public Player(String name) {
		this.name = name;
		this.calledKoiKoi = false;
	}
	
	// METHODS
	public String getName() {
		return this.name;
	}
	
	public void calledKoiKoi() {
		this.calledKoiKoi = true;
	}
	public void resetKoiKoi() {
		this.calledKoiKoi = false;
	}
	public boolean checkKoiKoi() {
		return this.calledKoiKoi;
	}
	
	public void setPassedYaku(int number) {
		this.passedYaku = number;
	}
	public int getPassedYaku() {
		return this.passedYaku;
	}
	
	public void deliverCardsToHand(Card[] cards) {
		for (Card card : cards) {
			this.hand.add(card);
		}
	}
	public void takeCardFromHand(Card card) {
		hand.remove(card);
	}
	
	public void deliverCardsToCollected(Card[] cards) {
		for (Card card : cards) {
			collected.add(card);
		}
	}
	public Card[] getHand() {
		Card[] cards = new Card[this.hand.size()];
		int i = 0;
		for (Card card : this.hand) {
			cards[i] = card;
			i++;
		}
		return cards;
	}
	public Card[] getCollected() {
		Card[] cards = new Card[this.collected.size()];
		int i = 0;
		for (Card card : this.collected) {
			cards[i] = card;
			i++;
		}
		return cards;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public void addPoints(int points) {
		this.points += points;
	}
	
	public void clearAll() {
		this.hand.clear();
		this.collected.clear();
		this.passedYaku = 0;
		this.calledKoiKoi = false;
	}
}
