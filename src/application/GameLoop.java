package application;

import application.controller.GameController;
import application.model.Game;

public class GameLoop implements Runnable{
	
	private GameController controller;
	
	public GameLoop(GameController controller) {
		this.controller=controller;
	}
	
	@Override
	public void run() {
		try {
			while(!Game.getInstance().isOnMainMenu()){
				controller.update();
				Thread.sleep(Settings.GAME_FREQUENCY);
			}
			System.out.println("Game finished... Game loop interrupted!");
		}catch(InterruptedException e) {
			return;
		}
		
	}

}

