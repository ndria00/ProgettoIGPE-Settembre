package application.model;

//rappresenta le missioni che vengono date dall'NPC
//al giocatore il quale ha la possibilità di svolgere e completare
public class Mission {
	
	private int id;
	private String name;
	private String description;
	private int minLevel;
	private boolean rewardType;
	private int rewardQuantity;
	private boolean completed;
	private boolean assigned;
	private int required_item;
	
	//crea una missione in base ai dati passati (lettura della missione dal DB)
	public Mission(int id, String name, String description, int livMin, boolean done, boolean rewardType, int rewardQuantity, int requiredItem, boolean assigned) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.rewardType = rewardType;
		this.rewardQuantity = rewardQuantity;
		this.assigned = assigned;
		completed = false;
		this.required_item = requiredItem;
		this.minLevel = livMin;
	}
	
	//crea una missione da una stringa di testo non utilizzato
	//utile in caso di lettura direttamente da file delle missioni
	public Mission(String text) {
		String [] result= text.split(";");
		if(result.length < 7)
			System.exit(1);
		
		this.id = Integer.parseInt(result[0]);
		this.name = result[1];
		this.description = result[2];
		this.rewardType = Boolean.parseBoolean(result[3]);
		this.rewardQuantity = Integer.parseInt(result[4]);	
		this.required_item = Integer.parseInt(result[5]);
		this.minLevel = Integer.parseInt(result[6]);
		
		completed = false;
		assigned = false;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getRewardQuantity() {
		return rewardQuantity;
	}
	
	public boolean getRewardType(){
		return rewardType;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void completed() {
		completed=true;
	}
	
	public boolean isAssigned() {
		return assigned;
	}
	public void assignedMission() {
		assigned = true;
	}
	
	public void setAssigned (boolean assigned) {
		this.assigned = assigned;
	}
	public int getRequiredItem() {
		return required_item;
	}
	public int getMinLevel() {
		return minLevel;
	}
}