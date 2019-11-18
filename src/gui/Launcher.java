package gui;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import logic.Controller;
import utils.Resolution;
import utils.ResourceLoader;

@SuppressWarnings("serial")
public class Launcher extends JFrame {
	
	// PROPERTIES
	private Controller controller;
	private final int frameWidth = 300;
	private final int frameHeight = 460;
	// Data inputs ---------
	private final JTextField player1Name;
	private final JTextField player2Name;
	private JCheckBox player2AICheck;
	private JComboBox<String> player2AIStrategy;
	private final JComboBox<String> cardStyle;
	private final ButtonGroup roundNumberGroup;
	private final JComboBox<String> languageSelector;
	private final JComboBox<String> resolution;
	private final JButton play;
	// Labels i18n ---------
	private final JLabel cardStyleLabel;
	private final JLabel inputPlayer1WrapperLabel;
	private final JLabel inputPlayer2WrapperLabel;
	private final JLabel languageLabel;
	private final JLabel resolutionLabel;
	private final JLabel inputGameRoundsLabel;
	
	
	// CONSTRUCTOR
	public Launcher() {
		super("Launcher");
		ResourceLoader.setLocale(0);
		// setup
		this.controller = new Controller(this);
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
		catch (Exception e) {  e.printStackTrace(); }
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(frameWidth, frameHeight));
		this.setResizable(false);
		this.setIconImage(ResourceLoader.requestImage("img/icon.png", 80, 100).getImage());
		this.setLocationRelativeTo(null);
		
		this.setVisible(true);
		
		
		
		player1Name					= new JTextField();	// Player 1 name
		player2Name					= new JTextField();	// Player 2 name
		cardStyle					= new JComboBox<>();// Card style
		roundNumberGroup			= new ButtonGroup();// Rounds
		languageSelector			= new JComboBox<>();// Language
		resolution					= new JComboBox<>();// Resolution
		play						= new JButton();	// Play button
		// Labels i18n ---------
		cardStyleLabel				= new JLabel();		// cardStyle
		inputPlayer1WrapperLabel	= new JLabel();		//
		inputPlayer2WrapperLabel	= new JLabel();		//
		languageLabel				= new JLabel();		//
		resolutionLabel				= new JLabel();		//
		inputGameRoundsLabel		= new JLabel();		// 
		
