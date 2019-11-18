package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import structure.Card;
import structure.Player;
import structure.Tabletop;
import structure.Yaku;
import utils.ResourceLoader;

public class Game {
	
	/** GAMESTATES
	 * > 1 -> Player1 chooses card in the introduction
	 * > 2 -> Player2 chooses card in the introduction
	 * > 100 -> Player1 matches card from their hand
	 * > 110 -> Player1 matches card from the pile
	 * > 200 -> Player2 matches card from their hand
	 * > 210 -> Player2 matches card from the pile
	 * > 300 -> Temporary block (awaiting koikoi response p1)
	 * > 400 -> temporary block (awaiting koikoi response p2)
	 */
	public enum Gamestate {
		GAMESTATE_1,
		GAMESTATE_2,
		GAMESTATE_100,
		GAMESTATE_200,
		GAMESTATE_110,
		GAMESTATE_210,
		GAMESTATE_300,
		GAMESTATE_400;
	}
	
	// PROPERTIES
	private final Controller controller;
	private final Tabletop tabletop;
	
	private final Player player1;
	private final Player player2;
	
	private final int maxRounds;
	
	private Gamestate gamestate;
	private int currentRound;
	private boolean koikoiCalled;
	private Gamestate startingState;
	
	private Card player1SelectedCard;
	private Card player2SelectedCard;
	
	private Timer delayedAction;
	
	// CONSTRUCTOR
	public Game(Controller controller, Player player1, Player player2, int rounds) {
		this.player1 = player1;
		this.player2 = player2;
		this.controller = controller;
		this.tabletop = new Tabletop();
		this.maxRounds = rounds;
	}
	
	// ====================== METHODS ====================== //
	
	/**
	 * Game entry point, this should be invoked when the game first starts.
	 * (Before the parent check)
	 * Updates the UI labels (names, points).
	 */
	public void milestoneStart() {
		System.out.println("[GAME] Starting game");
		controller.updateGUILabels();
		
		milestoneParentCheckFirst();
	}
	
	/**
	 * [ GAMESTATE 1 ]
	 * Initiates the parent check.
	 * Asks controller to generate the UI.
	 */
	private void milestoneParentCheckFirst() {
		System.out.println("[GAME] Starting parent check");
		this.gamestate = Gamestate.GAMESTATE_1;
		
		controller.updateFeedbackTop(ResourceLoader.localize("game_parent_info"));
		controller.updateFeedbackBottom(ResourceLoader.localize("game_parent_first"));
		
		tabletop.prepareParentCheck();
		controller.generateGUIParentCheck();
	}
	
	/**
	 * [ GAMESTATE 2 ]
	 * Initiates the second half of parent check.
	 * This should happen after the first player chooses a card.
	 */
	private void milestoneParentCheckSecond() {
		System.out.println("[GAME] Second half of the parent check");
		gamestate = Gamestate.GAMESTATE_2;
		
		controller.updateFeedbackTop(ResourceLoader.localize("game_parent_second"));
		controller.updateFeedbackBottom("");
		
		controller.eventMilestoneReachedParentCheckSecond(player1SelectedCard); // AI
	}
	
