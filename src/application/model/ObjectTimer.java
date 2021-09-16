package application.model;

import application.Settings;

//rappresenta i timer utilizzati per gestire le varie azioni
//che richiedono un intervallo temporale
public class ObjectTimer {
	//tempo di attesa prima di poter effettuare l'azione
	private long cooldown;
	private long timeSinceLastAction = cooldown;

	//crea un nuovo timer dove si assume che l'azione
	//in considerazione è stata appena compiuta
	public ObjectTimer(long cooldown) {
		this.cooldown = cooldown;	
		timeSinceLastAction = 0;
	}
	
	//restituisce se l'azione può essere compiuta
	public boolean canDoAction() {
		if(timeSinceLastAction < cooldown)
			return false;
		timeSinceLastAction = 0;
		return true;
		
	}
	
	public void reset() {
		actionDone();
	}
	
	//una volta effettuata l'azione il timer riparte da zero
	public void actionDone() {
		timeSinceLastAction = 0;
	}
	
	//fa avanzare il timer
	public void tick() {
		if(timeSinceLastAction < cooldown) {
			timeSinceLastAction += Settings.GAME_FREQUENCY;
		}
	}
	
	//tempo trascorso dall'ultima azione
	public long getValue() {
		return timeSinceLastAction;
	}
}
