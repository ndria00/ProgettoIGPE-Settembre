package application.view;

import java.awt.Color;
import java.awt.Rectangle;

import application.Settings;

//rappresenta i bottoni utilizzati nei menu (in-game) in fase di gioco
public class MenuButton {
	
	public static final int MOUSE_STATE_DEFAULT = 0;
	public static final int MOUSE_STATE_HOVER = 1;
	public static final int MOUSE_STATE_PRESSED = 2;
	
	private Rectangle rectangle;
	private Color color;
	private String text;
	private int state;
	
	public MenuButton(String text) {
		rectangle= new Rectangle(Settings.DEFAULT_BUTTONS_WIDTH, Settings.DEFAULT_BUTTONS_HEIGHT);
		color = Colors.BUTTON_DEFAULT_COLOR;
		this.text = text;
		state = MOUSE_STATE_DEFAULT;
	}
	
	//al cambio di stato del bottone viene anche cambiato il colore
	//per dare l'idea di avere un vero bottone (simile a JButton)
	public void setState(int state) {
		this.state=state;
		Color color = null;
		switch(state) {
			case MOUSE_STATE_DEFAULT:
				color = Colors.BUTTON_DEFAULT_COLOR;
				break;
			case MOUSE_STATE_HOVER:
				color = Colors.BUTTON_HOVER_COLOR;
				break;
			case MOUSE_STATE_PRESSED: 
				color = Colors.BUTTON_PRESSED_COLOR;
				break;
		}
		this.color = color;
	}
	
	public int getState() {
		return state;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getText() {
		return text;
	}
}
