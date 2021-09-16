package application.view;

import java.awt.Image;
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

//gestisce tutte le animazioni relative ai mostri
public class MonsterAllAnimations {
	Map<Integer, ArrayList<Image>> m;
	HashMap<Integer, String> directionToAction = new HashMap<>();
	
	public MonsterAllAnimations() {
		//è necessaria una concurrentHashMap perché parallelizzando la lettura si potrebbe avere una richiesta
		//di accesso multiplo alla mappa
		m = new ConcurrentHashMap<Integer, ArrayList<Image>>();
		System.out.println("Reading monsters animations");
		
		//definizione della mappa che contiene le coppie direzione-animazione dei mostri 
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
			m.put(entry.getKey(), readResources(entry.getValue()));
		});
	}
	
	
	//legge le immagini per ogni movimento/driezione possibile nel package con nome specificato
	public ArrayList<Image> readResources(String name) {
		ArrayList<File> files= new ArrayList<File>();
		ArrayList<Image> images= new ArrayList<Image>();
		
		String myPath= "/application/resources/monster/"+name+"/";
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
				images.add(ImageIO.read(getClass().getResourceAsStream(myPath + f.getName())));
			} catch (IOException e) {
				System.out.println("Errore: file non trovato nella directory");
				System.exit(1);
			}
			
		}
		
		return images;
	}	
}
