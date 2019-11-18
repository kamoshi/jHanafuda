package structure;

public enum Card{
	
	// ENUMERATED
	//January (ID, month, value)
	Matsu1		(1,	1, 1),	// plain
	Matsu2		(2, 1, 1),	// plain
	Matsu3		(3, 1, 5),	// red poem tan
	Matsu4		(4, 1, 20),	// crane
	// February
	Ume1		(5, 2, 1),	// plain
	Ume2		(6, 2, 1),	// plain
	Ume3		(7, 2, 5),	// red poem tan
	Ume4		(8, 2, 10),	// nightingale
	// March
	Sakura1		(9, 3, 1),	// plain
	Sakura2		(10, 3, 1),	// plain
	Sakura3		(11, 3, 5),	// red poem tan
	Sakura4		(12, 3, 20),// curtain
	// April
	Fuji1		(13, 4, 1),	// plain
	Fuji2		(14, 4, 1),	// plain
	Fuji3		(15, 4, 5),	// red tan
	Fuji4		(16, 4, 10),// cuckoo
	// May
	Ayame1		(17, 5, 1),	// plain
	Ayame2		(18, 5, 1),	// plain
	Ayame3		(19, 5, 5),	// red tan
	Ayame4		(20, 5, 10),// bridge
	// June
	Botan1		(21, 6, 1), // plain
	Botan2		(22, 6, 1), // plain
	Botan3		(23, 6, 5), // blue tan
	Botan4		(24, 6, 10),// butterfly
	// July
	Hagi1		(25, 7, 1), // plain
	Hagi2		(26, 7, 1), // plain
	Hagi3		(27, 7, 5), // red tan
	Hagi4		(28, 7, 10),// boar
	// August
	Susuki1		(29, 8, 1), // plain
	Susuki2		(30, 8, 1), // plain
	Susuki3		(31, 8, 10),// geese
	Susuki4		(32, 8, 20),// moon
	// September
	Kiku1		(33, 9, 1), // plain
	Kiku2		(34, 9, 1), // plain
	Kiku3		(35, 9, 5), // blue tan
	Kiku4		(36, 9, 10),// sake
	// October
	Momiji1		(37, 10, 1), // plain
	Momiji2		(38, 10, 1), // plain
	Momiji3		(39, 10, 5), // blue tan
	Momiji4		(40, 10, 10),// deer
	// November
	Yanagi1		(41, 11, 1), // lightning
	Yanagi2		(42, 11, 5), // red tan
	Yanagi3		(43, 11, 10),// swallow
	Yanagi4		(44, 11, 20),// rain
	// December
	Kiri1		(45, 12, 1), // plain
	Kiri2		(46, 12, 1), // plain
	Kiri3		(47, 12, 1), // plain
	Kiri4		(48, 12, 20);// phoenix
	
	// PROPERTIES
	public final int cardID;
	public final int month;
	public final int value;
	
	// CONSTRUCTOR
	private Card(int cardID, int month, int value) {
		this.cardID = cardID;
		this.month = month;
		this.value = value;
	}
	
	// METHODS
	public int getCardID() {
		return this.cardID;
	}
	public int getMonth() {
		return this.month;
	}
	
	/**
	 * Compares first card to another card by comparing their month and value.
	 * If the first card is better then returns true, else returns false.
	 * If they happen to be completely equal, returns true.
	 * @param card	Another card to which you compare a card.
	 * @return	boolean
	 */
	public boolean compare(Card card) {
		if (this.month < card.month) {
			return true;
		} else if (this.month > card.month) {
			return false;
		} else {
			if (this.value < card.value) {
				return false;
			} else if (this.value > card.value) {
				return true;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Checks if the card can be matched with another card.
	 * @param card	Another card.
	 * @return		True or False.
	 */
	public boolean match(Card card) {
		if (this.month == card.month) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the card can be matched with any cards in a given array.
	 * @param cards		The array containing cards.
	 * @return			True or False.
	 */
	public boolean checkCanBeMatched(Card[] cards) {
		boolean assumption = false;
		for (Card card : cards) {
			if (this.match(card)) assumption = true;
		}
		return assumption;
	}
	
	// Utilities
	@Override
	public String toString() {
		return "[" + this.name() + "] | ID:" + this.cardID + " | Month:" + this.month + " | Value:" + this.value;
	}
}
