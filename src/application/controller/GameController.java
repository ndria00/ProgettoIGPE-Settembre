package application.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import application.GameLoop;
import application.model.Game;
import application.model.GameDirection;
import application.model.GameKey;
import application.view.GamePanel;
import application.view.MenuButton;

//rappresenta il controller relativo alla fase di gioco (tutto tranne il menu iniziale)
public class GameController extends MouseAdapter implements KeyListener{
	private GamePanel gp;
	//direzione del personaggio utilizzata per continuare un'azione al personaggio
	//ad ogni ciclo del gameLoop piuttosto che alla pressione dei tasti
	//(evitando lo stacco tra la prima chiamata al keyPressed e le successive)
	private int characterDirection;
	private MenuController menuController;
	
	public GameController(GamePanel gp) {
		this.gp = gp;
		//gameStarted();

	}
	
	public void gameStarted() {
		gp.gameStarted();
		characterDirection = Game.getInstance().getCharacter().getCurrentDirection();
		
		Thread  loop= new Thread(new GameLoop(this));
		loop.start();
	}
	
	public void setMenuController(MenuController menuController) {
		this.menuController = menuController;
	}
	
	public void update() {
		if(!Game.getInstance().isOnMainMenu()) {
			Game.getInstance().update();
			if(!Game.getInstance().isPaused()) {
				if(characterDirection != GameDirection.DIRECTION_NULL)
					Game.getInstance().update(characterDirection);
				
				gp.update();
				if(Game.getInstance().getCharacter().isAlive())
					gp.updateDirection(characterDirection);
				
				
			}
	}
			gp.repaint();
	}



	@Override
	public void keyTyped(KeyEvent e) {
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
			
		if(!Game.getInstance().isPlaying() && !Game.getInstance().isPaused())
			return;
		
		int dir;
		
		switch(e.getKeyCode()) {
			case GameKey.KEY_RIGHT:
				dir=GameDirection.RIGHT;
				break;
			case GameKey.KEY_LEFT:
				dir=GameDirection.LEFT;
				break;
			case GameKey.KEY_UP:
				dir=GameDirection.UP;
				break;
			case GameKey.KEY_DOWN:
				dir=GameDirection.DOWN;
				break;
			case GameKey.KEY_ATTACK:
					dir=attack();
					if(dir == GameDirection.DIRECTION_NULL)
						return;
					break;
			case GameKey.KEY_GATHER:
				dir = GameDirection.GATHER;
				break;	
			case GameKey.KEY_INVENTORY:
				dir =GameDirection.INVETORY_OPEN_CLOSE;
				break;
			case GameKey.KEY_CONSUME_1:
				if(e.isShiftDown())
					dir = GameDirection.DROP_ITEM_1;
				else
					dir = GameDirection.CONSUME_ITEM_1;
				break;
			case GameKey.KEY_CONSUME_2:
				if(e.isShiftDown())
					dir = GameDirection.DROP_ITEM_2;
				else	
					dir = GameDirection.CONSUME_ITEM_2;
					break;
			case GameKey.KEY_CONSUME_3:
				if(e.isShiftDown())
					dir = GameDirection.DROP_ITEM_3;
				else	
				dir = GameDirection.CONSUME_ITEM_3;
				break;
			case GameKey.KEY_CONSUME_4:
				if(e.isShiftDown())
					dir = GameDirection.DROP_ITEM_4;
				else	
					dir = GameDirection.CONSUME_ITEM_4;
				break;
			case GameKey.KEY_CONSUME_5:
				if(e.isShiftDown())
					dir = GameDirection.DROP_ITEM_5;
			else	
				dir = GameDirection.CONSUME_ITEM_5;
				break;
			case GameKey.KEY_MAP:
					dir = GameDirection.MAP_OPEN_CLOSE;
					break;
			case GameKey.KEY_TALK_WITH_NPC:
				dir = GameDirection.TALK_WITH_NPC;
				break;
			case GameKey.KEY_SAVE_GAME:
					if(e.isControlDown())
						dir = GameDirection.SAVE_GAME;
					else
						return;
					break;
			case GameKey.KEY_MISSIONS:
					dir = GameDirection.MISSIONS_SHOW_HIDE;
					break;
			case GameKey.KEY_SWITCH_PAUSE:
				dir = GameDirection.SWITCH_PAUSE;
				break;
			case GameKey.KEY_OPEN_CLOSE_HELP_MENU:
				dir = GameDirection.OPEN_CLOSE_HELP_MENU;
				break;
			default:
				return;
			
		}
		
		//update da eseguire quando la direzione scelta non è di movimento
		if(dir > GameDirection.ATTACK_UP) {
			Game.getInstance().update(dir);
			characterDirection = GameDirection.DIRECTION_NULL;
		}
		else
			characterDirection = dir;
		
		if(dir == GameDirection.OPEN_CLOSE_HELP_MENU) {
			Game.getInstance().getHelpSettings().switchOpenState();
		}
			
	}
	
