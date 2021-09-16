package application.model;

//interfaccia implementata dagki oggetti del gioco che possono muoversi
public interface Movable {
	//rappresenta una velocit� massima possibile per gli oggetti che si possono muovere
	public static final int MAX_SPEED=20;
	
	public boolean move(int direction);
	public boolean collided();
	//calcola una direzione idle in base alla direzione corrente
	public int computeIdleDirection();
}