package application.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import application.Settings;
import application.controller.GameController;
import application.model.Character;
import application.model.GameDirection;
import application.model.Game;
import application.model.Inventory;
import application.model.Item;
import application.model.ItemSettings;
import application.model.GameMap;
import application.model.MapElement;
import application.model.Monster;
import application.model.NPC;
//pannello di gioco su cui vengono disegnate tutte le immagini e forme del gioco
public class GamePanel extends JPanel{
	private static final long serialVersionUID = -5246924065959206988L;
	
	private CharacterAllAnimations characterAnimations;
	private MonsterAllAnimations monsterAnimations;
	private ArrayList<MonsterAnimationState> monstersAnimationState;
	private NPCAnimations npcAnimations;
	private AllItemImages itemImages;
	private GameCamera gameCamera;
	private Font textFont;
	private Font buttonFont;
	private Font groundTextFont;
	private FontMetrics fMText;
	private WindowState wS;
	private SaveQuitMenu saveQuitMenu;
	private ReviveMenu reviveMenu;
	private MainMenu mainMenu;
	private InventoryView inventoryView;

	public GamePanel() {
		characterAnimations = new CharacterAllAnimations();
		monsterAnimations = new MonsterAllAnimations();
		npcAnimations = new NPCAnimations();	
		itemImages= new AllItemImages();	
		
		this.setBackground(Color.LIGHT_GRAY);
		this.monstersAnimationState = new ArrayList<MonsterAnimationState>();
		this.gameCamera= new GameCamera();
		

		textFont = new Font( "Arial", Font.BOLD, 12);
		buttonFont = new Font("Arial", Font.BOLD, 17);
		groundTextFont = new Font("Arial", Font.BOLD, 70);
		this.setFont(textFont);
		fMText= getFontMetrics(textFont);
		wS = new WindowState();
		this.setPreferredSize(new Dimension(wS.getWindowWidth(), wS.getWindowHeight()));
		this.setFocusable(true);
		gameCamera.setWindowDimension(wS.getWindowWidth(), wS.getWindowHeight());
		saveQuitMenu = new SaveQuitMenu();
		reviveMenu = new ReviveMenu();
		
	}
	
