package application.model;

import java.awt.Point;

import application.Settings;

//rappresenta il personaggio del gioco, quello che l'utente utilizza
// il personaggio può muoversi, oltre che avere le caratteristiche di un AliveObject
//e fare molto altro
public class Character extends AliveObject implements Movable{
	private String name;
	private int speed;
	private int level;
	private int exp;
	private int expToNextLevel=0;
	private int attackRange;
	private Inventory inventory;
	private GameMap map;
	//id delle missioni attive per il personaggio
	private ActiveMissions activeMissions;
	
	//costruttore utilizzato alla creazione di un nuovo profilo 
	public Character(String characterName) {
		super(AliveObjectSettings.CHARACTER_WARRIOR);
		this.name = characterName; 
		inventory = new Inventory();
		speed = AliveObjectSettings.CHARACTER_WARRIOR_SPEED;
		level = 1;
		exp = 0;
		calculateExpToNextLevel();
		map = new GameMap();
		activeMissions = new ActiveMissions();
		name = characterName;
		
	}
	
	//costruttore utilizzato per riprendere il gioco dai salvataggi (i dati sono contenuti del DB)
	public Character(String name, int x, int y, int speed, int level, int exp, int expToNextLevel, int maxHp, int currentHp, int attackDamage, int hpRegen, int currentDirection) {
		super(1, AliveObjectSettings.CHARACTER_WARRIOR, x, y, maxHp, currentHp, attackDamage,hpRegen, currentDirection);
		this.name=name;
		inventory = new Inventory();
		this.speed = speed;
		this.level = level;
		this.exp = exp;
		this.expToNextLevel = expToNextLevel;
		map = new GameMap();
		inventory = new Inventory();
		activeMissions = new ActiveMissions();
	}
	
	//calcolo dell'esperienza necessaria  per salire di livello in base al livello corrente
	private void calculateExpToNextLevel(){
		expToNextLevel = expToNextLevel + AliveObjectSettings.CHARACTER_INCREASE_EXP_TO_NEXT_LEVEL;
	}
	
	//incremento delle statistiche del giocatore che avviene salendo di livello
	private void updateStats() {
		this.getActionString().reset(Settings.ACTION_STRING_LVL_UP);
		this.setMaxHp(getMaxHp() + AliveObjectSettings.CHARACTER_WARRIOR_INCREASE_HP_PER_LEVEL );
		this.setCurrentHp(getCurrentHp() +  AliveObjectSettings.CHARACTER_WARRIOR_INCREASE_HP_PER_LEVEL);
		this.setAttackDamage(getAttackDamage() + AliveObjectSettings.CHARACTER_WARRIOR_INCREASE_ATTACK_DAMAGE_PER_LEVEL);
	}
	
	//aggiornamento dello stato dell'esperienza e dei livelli nel momento in cui il personaggio acquisisce esperienza
	public void gainedExp(int exp) {
		this.exp+=exp;
		while(this.exp >= expToNextLevel) {
			level++;
			updateStats();
			this.exp -= expToNextLevel;
			calculateExpToNextLevel();
		}
	}
	
	public int getSpeed() {
		return speed;
	}
	
	
	public Integer getLevel() {
		return level;
	}
	
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public int getXP() {
		return exp;
	}
	
	public int getXPToNextLevel() {
		return expToNextLevel;
	}
	
	public GameMap getMap() {
		return map;
	}
	
	public String getName() {
		return name;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	//metodo di movimento
	@Override
	public boolean move(int direction) {
		Point destination = new Point(getX(), getY());
		
		switch(direction) {
			case GameDirection.RIGHT:
				destination.x+=speed;
				break;
			case GameDirection.LEFT:
				destination.x-=speed;
				break;
			case GameDirection.UP:
				destination.y-=speed;
				break;
			case GameDirection.DOWN:
				destination.y+=speed;
				break;
			default:
				return false;
		
		}
		
		//sposto l'hitbox
		getHitbox().setLocation(destination);
		
		//verifico se il punto centrale dell'hitbox cade nell'area di gioco oppure no
		//se ricade nell'area aggiorno la posizione dell'entità
		if(!collided()) {
			setX(destination.x);
			setY(destination.y);
			this.setCurrentDirection(direction);
			getHitbox().setLocation(getX(), getY());
			return true;
		}
		//altrimenti ripristino l'hitbox alla coordinata precedente al movimento
		else {
			getHitbox().setLocation(getX(), getY());
			return false;
		}
	}
	
	//verifica delle collisioni
	@Override
	public boolean collided() {
		Point p = new Point(getCenterX(),  getCenterY());
		if(!Game.getInstance().getGameArea().contains(p))
			return true;
		return false;
	}
	
	//da una direzione idle 
	@Override
	public int computeIdleDirection() {
		int newDirection = GameDirection.DIRECTION_NULL;
		if(getCurrentDirection() == GameDirection.IDLE_RIGHT || getCurrentDirection() == GameDirection.IDLE_LEFT  || getCurrentDirection() == GameDirection.IDLE_DOWN || getCurrentDirection() == GameDirection.IDLE_UP)
			return getCurrentDirection();
		switch(getCurrentDirection()) {
			case GameDirection.RIGHT: 
				newDirection = GameDirection.IDLE_RIGHT;
				break;
			case GameDirection.ATTACK_RIGHT:
				newDirection = GameDirection.IDLE_RIGHT;
				break;
			case GameDirection.LEFT:
				newDirection = GameDirection.IDLE_LEFT;
				break;
			case GameDirection.ATTACK_LEFT:
				newDirection = GameDirection.IDLE_LEFT;
				break;
			case GameDirection.DOWN:
				newDirection = GameDirection.IDLE_DOWN;
				break;
			case GameDirection.ATTACK_DOWN:
				newDirection = GameDirection.IDLE_DOWN;
				break;
			case GameDirection.UP:
				newDirection = GameDirection.IDLE_UP;	
				break;
			case GameDirection.ATTACK_UP:
				newDirection = GameDirection.IDLE_UP;	
				break;
		}
			return newDirection;
	}
	
	public ActiveMissions getMissions(){
		return activeMissions;
	}
	
	public int getAttackRange() {
		return attackRange;
	}
	
	public void setActiveMissions(ActiveMissions activeMissions) {
		this.activeMissions = activeMissions;
	}
	
	//acquisizione di una missione dall'NPC (se la missione viene acquisita l'id viene aggiunto "all'indice delle missioni" del giocatore)
	public void takeMission(int id) {
		for(int i = 0; i < activeMissions.size(); ++i){
			if(activeMissions.get(i) == id) {
					return;
			}	
		}
			activeMissions.add(id);
	}

}