	public int attack() {
			int dir;
			switch(Game.getInstance().getCharacter().getCurrentDirection()) {
			case GameDirection.RIGHT:
				dir=GameDirection.ATTACK_RIGHT;
				break;
			case GameDirection.IDLE_RIGHT:
				dir=GameDirection.ATTACK_RIGHT;
				break;
			case GameDirection.LEFT:
				dir=GameDirection.ATTACK_LEFT;
				break;
			case GameDirection.IDLE_LEFT:
				dir=GameDirection.ATTACK_LEFT;
				break;
			case GameDirection.DOWN:
				dir=GameDirection.ATTACK_DOWN;
				break;
			case GameDirection.IDLE_DOWN:
				dir=GameDirection.ATTACK_DOWN;
				break;
			case GameDirection.UP:
				dir=GameDirection.ATTACK_UP;
				break;
			case GameDirection.IDLE_UP:
				dir=GameDirection.ATTACK_UP;
				break;
			default:
				return GameDirection.DIRECTION_NULL;
			}
			
			return dir;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!Game.getInstance().isPlaying() && !Game.getInstance().isPaused())
			return;
		
		int idleDirection=GameDirection.IDLE_RIGHT;
		switch(Game.getInstance().getCharacter().getCurrentDirection()) {
			case GameDirection.RIGHT:
				idleDirection=GameDirection.IDLE_RIGHT;
				break;
			case GameDirection.ATTACK_RIGHT:
				idleDirection=GameDirection.IDLE_RIGHT;
				break;
			case GameDirection.LEFT:
				idleDirection=GameDirection.IDLE_LEFT;
				break;
			case GameDirection.ATTACK_LEFT:
				idleDirection=GameDirection.IDLE_LEFT;
				break;
			case GameDirection.UP:
				idleDirection=GameDirection.IDLE_UP;
				break;
			case GameDirection.ATTACK_UP:
				idleDirection=GameDirection.IDLE_UP;
				break;
			case GameDirection.DOWN:
				idleDirection=GameDirection.IDLE_DOWN;
				break;
			case GameDirection.ATTACK_DOWN:
				idleDirection=GameDirection.IDLE_DOWN;
				break;
			
			default:
				characterDirection = GameDirection.DIRECTION_NULL;	
				return;
		}
		
		gp.updateDirection(idleDirection);
		this.characterDirection = idleDirection;
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("WEE");

			//per il momento l'input del mouse nel gioco serve solo per scegliere cosa fare dopo aver messo in pausa il gioco 
			//la posizione del click sulla mappa del gioco è calcolata come posizione del punto in alto a sinistra della camera del giocatore + le coordinate sullo schermo
			int x = Game.getInstance().getCharacter().getCenterX() - gp.getWindowState().getWindowWidth() / 2 + e.getX();
			int y = Game.getInstance().getCharacter().getCenterY() - gp.getWindowState().getWindowHeight() / 2 + e.getY();
			Point p= new Point(x, y);
			
