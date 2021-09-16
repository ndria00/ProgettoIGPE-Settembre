package application.model;

//rappresenta gli elementi di una mappa
//che possono esserre di diverso tipo ed hanno una posizione
//all'interno di essa
public class MapElement {
	public static final int TYPE_ENEMY=1, TYPE_PLAYER=2, TYPE_NPC=3;
	
	private int x, y;
	private int type;
	
	public MapElement(int x, int y, int type) {
		this.x=x;
		this.y=y;
		this.type=type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public int getType(){
		return type;
	}
}