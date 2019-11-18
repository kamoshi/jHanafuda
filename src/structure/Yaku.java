package structure;

import java.util.ArrayList;

public enum Yaku {
	
	// ENUMERATED
	Gokou( // 5 BRIGHTS
		new int[]{4, 12, 32, 44, 48},
		new int[]{},
		0,
		10,
		false
	),
	Shikou( // 4 BRIGHTS EXCLUDING RAIN
		new int[]{4, 12, 32, 48},
		new int[]{},
		0,
		8,
		false
	),
	AmeShikou( // 4 BRIGHTS INCLUDING RAIN
		new int[]{44},
		new int[]{4, 12, 32, 48},
		3,
		7,
		false
	),
	Sankou( // 3 BRIGHTS EXCLUDING RAIN
		new int[]{},
		new int[]{4, 12, 32, 48},
		3,
		5,
		false
	),
	InoShikaChou( // BUTTERFLY BOAR DEER | +1 FOR EACH OPTIONAL TANE
		new int[]{24, 28, 40},
		new int[]{8, 16, 20, 31, 36, 43},
		0,
		5,
		true
	),
	Tane( // 10 TANE | +1 POINT FOR EACH NEXT
		new int[]{},
		new int[]{8, 16, 20, 24, 28, 31, 36, 40, 43},
		5,
		1,
		true
	),
	Akatan( // 3 RED POEM | +1 POINT FOR EACH OPTIONAL
		new int[]{3, 7, 11},
		new int[]{15, 19, 23, 27, 35, 39, 42},
		0,
		5,
		true
	),
	Aotan( // 3 BLUE TAN | +1 POINT FOR EACH OPTIONAL TAN
		new int[]{23, 35, 39},
		new int[]{3, 7, 11, 15, 19, 27, 42},
		0,
		5,
		true
	),
	Tanzaku( // 5 TANZAKU | +1 POINT FOR EACH NEXT
		new int[]{},
		new int[]{3, 7, 11, 15, 19, 23, 27, 35, 39, 42},
		5,
		1,
		true
	),
	Kasu(	// 10 PLAINS | +1 POINT FOR EACH NEXT
		new int[]{},
		new int[]{1, 2, 5, 6, 9, 10, 13, 14, 17, 18, 21, 22, 25, 26, 29, 30, 33, 34, 37, 38, 45, 46, 47},
		10,
		1,
		true
	);
	
	// PROPERTIES
	private final int[] requiredID;		// Required cards
	private final int[] optionalID;		// Optional cards
	private final int optionalMinimum;	// Minimum of the optional
	private final int basePoints;		// Base points for yaku
	private final boolean bonus;		// Add bonus points for going above the minimum?
	
	// CONSTRUCTOR
	private Yaku(int[] requiredID, int[] optionalID, int optionalMinimum, int basePoints, boolean bonus) {
		this.requiredID = requiredID;
		this.optionalID = optionalID;
		this.optionalMinimum = optionalMinimum;
		this.basePoints = basePoints;
		this.bonus = bonus;
	}
	
	// Yaku on deal
	/**
	 * Returns true if the array contains four cards from the same month, else returns false.
	 * @param array	The array containing cards.
	 * @return boolean
	 */
	public static boolean checkTeshi(Card[] array) {
		final int[] months = new int[12];
		for (Card card : array) {
			months[card.getMonth()-1] += 1;
		}
		for (int occurencies : months) {
			if (occurencies == 4) return true;
		}
		return false;
	}
	/**
	 * Returns true if the array contains four pairs of cards from the same month, else returns false.
	 * @param array	The array containing cards.
	 * @return	boolean
	 */
	public static boolean checkKuttsuki(Card[] array) {
		final int[] months = new int[12];
		for (Card card : array) {
			months[card.getMonth()-1] += 1;
		}
		int pairs = 0;
		for (int occurencies : months) {
			if (occurencies == 2) pairs++;
		}
		if (pairs == 4) return true;
		return false;
	}
	
	/**
	 * Checks if an array of cards contains a single yaku.
	 * @param array		The array to check.
	 * @return			True or False.
	 */
	public boolean checkYaku(Card[] array) {
		boolean assumption = true;
		for (int id : requiredID) {
			if(!containsID(array, id)) {
				assumption = false;
			}
		}
		if (assumption == true) {
			int optional = 0;
			for (int id : optionalID) {
				if(containsID(array, id)) {
					optional++;
				}
			}
			if (optional < optionalMinimum) {
				assumption = false;
			}
		}
		if (assumption) System.out.println("Has " + this.name());
		return assumption;
	}
	
	/**
	 * Method that checks whether an array of cards fulfills any yaku.
	 * @param array	The array to check.
	 * @return		Array containing found yaku. Empty if not found any.
	 */
	public static Yaku[] checkAllYaku(Card[] array) {
		final ArrayList<Yaku> al = new ArrayList<>();
		for (Yaku yaku : Yaku.values()) {
			if (yaku.checkYaku(array)) {
				al.add(yaku);
			}
		}
		final Yaku[] yakus = new Yaku[al.size()];
		int i = 0;
		for (Yaku yaku : al) {
			yakus[i] = yaku;
			i++;
		}
		return yakus;
	}
	
	/**
	 * Calculates score for a single yaku based on the array provided.
	 * @param array		Array with cards.
	 * @return			Points awarded.
	 */
	public int calculateScoreForYaku(Card[] array) {
		if (checkYaku(array)) {
			if (this.bonus == true) {
				int optional = 0;
				for (int id : optionalID) {
					if(containsID(array, id)) {
						optional++;
					}
				}
				return this.basePoints + (optional - optionalMinimum);
			}
			return this.basePoints;
		}
		return 0;
	}
	
	// UTILITIES
	/**
	 * Method that checks whether an array of cards contains a card.
	 * @param array		The array of cards that need to be checked.
	 * @param cardID	The card searched for.
	 * @return			True or False
	 */
	public static boolean containsID(Card[] array, int cardID) {
		for (Card card : array) {
			if (card.getCardID() == cardID) return true;
		}
		return false;
	}
}
