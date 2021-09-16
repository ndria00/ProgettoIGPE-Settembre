package application.model;

//rappresenta le azioni che possono essere effettuate dal personaggio
//e con quali tasti/combinazione di tasti
public class GameSetting {
	
	private String description;
	private String associatedKey;
	
	public GameSetting(String description, String associatedKey){
		this.description  = description;
		this.associatedKey = associatedKey;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getAssociatedKey() {
		return associatedKey;
	}

}