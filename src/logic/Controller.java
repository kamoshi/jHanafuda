package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import ai.AnalysisAI;
import ai.IPerson;
import ai.SimpleAI;
import gui.Launcher;
import gui.game.GameFrame;
import logic.Game.Gamestate;
import structure.Card;
import structure.Player;
import structure.Yaku;
import utils.ResourceLoader;
import utils.Resolution;

public class Controller {
	
	// PROPERTIES
	private final Launcher launcher;
	
	private GameFrame gameFrame;
	private Game game;
	private IPerson strategyAI;
	private boolean overrideAI;
	
	// CONSTRUCTOR
	public Controller(Launcher launcher) {
		this.launcher = launcher;
	}
	
	/**
	 * Receives all the information from the launcher and uses it to prepare data for a new session.
	 * @param player1Name	Player1's name.
	 * @param player2Name	Player2's name.
	 * @param rounds		Number of rounds.
	 * @param resolution	Screen resolution.
	 * @param aiStrategy 
	 * @param aiEnabled 
	 */
	public void eventRequestGame(String player1Name, String player2Name, String cardSet, int rounds, String resolution, boolean aiEnabled, String aiStrategy) {
		System.out.println("[EVENT] Request Game");
		this.launcher.setVisible(false);
		ResourceLoader.setCardSet(cardSet);
		
		// AI
		this.overrideAI = aiEnabled;
		switch (aiStrategy) {
			case "SimpleAI":	strategyAI = new SimpleAI(this); break;
			case "AnalysisAI":	strategyAI = new AnalysisAI(this); break;
			default:			strategyAI = new SimpleAI(this); break;
		}
		if (aiEnabled) player2Name = "AI";
		
		// Players
		Player player1, player2;
		if (player1Name.isEmpty()) player1 = new Player("Player1"); else player1 = new Player(player1Name);
		if (player2Name.isEmpty()) player2 = new Player("Player2"); else player2 = new Player(player2Name);
		
		// Game
		this.game = new Game(this, player1, player2, rounds);
		
		// Resolution
		Resolution res;
		switch (resolution) {
			case "800x600"	: res = Resolution.res800x600;	break;
			case "1080x720"	: res = Resolution.res1080x720; break;
			case "1024x768"	: res = Resolution.res1024x768; break;
			case "1280x1024": res = Resolution.res1280x1024;break;
			case "1536x864" : res = Resolution.res1536x864; break;
			case "1600x1024": res = Resolution.res1600x1024;break;
			default: res = Resolution.res1080x720; break;
		}
		
		// Game Frame
		this.gameFrame = new GameFrame(this, res);
		this.gameFrame.setVisible(true);
		
		game.milestoneStart();
	}
	
	// ======================================= GAME ======================================= //
	
	public void eventCardPressed(Card card, int context) {
		System.out.println("[EVENT] Card pressed");
		final Gamestate state = game.getGamestate();
		if (!(overrideAI && (state == Gamestate.GAMESTATE_200 || state == Gamestate.GAMESTATE_210 || state == Gamestate.GAMESTATE_2))) {
			game.card(card, context);
		}
		
	}
	
	/**
	 * Fires when the dump button is used.
	 */
	public void eventDumpButtonPressed() {
		System.out.println("[EVENT] Dump button pressed");
		final Gamestate state = game.getGamestate();
		if (!(overrideAI && (state == Gamestate.GAMESTATE_200 || state == Gamestate.GAMESTATE_210 || state == Gamestate.GAMESTATE_2))) {
			game.dump();
		}
	}
	
	// ============================= GRAPHICAL USER INTERFACE ============================= //
	
	/**
	 * Updates the GUI to match the Player names and points data.
	 * Useful when the window is first initialized or at the end of a round (when points change).
	 */
	public void updateGUILabels() {
		gameFrame.updateNamePlayer1(game.getPlayer1().getName());
		gameFrame.updateNamePlayer2(game.getPlayer2().getName());
		gameFrame.updatePointsPlayer1(game.getPlayer1().getPoints());
		gameFrame.updatePointsPlayer2(game.getPlayer2().getPoints());
	}
	
