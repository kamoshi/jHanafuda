package gui.game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.card.CardFactory;
import gui.card.ICard;
import logic.Controller;
import structure.Card;
import utils.Resolution;

@SuppressWarnings("serial")
public class CollectedPanel extends JPanel {
	
	// STUFF
	private final HashSet<Integer> addedID = new HashSet<>();
	private final int rowHeight;
	private final int rowWidth;
	
	private final JPanel panel20;
	private final JPanel panel10;
	private final JPanel panel5;
	private final JLayeredPane panel1;
	
	private int panel1offset = 0;
	
	
	// CONSTRUCTOR
	public CollectedPanel(Resolution res, boolean dividerUp) {
		final int calcheight = (res.screenHeight - (res.topMargin *2))/2;
		setBorder(new EmptyBorder(0, 0, 0, res.padding*2));
		setPreferredSize(new Dimension(0, (res.screenHeight - (res.topMargin *2))/2));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		rowHeight = (calcheight)/4 - 2;
		rowWidth = res.rightMargin - res.padding*2;
		
		if (!dividerUp) this.add(Box.createRigidArea(new Dimension(5,5)));
		
		final Dimension dimensionBluePrint = new Dimension(0, rowHeight);
		
		panel20 = new JPanel();
		panel20.setPreferredSize(dimensionBluePrint);
			final FlowLayout layout20 = new FlowLayout(FlowLayout.RIGHT);
			layout20.setVgap(2);
			layout20.setHgap(1);
		panel20.setLayout(layout20);
		this.add(panel20);
		
		panel10 = new JPanel();
		panel10.setPreferredSize(dimensionBluePrint);
			final FlowLayout layout10 = new FlowLayout(FlowLayout.RIGHT);
			layout10.setVgap(2);
			layout10.setHgap(1);
		panel10.setLayout(layout10);
		this.add(panel10);
		
		panel5 = new JPanel();
		panel5.setPreferredSize(dimensionBluePrint);
			final FlowLayout layout5 = new FlowLayout(FlowLayout.RIGHT);
			layout5.setVgap(2);
			layout5.setHgap(1);
		panel5.setLayout(layout5);
		this.add(panel5);
		
		panel1 = new JLayeredPane();
		panel1.setPreferredSize(dimensionBluePrint);
		this.add(panel1);
		
		if (dividerUp) this.add(Box.createRigidArea(new Dimension(5,5)));
	}
	
	// METHODS
	/**
	 * Method that adds cards to collected area of the GUI, if they haven't been added yet.
	 * @param controller	Controller used for detecting clicks
	 * @param cards			Array of cards to show
	 * @param cardWidth		Card width
	 * @param cardHeight	card width
	 */
	public void updateVCards(Controller controller, Card[] cards) {
		final CardFactory cf = new CardFactory();
		final int calcWidth = ((rowHeight)*6)/10 -2;
		final int calcHeight = rowHeight -3;
		
		for (Card card : cards) {
			if (!addedID.contains(card.getCardID())) {
				addedID.add(card.getCardID());
				final ICard iCard = cf.createCard(CardFactory.Mode.Passive, controller, card, calcWidth, calcHeight, true, 0);
				
				switch (card.value) {
					case 20:	add20(iCard);	break;
					case 10:	add10(iCard);	break;
					case 5:		add5(iCard);	break;
					case 1:		add1(iCard, calcWidth, calcHeight);	break;
				}
			}
		}
	}
	private void add20(ICard iCard) {
		panel20.add((Component) iCard);
	}
	private void add10(ICard iCard) {
		panel10.add((Component) iCard);
	}
	private void add5(ICard iCard) {
		panel5.add((Component) iCard);
	}
	private void add1(ICard iCard, int calcWidth, int calcHeight) {
		((Component)iCard).setBounds((rowWidth - calcWidth) - (calcWidth/2 * panel1offset)-1, 0, calcWidth, calcHeight);
		panel1.add((Component) iCard, new Integer(panel1offset));
		panel1offset++;
	}

	/**
	 * Resets the GUI collected area
	 */
	public void clear() {
		panel20.removeAll();
		panel20.revalidate();
		panel20.repaint();
		panel10.removeAll();
		panel10.revalidate();
		panel10.repaint();
		panel5.removeAll();
		panel5.revalidate();
		panel5.repaint();
		panel1.removeAll();
		panel1.revalidate();
		panel1.repaint();
		addedID.clear();
		panel1offset = 0;
	}

}