		initLayout();
	}
	
	/**
	 * Initializes literally everything within the frame.
	 */
	private void initLayout() {
		
		JPanel contentWrapper = new JPanel();
		contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
		
		this.getContentPane().add(contentWrapper);
		
		// Logo image setup ------------------
		JLabel logo = new JLabel();
		logo.setOpaque(true);
		contentWrapper.add(logo);
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);
		logo.setPreferredSize(new Dimension(250,100));
		logo.setMinimumSize(new Dimension(250,100));
		logo.setMaximumSize(new Dimension(250,100));
		
		logo.setIcon(ResourceLoader.requestImage("img/logo.png", 250, 100));
		
		contentWrapper.add(Box.createRigidArea(new Dimension(0,20)));
		
		// Inner wrapper setup ------------------
		JPanel optionsWrapper = new JPanel();
		contentWrapper.add(optionsWrapper);
		optionsWrapper.setLayout(new BoxLayout(optionsWrapper, BoxLayout.Y_AXIS));
		// Inner wrapper setup ------------------
		
		// Player1 name input section ------------------
		JPanel inputPlayer1Wrapper = new JPanel();
		optionsWrapper.add(inputPlayer1Wrapper);
		inputPlayer1Wrapper.setMaximumSize(new Dimension(500, 35));
		
		inputPlayer1Wrapper.add(inputPlayer1WrapperLabel);
		
		inputPlayer1Wrapper.add(player1Name);
		player1Name.setPreferredSize(new Dimension(100, 20));
		// Player1 name input section ------------------
		
		// Player2 name input section ------------------
		JPanel inputPlayer2Wrapper = new JPanel();
		optionsWrapper.add(inputPlayer2Wrapper);
		inputPlayer2Wrapper.setMaximumSize(new Dimension(500, 30));

		
		inputPlayer2Wrapper.add(inputPlayer2WrapperLabel);
		
		inputPlayer2Wrapper.add(player2Name);
		player2Name.setPreferredSize(new Dimension(100, 20));
		
		JPanel inputPlayer2WrapperAISplitter = new JPanel();
		optionsWrapper.add(inputPlayer2WrapperAISplitter);
		inputPlayer2WrapperAISplitter.setMaximumSize(new Dimension(500, 30));
		
		inputPlayer2WrapperAISplitter.add(cardStyleLabel);
		inputPlayer2WrapperAISplitter.add(Box.createHorizontalStrut(2));
		
		cardStyle.addItem("Standard");
		cardStyle.addItem("Vocaloid");
		inputPlayer2WrapperAISplitter.add(cardStyle);
		// Player2 name input section ------------------
		
		// AI section ----------------------------------
		JPanel aiWrapper = new JPanel();
		aiWrapper.setLayout(new BoxLayout(aiWrapper, BoxLayout.X_AXIS));
		optionsWrapper.add(aiWrapper);
		
		aiWrapper.add(Box.createHorizontalGlue());
		
		player2AICheck = new JCheckBox("Enable AI"); // i18n
		player2AICheck.setHorizontalTextPosition(SwingConstants.LEFT);
		aiWrapper.add(player2AICheck);
		
		aiWrapper.add(Box.createHorizontalStrut(10));
		
		player2AIStrategy = new JComboBox<String>();
		player2AIStrategy.addItem("SimpleAI");
		player2AIStrategy.addItem("AnalysisAI");
		aiWrapper.add(player2AIStrategy);
		player2AIStrategy.setEnabled(false);
		
		aiWrapper.add(Box.createHorizontalGlue());
		// AI section ----------------------------------
		
		// Number of rounds ------------------
		JPanel inputGameRounds = new JPanel();
		optionsWrapper.add(inputGameRounds);
		inputGameRounds.setMaximumSize(new Dimension(500, 30));
		
		inputGameRounds.add(inputGameRoundsLabel);
		
		JRadioButton oneRoundButton = new JRadioButton("1");
		inputGameRounds.add(oneRoundButton);
		oneRoundButton.setActionCommand("1");
		
		JRadioButton threeRoundButton = new JRadioButton("3");
		inputGameRounds.add(threeRoundButton);
		threeRoundButton.setActionCommand("3");
		
		JRadioButton sixRoundButton = new JRadioButton("6");
		inputGameRounds.add(sixRoundButton);
		sixRoundButton.setActionCommand("6");
		
		roundNumberGroup.add(oneRoundButton);
		roundNumberGroup.add(threeRoundButton);
		roundNumberGroup.add(sixRoundButton);
		
		threeRoundButton.setSelected(true);
		// Number of rounds ------------------
		
		JSeparator sep2 = new JSeparator();
		sep2.setMaximumSize(new Dimension(200, 5));
		optionsWrapper.add(sep2);
		
		// Advanced settings ------------------
		JPanel languageSettingsWrapper = new JPanel();
		optionsWrapper.add(languageSettingsWrapper);
		languageSettingsWrapper.setMaximumSize(new Dimension(500, 35));
		
		languageSettingsWrapper.add(languageLabel);
		
		languageSettingsWrapper.add(languageSelector);
		languageSelector.addItem("English");
		languageSelector.addItem("Polski");
		languageSelector.addItem("\u65E5\u672C\u8A9E");
		
		
		JPanel advancedSettingsWrapper = new JPanel();
		optionsWrapper.add(advancedSettingsWrapper);
		advancedSettingsWrapper.setMaximumSize(new Dimension(500, 35));
		
		advancedSettingsWrapper.add(resolutionLabel);
		
		resolution.addItem("Default");
		
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		for (Resolution res : Resolution.values()) {
			if(res.screenWidth < dim.getWidth() && res.screenHeight < dim.getHeight()) {
				resolution.addItem(res.screenWidth + "x" + res.screenHeight);
			}
		}
		
		
		
		advancedSettingsWrapper.add(resolution);
		
		// Advanced settings ------------------
		

		// Play button ------------------
		JPanel playButtonWrapper = new JPanel();
		optionsWrapper.add(playButtonWrapper);
		playButtonWrapper.setLayout(new BorderLayout());
		
		playButtonWrapper.add(play, BorderLayout.PAGE_END);
		// Play button ------------------
		
		// Assign listeners
		this.play.addActionListener( new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	controller.eventRequestGame(player1Name.getText(), player2Name.getText(), cardStyle.getSelectedItem().toString().toLowerCase(), Integer.parseInt(roundNumberGroup.getSelection().getActionCommand()), resolution.getSelectedItem().toString(), player2AICheck.isSelected(), player2AIStrategy.getSelectedItem().toString());
		    }
		});
		this.languageSelector.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				ResourceLoader.setLocale(languageSelector.getSelectedIndex());
				updateLocale();
			}
		});
		this.player2AICheck.addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (player2AICheck.isSelected()) {
            		player2Name.setEnabled(false);
            		player2AIStrategy.setEnabled(true);
            	} else {
            		player2Name.setEnabled(true);
            		player2AIStrategy.setEnabled(false);
            	}
            }
        });
		
		this.updateLocale();
	}
	
	/**
	 * Updates UI of relevant elements to match the locale.
	 */
	public void updateLocale() {
		inputPlayer1WrapperLabel.setText(ResourceLoader.localize("launcher_player1_label"));
		inputPlayer2WrapperLabel.setText(ResourceLoader.localize("launcher_player2_label"));
		languageLabel.setText(ResourceLoader.localize("launcher_language_label"));
		resolutionLabel.setText(ResourceLoader.localize("launcher_resolution_label"));
		cardStyleLabel.setText(ResourceLoader.localize("launcher_set_label"));
		inputGameRoundsLabel.setText(ResourceLoader.localize("launcher_rounds_label"));
		play.setText(ResourceLoader.localize("launcher_play_button"));
		player2AICheck.setText(ResourceLoader.localize("launcher_enableai_label"));
	}
}
