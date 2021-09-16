package application.view;

import java.awt.Color;
import java.awt.Rectangle;

//
public class MenuLabel {

	private String text;
	private boolean selected;
	private boolean editable;
	private Rectangle rectangle;
	private Color color;
	
	public MenuLabel(String text, boolean editable) {
		this.text = text;
		this.editable = editable;
		this.rectangle = new Rectangle();
		if(editable)
			this.color = Colors.GAME_BUTTONS;
	}
	
	
	public void setLocation(int x, int y) {
		rectangle.setLocation(x, y);
	}
	
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setEditable(boolean editable) {
		if(!this.editable)
				text = "";
		this.editable = editable;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	public Color getColor() {
		return color;
	}
	
}