	/**
	 * Initializes the GUI for the start of parent check.
	 * This only should happen at the start of the entire game.
	 * Sets stack face down and cards on the table face down.
	 */
	public void generateGUIParentCheck() {
		gameFrame.updateVCardsForTable(game.getTableCards(), false);
		gameFrame.setCardStackDown();
	}
	
	/**
	 * Initializes the GUI for the start of the main part of the game (rounds).
	 * That is immediately after the parent has been decided or after the previous round ended.
	 * Generates GUI cards in decks for both players and then generates GUI card on the table. 
	 */
	public void generateGUIRoundStart() {
		gameFrame.generateVCardsForPlayer1Hand(game.getPlayer1Cards(), true);
		gameFrame.generateVCardsForPlayer2Hand(game.getPlayer2Cards(), !overrideAI);
		gameFrame.updateVCardsForTable(game.getTableCards(), true);
	}
	
	/**
	 * Updates cards for player 1.
	 * Updated: pile, player1's deck, player1's collected area, table.
	 * @param visibility	Controls the visibility of cards on the pile.
	 */
	public void updateGUIPlayer1(boolean visibility) {
		gameFrame.updateVCardsForPlayer1Hand(game.getPlayer1Cards());
		gameFrame.updateVCardsForPlayer1Collected(game.getPlayer1Collected());
		gameFrame.updateVCardsForTable(game.getTableCards(), true);
		if (visibility) gameFrame.setCardStackUp(game.getFirstCardPile());
		else gameFrame.setCardStackDown();
	}
	
	/**
	 * Updates cards for player 2.
	 * Updated: pile, player2's deck, player2's collected area, table.
	 * @param visibility	Controls the visibility of cards on the pile.
	 */
	public void updateGUIPlayer2(boolean visibility) {
		gameFrame.updateVCardsForPlayer2Hand(game.getPlayer2Cards());
		gameFrame.updateVCardsForPlayer2Collected(game.getPlayer2Collected());
		gameFrame.updateVCardsForTable(game.getTableCards(), true);
		if (visibility) gameFrame.setCardStackUp(game.getFirstCardPile());
		else gameFrame.setCardStackDown();
	}
	
	/**
	 * Clears all gui elements.
	 * Useful for starting new rounds when the state of the GUI needs to be completely reset.
	 */
	public void clearGUI() {
		gameFrame.clear();
	}
	
	/**
	 * Fires when the user closes the game window.
	 * Clears the reference and sets launcher visible for the user.
	 * Nullifies reference to game.
	 */
	public void eventClosedGame() {
		System.out.println("[EVENT] GameFrame closed");
		gameFrame = null;
		game.clear();
		game = null;
		launcher.setVisible(true);
		
		//ai cleanup
		if (aiCardAction != null) aiCardAction.stop();
		if (aiDumpAction != null) aiDumpAction.stop();
		if (aiKoikoiAction != null) aiKoikoiAction.stop();
	}
	
	/**
	 * Dynamically updates the launcher to match the locale chosen by the user.
	 */
	public void updateUILocale() {
		launcher.updateLocale();
	}
	
	/**
	 * Fires when the user closes the results screen.
	 * Disposes the game window and nullifies the reference.
	 * Nullifies reference to game.
	 */
	public void eventResultsClosed() {
		System.out.println("[EVENT] Results closed");
		gameFrame.dispose();
	}
	
	/**
	 * Summons results popup inside the game window, which displays names and points.
	 * @param plr1		Player object.
	 * @param plr2		Player object.
	 */
	public void requestResults(Player plr1, Player plr2) {
		gameFrame.generateResultsPopup(plr1.getName(), plr2.getName(), plr1.getPoints(), plr2.getPoints());
	}
	
