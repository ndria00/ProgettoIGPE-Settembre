package application.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import application.model.Game;
import application.view.GameWindow;
import application.view.MainMenu;

//gestisce le azioni dell'utente mentre interagisce con il menu del gioco
public class MenuController implements ActionListener{

	private GameWindow gameWindow;
	private GameController gameController;
	
	public MenuController(GameWindow gameWindow, GameController gameController) {
		this.gameWindow = gameWindow;
		this.gameController = gameController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MainMenu mainMenu = gameWindow.getMainMenu();
		
		//chiusura del gioco
		if(e.getActionCommand().equals(mainMenu.getExitButton().getActionCommand())) {
			int result = JOptionPane.showOptionDialog(mainMenu, "Are you sure you want to quit?", null, JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, null, null, e);
			if(result == JOptionPane.YES_OPTION)
				System.exit(0);
		}
		else if(e.getActionCommand().equals(mainMenu.getPlayButton().getActionCommand())) {

			gameWindow.getProfilesPanel().getAvailableProfilesPanel().updateAvailableProfiles((Game.getInstance().getConnectionDB().getAvailableProfiles()));
			gameWindow.switchToPanel("profiles panel");
		}
		//inizio di una partita con il profilo selezionato
		else if(e.getActionCommand().equals(gameWindow.getProfilesPanel().getAvailableProfilesPanel().getStartGameButton().getActionCommand())){
			if(gameWindow.getProfilesPanel().getAvailableProfilesPanel().getAvailableProfiles().isSelectionEmpty()) {
				JOptionPane.showMessageDialog(mainMenu, "No profile selected");
			}
			else {
				String profile = gameWindow.getProfilesPanel().getAvailableProfilesPanel().getAvailableProfiles().getSelectedValue();
				Game.getInstance().continueExistingGame(profile);
				//
				if(gameWindow.getWindowState().isExtended() ^ gameWindow.getGamePanel().getWindowState().isExtended())
					gameWindow.getGamePanel().getWindowState().switchWindowState();
				
				gameController.gameStarted();
				gameWindow.switchToPanel("game panel");
			}
		}
		else if(e.getActionCommand().equals(gameWindow.getMainMenu().getSettingsButton().getActionCommand())) {
			System.out.println("switching to settings panel");
			gameWindow.switchToPanel("settings panel");
		}
		//aggiunta di un nuovo profilo
		else if(e.getActionCommand().equals(gameWindow.getProfilesPanel().getAddNewProfilePanel().getAddButton().getActionCommand())) {
			System.out.println("adding a new profile");
			String newProfileName = gameWindow.getProfilesPanel().getAddNewProfilePanel().getNewProfileName().getText();
			gameWindow.getProfilesPanel().getAddNewProfilePanel().getNewProfileName().setText("");
			//casella del nome vuota
			if(newProfileName.equals("")) {
				JOptionPane.showMessageDialog(mainMenu, "profile name must not be empty");
			}
			//profilo già esistente
			else if(gameWindow.getProfilesPanel().getAvailableProfilesPanel().getListModel().contains(newProfileName)){
				JOptionPane.showMessageDialog(mainMenu, "profile " + newProfileName + " already exists ");
			}
			//creazione profilo
			else {
				Game.getInstance().createNewProfile(newProfileName);
				//update dei profili disponibili
				gameWindow.getProfilesPanel().getAvailableProfilesPanel().updateAvailableProfiles((Game.getInstance().getConnectionDB().getAvailableProfiles()));
				//selezione automatica del profilo appena creato
				gameWindow.getProfilesPanel().getAvailableProfilesPanel().getAvailableProfiles().setSelectedValue(newProfileName, false);
				
			}
				
		}
		//torna al main menu
		else if(e.getActionCommand().equals(gameWindow.getProfilesPanel().getBackToMenu().getActionCommand())) {
			switchToMainMenu();
			gameWindow.getProfilesPanel().getAddNewProfilePanel().getNewProfileName().setText("");
		}
		else if(e.getActionCommand().equals(gameWindow.getSettingsPanel().getBackToMenu().getActionCommand())){
			switchToMainMenu();
		}
	}
	
	public void switchToMainMenu() {
		System.out.println("Switching to main menu");
		gameWindow.switchToPanel("main menu");
	}
	
}
