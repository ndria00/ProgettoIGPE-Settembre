package application.model;

import java.util.ArrayList;

import application.Settings;

//rappresenta l'inventario posseduto dal giocatore
public class Inventory extends ArrayList<ItemInventory>{

	private static final long serialVersionUID = -5247454478909435228L;
	private boolean isOpen;
	
	public Inventory() {
		super();
		isOpen=false;
	}
	
	public boolean getIsOpen() {
		return isOpen;
	}
	
	public void switchOpen() {
		isOpen = !isOpen;
	}
	
	//aggiunge un item all'inventario, che già conteneva un item
	//dello stessp tipo di quello raccolot
	public boolean gatherItem(Item item) {
		for(int i=0; i< this.size(); ++i) {
			if(this.get(i).getItem().equals(item)) {
				this.get(i).gather();
				return true;
			}
		}
		
		//l'inventario è pieno
		if(this.size() > Settings.MAX_INVENTORY_SIZE)
			return false;
		
		//la tipologia dell'item non era presente nell'inventario e quindi va aggiunta con quantità 1
		this.add(new ItemInventory(item));
		return true;
	}
	
	//drop di un item dall'inventario
	public boolean drop(Item item) {
			for(int i = 0; i < this.size(); ++i) {
				if(this.get(i).getItem().equals(item)) {
					this.get(i).drop();
					if(this.get(i).getQuantity() == 0) {
						removeItem(this.get(i));
					}
					return true;
				}
			}	
			return false;
	}
	
	//rimozione del contenuto di uno slot (coppia item-numero) dall'inventario
	public void removeItem(ItemInventory itemInv) {
		this.remove(itemInv);	
	}
	
	
	//consumazioe dell'item specificato
	public boolean consume(Item item) {
		boolean canUse=false;
		for(int i = 0; i < this.size(); ++i) {
			
			if(this.get(i).getItem().equals(item)){
				
				canUse = this.get(i).use();
				if(this.get(i).getQuantity() == 0) {
					removeItem(this.remove(i));
					break;
				}
				
			}
		}
		return canUse;
	}
	
	//verifica se l'item è contenuto nell'inventario
	public boolean containsItem(Item item) {
		for(int i = 0; i < this.size(); ++i)
			if(this.get(i).getItem().equals(item))
				return true;
		return false;
	}
}