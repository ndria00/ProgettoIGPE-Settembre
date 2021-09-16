package application.model;

import application.Settings;

//raoppresenta gli oggetti "vivi" del gioco e che quindi hanno un livello vitale,
//una forza di attacco la capacità di rigenerrare gli hp, possono morire ed "interagire" con dei messaggi
public class AliveObject extends GameObject{
	private int id;
	private int maxHp;
	private int currentHp;
	private int attackDamage;
	private int hpRegen;
	private ObjectTimer attackTimer;
	private ObjectTimer hpRegenTimer;
	private ActionString actionString;
	private boolean alive;
	private int currentDirection;
	
	
	public AliveObject(int type) {
		super();
		setStats(type);
		actionString = new ActionString();
		alive=true;
		currentDirection = GameDirection.IDLE_RIGHT;
	}
	
	public AliveObject(int type, int x, int y) {
		super(x, y);
		setStats(type);
		actionString = new ActionString();
		alive=true;
		currentDirection = GameDirection.IDLE_RIGHT;
	}
	
	//costruttore per generare un oggetto avendo a disposizione tutti i dati
	//Utilizzato per riprendere il gioco dai salvataggi (i dati sono memorizzati del DB)
	public AliveObject(int id, int type, int x, int y, int maxHp, int currentHp, int attackDamage, int hpRegen, int currentDirection) {
		super(x, y);
		this.maxHp = maxHp;
		this.currentHp = currentHp;
		this.attackDamage = attackDamage;
		this.currentDirection = currentDirection;
		actionString = new ActionString();
		if(currentHp ==  0)
			alive = false;
		else
			alive = true;
		if(type == AliveObjectSettings.CHARACTER_WARRIOR) {
			id = 1;
			attackTimer= new ObjectTimer(AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_COOLDOWN);
			hpRegenTimer = new ObjectTimer(AliveObjectSettings.CHARACTER_WARRIOR_HP_REGEN_COOLDOWN);
			this.hpRegen = hpRegen;
			currentDirection = GameDirection.IDLE_RIGHT;
		}
		else if(type == AliveObjectSettings.MONSTER) {
			this.id = id;
			attackTimer= new ObjectTimer(AliveObjectSettings.MONSTER_ATTACK_COOLDOWN);
			hpRegenTimer =  new ObjectTimer(AliveObjectSettings.MONSTER_HP_REGEN_COOLDOWN);
			this.hpRegen = hpRegen;
		}
	}
	
	//vengono impostate  le statistiche in base al tipo di oggetto creato
	public void setStats(int type) {
		
		switch(type) {
		case AliveObjectSettings.CHARACTER_WARRIOR:
			maxHp=AliveObjectSettings.CHARACTER_WARRIOR_START_HP;
			currentHp=maxHp;
			attackDamage=AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_DAMAGE;
			attackTimer = new ObjectTimer(AliveObjectSettings.CHARACTER_WARRIOR_ATTACK_COOLDOWN);
			hpRegenTimer = new ObjectTimer(AliveObjectSettings.CHARACTER_WARRIOR_HP_REGEN_COOLDOWN);
			hpRegen = AliveObjectSettings.CHARACTER_WARRIOR_HP_REGEN_RATE;
			break;
	
		case AliveObjectSettings.MONSTER:
			maxHp=AliveObjectSettings.MONSTER_START_HP;
			currentHp=maxHp;
			attackDamage=AliveObjectSettings.MONSTER_START_ATTACK_DAMAGE;
			attackTimer = new ObjectTimer(AliveObjectSettings.MONSTER_ATTACK_COOLDOWN);
			hpRegenTimer = new ObjectTimer(AliveObjectSettings.MONSTER_HP_REGEN_COOLDOWN);
			hpRegen = AliveObjectSettings.MONSTER_HP_REGEN_RATE;
			break;
	
		}	
	}
	
	//aggiornamento dei timer necessari
	public void tick() {
		attackTimer.tick();
		//Non tutte le entità vive del gioco possono rigenerare gli hp, 
		//quelle che possono farlo avranno il timer non nullo e quindi lo aggiorneranno
		if(hpRegenTimer != null) {
			hpRegenTimer.tick();
		}
	}
	
	public boolean canRegenHP() {
		return hpRegenTimer.canDoAction() && currentHp < maxHp && alive;
	}
	
	public boolean canAttack() {
		return attackTimer.canDoAction();
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setCurrentHp(int hp) {
		if(hp > maxHp)
			currentHp = maxHp;
		else
			
			currentHp = hp;
	}
	
	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}
	
	public int getAttackDamage() {
		return attackDamage;
	}
	
	public int getCurrentHp() {
		return currentHp;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	public void setActionString(ActionString actionString) {
		this.actionString = actionString;
	}
	
	public void heal(int hpGained) {
		if(alive) {
			this.actionString.reset(Settings.ACTION_STRING_HEAL);
			this.currentHp += hpGained;
			if(currentHp > maxHp)
				currentHp = maxHp;
		}
	}
	
	public void damaged(int dmg) {
		this.currentHp -= dmg;
		if(currentHp <= 0) {
			alive = false;
			currentHp = 0;
		}
	}
	
	public ActionString getActionString() {
		return actionString;
	}
	public int getHpRegen() {
		return hpRegen;
	}
	
	public void regenHP() {
		this.setCurrentHp(getCurrentHp() + hpRegen);
	}
	
	public int getCurrentDirection() {
		return currentDirection;
	}
	
	public int getId() {
		return id;
	}
	
	public int attack() {
		//attackTimer.actionDone();
		return attackDamage;
	}
	
	public void respawn() {
		currentHp = maxHp;
		alive = true;
	}
	
	public void setCurrentDirection(int direction) {
		if(direction != GameDirection.DIRECTION_NULL)
			currentDirection = direction;
	}
	
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	
	public void respawn(int x, int y) {
		setX(x);
		setY(y);
		getHitbox().setLocation(x, y);
		this.currentHp = maxHp;
		alive = true;
	}
}