	public void setController(GameController gameController) {
		this.addKeyListener(gameController);
		this.addMouseListener(gameController);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		

		
		if(Game.getInstance().isPlaying() || Game.getInstance().isPaused()) {
			g.translate(gameCamera.getx(), gameCamera.gety());
			Character character=Game.getInstance().getCharacter();
			g.setColor(Colors.WORLD_BORDERS);
			int borderWorldFrame= 20;
			g.fillRect(0 - borderWorldFrame, 0 - borderWorldFrame, Settings.WORLD_SIZE + 2*borderWorldFrame, Settings.WORLD_SIZE + 2*borderWorldFrame);
			g.setColor(Colors.WORLD_GROUND);
			g.fillRect(0, 0, Settings.WORLD_SIZE, Settings.WORLD_SIZE);
			g.setColor(Colors.WORLD_GROUND_TEXT);
			g.setFont(groundTextFont);
			g.drawString("SAFE", 50, 100);
			g.drawString("ZONE", 50, 200);
			g.setFont(textFont);
			paintItems(g);
			paintNPC(g);
			paintCharacter(g);
			paintMonsters(g);
			g.setColor(Color.WHITE);
			g.drawString("X:"+character.getCenterX()+" Y:"+character.getCenterY(), character.getX() + wS.getWindowWidth() / 2 - Settings.OBJECT_DEFAULT_SIZE/2, character.getY()- wS.getWindowHeight() / 2 +Settings.OBJECT_DEFAULT_SIZE);
			paintXPBar(g);
			
			
			if(character.getInventory().getIsOpen()) {
				paintInventory(g);
			}
			if(character.getMap().isOpen()) {
				paintMap(g);
			}
			if(character.getMissions().isActive())
				paintMissions(g);
			
			if(Game.getInstance().getHelpSettings().isOpen()) {
				paintHelpMenu(g);
			}
			
			if(!character.isAlive()) {
				g.setColor(Colors.PLAYER_DEAD_BACKGROUND);
				g.fillRect(character.getCenterX() - wS.getWindowWidth() / 2, character.getCenterY() - wS.getWindowHeight() / 2, wS.getWindowWidth(), wS.getWindowHeight());
				paintReviveMenu(g);
			}
		}
		if(Game.getInstance().isPaused()) {

			Character character=Game.getInstance().getCharacter();
			g.setColor(Colors.GAME_PAUSED_BACKGROUND);
			g.fillRect(character.getCenterX() - wS.getWindowWidth() / 2, character.getCenterY() - wS.getWindowHeight() / 2, wS.getWindowWidth(), wS.getWindowHeight());
			saveQuitMenu.setButtonLocation(wS.getWindowWidth(), wS.getWindowHeight());
			paintSaveQuitMenu(g);
			
		}
	}
	
	
	protected void paintInventory(Graphics g) {
		Inventory inventory= Game.getInstance().getCharacter().getInventory();

		
		g.setColor(Color.BLUE);
		ArrayList<Rectangle> slots= inventoryView.getSlots();
		int x = (int)slots.get(0).getX();
		int y = (int)slots.get(0).getY();
		//bordo sinistro verticale
		g.fillRect(x - inventoryView.getPaddingSlots(), y, inventoryView.getPaddingSlots(), Settings.INVENTORY_CELL_SIZE);
		
		//bordi superiore ed inferiore
		int width = Settings.MAX_INVENTORY_SIZE * (Settings.INVENTORY_CELL_SIZE + inventoryView.getPaddingSlots()) + inventoryView.getPaddingSlots();
		g.fillRect(x - inventoryView.getPaddingSlots(), y - inventoryView.getPaddingSlots(), width, inventoryView.getPaddingSlots());
		g.fillRect(x - inventoryView.getPaddingSlots(), y + Settings.INVENTORY_CELL_SIZE, width, inventoryView.getPaddingSlots());
		
		for(int i = 0; i < Settings.MAX_INVENTORY_SIZE; ++i) {
			g.setColor(Color.BLUE);
			x = (int)slots.get(i).getX();
			y = (int) slots.get(i).getY();
			//padding di ogni cella (DESTRO)
			g.fillRect(x + Settings.INVENTORY_CELL_SIZE, y, inventoryView.getPaddingSlots(), Settings.INVENTORY_CELL_SIZE);
			
			//background delle celle dell'inventario
			g.setColor(Colors.INVENTORY_CELLS_BACKGROUND);
			g.fillRect(x, y, Settings.INVENTORY_CELL_SIZE, Settings.INVENTORY_CELL_SIZE);
			
			g.setColor(Color.BLACK);
			if(i < inventory.size()) {
				//immagine dell'oggetto
				g.drawImage(itemImages.getImages().get(NameToFileName.toFileName(inventory.get(i).getItem().getName())), x + inventoryView.getPaddingItemImage(), y + inventoryView.getPaddingItemImage(), Settings.DROP_DEFAULT_SIZE, Settings.DROP_DEFAULT_SIZE, null);
			
				//quantità
				g.drawString(Integer.toString(inventory.get(i).getQuantity()), x + inventoryView.getPaddingSlots(), y + Settings.INVENTORY_CELL_SIZE - inventoryView.getPaddingSlots());
			}
			//tasto di utilizzo
			g.drawString(Integer.toString(i+1), x + Settings.INVENTORY_CELL_SIZE - inventoryView.getPaddingItemImage(), y + this.getFont().getSize());
		}
		if(inventoryView.getIndexShowItem() != InventoryView.NO_ITEM_SELECTED) {
			int index = inventoryView.getIndexShowItem();
			g.setColor(Colors.GAME_PAUSED_BACKGROUND);
			int xInfo= (int)inventoryView.getSlots().get(index).getX();
			int yInfo= (int)inventoryView.getSlots().get(index).getY() - 20 -Settings.OBJECT_DEFAULT_SIZE;
			g.fillRect(xInfo, yInfo, (int) (Settings.OBJECT_DEFAULT_SIZE* 2.8), Settings.OBJECT_DEFAULT_SIZE);
			g.setColor(Colors.TEXT_WHITE_COLOR);
			g.drawString("Name: " + inventory.get(index).getItem().getName() , xInfo + 10, yInfo + 20);
			
			g.drawString("Description: " + inventory.get(index).getItem().getDescription() , xInfo + 10, yInfo + 40);
			g.drawString("Consumable: " + inventory.get(index).getItem().isConsumable() , xInfo + 10, yInfo + 60);
		}
	}
	
