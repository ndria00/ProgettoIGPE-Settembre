package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import application.Settings;
import application.model.Character;
import application.model.Game;
import application.model.GameSetting;
import application.model.Inventory;
import application.model.ItemInventory;
import application.model.ItemSettings;
import application.model.Mission;
import application.model.Monster;

//gestisce l'interazione tra gioco e DB
public class DBConnection {
	private String url;
	private Connection con;
	
	//crea la connessione
	public DBConnection() {
		try {
			url = "jdbc:sqlite:db_game.db";
			con = DriverManager.getConnection(url);
			//Forza i vincoli di foreign key che altrimenti verrebbero ignorati 
			con.createStatement().execute("PRAGMA foreign_keys = ON;");
			
			
		} catch (SQLException e) {
			System.out.println("Error while connecting to database" );
			System.exit(1);
		}
		
	}
	
	//legge i messaggi di aiuto
	public ArrayList<String> getHelpMessages(){
		
		System.out.println("Reading help messages");
		String query = "SELECT * FROM help_messages;";
		ArrayList<String> helpMessages = new ArrayList<String>();
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				helpMessages.add(result.getString("description"));
				
			}
		} catch (SQLException e) {
			System.out.println("Error while reading help messages");
		}
		return helpMessages;
	}
	
	//Legge le informazioni relative alle impostazioni del gioco
	public ArrayList<GameSetting> getSettings(){
		System.out.println("Reading game Settings");
		String query = "SELECT * FROM settings;";
		ArrayList<GameSetting> settings = new ArrayList<GameSetting>();
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				settings.add(new GameSetting(result.getString("description"),result.getString("associated_key")));
			}
		} catch (SQLException e) {
			System.out.println("Error while loading settings");
		}
		
		return settings;
	}
	
	//legge i profili esistenti
	public ArrayList<String> getAvailableProfiles(){
		System.out.println("Reading available profiles");
		String query = "SELECT name FROM characters;";
		ArrayList<String> availableProfiles = new ArrayList<String>();
		try {
			PreparedStatement stmt =con.prepareStatement(query);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				availableProfiles.add(result.getString("name"));
				
			}
		} catch (SQLException e) {
			System.out.println("Error while reading profiles");
		}
		return availableProfiles;
	}
	
	//generazione di alcune missioni in modo abbastanza semplice
	//le missioni vengono create insieme al profilo e vengono memorizzate del DB
	public void createAndSaveMissionsForNewPlayer(String characterName) {
		System.out.println("Generating missions for new player");
		for(int i = 0; i < Settings.NUMBER_OF_MISSION_PER_PLAYER; ++i) {
			String name = "missione " + i;
			String description;
			boolean rewardType;
			int rewardQuantity ;
			int requiredItem;
			if(i % 2 == 0) {
				requiredItem = ItemSettings.MEDICINE;
				rewardType = Settings.REWARD_HP;
				rewardQuantity = Settings.REWARD_HP_QUANTITY;
				description = "Drop a medicine and bring it to Isaac";
			}
			else {
				requiredItem = ItemSettings.ESSENCE;
				rewardType = Settings.REWARD_EXP;
				rewardQuantity = Settings.REWARD_XP_QUANTITY;
				description = "Drop an essence and bring it to Isaac";
			}
			int livMin = i + 1;
			
			String query ="INSERT INTO missions VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt;
			try {
				stmt = con.prepareStatement(query);
				stmt.setNull(1, Types.NULL);
				stmt.setString(2, name);
				stmt.setString(3, description);
				stmt.setInt(4, livMin);
				stmt.setBoolean(5, false);
				stmt.setBoolean(6, rewardType);
				stmt.setInt(7, rewardQuantity);
				stmt.setInt(8, requiredItem);
				stmt.setBoolean(9, false);
				stmt.setString(10, characterName);
				
				stmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error while generating new missions");
				e.printStackTrace();
				System.exit(1);
			}

		}
	}
	
	//legge le missioni
	public ArrayList<Mission> getMissions() {
		System.out.println("Reading missions");
		ArrayList<Mission> missions = new ArrayList<Mission>();
		try {
			
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM missions WHERE  done = ? AND character_name =?;");
			stmt.setBoolean(1, false);
			stmt.setString(2, Game.getInstance().getCharacter().getName());
			
			ResultSet result= stmt.executeQuery();
			while(result.next()) {
				//System.out.println("Here's  a mission");
				int id = result.getInt("id");
				String name= result.getString("name");
				String description = result.getString("description");
				int livMin = result.getInt("liv_min");
				boolean done = result.getBoolean("done");
				boolean rewardType = result.getBoolean("reward_type");
				int rewardQuantity = result.getInt("reward_quantity");
				int requiredItem = result.getInt("required_item");
				boolean assigned = result.getBoolean("assigned");

				Mission mission = new Mission(id, name, description, livMin, done, rewardType, rewardQuantity, requiredItem, assigned);
				missions.add(mission);
			
			}

		} catch (SQLException e) {
			System.out.println("Error while reading missions");
		}
		return missions;
	}
	
	//legge i dati relativi al personaggio specificato
	public Character getCharacter(String characterName) {
		try {
			String query = "SELECT * FROM characters where name =?;";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, characterName);
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				System.out.println("Reading character");
				int x = result.getInt("x");
				int y = result.getInt("y");
				int speed = result.getInt("speed");
				int exp = result.getInt("exp");
				int level = result.getInt("level");
				int expToNextLevel = result.getInt("exp_to_next_lvl");
				int maxHp = result.getInt("max_hp");
				int currentHp = result.getInt("current_hp");
				int attackDamage = result.getInt("attack_damage");
				int hpRegen = result.getInt("hp_regen");
				int currentDirection = result.getInt("current_direction");
				Character character = new Character(characterName, x, y, speed, level, exp, expToNextLevel, maxHp, currentHp, attackDamage, hpRegen, currentDirection);
				return character;
			}
		} catch (SQLException e) {
			System.out.println("The selected player does not exist");
		}
		return null;
	}
	
	//legge i mostri relativi al profilo selezionato
	public ArrayList<Monster> getMonsters(String characterName){
		System.out.println("Reading monsters");
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		try {
			String query = "SELECT * FROM monsters WHERE character_name =?;";
			PreparedStatement stmt =  con.prepareStatement(query);
			stmt.setString(1, characterName);
			ResultSet result = stmt.executeQuery();
			
			while(result.next()) {
				int id = result.getInt("id");
				int x = result.getInt("x");
				int y = result.getInt("y");
				int speed = result.getInt("speed");
				int exp = result.getInt("exp");
				int maxHp = result.getInt("max_hp");
				int currentHp = result.getInt("current_hp");
				int attackDamage = result.getInt("attack_damage");
				int currentDirection = result.getInt("current_direction");
				int hpRegen = result.getInt("hp_regen");
				monsters.add(new Monster(id, x, y, speed, exp, maxHp, currentHp, attackDamage, currentDirection, hpRegen));
			}
			return monsters;
			
		} catch (SQLException e) {
			System.out.println("Error while loading monsters");
		}
		return null;
		
		
	}
	
	//legge l'inventario relativo al profilo selezionato
	public Inventory getInventory(String characterName){
		System.out.println("Loading inventory");
		try {
			String query = "SELECT item_type, quantity FROM items_inventories WHERE character_name =?;";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, characterName);
			ResultSet result= stmt.executeQuery();
			Inventory inventory = new Inventory();
			while(result.next()) {
				//System.out.println("wewe");
				int itemType = result.getInt(1);
				int quantity = result.getInt(2);
				ItemInventory itemInv = new ItemInventory(itemType, quantity);
				inventory.add(itemInv);
			}
			return inventory;
		}catch(SQLException e) {
			System.out.println("Error while loading inventory");
		}
		
		return null;
	}
	
	//salva la missione completata con id specificato
	public void saveCompletedMission(int id) throws SQLException{
		PreparedStatement stmt;
			stmt = con.prepareStatement("UPDATE missions SET done = true, assigned = false WHERE id = ?;");
			stmt.setInt(1, id);
			stmt.executeUpdate();
	}
	
	//salva la missione assegnata con id specificato
	public void saveAssignedMission(int id) throws SQLException{
		PreparedStatement stmt;
			stmt = con.prepareStatement("UPDATE missions SET assigned = true WHERE id = ?;");
			stmt.setInt(1, id);
			stmt.executeUpdate();
	}
	
	//effettua il primo salvataggio del gioco (quando viene creato il profilo)
	public void firstSaveGame() {
		Character character = Game.getInstance().getCharacter();
		//save character
		String saveCharacterStats = "INSERT INTO characters values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement saveCharacter;
		try {
			saveCharacter = con.prepareStatement(saveCharacterStats);
			saveCharacter.setString(1, character.getName());
			saveCharacter.setInt(2, character.getX());
			saveCharacter.setInt(3, character.getY());
			saveCharacter.setInt(4, character.getSpeed());
			saveCharacter.setInt(5, character.getXP());
			saveCharacter.setInt(6, character.getLevel());
			saveCharacter.setInt(7, character.getXPToNextLevel());
			saveCharacter.setInt(8, character.getMaxHp());
			saveCharacter.setInt(9, character.getCurrentHp());
			saveCharacter.setInt(10, character.getAttackDamage());
			saveCharacter.setInt(11, character.getHpRegen());
			saveCharacter.setInt(12, character.computeIdleDirection());
			saveCharacter.executeUpdate();
		
			//save  monsters
			for(int i = 0; i <Game.getInstance().getMonsters().size(); ++i) {
				Monster monster = Game.getInstance().getMonsters().get(i);
				String saveMonsterUpdate = "INSERT INTO monsters values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement saveMonsters = con.prepareStatement(saveMonsterUpdate);
				saveMonsters.setNull(1, Types.NULL);
				saveMonsters.setInt(2, monster.getX());
				saveMonsters.setInt(3, monster.getY());
				saveMonsters.setInt(4, monster.getSpeed());
				saveMonsters.setInt(5, monster.getExp());
				saveMonsters.setInt(6, monster.getMaxHp());
				saveMonsters.setInt(7, monster.getCurrentHp());
				saveMonsters.setInt(8, monster.getAttackDamage());
				saveMonsters.setString(9, character.getName());
				//il personaggio deve rimanere sempre idle nell'ultima direzione di movimento
				saveMonsters.setInt(10, monster.computeIdleDirection());
				saveMonsters.setInt(11, monster.getHpRegen());
				saveMonsters.executeUpdate();
			}
		
		} catch (SQLException e) {
			System.out.println("Error while saving profile data");
		}

	}
	
	//salva lo stato del gioco
	public void saveGameState() {
		System.out.println("Saving game state");

		try {
			Character character = Game.getInstance().getCharacter();
			//Rimozione dei dati relativi all'ultimo salvataggio
			removeLastSave(character);
			
			//salva il personaggio
			String saveCharacterUpdate = "UPDATE  characters SET x = ?, y = ?, speed = ?, exp = ?, level = ?,"
					+ " exp_to_next_lvl = ?, max_hp = ?, current_hp = ?, attack_damage = ?, hp_regen  = ?, current_direction = ? WHERE name = ?;";
			PreparedStatement saveCharacter = con.prepareStatement(saveCharacterUpdate);
			saveCharacter.setInt(1, character.getX());
			saveCharacter.setInt(2, character.getY());
			saveCharacter.setInt(3, character.getSpeed());
			saveCharacter.setInt(4, character.getXP());
			saveCharacter.setInt(5, character.getLevel());
			saveCharacter.setInt(6, character.getXPToNextLevel());
			saveCharacter.setInt(7, character.getMaxHp());
			saveCharacter.setInt(8, character.getCurrentHp());
			saveCharacter.setInt(9, character.getAttackDamage());
			saveCharacter.setInt(10, character.getHpRegen());
			saveCharacter.setInt(11, character.computeIdleDirection());
			saveCharacter.setString(12, character.getName());
			saveCharacter.executeUpdate();
			
			//salva l'inventario
			for(int i = 0; i < character.getInventory().size(); ++i) {
					String saveIventoryUpdate = "INSERT INTO items_inventories values(?, ?, ?);";
					PreparedStatement saveInventory = con.prepareStatement(saveIventoryUpdate);
					saveInventory.setString(1, character.getName());
					saveInventory.setInt(2,  character.getInventory().get(i).getItem().getItemType());
					saveInventory.setInt(3, character.getInventory().get(i).getQuantity());
					saveInventory.executeUpdate();
			}
			
			//salva i mostri
			for(int i = 0; i <Game.getInstance().getMonsters().size(); ++i) {
				Monster monster = Game.getInstance().getMonsters().get(i);
				String saveMonsterUpdate = "UPDATE monsters SET x = ?, y = ?, speed = ?, exp = ?, max_hp = ?, current_hp = ?, "
						+ "attack_damage = ?, character_name = ?, current_direction = ?, hp_regen = ? WHERE id = ?";
				PreparedStatement saveMonsters = con.prepareStatement(saveMonsterUpdate);
				saveMonsters.setInt(1, monster.getX());
				saveMonsters.setInt(2, monster.getY());
				saveMonsters.setInt(3, monster.getSpeed());
				saveMonsters.setInt(4, monster.getExp());
				saveMonsters.setInt(5, monster.getMaxHp());
				saveMonsters.setInt(6, monster.getCurrentHp());
				saveMonsters.setInt(7, monster.getAttackDamage());
				saveMonsters.setString(8, character.getName());
				saveMonsters.setInt(9, monster.computeIdleDirection());
				saveMonsters.setInt(10, monster.getHpRegen());
				saveMonsters.setInt(11, monster.getId());
				saveMonsters.executeUpdate();
				
			}
			
			//salva i progressi relativi alle missioni
			for(int i = 0; i < Game.getInstance().getNPC().getMissions().size(); ++i){
				Mission mission= Game.getInstance().getNPC().getMissions().get(i);
				if(mission.isCompleted()) {
					saveCompletedMission(mission.getId());
				}
				else if(mission.isAssigned())
					saveAssignedMission(mission.getId());
			}
			
		} catch (SQLException e) {
			System.out.println("Error while saving game state");
		}
		
	}
	
	//elimina i dati relativi all'ultimo salvataggio
	public void removeLastSave(Character character) throws SQLException{
		//rimozione dell'inventario
		String removeInventoryUpdate = "DELETE FROM items_inventories WHERE character_name = ?";
		PreparedStatement removeInventory = con.prepareStatement(removeInventoryUpdate);
		removeInventory.setString(1, character.getName());
		removeInventory.executeUpdate();
	}
	
	//elimina il profilo di gioco
	public void removeProfile(String characterName) {
		System.out.println("Removing profile");
		try {
			//rimozione dei mostri
			String removeMonsters = "DELETE FROM monsters WHERE character_name =?;";
			PreparedStatement stmt1 = con.prepareStatement(removeMonsters);
			stmt1.setString(1, characterName);
			
			//rimozione degli item
			String removeItems = "DELETE FROM items_inventories WHERE character_name =?;";
			PreparedStatement stmt2 = con.prepareStatement(removeItems);
			stmt2.setString(1, characterName);
			
			//rimozione delle missioni
			String removeMissions = "DELETE FROM missions WHERE character_name =?;";
			PreparedStatement stmt3 = con.prepareStatement(removeMissions);
			stmt3.setString(1, characterName);
			
			
			//rimozione del personaggio
			String removeCharacter = "DELETE FROM characters WHERE name =?;";
			PreparedStatement stmt4 = con.prepareStatement(removeCharacter);
			stmt4.setString(1, characterName);
			
			//esecuzione delle query
			stmt1.executeUpdate();
			stmt2.executeUpdate();
			stmt3.executeUpdate();
			stmt4.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Error while deleting profile");
		}	
	}
}