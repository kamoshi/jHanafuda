package gui.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import structure.Card;
import utils.Resolution;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public final class LeftPanel extends JPanel {
	
	// PROPERTIES
	private final Resolution res;
	
	private final JLabel textDisplayTop;
	private final JLabel textDisplayBot;
	private final JLabel cardStack;
	
	// CONSTRUCTOR
	public LeftPanel(Resolution res) {
		
		// Dimensions
		this.res = res;
		this.setPreferredSize(new Dimension(res.leftMargin, 0));
		this.setLayout(new BorderLayout());

		// Sections
		final JPanel panelTop = new JPanel();
		add(panelTop, BorderLayout.NORTH);
		final JPanel panelCen = new JPanel(); panelCen.setLayout(new GridBagLayout());
		add(panelCen, BorderLayout.CENTER);
		final JPanel panelBot = new JPanel();
		add(panelBot, BorderLayout.SOUTH);
		
		
		// Mutable
		textDisplayTop = new JLabel();
		textDisplayBot = new JLabel();
		cardStack = new JLabel();
		
		// TOP -------------------------------
		panelTop.add(textDisplayTop);
		textDisplayTop.setVerticalAlignment(SwingConstants.TOP);
		textDisplayTop.setPreferredSize(new Dimension(res.leftMargin, res.cardHeight*2));
		// CENTER ----------------------------
		final JPanel cardStackSlot = new JPanel();
		panelCen.add(cardStackSlot);
		cardStackSlot.setPreferredSize(new Dimension(res.cardWidth, res.cardHeight));
		cardStackSlot.setLayout(new BorderLayout());
		
		cardStackSlot.add(cardStack, BorderLayout.CENTER);
		// BOTTOM ----------------------------
		panelBot.add(textDisplayBot);
		textDisplayBot.setVerticalAlignment(SwingConstants.BOTTOM);
		textDisplayBot.setPreferredSize(new Dimension(res.leftMargin, res.cardHeight*2));
	}
	
	
	// Card stack
	public void setCardStackDown() {
		cardStack.setOpaque(true);
		cardStack.setIcon(ResourceLoader.requestImage("cards/cardback.png", res.cardWidth, res.cardHeight));
	}
	public void setCardStackUp(Card card) {
		cardStack.setOpaque(true);
		cardStack.setIcon(ResourceLoader.requestImage("cards/" + ResourceLoader.getCardSet() + "/" + card.name().toLowerCase() + ".png", res.cardWidth, res.cardHeight));
	}
	public void setCardStackEmpty() {
		cardStack.setVisible(false);
	}
	
	// Info messages
	/**
	 * Updates information in the top left corner of the screen.
	 * @param message	The information to show.
	 */
	public void setMessageTop(String message) {
		textDisplayTop.setText("<html>" + message + "</html>");
	}
	/**
	 * Updates information in the bottom left corner of the screen.
	 * @param message	The information to show.
	 */
	public void setMessageBot(String message) {
		textDisplayBot.setText("<html>" + message + "</html>");
	}
	
}