	protected void paintXPBar(Graphics g){
		Character character = Game.getInstance().getCharacter();
		// XPBar
		int paddingBorders = 10;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(character.getCenterX() - wS.getWindowWidth() / 2 + paddingBorders, character.getCenterY() - wS.getWindowHeight() / 2 + paddingBorders, Settings.XP_BAR_LENGTH, Settings.XP_BAR_HEIGHT);
		g.setColor(Colors.XP_BAR_COLOR);
		double percentageXP = (double)character.getXP() / (double)character.getXPToNextLevel();
		int gainedXPBarWidth = (int) (percentageXP * Settings.XP_BAR_LENGTH);
		g.fillRect(character.getCenterX() - wS.getWindowWidth() / 2 + paddingBorders, character.getCenterY() - wS.getWindowHeight() / 2 + paddingBorders, gainedXPBarWidth, Settings.XP_BAR_HEIGHT);
		g.setColor(Colors.XP_PERCENTAGE);
		String s = Integer.toString((int)(percentageXP*100)) + "%";
		g.drawString(s, character.getCenterX() - wS.getWindowWidth() / 2 + Settings.XP_BAR_LENGTH/2 - fMText.stringWidth(s) / 2 + paddingBorders, character.getCenterY() - wS.getWindowHeight() / 2 + Settings.XP_BAR_HEIGHT/2 + paddingBorders);
	
	}
	
	protected void paintCharacter(Graphics g) {
		//stampa del personaggio
		Character character= Game.getInstance().getCharacter();
		g.drawImage(characterAnimations.currentAnimation.currentImage ,character.getX(), character.getY(), Settings.OBJECT_DEFAULT_SIZE, Settings.OBJECT_DEFAULT_SIZE, null);
		g.setColor(Colors.CURRENT_HP);
		
		double lifePercentage=(double)character.getCurrentHp()/(double)character.getMaxHp();
		int greenBarWidth=(int)(lifePercentage*(Settings.OBJECT_DEFAULT_SIZE));
		
		
		//rappresentazione degli hp correnti del personaggio
		g.fillRect(character.getX(), character.getY()-Settings.DEFAULT_BARS_HEIGTH, greenBarWidth, Settings.DEFAULT_BARS_HEIGTH);
		//rappresentazione degli hp mancanti del personaggio
		
		g.setColor(Colors.MISSING_HP);
		if(greenBarWidth<Settings.OBJECT_DEFAULT_SIZE) {
			g.fillRect(character.getX()+greenBarWidth, character.getY()-Settings.DEFAULT_BARS_HEIGTH, Settings.OBJECT_DEFAULT_SIZE-greenBarWidth, Settings.DEFAULT_BARS_HEIGTH);
		}
		
		g.setColor(Color.BLACK);
		String hpStatus=character.getCurrentHp() + " / " + character.getMaxHp();
		g.drawString(hpStatus, character.getCenterX() - fMText.stringWidth(hpStatus) / 2, character.getY()-3);
		//level
		int levelStatusWidth=Settings.DEFAULT_BARS_HEIGTH;
		g.setColor(Colors.XP_BAR_COLOR);
		g.fillOval(character.getX(), character.getY()-Settings.DEFAULT_BARS_HEIGTH-levelStatusWidth, levelStatusWidth+5, levelStatusWidth);
		g.setColor(Color.BLACK);
		g.drawString(character.getLevel().toString(), character.getX() +  3, character.getY() - Settings.DEFAULT_BARS_HEIGTH - 2);		
		
		//name
		g.setColor(Color.BLACK);
		g.drawString(character.getName() ,character.getX() + levelStatusWidth * 2 , character.getY() - Settings.DEFAULT_BARS_HEIGTH - 2);
		
		if(character.getActionString().getMessage() != null) {
			int x = character.getCenterX() - fMText.stringWidth(character.getActionString().getMessage()) / 2;
			int y = character.getCenterY() - Settings.OBJECT_DEFAULT_SIZE - (int)character.getActionString().getTimer().getValue() / Settings.GAME_FREQUENCY;
			if(character.getActionString().getMessage() == Settings.ACTION_STRING_LVL_UP) {
				g.setColor(Colors.XP_BAR_COLOR);
				g.drawString(character.getActionString().getMessage(), x, y);
			}
			else if(character.getActionString().getMessage() == Settings.ACTION_STRING_HEAL) {
				g.setColor(Color.GREEN);
				g.drawString(character.getActionString().getMessage(), x, y);
			}
		}
		
	}
	
