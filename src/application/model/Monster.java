package application.model;

import java.awt.Point;
//rappresenta i mostri del gioco i quali sono
//"oggetti vivi", quindi hanno le caratteristiche di questi ultimi.
//Inoltre i mostri si possono muovere, quindi implementano l'interfaccia Movable
public class Monster extends AliveObject implements Movable{
	
	private int speed;
	private int exp;
	
	
	// genera un mostro sulle coordinate specificate (all'inizio del gioco)
	public Monster( int x, int y) {
		super(AliveObjectSettings.MONSTER, x, y);
		speed = 1;
		exp = 50;
		assignInitialDirection(Game.getInstance().getRandom().nextInt(4));	
	}
	
	//ripristina i mostri da un salvataggio (altrimenti inutilizzato)
	public Monster(int id, int x, int y, int speed, int exp, int maxHp, int currentHp, int attackDamage, int currentDirection, int hpRegen) {
		super(id, AliveObjectSettings.MONSTER, x, y, maxHp, currentHp, attackDamage, hpRegen, currentDirection);
		this.exp = exp;
		this.speed = speed;
		this.setCurrentDirection(currentDirection);		
	}
	
	//utilizzato per generare un mostro con caratteristiche adeguate al livello del personaggio
	public void setStatsBasedOnCharacterLevel(int characterLevel){
		exp= AliveObjectSettings.MONSTER_DEFAULT_EXP + AliveObjectSettings.MONSTER_INCREASED_EXP_PER_PLAYER_LVL * (characterLevel - 1);
		this.setMaxHp(AliveObjectSettings.MONSTER_START_HP + AliveObjectSettings.MONSTER_INCREASED_HP_PER_PLAYER_LVL * (characterLevel - 1));
		this.setCurrentHp(getMaxHp());
		this.setAttackDamage(AliveObjectSettings.MONSTER_START_ATTACK_DAMAGE + AliveObjectSettings.MONSTER_INCREASED_ATTACK_DAMAGE_PER_PLAYER_LVL *(characterLevel - 1));
	}
	
	public int getExp() {
		return exp;
	}
	
	public int getSpeed() {
		return speed;
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
			setCurrentDirection(direction);
			getHitbox().setLocation(getX(), getY());
			return true;
		}
		//altrimenti ripristino l'hitbox alla coordinata precedente al movimento
		else {
			getHitbox().setLocation(getX(), getY());
			return false;
		}
	}

	//metodo per verificare le collisioni
	@Override
	public boolean collided() {
		Point p  = new Point((int)getHitbox().getCenterX(), (int)getHitbox().getCenterY());
		
		if(!Game.getInstance().getGameArea().contains(p))
			return true;
		
		for(int i = 0; i < Game.getInstance().getSafeZones().size(); ++i) {
			if(Game.getInstance().getSafeZones().get(i).contains(p))
				return true;
		}
		return false;
	}
	
	//override del metodo respawn di AliveObject necessario per
	//dare una direzione di idle random al "nuovo" mostro
	@Override
	public void respawn(int x, int y) {
		super.respawn(x, y);
		assignInitialDirection(Game.getInstance().getRandom().nextInt(4));
	}

	//restituisce se il mostro sta attaccando oppure no
	public boolean attacking() {
		return getCurrentDirection() == GameDirection.ATTACK_DOWN || getCurrentDirection() == GameDirection.ATTACK_LEFT || getCurrentDirection() == GameDirection.ATTACK_UP || getCurrentDirection() == GameDirection.ATTACK_RIGHT;
	}
	
	//da una direzione idle iniziale al mostro
	public void assignInitialDirection(int number) {
		switch(number) {
			case 0:
				setCurrentDirection(GameDirection.IDLE_RIGHT);
				break;
			case 1:
				setCurrentDirection(GameDirection.IDLE_LEFT);
				break;
			case 2: 
				setCurrentDirection(GameDirection.IDLE_UP);
				break;
			case 3:
				setCurrentDirection(GameDirection.IDLE_DOWN);
				break;
		}
	}
	
	//da una direzione idle in un secondo momento e a seguito
	//di qualche azione che lo richieda (quando il mostro non riesce più a seguire il giocatore)
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
}
