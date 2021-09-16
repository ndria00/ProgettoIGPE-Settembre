package application.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import application.model.Game;

public class GameWindowController extends WindowAdapter{
	
	@Override
	public void windowClosing(WindowEvent e) {
		if(Game.getInstance().isPlaying() || Game.getInstance().isPaused())
			Game.getInstance().saveGame();
	}
}
