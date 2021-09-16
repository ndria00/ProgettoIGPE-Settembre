package application.model;

import java.util.ArrayList;

//rappresenta lamappa del gioco composta da
//un insieme di elementi di mappa
public class GameMap extends ArrayList<MapElement>{
	private static final long serialVersionUID = 1483909159886436987L;
	
	private boolean isOpen;
	
	public GameMap() {
		super();
		isOpen=false;
	}
	
	public void refresh() {
		this.clear();
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public void switchIsOpen() {
		isOpen = !isOpen;
	}
}