	protected void paintHelpMenu(Graphics g) {
		g.setFont(textFont);
		fMText = getFontMetrics(textFont);
		ArrayList<String> helpMessages  = Game.getInstance().getHelpSettings().getHelpMessages();
		Character character = Game.getInstance().getCharacter();
		int x= character.getCenterX() + wS.getWindowWidth()/2 - 300;
		int y= character.getY() - 200;
		g.setColor(Colors.GAME_PAUSED_BACKGROUND);
		g.fillRect(x - 10, y - 20, 300, helpMessages.size()*(15 + fMText.getHeight()));
		
		g.setColor(Color.WHITE);
		for(int i = 0; i < helpMessages.size(); ++i) {
				g.drawString(helpMessages.get(i), x, y);
				y += (int)fMText.getHeight() + 15; 
		}
	}
	
	protected void paintMissions(Graphics g) {
		g.setFont(textFont);
		fMText = getFontMetrics(textFont);
		Character character = Game.getInstance().getCharacter();
		int missionRectHeight = Settings.OBJECT_DEFAULT_SIZE / 2;
		int missionRectWidth = (int) (2.3 * Settings.OBJECT_DEFAULT_SIZE);
		int verticalPadding = 20;
		int x = character.getCenterX() - wS.getWindowWidth() / 2;
		int y = character.getY() - Settings.OBJECT_DEFAULT_SIZE;
		
		NPC npc = Game.getInstance().getNPC();
		String s;
		
		if(character.getMissions().size() == 0) {
			g.setColor(Colors.TEXT_WHITE_COLOR);
			g.drawString(Settings.NO_ACTIVE_MISSIONS, x, y);
		}
		
		for(int i = 0; i < character.getMissions().size(); ++i) {
			g.setColor(Colors.MAP_COLOR);
			g.fillRect(x , y, missionRectWidth, missionRectHeight);
			g.setColor(Color.BLACK);
			s = npc.getMission(character.getMissions().get(i)).getDescription();
			g.drawString(s, x +   missionRectWidth / 2 - fMText.stringWidth(s) / 2, y +  missionRectHeight / 2);
			y += verticalPadding + missionRectHeight; 
		}
		
	}
	
	protected void paintNPC(Graphics g) {
		//stampa dell'NPC
		NPC npc= Game.getInstance().getNPC();
		g.drawImage(npcAnimations.getAnimations().currentImage, npc.getX(), npc.getY(), Settings.OBJECT_DEFAULT_SIZE, Settings.OBJECT_DEFAULT_SIZE, null);
		g.setColor(Color.BLACK);
		g.drawString(npc.getName(), npc.getCenterX() - fMText.stringWidth(npc.getName()) / 2, npc.getY());
		g.setColor(Colors.XP_BAR_COLOR);
		if(npc.getActionString().getMessage() != null) {
			int x = npc.getCenterX() - fMText.stringWidth(npc.getActionString().getMessage()) / 2;
			int y = npc.getCenterY() - Settings.OBJECT_DEFAULT_SIZE - (int)npc.getActionString().getTimer().getValue() / Settings.GAME_FREQUENCY;
			g.drawString(npc.getActionString().getMessage(), x, y);
		
		}
	}
	
	protected void paintMonsters(Graphics g) {
		double lifePercentage;
		int greenBarWidth;
		String hpStatus;
		for(int i=0; i<Game.getInstance().getMonsters().size(); ++i) {
			Monster monster=Game.getInstance().getMonster(i);
			lifePercentage=(double)monster.getCurrentHp()/(double)monster.getMaxHp();
			g.setColor(Color.RED);
			greenBarWidth=(int)(lifePercentage*Settings.OBJECT_DEFAULT_SIZE);
			g.drawImage(monsterImageFromState(i) ,monster.getX(), monster.getY(), Settings.OBJECT_DEFAULT_SIZE, Settings.OBJECT_DEFAULT_SIZE, null);
			g.fillRect(monster.getX(), monster.getY()-Settings.DEFAULT_BARS_HEIGTH, greenBarWidth, Settings.DEFAULT_BARS_HEIGTH);
			
			g.setColor(Color.GRAY);
			
			if(greenBarWidth<Settings.OBJECT_DEFAULT_SIZE) {
				g.fillRect(monster.getX()+greenBarWidth, monster.getY()-Settings.DEFAULT_BARS_HEIGTH, Settings.OBJECT_DEFAULT_SIZE-greenBarWidth, Settings.DEFAULT_BARS_HEIGTH);
			}
			
			hpStatus=monster.getCurrentHp() + " / " + monster.getMaxHp();
			g.setColor(Color.BLACK);
			g.drawString(hpStatus, monster.getCenterX() - fMText.stringWidth(hpStatus) / 2, monster.getY() - 3);
		}
	}
	
