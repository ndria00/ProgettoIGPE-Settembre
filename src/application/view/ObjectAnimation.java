package application.view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Rappresenta una delle animazioni possibili per un personaggio o altre entità del gioco
public class ObjectAnimation {
	
	BufferedImage currentImage;
	ArrayList<BufferedImage> images;
	int index;
	
	public ObjectAnimation(ArrayList<BufferedImage> images) {
		this.images = images;
		this.currentImage = images.get(0);
		index = 0;
		
	}
	
	public void update() {
		index++;
		if(index == images.size())
			index = 0;
		
		currentImage = images.get(index);
	}
	
	public BufferedImage getCurrentImage() {
		return currentImage;
	}
	
	public void reset() {
		index=0;
		currentImage=images.get(0);
	}
}
