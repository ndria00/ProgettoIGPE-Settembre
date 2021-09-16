package application.model;

import java.awt.Rectangle;

//rappresenta l'area occupata dagli oggetti del gioco
//che poi è quella utilizzata per effettuare molte azioni
public class Hitbox extends Rectangle{
	private static final long serialVersionUID = 3647295498903239821L;
	
	public Hitbox(int x, int y, int object_width, int object_height) {
		super(x, y, object_width, object_height);
	}
}
