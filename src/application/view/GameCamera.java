package application.view;

import application.Settings;
import application.model.GameObject;
//rappresenta la camera del giocatore, il quale si trova sempre al centro della schermata di gioco
public class GameCamera {
	public static int X=0;
	public static int Y=1;
	
	int x;
	int y;
	int windowWidth;
	int windowHeight;
	
	public GameCamera() {}
	
	public GameCamera(int x, int y) {
		this.x=x;
		this.y=y;	
	}
	
	public void centerOnObject(GameObject o) {
		x= -o.getX() + windowWidth / 2 - Settings.OBJECT_DEFAULT_SIZE/2;
		y= -o.getY() + windowHeight / 2 - Settings.OBJECT_DEFAULT_SIZE/2;
	}
	
	public void setWindowDimension(int windowWidth, int windowHeight) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
	}
	public int getx() {
		return x;
	}
	public int gety() {
		return y;
	}
	
	public void setx(int xOffset) {
		this.x = xOffset;
	}
	
	public void sety(int yOffset) {
		this.y = yOffset;
	}
	
	public void move(int offset, int d) {
		if(d==X)
			x+=offset;
		else if(d==Y)
			y+=offset;
	}
}
