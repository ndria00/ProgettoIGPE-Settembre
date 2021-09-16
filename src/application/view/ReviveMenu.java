package application.view;

import java.util.ArrayList;

import application.Settings;
import application.model.Game;

//menu che viene mostrato quando il giocatore muore
public class ReviveMenu {

		public static final int REVIVE_HERE_BUTTON_INDEX = 0;
		public static final int REVIVE_AT_SPAWN_BUTTON_INDEX = 1;
		
		private ArrayList<MenuButton> buttons;
		
		public ReviveMenu() {
			buttons= new ArrayList<MenuButton>();
			
			buttons.add(new MenuButton(ButtonText.REVIVE_HERE_BUTTON_TEXT));
			buttons.add(new MenuButton(ButtonText.REVIVE_AT_SPAWN_BUTTON_TEXT));
		}
		
		//posizionamento dei bottoni
		public void setButtonLocation(int windowWidth, int windowHeight) {
			int firstButtonX =Game.getInstance().getCharacter().getCenterX() - Settings.DEFAULT_BUTTONS_PADDING / 2  - Settings.DEFAULT_BUTTONS_WIDTH;
			int firstButtonY = Game.getInstance().getCharacter().getY();
			
			buttons.get(0).getRectangle().setLocation(firstButtonX, firstButtonY);
			buttons.get(1).getRectangle().setLocation(firstButtonX + Settings.DEFAULT_BUTTONS_WIDTH + Settings.DEFAULT_BUTTONS_PADDING, firstButtonY);
		}
		
		public MenuButton getReviveHereButton() {
			return buttons.get(REVIVE_HERE_BUTTON_INDEX);
		}
		
		public MenuButton getReviveAtSpawnButton() {
			return buttons.get(REVIVE_AT_SPAWN_BUTTON_INDEX);
		}
		
		
		public ArrayList<MenuButton> getButtons() {
			return buttons;
		}
}