			if(Game.getInstance().isPaused()) {
				//System.out.println("WE " + x + " " + y);
				//System.out.println("Rect" + gp.getSaveQuitMenu().getSaveAndQuitButton().getRectangle().getX() + " " + gp.getSaveQuitMenu().getSaveAndQuitButton().getRectangle().getY());
				
				if(gp.getSaveQuitMenu().getContinueButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getContinueButton().setState(MenuButton.MOUSE_STATE_DEFAULT);
					Game.getInstance().update(GameDirection.SWITCH_PAUSE);
				}
				else if(gp.getSaveQuitMenu().getSaveAndQuitButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getSaveAndQuitButton().setState(MenuButton.MOUSE_STATE_DEFAULT);
					Game.getInstance().saveGame();
					Game.getInstance().exitProfile();
					menuController.switchToMainMenu();
				}
				else if(gp.getSaveQuitMenu().getExitButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getExitButton().setState(MenuButton.MOUSE_STATE_DEFAULT);
					System.exit(0);
				}
			}//menu per il respawn
			else if(Game.getInstance().isPlaying() && !Game.getInstance().getCharacter().isAlive()) {
				if(gp.getReviveMenu().getReviveHereButton().getRectangle().contains(p)) {
					Game.getInstance().getCharacter().respawn();
				}
				else if(gp.getReviveMenu().getReviveAtSpawnButton().getRectangle().contains(p)) {
					Game.getInstance().getCharacter().respawn(0, 0);
				}
			}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//la posizione del click sulla mappa del gioco è calcolata come posizione del punto in alto a sinistra della camera del giocatore + le coordinate sullo schermo
		int x = Game.getInstance().getCharacter().getCenterX() - gp.getWindowState().getWindowWidth() / 2 + e.getX();
		int y = Game.getInstance().getCharacter().getCenterY() - gp.getWindowState().getWindowHeight() / 2 + e.getY();
		Point p= new Point(x, y);
			if(Game.getInstance().isPaused()) {
				if(gp.getSaveQuitMenu().getContinueButton().getRectangle().contains(p))
					gp.getSaveQuitMenu().getContinueButton().setState(MenuButton.MOUSE_STATE_PRESSED);
				else if(gp.getSaveQuitMenu().getSaveAndQuitButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getSaveAndQuitButton().setState(MenuButton.MOUSE_STATE_PRESSED);
				}
				else if(gp.getSaveQuitMenu().getExitButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getExitButton().setState(MenuButton.MOUSE_STATE_PRESSED);
				}
			}
			else if(!Game.getInstance().getCharacter().isAlive() && Game.getInstance().isPlaying()) {
				if(gp.getReviveMenu().getReviveHereButton().getRectangle().contains(p)) {
					gp.getReviveMenu().getReviveHereButton().setState(MenuButton.MOUSE_STATE_PRESSED);
				}
				else if(gp.getReviveMenu().getReviveAtSpawnButton().getRectangle().contains(p)) {
					gp.getReviveMenu().getReviveAtSpawnButton().setState(MenuButton.MOUSE_STATE_PRESSED);
				}
			}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
			if(!Game.getInstance().isOnMainMenu()) {
				for(int i = 0; i < gp.getSaveQuitMenu().getButtons().size(); ++i) {
					gp.getSaveQuitMenu().getButtons().get(i).setState(MenuButton.MOUSE_STATE_DEFAULT);
				}
			}
			if(!Game.getInstance().getCharacter().isAlive()) {
				for(int i = 0; i < gp.getReviveMenu().getButtons().size(); ++i) {
					gp.getReviveMenu().getButtons().get(i).setState(MenuButton.MOUSE_STATE_DEFAULT);
				}
			}
	}
	
	/*
	@Override
	public void mouseMoved(MouseEvent e) {


			int x = Game.getInstance().getCharacter().getCenterX() - gp.getWindowState().getWindowWidth() / 2 + e.getX();
			int y = Game.getInstance().getCharacter().getCenterY() - gp.getWindowState().getWindowHeight() / 2 + e.getY();
			Point p= new Point(x, y);
			
			if(Game.getInstance().isPaused()){
				if(gp.getSaveQuitMenu().getContinueButton().getRectangle().contains(p))
					gp.getSaveQuitMenu().getContinueButton().setState(MenuButton.MOUSE_STATE_HOVER);
				else if(gp.getSaveQuitMenu().getSaveAndQuitButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getSaveAndQuitButton().setState(MenuButton.MOUSE_STATE_HOVER);
				}
				else if(gp.getSaveQuitMenu().getExitButton().getRectangle().contains(p)) {
					gp.getSaveQuitMenu().getExitButton().setState(MenuButton.MOUSE_STATE_HOVER);
				}
			}
		}
		*/
}