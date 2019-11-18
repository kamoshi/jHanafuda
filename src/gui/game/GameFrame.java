package gui.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import logic.Controller;
import utils.Resolution;
import structure.Card;
import utils.ResourceLoader;


/**
 * Game window frame container.
 */@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	// PROPERTIES
		private final Controller controller;
		private final Resolution res;
		
		private final LeftPanel leftPanel;
		private final PlayerPanel plr1Panel;
		private final PlayerPanel plr2Panel;
		private final DeckPanel plr1Deck;
		private final DeckPanel plr2Deck;
		private final TablePanel table;
		private final CollectedPanel plr1Collected;
		private final CollectedPanel plr2Collected;
		
		private KoikoiPopup koikoiPopup;
		private ResultsPopup resultsPopup;
		
		// CONSTRUCTOR
		public GameFrame(Controller controller, Resolution res) {
			super(ResourceLoader.localize("gameapp_window_title"));
			// setup
			this.controller = controller;
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
			catch (Exception e) {  e.printStackTrace(); }
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setIconImage(ResourceLoader.requestImage("img/icon.png", 80, 100).getImage());
			this.setLocationRelativeTo(null);
			
			this.res = res;
			setSize(res.screenWidth, res.screenHeight);
			setMinimumSize(new Dimension(res.screenWidth, res.screenHeight));
			
			leftPanel = new LeftPanel(res);
			plr1Panel = new PlayerPanel(res, true);
			plr2Panel = new PlayerPanel(res, false);
			
			plr1Deck = new DeckPanel(res);
			plr2Deck = new DeckPanel(res);
			
			table  = new TablePanel(res);
			
			plr1Collected = new CollectedPanel(res, false);
			plr2Collected = new CollectedPanel(res, true);
			
			
			// size
			
			this.setResizable(true);
			
			this.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                controller.eventClosedGame();
	            }
	        });
			
			initLayout();
		}
		
		private void initLayout() {
			System.out.println("[GUI] Initalizing GFrame window");
			
			// Left Side
			this.add(leftPanel, BorderLayout.WEST);
			
			// Right Side Info
			final JPanel rightSide = new JPanel();
			this.add(rightSide, BorderLayout.EAST);
			rightSide.setPreferredSize(new Dimension(res.rightMargin, 0));
			rightSide.setLayout(new BorderLayout());
			rightSide.add(plr2Panel, BorderLayout.NORTH);
			rightSide.add(plr1Panel, BorderLayout.SOUTH);
			
			// Center Decks
			final JPanel centerContainer = new JPanel();
			this.add(centerContainer, BorderLayout.CENTER);
			centerContainer.setLayout(new BorderLayout());
			
			centerContainer.add(plr1Deck, BorderLayout.SOUTH);
			centerContainer.add(plr2Deck, BorderLayout.NORTH);
			
			// Center Cards
			centerContainer.add(table, BorderLayout.CENTER);
			
			// Right Side collected
			final JPanel collectedWrapper = new JPanel();
			collectedWrapper.setLayout(new BoxLayout(collectedWrapper, BoxLayout.Y_AXIS));
			rightSide.add(collectedWrapper, BorderLayout.CENTER);
			
			collectedWrapper.add(plr2Collected);
			collectedWrapper.add(plr1Collected);
			
		}
		
		
		
		// UPDATE UI INFO
		public void updateFeedbackTop(String s) {
			leftPanel.setMessageTop(s);
		}
		public void updateFeedbackBottom(String s) {
			leftPanel.setMessageBot(s);
		}
		public void updateNamePlayer1(String name) {
			plr1Panel.updateName(name);
		}
		public void updateNamePlayer2(String name) {
			plr2Panel.updateName(name);
		}
		public void updatePointsPlayer1(int points) {
			plr1Panel.updatePoints(points);
		}
		public void updatePointsPlayer2(int points) {
			plr2Panel.updatePoints(points);
		}
		
		// Card stack
		public void setCardStackDown() {
			leftPanel.setCardStackDown();
		}
		public void setCardStackUp(Card card) {
			leftPanel.setCardStackUp(card);
		}
		public void setCardStackEmpty() {
			leftPanel.setCardStackEmpty();
		}
		
		// ============================ CARD GENERATION ============================
		// Player 1
		public void generateVCardsForPlayer1Hand(Card[] cards, boolean active) {
			plr1Deck.generateVCards(controller, cards, 1, active);
		}
		public void updateVCardsForPlayer1Hand(Card[] cards) {
			plr1Deck.removeUnneededVCards(cards);
		}
		public void resetVCardsForPlayer1Hand() {
			plr1Deck.clear();
		}

		// Player 2
		public void generateVCardsForPlayer2Hand(Card[] cards, boolean active) {
			plr2Deck.generateVCards(controller, cards, 2, active);
		}
		public void updateVCardsForPlayer2Hand(Card[] cards) {
			plr2Deck.removeUnneededVCards(cards);
		}
		public void resetVCardsForPlayer2Hand() {
			plr2Deck.clear();
		}

		// COLLECTED
		// Player1
		public void updateVCardsForPlayer1Collected(Card[] cards) {
			plr1Collected.updateVCards(controller, cards);
		}
		public void resetVCardsForPlayer1Collected() {
			plr1Collected.clear();
		}
		// Player2
		public void updateVCardsForPlayer2Collected(Card[] cards) {
			plr2Collected.updateVCards(controller, cards);
		}
		public void resetVCardsForPlayer2Collected() {
			plr2Collected.clear();
		}
		
		// TABLE
		public void updateVCardsForTable(Card[] cards, boolean faceup) {
			table.updateVCards(controller, cards, faceup);
		}
		public void resetVCardsForTable() {
			table.clear();
		}
		public void createDumpSpace() {
			table.createDumpSpace(controller);
		}
		public void enableDump() {
			table.enableDump();
		}
		public void disableDump() {
			table.disableDump();
		}
		public void setFaceup(Card card) {
			table.setFaceup(card);
		}
		
		// KOIKOI POPUP
		public void generateKoikoiPopup(String plr, String yaku, int score, boolean active) {
			koikoiPopup = new KoikoiPopup(controller, res, plr, yaku, score, active);
			this.getLayeredPane().add(koikoiPopup, JLayeredPane.POPUP_LAYER);
		}
		public void eraseKoikoiPopup() {
			this.getLayeredPane().remove(koikoiPopup);
			this.getLayeredPane().revalidate();
			this.getLayeredPane().repaint();
		}
		
		// RESULTS POPUP
		public void generateResultsPopup(String plr1, String plr2, int score1, int score2) {
			resultsPopup = new ResultsPopup(controller, res, plr1, plr2, score1, score2);
			this.getLayeredPane().add(resultsPopup, JLayeredPane.POPUP_LAYER);
		}
		
		
		public void clear() {
			table.clear();
			plr1Deck.clear();
			plr1Collected.clear();
			plr2Deck.clear();
			plr2Collected.clear();
		}

		
}
