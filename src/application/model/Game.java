package application.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import application.Settings;
import application.db.DBConnection;

//rappresenta il gioco in quanto insieme di diversi componenti e si occupa della sua gestione,
//aggiornamento in funzione alle scelte del personaggio. La classe utilizza il pattern signleton
//in quanto nell'applicazione si ha necessità di una sola istanza di questa  classe ed è necessario
//che possa essere raggiunta da ogni punto del codice (quindi sui diversi componenti del pattern MVC)
//evitando di avere una referenza all'oggetto della classe
public class Game {
	
	private String gameName;
	private Character character;
	private ArrayList<Monster> monsters;
	private NPC npc;
	private Random random;
	private Rectangle gameArea;
	private ArrayList<Rectangle> safeZones;
	private ArrayList<Item> items;
	private DBConnection conn;
	private ArrayList<GameSetting> settings;
	private HelpSettings helpSettings;
	private int gameState;
	
	private static Game game;
	
	
	private Game() {
		gameName = Settings.GAME_NAME;
		random = new Random();

		gameArea = new Rectangle(0,0, Settings.WORLD_SIZE, Settings.WORLD_SIZE);
		conn = new DBConnection();
		settings = conn.getSettings();
		safeZones = new ArrayList<Rectangle>();
		safeZones.add(new Rectangle(0, 0, Settings.INITIAL_SAFE_ZONE, Settings.INITIAL_SAFE_ZONE));
		gameState = Settings.GAME_STATE_MAIN_MENU;
	}
	
	//creazione di un nuovo "profilo di gioco"
	public void createNewProfile(String characterName) {
		character = new Character(characterName);
		npc = new NPC();
		monsters = new ArrayList<Monster>();
		Point p;
		for(int i = 0; i < Settings.NUMBER_OF_MONSTERS; ++i) {
			p= randomPoint();
			monsters.add(new Monster(p.x, p.y));
		}
		
		//salvataggio del personaggio e dei mostri
		conn.firstSaveGame();
		
		//salvataggio delle missioni che deve avvenire in un secondo momento (o comunque dopo) del
		//salvataggio del giocatore in quanto la generazione delle missioni richiede  di rispettare un vincolo di
		//integrità referenziale con il nome del giocatore
		conn.createAndSaveMissionsForNewPlayer(characterName);
	}
	

	//ripresa del gioco dal salvataggio per il personaggio specificato
	public void continueExistingGame(String playerName) {
		items = new ArrayList<Item>();
		gameState=Settings.GAME_STATE_PLAYING;
		npc = new NPC();
		character = conn.getCharacter(playerName);
		character.setInventory(conn.getInventory(playerName));
		npc.readMissions(conn);
		//per inserire tra gli item del gioco quelli che il player ha nell'inventario
		addItemsFromInventory(character.getInventory());
		character.setActiveMissions(npc.getAssignedMissionId(character.getLevel()));
		monsters = conn.getMonsters(playerName);
		helpSettings = new HelpSettings(conn.getHelpMessages(),  character.getLevel() == 1);
	}
	
	//generazione di una coordinata random
	public int generateRandomCoordinate() {
		int t=random.nextInt(Settings.WORLD_SIZE)-Settings.OBJECT_DEFAULT_SIZE;
		if(t < 0)
			t = 0;
		return t;
	}
	
	//generazione di un punto in cui aggiungere un mostro
	public Point randomPoint() {
		
		int x;
		int y;
		
		do  {
			x = generateRandomCoordinate();
			y = generateRandomCoordinate();
			
		}while(x < Settings.INITIAL_SAFE_ZONE &&  y < Settings.INITIAL_SAFE_ZONE);
		
		Point p = new Point(x, y);
		return p;
	}
	
	
	//metodo che chiama il costruttore privato, necessario nel pattern signleton
	public static Game getInstance() {
		if(game == null) {
			game = new Game();
		}
		return game;
	}
	
	public Monster getMonster(int index) {
		return monsters.get(index);
	}
	