	//restituisce l'immagine corrispondente all'animazione corrente del mostro iesimo
	public Image monsterImageFromState(int index) {
		return monsterAnimations.m.get(monstersAnimationState.get(index).getAnimationType()).get(monstersAnimationState.get(index).getAnimationState());
	
	}
	
	protected void paintItems(Graphics g) {
		ArrayList<Item> items = Game.getInstance().getItems();
		for(int i=0; i< items.size(); ++i) {
			Item item= items.get(i);
			if(item.isDropped()) {
				switch(item.getItemType()) {
					case ItemSettings.HP_POTION:
						g.drawImage(itemImages.getImages().get(NameToFileName.toFileName(ItemSettings.HP_POTION_NAME)), item.getX(), item.getY(), Settings.DROP_DEFAULT_SIZE, Settings.DROP_DEFAULT_SIZE, null);
						break;
					case ItemSettings.EXP_POTION:
						g.drawImage(itemImages.getImages().get(NameToFileName.toFileName(ItemSettings.EXP_POTION_NAME)), item.getX(), item.getY(), Settings.DROP_DEFAULT_SIZE, Settings.DROP_DEFAULT_SIZE, null);
						break;
					case ItemSettings.MEDICINE:
						g.drawImage(itemImages.getImages().get(NameToFileName.toFileName(ItemSettings.MEDICINE_NAME)), item.getX(), item.getY(), Settings.DROP_DEFAULT_SIZE, Settings.DROP_DEFAULT_SIZE, null);
						break;
					case ItemSettings.ESSENCE:
						g.drawImage(itemImages.getImages().get(NameToFileName.toFileName(ItemSettings.ESSENCE_NAME)), item.getX(), item.getY(), Settings.DROP_DEFAULT_SIZE, Settings.DROP_DEFAULT_SIZE, null);
						break;
				}
			}
		}
		
	}
	
	protected void paintMap(Graphics g) {
	
		Character character = Game.getInstance().getCharacter();
		GameMap map = Game.getInstance().getCharacter().getMap();
		int padding=50;
		int xMap = character.getCenterX() + padding, yMap = character.getCenterY() - wS.getWindowHeight() / 2 + padding;

		int dimPoint = 5;
		//aggiungo la dimensione del punto alla visualizzazione della mappa per evitare che i punti relativi alle entità in basso ed a destra (sui bordi)
		//ricadano fuori dalla mappa. Questo succede perchè ovviamente i punti devono avere una dimensione
		int mapFrameWidth = 20;
		
		g.setColor(Colors.MAP_BORDER_COLOR);
		g.fillRect(xMap - mapFrameWidth, yMap - mapFrameWidth, Settings.DEFAULT_MAP_SIZE + 2 * mapFrameWidth + dimPoint, Settings.DEFAULT_MAP_SIZE + 2 * mapFrameWidth + dimPoint);
		
		g.setColor(Colors.MAP_COLOR);
		g.fillRect(xMap, yMap, Settings.DEFAULT_MAP_SIZE + dimPoint, Settings.DEFAULT_MAP_SIZE + dimPoint);

		for(int i = 0; i < map.size(); ++i) {
			if(map.get(i).getType() == MapElement.TYPE_ENEMY) {
				g.setColor(Colors.MAP_MONSTER_COLOR);
			}
			else if(map.get(i).getType() == MapElement.TYPE_NPC ) {
				g.setColor(Colors.MAP_NPC_COLOR);
				
			}
			else {
				g.setColor(Colors.MAP_CHARACTER_COLOR);
			}
			
			g.fillOval(map.get(i).getX()+ xMap, map.get(i).getY()+ yMap, dimPoint, dimPoint);
		}
		g.setColor(Colors.TEXT_WHITE_COLOR);
		int x = xMap + Settings.DEFAULT_MAP_SIZE / 2  - fMText.stringWidth("Map") /2;
		int y = yMap - mapFrameWidth/3;
		g.drawString("Map", x, y);
	}
	
	//aggiornamento dell'animazione del personaggio  e dello stato dei componenti della view
	public void updateDirection(int direction) {
		if(direction >= GameDirection.RIGHT && direction <= GameDirection.ATTACK_UP && direction != GameDirection.DIRECTION_NULL)
			characterAnimations.update(direction);
	}
	
