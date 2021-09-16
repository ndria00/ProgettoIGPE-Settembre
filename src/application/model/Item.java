package application.model;

import application.Settings;

//rappresenta un item del gioco con il suo stato e le sue caratteristiche
public class Item extends GameObject{
	public static final int NO_OWNER=-1;
	
	private boolean dropped;
	private boolean consumable;
	private int ownerId;
	private int itemType;
	private String name;
	private String description;
	private Hitbox hitbox;
	private ObjectTimer timer;
	
	public Item(int itemType) {
		this.itemType = itemType;
		ownerId = NO_OWNER;
		dropped=false;
		newItemByType(itemType);
	}
	
	//il secondo è un parametro  che serve solo a specificare che
	//l'item che si sta creando è di proprietà del giocatore
	public Item(int itemType, boolean characterIsOwner) {
		this.itemType=itemType;
		ownerId = Settings.ID_PLAYER;
		dropped = false;
		newItemByType(itemType);
	}
	
	//valorizzazione dei campi che dipendono dal tipo di item che si sta creando
	public void newItemByType(int itemType) {
		
		switch(itemType) {
		case ItemSettings.HP_POTION:
			name = ItemSettings.HP_POTION_NAME;
			description = ItemSettings.HP_POTION_DESCRIPTION;
			consumable = ItemSettings.HP_POTION_CONSUMABLE;
			break;
		case ItemSettings.EXP_POTION:
			name = ItemSettings.EXP_POTION_NAME;
			description = ItemSettings.EXP_POTION_DESCRIPTION;
			consumable = ItemSettings.EXP_POTION_CONSUMABLE;
			break;
		case ItemSettings.MEDICINE:
				name = ItemSettings.MEDICINE_NAME;
				description = ItemSettings.MEDICINE_DESCRIPTION;
				consumable = ItemSettings.MEDICINE_CONSUMABLE;
				break;
		case ItemSettings.ESSENCE:
			name = ItemSettings.ESSENCE_NAME;
			description = ItemSettings.ESSENCE_DESCRIPTION;
			consumable = ItemSettings.ESSENCE_CONSUMABLE;
			break;
	
	}
		
	}
	
	public int getOwnerId() {
		return ownerId;
	}
	
	public boolean isConsumable() {
		return consumable;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getItemType() {
		return itemType;
	}
	
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	//definizione delle coordinate dell'item che hanno un significato
	//solo quando l'item è a terra perché droppato dai mostri o buttato via dal giocatore
	private void setCoordinates(int x, int y) {
		timer= new ObjectTimer(Settings.OBJECT_DISAPPEAR_TIME);
		dropped=true;
		setX(x);
		setY(y);
		this.hitbox = new Hitbox(x, y, Settings.OBJECT_DEFAULT_SIZE, Settings.OBJECT_DEFAULT_SIZE);
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public void gathered(int ownerId) {
		timer=null;
		this.ownerId = ownerId;
		dropped = false;
		
	}
	
	public boolean isDropped() {
		return dropped;
	}
	
	
	public void drop(int x, int y) {
		setCoordinates(x, y);
		ownerId = -1;
	}
	
	public void tick() {
		if(timer != null)
			timer.tick();
	}
	
	public ObjectTimer getTimer(){
		return timer;
	}
	
	//ridefinizione metodo equals
	public boolean equals(Object o) {
		//riflessività
		if(this == o)
			return true;
		//confronto con null
		if(o == null)
			return false;
		
		if(this.getClass() != o.getClass())
			return false;
		//ora posso fare il downcast perché ho oggetti della stessa classe
		Item item= (Item) o;
		
		return item.name.equals(this.name);
		
	}
}