	public ArrayList<Monster> getMonsters(){
		return monsters;
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	//aggiornamento dello stato di gioco da pausa a "play"
	public void switchPause() {
		if(gameState == Settings.GAME_STATE_PLAYING)
			gameState = Settings.GAME_STATE_PAUSED;
		else if(gameState == Settings.GAME_STATE_PAUSED)
			gameState = Settings.GAME_STATE_PLAYING;
	}
	
	public boolean isPaused() {
		return gameState == Settings.GAME_STATE_PAUSED;
	}
	
	public boolean isPlaying() {
		return gameState == Settings.GAME_STATE_PLAYING;
	}
	
	public boolean isOnMainMenu() {
		return gameState == Settings.GAME_STATE_MAIN_MENU;
	}
	
	public DBConnection getConnectionDB() {
		return conn;
	}
	
	//aggiunta degli item nell'inventario (recuperati dal DB) a quelli del gioco
	public void addItemsFromInventory(Inventory inventory) {
		System.out.println("Filling inventory");
		for(int i = 0; i < inventory.size(); ++i) {
			for(int j = 0; j < inventory.get(i).getQuantity(); ++j) {
				Item item = new Item(inventory.get(i).getItem().getItemType(), true);
				items.add(item);
			}
		}
	}
	
	//aggiornamento del gioco in funzione dell'operazione compiuta dal giocatore
	public void update(int playerDirection) {
		
		//il gioco è in pausa e l'unica azione consentita (da tastiera) è uscire dalla pausa
		if(isPaused() && playerDirection != GameDirection.SWITCH_PAUSE) 
			return;
		
		//il player è morto e non può fare nessun movimento relativo al gioco
		if(!character.isAlive() && playerDirection != GameDirection.SWITCH_PAUSE) {
			return;
		}
		
			switch(playerDirection) {
				case GameDirection.MAP_OPEN_CLOSE:
					character.getMap().switchIsOpen();
					break;
				case GameDirection.ATTACK_RIGHT:
					characterAttack(playerDirection);
					break;
				case GameDirection.ATTACK_LEFT:
					characterAttack(playerDirection);
					break;
				case GameDirection.ATTACK_UP:
					characterAttack(playerDirection);
					break;
				case GameDirection.ATTACK_DOWN:
					characterAttack(playerDirection);
					break;
				case GameDirection.GATHER:
					gatherItem();
					break;
				case GameDirection.INVETORY_OPEN_CLOSE:
					character.getInventory().switchOpen();
					break;
				case GameDirection.CONSUME_ITEM_1:
					if(character.getInventory().size() > 0)
						consumeItem(1);
					break;
				case GameDirection.CONSUME_ITEM_2:
					if(character.getInventory().size() > 1)
						consumeItem(2);
					break;
				case GameDirection.CONSUME_ITEM_3:
					if(character.getInventory().size() > 2)
						consumeItem(3);
					break;
				case GameDirection.CONSUME_ITEM_4:
					if(character.getInventory().size() > 3)
						consumeItem(4);
					break;
				case GameDirection.CONSUME_ITEM_5:
					if(character.getInventory().size() > 4)
						consumeItem(5);
					break;
				case GameDirection.DROP_ITEM_1:
					if(character.getInventory().size() > 0)
						dropItem(1);
					break;
				case GameDirection.DROP_ITEM_2:
					if(character.getInventory().size() > 1)
						dropItem(2);
					break;
				case GameDirection.DROP_ITEM_3:
					if(character.getInventory().size() > 2)
						dropItem(3);
					break;
				case GameDirection.DROP_ITEM_4:
					if(character.getInventory().size() > 3)
						dropItem(4);
					break;
				case GameDirection.DROP_ITEM_5:
					if(character.getInventory().size() > 4)
						dropItem(5);
					break;
				case GameDirection.TALK_WITH_NPC:
					talkWithNPC();
					break;
				case GameDirection.SAVE_GAME:
					saveGame();
					break;
				case GameDirection.MISSIONS_SHOW_HIDE:
					character.getMissions().switchActiveState();
					break;
				case GameDirection.SWITCH_PAUSE:
					switchPause();
					break;			
			}

		
		//Se la direzione non è di movimento il personaggio non deve muoversi,
		//altrimenti avviene il movimento e viene centrata la camera
		if(playerDirection < GameDirection.GATHER) {
			character.setCurrentDirection(playerDirection);
			character.move(playerDirection);
		}
	
	}
	
	//aggiornamento periodico del gioco che avviene ad ogni ciclo
	//del gameloop
	public void update() {
		if(isPaused()) {
			return;
		}
		
		character.tick();
		
		if(character.getMap().isOpen())
			updateMap();
		
		if(character.canRegenHP())
			character.regenHP();
		
		// update del messaggio di azione qualora questo fosse attivo
		if(character.getActionString().getMessage() !=null)
			character.getActionString().tick();
		
		//fa sparire gli item droppati per cui è scaduto il tempo di permanenza a terra
		for(int i=0; i<items.size(); ++i) {
			if(items.get(i).isDropped())
				items.get(i).tick();
			
			if(items.get(i).getTimer() != null && items.get(i).getTimer().canDoAction())
				items.remove(i);
		}
		
		Monster monster;
		for(int i = 0; i < monsters.size(); ++i) {
			
			monster = monsters.get(i);
			boolean wasAttacking = monster.attacking();
			boolean canAttack = monster.canAttack();
			
			boolean playerInAttackRange = false;
			monsters.get(i).tick();
			if(canAttack){
				
				int attackDirection = monster.getCurrentDirection();
				
				switch(monster.getCurrentDirection()) {
					case GameDirection.RIGHT:
						attackDirection = GameDirection.ATTACK_RIGHT;
						break;
					case GameDirection.IDLE_RIGHT:
						attackDirection = GameDirection.ATTACK_RIGHT;
						break;
					case GameDirection.LEFT:
						attackDirection = GameDirection.ATTACK_LEFT;
						break;
					case GameDirection.IDLE_LEFT:
						attackDirection = GameDirection.ATTACK_LEFT;
						break;
					case GameDirection.UP: 
						attackDirection = GameDirection.ATTACK_UP;
						break;
					case GameDirection.IDLE_UP:
						attackDirection = GameDirection.ATTACK_RIGHT;
						break;
					case GameDirection.DOWN: 
						attackDirection = GameDirection.ATTACK_DOWN;
						break;
					case GameDirection.IDLE_DOWN:
						attackDirection = GameDirection.ATTACK_RIGHT;
						break;
						
				
				}
				
				Rectangle attackedArea = getMonsterAttackedArea(i, attackDirection);
				
				if(character.getHitbox().intersects(attackedArea) && character.isAlive()) {
					character.damaged(monster.attack());
					monster.setCurrentDirection(attackDirection);
					playerInAttackRange = true;
				}
				else {
					monster.setCurrentDirection(monster.computeIdleDirection());
				}
			}

			// update del messaggio di azione qualora questo fosse attivo
			if(monster.getActionString().getMessage() !=null)
				monster.getActionString().tick();
			
			//fa muovere i mostri se questi devono muoversi
			//ovvero se non stavano attaccando e non stanno attaccando nemmeno ora
			//oppure se stavano attaccando ed hanno finito la sequenza di attacco
			if((wasAttacking && canAttack && !playerInAttackRange) || (!wasAttacking && !monster.attacking()) && character.isAlive()){
				moveMonster(i);
			}
			
		}
		// update del messaggio di azione dell'NPC
		if(npc.getActionString().getMessage() != null)
			npc.getActionString().tick();
		
		//aggiorna l'action string del npc se ci sono nuove missioni per il personaggio
		if(npc.getActionString().getMessage() == null && npc.getAvailableMission(character.getLevel()) != null)
				npc.getActionString().reset(Settings.ACTION_STRING_NPC_MISSION_READY);
		
		// se il player è vicino al NPC ed ha l'item da consegnare per completare una missione allora questa viene 
		//completata ed al player viene assegnato il reward relativo ad essa
		if(npc.getHitbox().intersects(character.getHitbox())){
			
			int idMission;
			for(int i = 0; i < character.getMissions().size(); ++i) {
				idMission = character.getMissions().get(i);
				//item richiesto dalla missione
				Item item = new Item(npc.getMission(idMission).getRequiredItem());
				if(character.getInventory().containsItem(item)) {
						npc.completeMission(idMission);
						npc.getActionString().reset(Settings.ACTION_STRING_NPC_MISSION_COMPLETE);
						//rimuovi la missione con id idMission
						character.getMissions().remove((Integer)idMission);
						//assegna il bonus al giocatore
						giveBonus(idMission);
						character.getInventory().drop(item);
				}
			}
		}
	
	}
	
	//il personaggio butta un item tra quelli che ha nell'inventario
	public void dropItem(int position) {
		Item item =character.getInventory().get(position-1).getItem();
		if(character.getInventory().drop(character.getInventory().get(position-1).getItem())) {
			//trova l'item droppato ed aggiorna il suo stato
			for(int i = 0; i < items.size(); ++i) {
				if(items.get(i).equals(item) && !items.get(i).isDropped() && items.get(i).getOwnerId() == character.getId()) {
					items.get(i).drop(character.getCenterX(), character.getCenterY());
					break;
				}
			}
		}

	}
	
	//utilizzo dell'item che il personaggio ha nell'inventario
	public void consumeItem(int position) {
		//item che il player vuole consumare
		Item item = character.getInventory().get(position-1).getItem();
		if(character.getInventory().get(position - 1).getQuantity() > 0 && item.isConsumable()) {
			items.remove(character.getInventory().get(position - 1).getItem());
			
			character.getInventory().consume(character.getInventory().get(position - 1).getItem());
			
			switch(item.getItemType()) {
				case ItemSettings.HP_POTION: 
					character.heal(ItemSettings.HP_POTION_BONUS);
					break;
				case ItemSettings.EXP_POTION:
					character.gainedExp(ItemSettings.EXP_POTION_BONUS);
					break;
			
			}
		}
		
	}
	
	//area attaccata dal player
	public Rectangle getCharacterAttackedArea(int playerDirection) {
		Rectangle r= new Rectangle();
		switch(playerDirection) {
			case GameDirection.ATTACK_RIGHT:
				r.setBounds(character.getCenterX(), character.getCenterY(), AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_RANGE, Settings.OBJECT_DEFAULT_SIZE/2);
				break;
			case GameDirection.ATTACK_LEFT:
				r.setBounds(character.getCenterX() - AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_RANGE, character.getCenterY(), AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_RANGE, Settings.OBJECT_DEFAULT_SIZE/2);
				break;
			case GameDirection.ATTACK_DOWN:
				r.setBounds(character.getX(), character.getCenterY(), Settings.OBJECT_DEFAULT_SIZE/2, AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_RANGE);
				break;
			case GameDirection.ATTACK_UP:
				r.setBounds(character.getX(), character.getCenterY()- AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_RANGE, Settings.OBJECT_DEFAULT_SIZE/2,  AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_RANGE);
				break;
		}
		return r;
	}
	
	//area attaccata dal mostro
	public Rectangle getMonsterAttackedArea(int monsterIndex, int monsterDirection) {
		Monster monster = monsters.get(monsterIndex);
		Rectangle r= new Rectangle();
		switch(monsterDirection) {
			case GameDirection.ATTACK_RIGHT:
				r.setBounds(monster.getCenterX(), monster.getCenterY(), AliveObjectSettings.MONSTER_ATTACK_RANGE, Settings.OBJECT_DEFAULT_SIZE/2);
				break;
			case GameDirection.ATTACK_LEFT:
				r.setBounds(monster.getCenterX() - AliveObjectSettings.MONSTER_ATTACK_RANGE, monster.getCenterY(), AliveObjectSettings.MONSTER_ATTACK_RANGE, Settings.OBJECT_DEFAULT_SIZE/2);
				break;
			case GameDirection.ATTACK_DOWN:
				r.setBounds(monster.getX(), monster.getCenterY(), Settings.OBJECT_DEFAULT_SIZE/2, AliveObjectSettings.MONSTER_ATTACK_RANGE);
				break;
			case GameDirection.ATTACK_UP:
				r.setBounds(monster.getX() + Settings.OBJECT_DEFAULT_SIZE / 4, monster.getCenterY()- AliveObjectSettings.MONSTER_ATTACK_RANGE +15, Settings.OBJECT_DEFAULT_SIZE/2,  AliveObjectSettings.MONSTER_ATTACK_RANGE);
				break;
		}
		return r;
	}
	
	//aggiornamento della mappa
	public void updateMap() {
		
		GameMap map= character.getMap();
		map.clear();
		float worldIntoMap = (float)Settings.WORLD_SIZE / (float)Settings.DEFAULT_MAP_SIZE; 
		for(int i = 0; i < monsters.size(); ++i)
			map.add(new MapElement((int)(monsters.get(i).getCenterX() / worldIntoMap), (int)(monsters.get(i).getCenterY() / worldIntoMap), MapElement.TYPE_ENEMY));
		
		map.add(new MapElement((int)(character.getCenterX() / worldIntoMap), (int)(character.getCenterY() / worldIntoMap), MapElement.TYPE_PLAYER));
		map.add(new MapElement((int)(npc.getCenterX() / worldIntoMap), (int)(npc.getCenterY() / worldIntoMap), MapElement.TYPE_NPC));
		
	}
	
	//spostamento del mostro di indice i
	public void moveMonster(int index) {
		//System.out.println("Moving");
		double xCharacter = character.getHitbox().getCenterX();
		double yCharacter = character.getHitbox().getCenterY();
		
		Monster monster = monsters.get(index);
			double xMonster = monster.getHitbox().getCenterX();
			double yMonster = monster.getHitbox().getCenterY();
			double distance = Math.sqrt((xMonster-xCharacter)*(xMonster-xCharacter) + (yMonster-yCharacter)*(yMonster-yCharacter));
			
			boolean movedAlongY = true;
			boolean movedAlongX= true;
			if(distance < AliveObjectSettings.DISTANCE_TO_ATTACK) {
				//se il mostro può muoversi tenta di muoversi prima lungo l'asse y e, solo se non ci riesce, tenta di muoversi lungo x
				movedAlongY = moveMonsterToCharacterY(index);
				if(!movedAlongY)
					movedAlongX = moveMonsterToCharacterX(index);
				
				if(!movedAlongX && !movedAlongY)
					monster.setCurrentDirection(monster.computeIdleDirection());
				
			}
			//se il mostro va out of range e quindi non riesce più ad "inseguire" il player deve aggiornare la sua direzione 
			//la direzione diventerà idle verso l'ultima direzione di movimento
			else { /*if(monster.getCurrentDirection() < GameDirection.IDLE_RIGHT || monster.getCurrentDirection() > GameDirection.IDLE_DOWN) {*/
				monster.setCurrentDirection(monster.computeIdleDirection());
				if(monster.canRegenHP())
					monster.regenHP();
			}
			//}
				
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public Rectangle getGameArea() {
		return gameArea;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	public Random getRandom() {
		return random;
	}
	public ArrayList<GameSetting> getSettings() {
		return settings;
	}
	
	public HelpSettings getHelpSettings() {
		return helpSettings;
	}
	
	public ArrayList<Rectangle> getSafeZones() {
		return safeZones;
	}
	
	//movimento  lungo x
	public boolean moveMonsterToCharacterX(int index) {
		int monsterDirection = GameDirection.DIRECTION_NULL;
		Monster currentMonster = monsters.get(index);
		
		//il mostro si trova a destra del personaggio
		if(currentMonster.getX() > character.getX())
			monsterDirection = GameDirection.LEFT;
		//il mostro si trova a sinistra del personaggio
		else if(currentMonster.getX() < character.getX())
			monsterDirection = GameDirection.RIGHT;
		return currentMonster.move(monsterDirection);
	}
	
	//movimento lungo y
	public boolean moveMonsterToCharacterY(int index) {
		int monsterDirection = GameDirection.DIRECTION_NULL;
		Monster currentMonster = monsters.get(index);
		
		//il mostro si trova più in basso del personaggio
		if(currentMonster.getY() > character.getY())
			monsterDirection = GameDirection.UP;
		else if(currentMonster.getY() < character.getY())
			monsterDirection = GameDirection.DOWN;
		
		return currentMonster.move(monsterDirection);
	}
	
	
	//assegna il bonus relativo alla missione di id idMission al personaggio 
	public void giveBonus(Integer idMission) {
		if(npc.getMission(idMission).getRewardType() == Settings.REWARD_HP) {
			character.setMaxHp(character.getMaxHp() + npc.getMission(idMission).getRewardQuantity());
			character.heal(npc.getMission(idMission).getRewardQuantity());
		}
		else {
			character.gainedExp(npc.getMission(idMission).getRewardQuantity());
			
		}
		
	}
	
	//verifica la possibilità di droppare gli item relativi alle missioni
	//NOTA: è possibile droppare un item relativo alla missione solo se la missione che lo richiede è attiva
	// e non ci sono limiti sul numero di item che si possono ottenere mentre questa è attiva
	public boolean canDropItemForMission(int itemType) {
		return npc.assignedMission(itemType);
	}
	
	//genera l'item droppato dal mostro quando muore
	public Item monsterDrop(int percentageOfDrop) {
		Item item = null;
		
		if(percentageOfDrop > 0 && percentageOfDrop < 20)
			item = new Item(ItemSettings.EXP_POTION);
		else if(percentageOfDrop >20 && percentageOfDrop < 60)
			item = new Item(ItemSettings.HP_POTION);
		else if(canDropItemForMission (ItemSettings.MEDICINE) && percentageOfDrop >60 && percentageOfDrop < 80)
			item= new Item(ItemSettings.MEDICINE);
		else if(canDropItemForMission (ItemSettings.ESSENCE) && percentageOfDrop >80 && percentageOfDrop < 100)
			item= new Item(ItemSettings.ESSENCE);
		
		return item;
		
	}
	
	//attacco da parte del personaggio
	public void characterAttack(int playerDirection) {
		if(character.canAttack()) {
			character.attack();
			for(int i=0; i<monsters.size(); ++i) {
				if(monsters.get(i).getHitbox().intersects(getCharacterAttackedArea(playerDirection)) ){
					
					monsters.get(i).damaged(character.getAttackDamage());
						
					if(monsters.get(i).getCurrentHp() == 0) {
						character.gainedExp(monsters.get(i).getExp());
						//100 è utilizzato come "seed" per generare un nuovo Item
						Item item = monsterDrop(random.nextInt(100));
						
						if(item != null) {
							items.add(item);
							item.drop(monsters.get(i).getCenterX(), monsters.get(i).getCenterY());
						}

						//respawn del mostro morto
						Point p  = randomPoint();
						monsters.get(i).respawn(p.x, p.y);
						monsters.get(i).setStatsBasedOnCharacterLevel(character.getLevel());
					}
				}
			}
		}
	}
	
	//raccolta di un item (che si trova a terra) da parte del perrsonaggio
	public void gatherItem() {
		for(int i = 0; i < items.size(); ++i) {
			//solo un item per volta può essere preso tra quelli "vicini" al giocatore e verrà preso il primo droppato
			if(items.get(i).isDropped() && items.get(i).getHitbox().intersects(character.getHitbox())) {
				character.getInventory().gatherItem(items.get(i));
				items.get(i).gathered(character.getId());
				break;
			}

		}
	}
	
	//dialogo con l'NPC, cioè acquisizione della missione di livello più basso tra quelle ottenibili
	public void talkWithNPC() {
		if(character.getHitbox().intersects(npc.getHitbox())) {
			Mission mission = npc.getAvailableMission(character.getLevel());
			if( npc.getAvailableMission(character.getLevel()) != null) {
				character.takeMission(mission.getId());
				npc.getMission(mission.getId()).assignedMission();
			}
			else
				npc.getActionString().reset(Settings.ACTION_STRING_NPC_NO_MISSIONS_AVAILABLE);
		}
	}
	
	//ritorno allo stato del menu, che equivale ad interrompere il gioco
	public void exitProfile() {
		gameState = Settings.GAME_STATE_MAIN_MENU;
		
	}
	
	//salvataggio dello stato di gioco
	public void saveGame() {
		conn.saveGameState();
	}

}