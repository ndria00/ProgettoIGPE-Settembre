package application.view;

import java.util.ArrayList;

import application.Settings;
import application.model.Game;
//menu di gioco visualizzato quando il gioco è in pausa. 
//Viene utilizzato dal giocatore per riprendere il gioco,
//salvare e tornare al menu principale o chiudere il gioco
public class SaveQuitMenu {
	public static final int CONTINUE_BUTTON_INDEX = 0;
	public static final int SAVE_AND_QUIT_BUTTON_INDEX = 1;
	public static final int EXIT_BUTTON_INDEX = 2;
	
	private ArrayList<MenuButton> buttons;
	
	public SaveQuitMenu() {
		buttons= new ArrayList<MenuButton>();
		
		buttons.add(new MenuButton(ButtonText.CONTINUE_BUTTON_TEXT));
		buttons.add(new MenuButton(ButtonText.SAVE_AND_QUIT_BUTTON_TEXT));
		buttons.add(new MenuButton(ButtonText.EXIT_BUTTON_TEXT));
	}
	
	public void setButtonLocation(int windowWidth, int windowHeight) {
		int firstButtonX =Game.getInstance().getCharacter().getCenterX() - Settings.DEFAULT_BUTTONS_PADDING - Settings.DEFAULT_BUTTONS_WIDTH - Settings.DEFAULT_BUTTONS_WIDTH / 2;
		int firstButtonY = Game.getInstance().getCharacter().getY();
		
		buttons.get(0).getRectangle().setLocation(firstButtonX, firstButtonY);
		buttons.get(1).getRectangle().setLocation(firstButtonX + Settings.DEFAULT_BUTTONS_WIDTH + Settings.DEFAULT_BUTTONS_PADDING, firstButtonY);
		buttons.get(2).getRectangle().setLocation(firstButtonX + 2 * Settings.DEFAULT_BUTTONS_WIDTH + 2 * Settings.DEFAULT_BUTTONS_PADDING, firstButtonY);
	}
	
	public MenuButton getSaveAndQuitButton() {
		return buttons.get(SAVE_AND_QUIT_BUTTON_INDEX);
	}
	
	public MenuButton getContinueButton() {
		return buttons.get(CONTINUE_BUTTON_INDEX);
	}
	
	public MenuButton getExitButton() {
		return buttons.get(EXIT_BUTTON_INDEX);
	}
	
	public ArrayList<MenuButton> getButtons() {
		return buttons;
	}
}