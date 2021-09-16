package application.model;

import java.util.ArrayList;

//rappresenta i messaggi di aiuto che possono essere visualizzati dal giocatore
//in fase di gioco, i quali suggeriscono come effettuare determinate azioni
public class HelpSettings {
	private ArrayList<String> helpMessages;
	private boolean isOpen;
	
	public HelpSettings(ArrayList<String> helpMessages, boolean isOpen) {
		this.helpMessages = helpMessages;
		this.isOpen= isOpen;
	}
	
	public ArrayList<String> getHelpMessages(){
		return helpMessages;
	}
	public boolean isOpen() {
		return isOpen;
	}
	
	public void switchOpenState() {
		isOpen= !isOpen;
	}
	
}
