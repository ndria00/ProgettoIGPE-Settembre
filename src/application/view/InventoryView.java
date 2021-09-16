package application.view;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import application.Settings;
import application.model.Game;

//consente di gestire la visualizzazione dei tool tip relativi agli oggetti in possesso del personaggio
public class InventoryView {
	public static final int NO_ITEM_SELECTED = -1;
	private ArrayList<Rectangle> slots;
	private int paddingSlots;
	private int paddingItemImage;
	private int indexShowItem;
	
	public InventoryView(int windowWidth, int windowHeight) {
		slots = new ArrayList<Rectangle>(Settings.MAX_INVENTORY_SIZE);
		paddingSlots = 3;
		paddingItemImage = 10;
		int x = Game.getInstance().getCharacter().getCenterX() - windowWidth / 2 + Settings.OBJECT_DEFAULT_SIZE + paddingSlots; 
		int y = Game.getInstance().getCharacter().getCenterY() + windowHeight / 2 - Settings.OBJECT_DEFAULT_SIZE; //- Settings.INVENTORY_CELL_SIZE;
		
		for(int i = 0; i < Settings.MAX_INVENTORY_SIZE; ++i) {
			Rectangle r = new Rectangle(Settings.INVENTORY_CELL_SIZE, Settings.INVENTORY_CELL_SIZE);
			r.setLocation(x, y);
			
			slots.add(r);
			x += paddingSlots + Settings.INVENTORY_CELL_SIZE;
		}
		indexShowItem = NO_ITEM_SELECTED;
	}
	
	public void updateLocation(int windowWidth, int windowHeight, Point p) {
		
		if(p!=null) {
			p.x = Game.getInstance().getCharacter().getCenterX() - windowWidth / 2 + p.x;
			p.y = Game.getInstance().getCharacter().getCenterY() - windowHeight / 2 + p.y;
		}
		
		int x = Game.getInstance().getCharacter().getCenterX() - windowWidth / 2 + Settings.OBJECT_DEFAULT_SIZE + paddingSlots; 
		int y = Game.getInstance().getCharacter().getCenterY() + windowHeight / 2 - Settings.OBJECT_DEFAULT_SIZE;
		
		//indica se nessun item è selezionato
		boolean noSelection = true;
		
		for(int i = 0; i < Settings.MAX_INVENTORY_SIZE; ++i) {
			slots.get(i).setLocation(x, y);
			if(p != null) {
				if(slots.get(i).contains(p) && i < Game.getInstance().getCharacter().getInventory().size()) {
					indexShowItem = i;
					noSelection=false;
				}
			}
			x += paddingSlots + Settings.INVENTORY_CELL_SIZE;
		}
		if(noSelection)
			indexShowItem = NO_ITEM_SELECTED;
	}
	
	public ArrayList<Rectangle> getSlots() {
		return slots;
	}
	
	public int getPaddingSlots() {
		return paddingSlots;
	}
	
	public int getPaddingItemImage() {
		return paddingItemImage;
	}
	public int getIndexShowItem() {
		return indexShowItem;
	}
}
