package application.view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

//gestisce le immagini relative agli item del gioco
public class AllItemImages {
	private HashMap<String, Image> images;
	
	public AllItemImages() {
		images= new HashMap<String, Image>();
		readResources();
	}
	
	
	public HashMap<String, Image> getImages(){
		return images;
	}
	
	//lettura delle immagini relative a tutti gli items
	public void readResources() {
		
		ArrayList<File> files= new ArrayList<File>();
		String myPath= "/application/resources/item/";
		
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
		
		
		for(File f:files) {
			try {
				String fileName= f.getName().substring(0, f.getName().length()-4);
				//fileName.replace(".png", "");
				
				images.put(fileName, ImageIO.read(getClass().getResourceAsStream(myPath + f.getName())));
			} catch (IOException e) {		
				System.out.println("Errore: file non trovato nella directory");
				System.exit(1);
			}
			
		}
		
	}
	
	
}
