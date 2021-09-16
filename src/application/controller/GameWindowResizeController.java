package application.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import application.view.GameWindow;

public class GameWindowResizeController extends AbstractAction{
	
	private static final long serialVersionUID = -2492930444164645511L;
	
	private GameWindow gameWindow;
	
	public GameWindowResizeController(GameWindow gameWindow) {
	this.gameWindow = gameWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Resizing game window");
			gameWindow.getGamePanel().updateWindowState();
			gameWindow.getWindowState().switchWindowState();
			//quando nel menù avviene un cambio di risoluzione della schermata di gioco bisogna anche cambiare le dimensioni 
			//al pannello di gioco e alla camera in modo che iniziando a giocare la scelta fatta in precedenza venga mantenuta
			gameWindow.getGamePanel().getGameCamera().setWindowDimension(gameWindow.getWindowState().getWindowWidth(), gameWindow.getWindowState().getWindowHeight());
			gameWindow.adaptWindowToPanels();
		}
}
