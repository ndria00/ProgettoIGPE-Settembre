package application.model;

import java.util.ArrayList;

//rappresenta l'id (corrispondente alla chiave della missione nella base di dati)
//delle missioni che il personaggio ha attive
public class ActiveMissions extends ArrayList<Integer>{

	private static final long serialVersionUID = -5673007159493718949L;

	private boolean isActive;
	
	public ActiveMissions() {
		super();
		isActive = true;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void switchActiveState() {
		isActive = !isActive;
	}
}
