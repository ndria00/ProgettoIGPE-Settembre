package application.model;

import java.util.ArrayList;

import application.Settings;
import application.db.DBConnection;

//rappresenta l'entità del gioco attraverso la quale il giocatore gestisce le missioni
public class NPC extends GameObject{
	
	private ArrayList<Mission> missions;
	private String name;
	private ActionString actionString;
	
	public NPC() {
			super(Settings.NPC_X, Settings.NPC_Y);
			this.name = Settings.NPC_STRING_NAME;
			missions = new ArrayList<Mission>();
			actionString = new ActionString();
	}
	
	//assegna alle missioni quelle lete dal DB
	public void readMissions(DBConnection conn) {
			missions = conn.getMissions();
	}
	
	//completa la missione con l'id specificato
	public void completeMission(int id) {
		for(int i = 0; i < missions.size(); ++i) {
			if(missions.get(i).getId() == id) {
				missions.get(i).completed();
				
				missions.get(i).setAssigned(false);
				System.out.println("Mission: " + missions.get(i).getId() + ". Mission complete!");
			}
		}
	}
	
	//restituisce la missione con l'id specificato
	public Mission getMission(int id) {
		
		for(int i = 0; i < missions.size(); ++i) {
			if(missions.get(i).getId() == id) {
				return missions.get(i);
				
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Mission> getMissions(){
		return missions;
	}
	
	//restituisce la prima missione diponibile (in termini di livello) al giocatore
	public Mission getAvailableMission(int characterLevel) {
		for(int i=0; i< missions.size(); ++i) {
			if(!missions.get(i).isCompleted() && !missions.get(i).isAssigned() && characterLevel >= missions.get(i).getMinLevel())
				return missions.get(i);
		}
		return  null;
	}
	
	//restituisce le missioni attive per il giocatore
	public ActiveMissions getAssignedMissionId(int characterLevel) {
		ActiveMissions activeMissions = new ActiveMissions();
		for(int i=0; i< missions.size(); ++i) {
			if(missions.get(i).isAssigned() && !missions.get(i).isCompleted())
				activeMissions.add(missions.get(i).getId());
		}
		return activeMissions;
	}
	
	public ActionString getActionString() {
		return actionString;
	}
	
	//controlla se l'item passato è tra quelli richiesti da una missione attiva
	public boolean assignedMission(int itemType) {
		for(int i=0; i< missions.size(); ++i) {
			if(missions.get(i).isAssigned() && !missions.get(i).isCompleted() && missions.get(i).getRequiredItem() == itemType)
				return true;
		}
		return false;
	}
	
}