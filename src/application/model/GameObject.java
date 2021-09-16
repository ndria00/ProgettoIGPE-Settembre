package application.model;

import application.Settings;

//rappresenta gli oggetti del gioco non vivi e che non possono muoversi,
//quindi quelli che hanno una hitbox che può essere sfruttata secondo vari scopi
//come la possibilità di attaccare, essere attaccati, o per impedire
//che altri oggetti si sovrappongano a questi
public class GameObject {
	private int x;
	private int y;
	private Hitbox hitbox;
	
	public GameObject() {
		x=0;
		y=0;
		initializeHitbox();
	}
	
	public GameObject(int x, int y) {
		this.x=x;
		this.y=y;
		initializeHitbox();
	}

	//hitbox di dimensione di default
	public void initializeHitbox() {
		hitbox=new Hitbox(x, y, Settings.OBJECT_DEFAULT_SIZE, Settings.OBJECT_DEFAULT_SIZE);
	}
	
	//hitbox di dimensione a piacere
	public void initializeHitbox(int width) {
		hitbox=new Hitbox(x, y, width, width);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public int getCenterX() {
		return (int)hitbox.getCenterX();
	}
	
	public int getCenterY() {
		return (int)hitbox.getCenterY();
	}
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
}
