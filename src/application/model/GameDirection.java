package application.model;

public class GameDirection {
	
	//Azioni legate al movimento ed alle quali è associata un'animazione
	public static final int DIRECTION_NULL = -1;
	public static final int RIGHT = 0;
	public static final int LEFT = 1;	
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int ATTACK_RIGHT = 8;
	public static final int ATTACK_LEFT = 9;
	public static final int ATTACK_DOWN = 10;
	public static final int ATTACK_UP = 11;
	
	public static final int IDLE_RIGHT = 4;
	public static final int IDLE_LEFT = 5;
	public static final int IDLE_UP = 6;
	public static final int IDLE_DOWN = 7;
	
	public static final int GATHER = 13;
	public static final int INVETORY_OPEN_CLOSE = 14;
	public static final int CONSUME_ITEM_1 = 15;
	public static final int CONSUME_ITEM_2 = 16;
	public static final int CONSUME_ITEM_3 = 17;
	public static final int CONSUME_ITEM_4 = 18;
	public static final int CONSUME_ITEM_5 = 19;
	public static final int DROP_ITEM_1 = 20;
	public static final int DROP_ITEM_2 = 21;
	public static final int DROP_ITEM_3 = 22;
	public static final int DROP_ITEM_4 = 23;
	public static final int DROP_ITEM_5 = 24;
	
	public static final int TALK_WITH_NPC = 25;
	public static final int MAP_OPEN_CLOSE = 26;
	public static final int SAVE_GAME = 27;
	public static final int MISSIONS_SHOW_HIDE = 28;
	public static final int SWITCH_PAUSE = 29;
	
	public static final int SWITCH_WINDOW_DIMENSION = 30;
	public static final int CLOSE_GAME = 31;
	public static final int OPEN_CLOSE_HELP_MENU = 32;
	
}
