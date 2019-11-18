package gui.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import gui.card.CardFactory;
import gui.card.CardFactory.Mode;
import gui.card.ICard;
import logic.Controller;
import structure.Card;
import structure.Yaku;
import utils.Resolution;

@SuppressWarnings("serial")
public class DeckPanel extends JPanel {
	
	// PROPERTIES
	private final JPanel cardContainer = new JPanel();
	private final int cardWidth;
	private final int cardHeight;
	
	// CONSTRUCTOR
	public DeckPanel(Resolution res) {
		setLayout(new GridBagLayout());
		
		this.cardWidth = res.cardWidth;
		this.cardHeight = res.cardHeight;
		
		//setBorder(new EmptyBorder(res.padding, res.padding, res.padding, res.padding));
		
		add(cardContainer);
		cardContainer.setBackground(Color.LIGHT_GRAY);
		cardContainer.setPreferredSize(new Dimension(res.cardWidth*9 + res.padding, res.cardHeight + res.padding));
	}
	
	// METHODS
	
	/**
	 * Generates visual cards for player.
	 * @param cards		The list of cards to generate in GUI.
	 * @param context	Context: 1 -> player1 / 2 -> player2
	 */
	public void generateVCards(Controller controller, Card[] cards, int context, boolean active) {
		final CardFactory cf = new CardFactory(); Mode mode;
		if (active) mode = Mode.Active; else mode = Mode.Passive;
		
		for (Card card : cards) {
			final ICard iCard = cf.createCard(mode, controller, card, cardWidth, cardHeight, true, context);
			cardContainer.add((Component) iCard);
		}
	}
	
	/**
	 * Removes VCards that are not needed.
	 * @param cards		The list of cards that are needed.
	 */
	public void removeUnneededVCards(Card[] cards) {
		final Component[] children = cardContainer.getComponents();
		for (Component component : children) {
			if (component instanceof ICard) {
				final ICard iCard = (ICard) component;
				if (!Yaku.containsID(cards, iCard.getCard().getCardID())) {
					component.setVisible(false);
					component.setEnabled(false);
					cardContainer.remove(component);
				}
			}
		}
	}
	
	public void clear() {
		cardContainer.removeAll();
		cardContainer.revalidate();
		cardContainer.repaint();
	}
	
}
