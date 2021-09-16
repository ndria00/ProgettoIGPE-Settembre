package application.model;

//rappresenta il contenuto di un slot dell'inventario che è definito come
//la coppia item-quantità
public class ItemInventory {
	
	private Item item; 
	private int quantity;
	
	//crea un nuovo contenuto perché il personaggio
	//ha raccolto un nuovo item da terra
	public ItemInventory(Item item) {
		this.item = item;
		quantity=1;
	}
	
	//crea un nuovo contenuto in cui la quantità non è per forza 1
	//utilizzato nella ripresa del gioco da un salvataggio 
	public ItemInventory(int itemType, int quantity) {
		Item item= new Item(itemType, true);
		this.item = item;
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void gather() {
		quantity++;
	}
	
	//diminuzione della quantità quando l'item viene buttato via
	public boolean drop() {
		if(quantity > 0) {
			quantity--;
			return true;
		}
		return false;
	}
	
	//diminuzione della quantita quando l'item viene usato
	public boolean use() {
		if(quantity > 0) {
			quantity--;
			return true;
		}
		return false;
	}

}