package application.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import application.Settings;
//rappresenta lo stato della finestra che può essere minimizzato oppure schermo intero.
//In base al suo stato verranno ridimensioniati i componenti della grafica
//e quindi la finestra di gioco
public class WindowState {
	
	private int windowWidth;
	private int windowHeight;
	private boolean extendedMode;
	private Dimension SCREEN_SIZE ;
	
	public WindowState() {
		SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
		windowWidth = Settings.WINDOW_MINIMAL_SIZE;
		windowHeight = Settings.WINDOW_MINIMAL_SIZE;
		extendedMode = false;
	}
	
	public void switchWindowState() {
		if(windowWidth == Settings.WINDOW_MINIMAL_SIZE) {
			windowWidth = (int)SCREEN_SIZE.getWidth();
			windowHeight = (int)SCREEN_SIZE.getHeight();
			extendedMode = true;
		}
		else
		{
			windowWidth = Settings.WINDOW_MINIMAL_SIZE;
			windowHeight  = Settings.WINDOW_MINIMAL_SIZE;
			extendedMode = false;
		}
		
	}
	
	public int getWindowWidth() {
		return windowWidth;
	}
	
	public int getWindowHeight() {
		return windowHeight;
	}
	
	public boolean isExtended() {
		return extendedMode;
	}
	public Dimension getScreenSize() {
		return SCREEN_SIZE;
	}
}