	/**
	 * Summons the koikoi decision popup.
	 * @param plr		Player object.
	 * @param yaku		Yaku combination that triggered the koikoi chance.
	 */
	public void requestKoikoiWindow(Player plr, Yaku yaku) {
		final Gamestate state = game.getGamestate();
		boolean active = true;
		if (!(overrideAI && state == Gamestate.GAMESTATE_400)) {
			active = false;
		}
		gameFrame.generateKoikoiPopup(plr.getName(), yaku.name().toLowerCase(), yaku.calculateScoreForYaku(plr.getCollected()), active);
	}
	
	/**
	 * Receives the answer chosen to the koikoi:
	 * True		->	Koikoi declared
	 * False	->	koikoi refused
	 * @param bool		False or True
	 */
	public void eventKoikoiAnswer(boolean bool) {
		System.out.println("[EVENT] Koikoi answer: " + bool);
		gameFrame.eraseKoikoiPopup();
		game.koikoi(bool);
	}
	
	/**
	 * Requests GameFrame to disable the dump button.
	 */
	public void requestDisableDump() {
		gameFrame.disableDump();
	}
	
	/**
	 * Requests GameFrame to enable the dump button.
	 */
	public void requestEnableDump() {
		gameFrame.enableDump();
	}
	
	/**
	 * Updates the top-left corner of the screen to display a message.
	 * @param info	The message that needs to be displayed to the user.
	 */
	public void updateFeedbackTop(String info) {
		gameFrame.updateFeedbackTop(info);
	}
	
	/**
	 * Updates the bottom-left corner of the screen to display a message.
	 * @param info	The message that needs to be displayed to the user.
	 */
	public void updateFeedbackBottom(String info) {
		gameFrame.updateFeedbackBottom(info);
	}
	
	/**
	 * Searches for the GUI representation of a card on the table and sets it face up.
	 * @param card	The card that needs to be face up (not GUI representation).
	 */
	public void setFaceup(Card card) {
		gameFrame.setFaceup(card);
	}
	
	/**
	 * Sets the pile facedown.
	 */
	public void setPileFacedown() {
		gameFrame.setCardStackDown();
	}
	
	// ============================= ARTIFICIAL INTELLIGENCE ============================= //
	
	private void askAIParentMove(Card cardAlreadyTaken) {
		strategyAI.askParentMove(game.getTableCards(), cardAlreadyTaken);
	}
	private void askAIFirstMove() {
		strategyAI.askFirstMove(game.getPlayer2Cards(), game.getTableCards());
	}
	private void askAISecondMove() {
		strategyAI.askSecondMove(game.getFirstCardPile(), game.getTableCards());
	}
	private void askAIKoikoi() {
		strategyAI.askKoikoiMove(game.getPlayer2Cards(), game.getTableCards());
	}

	// AI Out
	public void eventMilestoneReachedParentCheckSecond(Card cardAlreadyTaken) {
		if (overrideAI) {
			askAIParentMove(cardAlreadyTaken);
		}
	}
	public void eventMilestoneReachedTurnFirstPlayer2() {
		if (overrideAI) {
			askAIFirstMove();
		}
	}
	public void eventMilestoneReachedTurnSecondPlayer2() {
		if (overrideAI) {
			askAISecondMove();
		}
	}
	public void eventKoikoiReachedPlayer2() {
		if (overrideAI) {
			askAIKoikoi();
		}
	}
	
	// AI In
	final private int delay = 1000;
	private Timer aiCardAction;
	private Timer aiDumpAction;
	private Timer aiKoikoiAction;
	
	public void delayedAICardAction(Card card, int context) {
		aiCardAction = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				game.card(card, context);
			}
		});
		aiCardAction.setRepeats(false);
		aiCardAction.start();
	}
	public void delayedAIDumpAction() {
		aiDumpAction = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				game.dump();
			}
		});
		aiDumpAction.setRepeats(false);
		aiDumpAction.start();
	}
	public void delayedAIKoikoiAction(boolean answer) {
		aiKoikoiAction = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gameFrame.eraseKoikoiPopup();
				game.koikoi(answer);
			}
		});
		aiKoikoiAction.setRepeats(false);
		aiKoikoiAction.start();
	}
	
}
