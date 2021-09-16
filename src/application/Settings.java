package application;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Settings {
	
	public static final String GAME_NAME = "Battle of Warriors";
	public static final int WORLD_SIZE = 2500;
	public static final int INITIAL_SAFE_ZONE = 300;
	public static final int OBJECT_DEFAULT_SIZE = 100;
	public static final int DEFAULT_MAP_SIZE = 200;
	
	public static final int MAX_PROFILE_NUMBER = 5;
	
	public static final int GAME_STATE_PLAYING = 0;
	public static final int GAME_STATE_PAUSED = 1;
	public static final int GAME_STATE_OFF = 2;
	public static final int GAME_STATE_MAIN_MENU = 3;
	
	public static final int ID_PLAYER = 1;
	public static final int OBJECT_DISAPPEAR_TIME = 10000;
	public static final int DROP_DEFAULT_SIZE = 30;
	public static final int INVENTORY_CELL_SIZE = 50;
	
	public static final int MAX_INVENTORY_SIZE = 5;
	
	public static final int NUMBER_OF_MONSTERS = 20;
	
	public static final int WINDOW_MINIMAL_SIZE = 800;
	
	public static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WINDOW_HEIGHT = (int)SCREEN_SIZE.getHeight();
	public static final int WINDOW_WIDTH = (int)SCREEN_SIZE.getWidth();
	public static final int DEFAULT_BUTTONS_WIDTH = 170;
	public static final int DEFAULT_BUTTONS_HEIGHT = 50;
	public static final int DEFAULT_BUTTONS_PADDING = 50;
	public static final int ADD_PROFILE_BUTTON_SIZE = 50;
	
	public static final int MAIN_MENU_TOP_PADDING = 150;
	public static final int DEFAULT_LABEL_LENGTH = 200;
	public static final int DEFAULT_LABEL_HEIGHT = 50;
	public static final int DEFAULT_LABELS_PADDING = 25;
	
	public static final int XP_BAR_HEIGHT = 40;
	public static final int XP_BAR_LENGTH = 200;
	public static final int DEFAULT_BARS_HEIGTH = 15;
	public static final int MONSTER_BARS_HEIGHT = 8;
	
	public static final int NPC_X = 70;
	public static final int	NPC_Y = 220;
	public static final String ACTION_STRING_LVL_UP = "LVL UP!";
	public static final String ACTION_STRING_HEAL = "+ HP";
	
	public static final String ACTION_STRING_NPC_MISSION_READY = "I have a mission for you!";
	public static final String ACTION_STRING_NPC_NO_MISSIONS_AVAILABLE = "There are no missions for you!";
	public static final String ACTION_STRING_NPC_MISSION_COMPLETE = "Congratulations!!!";
	public static final String NPC_STRING_NAME = "Isaac";
	public static final String NO_ACTIVE_MISSIONS = "You do not have active missions";
	
	public static final boolean REWARD_HP = false;
	public static final int REWARD_HP_QUANTITY = 500;
	public static final boolean REWARD_EXP = true;
	public static final int REWARD_XP_QUANTITY = 1000;
	public static final int NUMBER_OF_MISSION_PER_PLAYER = 5;
	
	public static final int GAME_FREQUENCY = 60;
}
