package gui.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.card.ActiveCard;
import gui.card.CardFactory;
import gui.card.ICard;
import logic.Controller;
import structure.Card;
import structure.Yaku;
import utils.Resolution;

@SuppressWarnings("serial")
public final class TablePanel extends JPanel {
	
	// PROPERTIES
	private final JPanel cardContainer = new JPanel();
	
	private final int cardWidth;
	private final int cardHeight;
	
	private JButton dumpButton;	
	
	
	// CONSTRUCTOR
	public TablePanel(Resolution res) {
		this.cardWidth = res.cardWidth;
		this.cardHeight = res.cardHeight;
		
		setBorder(new EmptyBorder(res.padding, 0, res.padding, 0));
		setLayout(new GridBagLayout());

		this.add(cardContainer );
		cardContainer.setBackground(Color.LIGHT_GRAY);
		cardContainer.setPreferredSize(new Dimension(res.screenWidth - (res.leftMargin + res.rightMargin + res.padding*2), (2 * res.cardHeight + 20)));
		
		cardContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	// METHODS
	
	/**
	 * Updating cards in the table area. Removing old, adding new.
	 * @param cards		Cards that need to be presented on the table.
	 * @param faceup	Whether they need to be generated faceup or not.
	 */
	public void updateVCards(Controller controller, Card[] cards, boolean faceup) { // Context 3
		System.out.println("[GUI] Updating table contents");
		final CardFactory cf = new CardFactory();
		
		if (dumpButton != null) {
			dumpButton.setVisible(false);
			dumpButton.setEnabled(false);
			cardContainer.remove(this.dumpButton);
			dumpButton = null;
		}
		
		// Removing cards
		final Component[] children1 = cardContainer.getComponents();
		for (Component component : children1) {
			if (component instanceof ActiveCard) {
				final ActiveCard vcard = (ActiveCard) component;
				if (!Yaku.containsID(cards, vcard.getCard().getCardID())) {
					component.setVisible(false);
					component.setEnabled(false);
					cardContainer.remove(component);
				}
			}
		}
		
		// Adding cards
		final HashSet<Integer> tempList = new HashSet<>();
		
		final Component[] children2 = cardContainer.getComponents();
		for (Component component : children2) {
			if(component instanceof ICard) {
				tempList.add(((ICard)component).getCard().getCardID());
			}
		}
		
		for (Card card : cards) {
			if (!tempList.contains(card.getCardID())) {
				final ICard iCard = cf.createCard(CardFactory.Mode.Active, controller, card, cardWidth, cardHeight, faceup, 3);
				cardContainer.add((Component) iCard);
			}
		}
		
		// Dump
		createDumpSpace(controller);
	}
	
	public void clear() {
		cardContainer.removeAll();
		cardContainer.revalidate();
		cardContainer.repaint();
	}
	
	public void setFaceup(Card card) {
		for (Component component : this.cardContainer.getComponents()) {
			if (component instanceof ActiveCard) {
				final ActiveCard vcard = (ActiveCard) component;
				if (vcard.getCard().equals(card)) {
					vcard.setFaceup();
				}
			}
		}
	}
	
	/**
	 * Method that instantiates the "dump" button for when card can't be matched with any card.
	 */
	public void createDumpSpace(Controller controller) {
		if (dumpButton == null) {
			this.dumpButton = new JButton("+");
			dumpButton.setPreferredSize(new Dimension(cardWidth, cardHeight));
			cardContainer.add(dumpButton);
			dumpButton.setEnabled(false);
			dumpButton.setVisible(false);
			dumpButton.addActionListener( new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					controller.eventDumpButtonPressed();
				}
			});
		}
	}
	
	/**
	 * Enables the "dump" button.
	 */
	public void enableDump() {
		dumpButton.setEnabled(true);
		dumpButton.setVisible(true);
	}
	/**
	 * Disables the "dump" button.
	 */
	public void disableDump() {
		dumpButton.setEnabled(false);
		dumpButton.setVisible(false);
	}
}