	/**
	 * [ GAMESTATE 100/200 ]
	 * Finishes the parent check by determining the parent.
	 * Updates gamestate to either 100 or 200 depending on who is selected as the parent (determineParent).
	 * Nullifies cards chosen by the players.
	 */
	private void milestoneParentCheckFinished() {
		System.out.println("[GAME] Parent check finished");                                                                                     
		determineParent(player1SelectedCard, player2SelectedCard);
		
		if (gamestate == Gamestate.GAMESTATE_100) {
			System.out.println("[GAME] Player1 selected as the parent");
			controller.updateFeedbackTop(player2SelectedCard.name() + "(" + player2SelectedCard.month + " | " + player1SelectedCard.value + ")" );
			controller.updateFeedbackBottom(player1SelectedCard.name() + "(" + player1SelectedCard.month + " | " + player1SelectedCard.value + ")<br />" + player1.getName() + " " + ResourceLoader.localize("game_parent_begins") );    
		} else if (gamestate == Gamestate.GAMESTATE_200) {
			System.out.println("[GAME] Player2 selected as the parent");
			controller.updateFeedbackTop(player2SelectedCard.name() + "(" + player2SelectedCard.month + " | " + player1SelectedCard.value + ")<br />" + player2.getName() + " " + ResourceLoader.localize("game_parent_begins"));
			controller.updateFeedbackBottom(player1SelectedCard.name() + "(" + player1SelectedCard.month + " | " + player1SelectedCard.value + ")" );    
		}
		player1SelectedCard = null;
		player2SelectedCard = null;
		
		// Delay after choosing cards
		delayedAction = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				milestoneStartRound();
			}
		});
		delayedAction.setRepeats(false);
		delayedAction.start();
	}
	
	/**
	 * Prepares one game round.
	 * Increases current round counter.
	 */
	private void milestoneStartRound() {
		System.out.print("[GAME] Preparing new round");
		this.clear();
		this.currentRound++;
		System.out.println(currentRound + "/" + maxRounds);
		
		startingState = gamestate;
		
		controller.updateGUILabels();
		controller.setPileFacedown();
		
		tabletop.prepareGame(player1, player2);
		
		controller.clearGUI();
		controller.generateGUIRoundStart();
		
		milestoneTurnFirst();
	}
	
	/**
	 * Should be invoked when the first turn starts.
	 */
	private void milestoneTurnFirst() {
		System.out.print("[GAME] First turn begins, player matches card from the deck");
		player1SelectedCard = null;
		player2SelectedCard = null;
		
		if (gamestate == Gamestate.GAMESTATE_100) {
			if (player1.getHand().length == 0) { // check if the turn can be carried out
				unableToContinueRound();
			} else {
				controller.updateFeedbackBottom(player1.getName() + ResourceLoader.localize("game_turn_player1"));
				controller.updateFeedbackTop("");
			}
		}
		else if (gamestate == Gamestate.GAMESTATE_200) {
			if (player2.getHand().length == 0) { // check if the turn can be carried out
				unableToContinueRound();
			} else {
				controller.updateFeedbackBottom("");
				controller.updateFeedbackTop(player2.getName() + ResourceLoader.localize("game_turn_player2"));
				
				controller.eventMilestoneReachedTurnFirstPlayer2(); // AI
			}
		}
	}
	
	/**
	 * [ GAMESTATE 110 / 210 ]
	 * Invoked after the player matches card from their hand with the table, or dumps card.
	 */
	private void milestoneTurnSecond() {
		System.out.println("[GAME] Second part begins, player draws from the pile");
		player1SelectedCard = null;
		player2SelectedCard = null;
		
		if (gamestate == Gamestate.GAMESTATE_100) 
		{
			gamestate = Gamestate.GAMESTATE_110;
			controller.updateGUIPlayer1(true);
		}
		else if (gamestate == Gamestate.GAMESTATE_200) 
		{
			gamestate = Gamestate.GAMESTATE_210;
			controller.updateGUIPlayer2(true);
			
			controller.eventMilestoneReachedTurnSecondPlayer2(); // AI
		}
		

		if (!tabletop.getFirstCardPile().checkCanBeMatched(getTableCards())) {
			controller.requestEnableDump();
		}
	}
	
	/**
	 * Invoked after the player matches card from the pile or dumps it.
	 * Checks if any of the players collected any new yaku.
	 */
	private void milestonePlayerFinishedTurn() {
		System.out.println("[GAME] Player's turn finished");
		player1SelectedCard = null;
		player2SelectedCard = null;
		
		if (gamestate == Gamestate.GAMESTATE_110) {
			controller.updateGUIPlayer1(false);
			
			final Yaku[] checkedYaku = Yaku.checkAllYaku(player1.getCollected());
			if (checkedYaku.length != player1.getPassedYaku()){
				player1YakuDetected(checkedYaku);
			} else {
				gamestate = Gamestate.GAMESTATE_200;
				milestoneTurnFirst(); // move to next turn
			}
			
		}
		else if (gamestate == Gamestate.GAMESTATE_210) {
			controller.updateGUIPlayer2(false);
			
			final Yaku[] checkedYaku = Yaku.checkAllYaku(player2.getCollected());
			if (checkedYaku.length != player2.getPassedYaku()){
				player2YakuDetected(checkedYaku);
			} else {
				gamestate = Gamestate.GAMESTATE_100;
				milestoneTurnFirst(); // move to next turn
			}
			
		}
	}

	private void unableToContinueRound() {
		System.out.println("[GAME] Unable to continue");
		if (player1.checkKoiKoi()) {
			player1Wins();
		} else if (player2.checkKoiKoi()) {
			player2Wins();
		} else {
			if (currentRound == maxRounds) {
				gameEnd();
			} else {
				gamestate = startingState;
				milestoneStartRound();
			}
		}
	}

	private void player1YakuDetected(Yaku[] checkedYaku) {
		System.out.println("[GAME] Player 1 yaku detected");
		
		if (this.koikoiCalled) {
			player1Wins();
		} else {
			gamestate = Gamestate.GAMESTATE_300;
			controller.requestKoikoiWindow(player1, checkedYaku[0]);
		}
	}
	private void player2YakuDetected(Yaku[] checkedYaku) {
		System.out.println("[GAME] Player 2 yaku detected");
		
		if (this.koikoiCalled) {
			player2Wins();
		} else {
			gamestate = Gamestate.GAMESTATE_400;
			controller.requestKoikoiWindow(player2, checkedYaku[0]);
			
			controller.eventKoikoiReachedPlayer2();
		}
	}
	
	private void player1Wins(){
		System.out.println("[GAME] Player1 wins");
		
		final Yaku[] yakus = Yaku.checkAllYaku(getPlayer1Collected());
		int totalScore = 0;
		for (Yaku yaku : yakus) {
			totalScore += yaku.calculateScoreForYaku(getPlayer1Collected());
		}
		player1.addPoints(totalScore);
		
		controller.updateFeedbackTop("");
		controller.updateFeedbackBottom(this.player1.getName() + " " + ResourceLoader.localize("game_round_win"));
		
		if (this.maxRounds == this.currentRound) {
			gameEnd();
		} else {
			gamestate = Gamestate.GAMESTATE_100;
			this.milestoneStartRound(); // Go to next round
		}
	}

	private void player2Wins() {
		System.out.println("[GAME] Player2 wins");
		
		final Yaku[] yakus = Yaku.checkAllYaku(getPlayer2Collected());
		int totalScore = 0;
		for (Yaku yaku : yakus) {
			totalScore += yaku.calculateScoreForYaku(getPlayer2Collected());
		}
		player2.addPoints(totalScore);
		
		controller.updateFeedbackTop(this.player2.getName() + " " + ResourceLoader.localize("game_round_win"));
		controller.updateFeedbackBottom("");
		
		if (this.maxRounds == this.currentRound) {
			gameEnd();
		} else {
			gamestate = Gamestate.GAMESTATE_200;
			milestoneStartRound();
		}
	}
	
	private void gameEnd() {
		System.out.println("[GAME] Reached the end of the game");
		controller.requestResults(player1, player2);
	}
	
	// ====================== DATA INPUTS ====================== //
	/**
	 * Invoked when player presses the dump button.
	 */
	public void dump() {
		switch (gamestate) {
			case GAMESTATE_100: dumpCardFromHand1(); break;
			case GAMESTATE_110: dumpCardFromPile1(); break;
			case GAMESTATE_200: dumpCardFromHand2(); break;
			case GAMESTATE_210: dumpCardFromPile2(); break;
			default: throw new RuntimeException("Wrong gamestate during dumping");
		}
	}
	private void dumpCardFromHand1() {
		player1.takeCardFromHand(player1SelectedCard);
		tabletop.deliverCardsToTable(new Card[]{player1SelectedCard});
		milestoneTurnSecond();
	}
	private void dumpCardFromPile1() {
		tabletop.deliverCardsToTable(this.tabletop.pileTakeCards(1));
		milestonePlayerFinishedTurn();
	}
	private void dumpCardFromHand2() {
		player2.takeCardFromHand(player2SelectedCard);
		tabletop.deliverCardsToTable(new Card[]{player2SelectedCard});
		milestoneTurnSecond();
	}
	private void dumpCardFromPile2() {
		tabletop.deliverCardsToTable(this.tabletop.pileTakeCards(1));
		milestonePlayerFinishedTurn();
	}
	

	public void koikoi(boolean bool) {
		if (bool) {
			koikoiAccepted();
		} else {
			koikoiRefused();
		}
	}
	private void koikoiRefused() {
		System.out.println("[GAME] Koikoi Refused");
		switch (gamestate) {
			case GAMESTATE_300: 
				gamestate = Gamestate.GAMESTATE_110;
				player1Wins();
				break;
			case GAMESTATE_400: 
				gamestate = Gamestate.GAMESTATE_210;
				player2Wins();
				break;
			default: throw new RuntimeException("Wrong gamestate during koikoi refusal");
		}
	}
	private void koikoiAccepted() {
		System.out.println("[GAME] Koikoi Accepted | " + gamestate);
		
		switch (gamestate) {
			case GAMESTATE_300: // player 1 accepted
				gamestate = Gamestate.GAMESTATE_200; // return to normal course of game
				player1.calledKoiKoi();
				koikoiCalled = true;
				milestoneTurnFirst();
				break;
			case GAMESTATE_400: // player 2 accepted
				gamestate = Gamestate.GAMESTATE_100; // return to normal course of game
				player2.calledKoiKoi();
				koikoiCalled = true;
				milestoneTurnFirst();
				break;
			default: throw new RuntimeException("Wrong gamestate during koikoi acceptance");
		}
	}
	
	/**
	 * Fired when player chooses a card.
	 * @param card		The card that the player clicked on.
	 * @param context	Where the card is.
	 */
	public void card(Card card, int context) {
		System.out.println("[GAME] Card chosen: " + card.name() + " | " + gamestate + " | " + context);
		switch (gamestate) {
			case GAMESTATE_1: parentCheckFirstSelection(card, context);		break; // player 1 chooses card > parent check
			case GAMESTATE_2: parentCheckSecondSelection(card, context);	break; // player 2 chooses card > parent check
			case GAMESTATE_100: turnPlayer1Table(card, context);			break; // player 1 chooses card > normal turn
			case GAMESTATE_110: turnPlayer1Pile(card, context);				break; // player 1 chooses card > normal turn / part 2
			case GAMESTATE_200: turnPlayer2Table(card, context);			break; // player 2 chooses card > normal turn
			case GAMESTATE_210: turnPlayer2Pile(card, context);				break; // player 2 chooses card > normal turn / part 2
			default: System.out.println("[GAME] Card choice dismissed by the game");
		}
	}
	private void parentCheckFirstSelection(Card card, int context) {
		if (context == 3) {
			player1SelectedCard = card;
			controller.setFaceup(card);
			milestoneParentCheckSecond();
		}
	}
	private void parentCheckSecondSelection(Card card, int context) {
		if (context == 3 && !(player1SelectedCard.getCardID() == card.getCardID())) {
			player2SelectedCard = card;
			controller.setFaceup(card);
			milestoneParentCheckFinished();
		}
	}
	private void turnPlayer1Table(Card card, int context) {
		if (context == 1) {
			player1SelectedCard = card;
			if(!card.checkCanBeMatched(getTableCards())) {
				controller.requestEnableDump();
			} else {
				controller.requestDisableDump();
			}
		}
		else if (context == 3 && player1SelectedCard != null) {
			matchedAndMove(player1, player1SelectedCard, card);
		}
	}
	private void turnPlayer1Pile(Card card, int context) {
		if (context == 3) {
			matchPileAndTable(player1, card);
		}
	}
	private void turnPlayer2Table(Card card, int context) {
		if (context == 2) {
			player2SelectedCard = card;
			if(!card.checkCanBeMatched(getTableCards())) {
				controller.requestEnableDump();
			} else {
				controller.requestDisableDump();
			}
		}
		else if (context == 3 && player2SelectedCard != null) {
			matchedAndMove(player2, player2SelectedCard, card);
		}
	}
	private void turnPlayer2Pile(Card card, int context) {
		if (context == 3) {
			matchPileAndTable(player2, card);
		}
	}
	
	
	// ====================== ACTIONS ====================== //
	
	/**
	 * After the player matched a card from their hand with a card on the table they can use this method to move
	 * these cards to their collected cards.
	 * @param player The player taking the cards to their collection.
	 * @param card1	Card from player's hand.
	 * @param card2	Card from the table.
	 */
	public void matchedAndMove(Player player, Card card1, Card card2) {
		if(card1.match(card2)) {
			player.takeCardFromHand(card1);
			tabletop.tableTakeCard(card2);
			player.deliverCardsToCollected(new Card[]{card1, card2});
			
			milestoneTurnSecond();
		}
	}

	/**
	 * Checks if the card selected by player2 matches the card from the pile.
	 * @param card	The card picked by the player from the table.
	 */
	public void matchPileAndTable(Player plr, Card card) {
		if (card.match(this.tabletop.getFirstCardPile())) {
			plr.deliverCardsToCollected(this.tabletop.pileTakeCards(1));
			plr.deliverCardsToCollected(new Card[]{card});
			tabletop.tableTakeCard(card);
			
			milestonePlayerFinishedTurn();
		}
	}
	
	// ====================== UTILS ====================== //

	/**
	 * Clears hands and collected of every player, clears table and pile in table-top.
	 * Sets koikoiCalled to false.
	 */
	public void clear() {
		tabletop.clearAll();
		player1.clearAll();
		player2.clearAll();
		koikoiCalled = false;
		if (delayedAction != null) delayedAction.stop();
	}
	
	/**
	 * Sets gamestate depending on who selected which card.
	 * @param card1	First player's card
	 * @param card2 Second player's card
	 */
	private void determineParent(Card card1, Card card2) {
		if (card1.compare(card2)) {
			gamestate = Gamestate.GAMESTATE_100;
		} else {
			gamestate = Gamestate.GAMESTATE_200;
		}
	}
	
	public Card[] getPlayer1Cards() {
		return this.player1.getHand();
	}
	public Card[] getPlayer1Collected() {
		return this.player1.getCollected();
	}
	public Card[] getPlayer2Cards() {
		return this.player2.getHand();
	}
	public Card[] getPlayer2Collected() {
		return this.player2.getCollected();
	}
	public Card[] getTableCards() {
		return this.tabletop.getTableCards();
	}
	
	public Card getFirstCardPile() {
		return tabletop.getFirstCardPile();
	}
	
	public Player getPlayer1() {
		return this.player1;
	}
	public Player getPlayer2() {
		return this.player2;
	}
	
	public Gamestate getGamestate() {
		return this.gamestate;
	}
}