	public void paintSaveQuitMenu(Graphics g) {
		g.setFont(buttonFont);
		fMText = getFontMetrics(buttonFont);
		
		MenuButton bM;
		for(int i = 0; i < saveQuitMenu.getButtons().size(); ++i) {
			
			bM = saveQuitMenu.getButtons().get(i);
			g.setColor(bM.getColor());	
			g.fillRect((int)bM.getRectangle().getX(), (int)bM.getRectangle().getY(), Settings.DEFAULT_BUTTONS_WIDTH, Settings.DEFAULT_BUTTONS_HEIGHT);
			g.setColor(Colors.TEXT_WHITE_COLOR);
			g.drawString(bM.getText(), (int)bM.getRectangle().getCenterX() - fMText.stringWidth(bM.getText()) / 2, (int)bM.getRectangle().getCenterY());
		}
		
		fMText = getFontMetrics(textFont);
		g.setFont(textFont);
	}
	
	public void paintReviveMenu(Graphics g) {
		g.setFont(buttonFont);
		fMText = getFontMetrics(buttonFont);
		//System.out.println("WEEEE");
		MenuButton bM;
		reviveMenu.setButtonLocation(wS.getWindowWidth(), wS.getWindowHeight());
		for(int i = 0; i < reviveMenu.getButtons().size(); ++i) {
			//System.out.println("wewe" + reviveMenu.getButtons().get(i).getRectangle().getX() + " " +  reviveMenu.getButtons().get(i).getRectangle().getY() );
			bM = reviveMenu.getButtons().get(i);
			g.setColor(bM.getColor());	
			g.fillRect((int)bM.getRectangle().getX(), (int)bM.getRectangle().getY(), Settings.DEFAULT_BUTTONS_WIDTH, Settings.DEFAULT_BUTTONS_HEIGHT);
			g.setColor(Colors.TEXT_WHITE_COLOR);
			g.drawString(bM.getText(), (int)bM.getRectangle().getCenterX() - fMText.stringWidth(bM.getText()) / 2, (int)bM.getRectangle().getCenterY());
		}
		fMText = getFontMetrics(textFont);
		g.setFont(textFont);
	}
	
	// resize del pannello di gioco ed aggiustamento  della inquadratura della camera 
	public void updateWindowState() {
		//cambia lo stato della schermata
		wS.switchWindowState();
		//cambia la dimensione del pannello
		this.setPreferredSize(new Dimension(wS.getWindowWidth(), wS.getWindowHeight()));

		//centra la camera
		if(Game.getInstance().isPlaying() || Game.getInstance().isPaused()) {
			gameCamera.setWindowDimension(wS.getWindowWidth(), wS.getWindowHeight());
			gameCamera.centerOnObject(Game.getInstance().getCharacter());
		}
	}
	
	public void update() {
		characterAnimations.update();
		gameCamera.centerOnObject(Game.getInstance().getCharacter());
		//aggiornamento dello stato dell'animazione di ogni mostro
		int monsterDirection;
		for(int i=0; i < Game.getInstance().getMonsters().size(); ++i) {
			monsterDirection = Game.getInstance().getMonsters().get(i).getCurrentDirection();
			monstersAnimationState.get(i).update(monsterDirection, monsterAnimations.m.get(monsterDirection).size() -1);
		}
		
		npcAnimations.update();
		if(Game.getInstance().getCharacter().getInventory().getIsOpen())
			inventoryView.updateLocation(wS.getWindowWidth(), wS.getWindowHeight(), this.getMousePosition());
	}
	
	public void gameStarted() {
		inventoryView = new InventoryView(wS.getWindowWidth(), wS.getWindowHeight());
		
		for(int i = 0; i < Game.getInstance().getMonsters().size(); ++i) {
			monstersAnimationState.add(new MonsterAnimationState());
			
		}
	}
	
	public CharacterAllAnimations getAnimations(){
		return characterAnimations;
	}
	
	public WindowState getWindowState() {
		return wS;
	}
	
	public SaveQuitMenu getSaveQuitMenu() {
		return saveQuitMenu;
	}
	public ReviveMenu getReviveMenu() {
		return reviveMenu;
	}
	
	public MainMenu getMainMenu() {
		return mainMenu;
	}
	public WindowState getWS() {
		return wS;
	}
	
	public GameCamera getGameCamera() {
		return gameCamera;
	}
}