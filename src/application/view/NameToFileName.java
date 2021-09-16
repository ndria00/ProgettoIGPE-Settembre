package application.view;


public class NameToFileName {
	//elimina tutti gli spazi in una stringa e li sostituisce con _
	//utilizzato per passare dal nome degli oggetti (item) del gioco 
	//all'immagine ed essi relativa
	public static String toFileName(String s) {
		return s.replaceAll(" ", "_");
	}
}
