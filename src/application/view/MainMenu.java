package application.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.controller.MenuController;
import application.model.Game;

//pannello principale del gioco
public class MainMenu extends JPanel{
	private static final long serialVersionUID = -4538518220054972545L;
	private JLabel gameName;
	private JButton playButton;
	private JButton settingsButton;
	private JButton exitButton;
	private Dimension buttonDim;
	
	public MainMenu() {
		gameName = new JLabel(Game.getInstance().getGameName());
		playButton = new JButton("Play");
		settingsButton = new JButton("Settings");
		exitButton = new JButton ("Quit");
		buttonDim= new Dimension(300, 120);
		
		playButton.setBackground(Colors.BUTTON_DEFAULT_COLOR);
		settingsButton.setBackground(Colors.BUTTON_DEFAULT_COLOR);
		exitButton.setBackground(Colors.BUTTON_DEFAULT_COLOR);
		
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		gameName.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		playButton.setMaximumSize(buttonDim);
		settingsButton.setMaximumSize(buttonDim);
		exitButton.setMaximumSize(buttonDim);
		gameName.setMaximumSize(new Dimension(500, 120));
		
		gameName.setFont(new Font("Copperplate gothic bold", Font.BOLD, 40));
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(gameName);
		this.add(Box.createRigidArea(new Dimension(0, 50)));
		this.add(playButton);
		this.add(Box.createVerticalStrut(30));
		this.add(settingsButton);
		this.add(Box.createVerticalStrut(30));
		this.add(exitButton);
		this.add(Box.createRigidArea(new Dimension(0, 200)));
		
		this.setBackground(Colors.MAP_COLOR);
		this.setFocusable(true);
		setActionCommands();
	}
	
	public void setActionCommands() {
		playButton.setActionCommand("Play");
		settingsButton.setActionCommand("Options");
		exitButton.setActionCommand("Quit");
	}
	
	
	public void setController(MenuController controller) {
		playButton.addActionListener(controller);
		settingsButton.addActionListener(controller);
		exitButton.addActionListener(controller);
	}
	
	public JButton getPlayButton() {
		return playButton;
	}
	
	public JButton getSettingsButton() {
		return settingsButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}
	
	
	public void startGame() {
		
	}
}