package application.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.imageio.ImageIO;

//gestisce l'animazione relativa al NPC
public class NPCAnimations {
	private ObjectAnimation animations;
	
	public NPCAnimations() {
		ArrayList<BufferedImage> images=readAnimations("/application/resources/npc/animations");
		System.out.println("Reading npc animations");
		animations= new ObjectAnimation(images);
	}
	
	
	public ArrayList<BufferedImage> readAnimations(String name) {
		ArrayList<BufferedImage> images=new ArrayList<BufferedImage>();
		try {
			File directory= new File(getClass().getResource(name).toURI());
			ArrayList<File> files= new ArrayList<File>();
			for(File f:directory.listFiles()) {
				files.add(f);
			}
			files.sort(new Comparator<File>() {

				@Override
				public int compare(File f1, File f2) {
					
					return f1.getName().compareTo(f2.getName());
				}	
			});
			
			for(File f:files) {
				images.add(ImageIO.read(f));
			}
			
		} catch (URISyntaxException e) {
			System.out.println("URI Syntax not correct");
		} catch (IOException e) {
			System.out.println("Error while reading file");
		}
		
		return images;
	}
	
	public ObjectAnimation getAnimations() {
		return animations;
	}
	
	public void update() {
		animations.update();
	}
}
