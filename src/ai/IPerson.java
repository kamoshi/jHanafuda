package ai;

import structure.Card;

public interface IPerson {
	
	/**
	 * Ask AI to perform the parent check move.
	 */
	public abstract void askParentMove(Card[] table, Card cardAlreadyTaken);
	
	/**
	 * Ask AI to perform the first move in their turn.
	 */
	public abstract void askFirstMove(Card[] hand, Card[] table);
	
	/**
	 * Ask AI to perform the second move in their turn.
	 */
	public abstract void askSecondMove(Card fromPile, Card[] table);
	
	/**
	 * Ask AI to choose whether to do koikoi or not.
	 */
	public abstract void askKoikoiMove(Card[] hand, Card[] table);

}
