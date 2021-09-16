package application.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import application.model.GameDirection;

//gestisce le immagini relative a tutte le animazioni del personaggio
public class CharacterAllAnimations {
	Map<Integer, ObjectAnimation> m;
	ObjectAnimation currentAnimation;
	HashMap<Integer, String> directionToAction = new HashMap<>();
	
	public CharacterAllAnimations() {
		//serve una concurrentHashMap perché per parallelizzare la lettura è possibile che avvenga una richiesta
		//di accesso multiplo alla mappa 
		m = new ConcurrentHashMap<Integer, ObjectAnimation>();
		System.out.println("Reading character animations");
		
		//definizione della mappa che contiene le coppie direzione-animazione del personaggio 
		directionToAction.put(GameDirection.IDLE_RIGHT, "idle/right");
		directionToAction.put(GameDirection.IDLE_LEFT, "idle/left");
		directionToAction.put(GameDirection.IDLE_UP, "idle/up");
		directionToAction.put(GameDirection.IDLE_DOWN, "idle/down");
		
		directionToAction.put(GameDirection.RIGHT, "right");
		directionToAction.put(GameDirection.LEFT, "left");
		directionToAction.put(GameDirection.UP, "up");
		directionToAction.put(GameDirection.DOWN, "down");
		
		directionToAction.put(GameDirection.ATTACK_RIGHT, "attack/right");
		directionToAction.put(GameDirection.ATTACK_LEFT, "attack/left");
		directionToAction.put(GameDirection.ATTACK_UP, "attack/up");
		directionToAction.put(GameDirection.ATTACK_DOWN, "attack/down");
		
		//parallelizzazione della lettura delle immagini relative alle animazioni
		directionToAction.entrySet().parallelStream().forEach(entry -> {
			m.put(entry.getKey(), new ObjectAnimation(readResources(entry.getValue())));
		});
		currentAnimation = m.get(GameDirection.IDLE_RIGHT);
	}
	
	
	//legge le immagini per ogni movimento/driezione possibile nel package con nome specificato
	public ArrayList<BufferedImage> readResources(String name) {
		ArrayList<File> files= new ArrayList<File>();
		ArrayList<BufferedImage> images= new ArrayList<BufferedImage>();
		
		String myPath= "/application/resources/character/"+ "warrior"+"/"+name+"/";
		try {
			//lettura di tutti i file nella directory che contiene le risorse per quella "mossa" del personaggio scelto
			File directory= new File(getClass().getResource(myPath).toURI());
			for(File f:directory.listFiles()) {
				files.add(f);
			}
			
		} catch (URISyntaxException e) {
			System.out.println("Errore: directory non trovata(possibili problemi nel path)");
			System.exit(1);
		}
		
		//ordino i file in ordine alfabetico (sto assumendo che le immaggini della mossa siano progressive per nome)
		files.sort(new Comparator<File>(){

			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
			
			
		});
		
		for(File f:files) {
			try {
				BufferedImage img = ImageIO.read(getClass().getResourceAsStream(myPath + f.getName()));
				images.add(img);
			} catch (IOException e) {
				System.out.println("Errore: file non trovato nella directory");
				System.exit(1);
			}
			
		}
		
		return images;
	}
	
	public void update(int direction) {
			currentAnimation = m.get(direction);
	}
	
	//update in cui non è cambiata la direzione del personaggio
	public void update() {
		if(currentAnimation==null) {
			return;
		}
		currentAnimation.update();
	}
	
	
}
