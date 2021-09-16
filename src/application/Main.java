package application;

import application.controller.GameController;
import application.controller.GameWindowController;
import application.controller.GameWindowResizeController;
import application.controller.MenuController;
import application.model.Game;
import application.view.GameWindow;
public class Main {
	public static void main(String[] args) {
		
		Game.getInstance();
		
		GameWindow gameWindow = new GameWindow();
		//controller relativo alla fase di gioco
		GameController gameController= new GameController(gameWindow.getGamePanel());
		gameWindow.getGamePanel().setController(gameController);
		
		//controller relativo al menu
		MenuController menuController = new MenuController(gameWindow, gameController);
		gameWindow.setMenuController(menuController);
		gameController.setMenuController(menuController);
		
		//controller che gestisce la chiusura "involontaria" del gioco (che effettua semplicemente il salvataggio del gioco)
		GameWindowController windowController = new GameWindowController();
		gameWindow.setWindowController(windowController);
		
		//controller che gestisce l'operazione di resize della finestra di gioco
		GameWindowResizeController resizeController = new GameWindowResizeController(gameWindow);
		gameWindow.setWindowResizeController(resizeController);
	}
}
