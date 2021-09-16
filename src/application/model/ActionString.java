package application.model;

//notifica di azione che viene mostrata al giocatore quando effettua una particolare azione
//per esempio al cambio di livello o all'utilizzo di una pozione
public class ActionString {
	
	private String message;
	private ObjectTimer timer;
	
	public ActionString (String message) {
		reset(message);
	}
	public ActionString () {
		message = null;
		timer = new ObjectTimer(AliveObjectSettings.ACTION_STRING_DURATION);
	}
	public String getMessage() {
		return message;
	}
	
	public ObjectTimer getTimer() {
		return timer;
	}
	
	//cambia il testo visualizzato dalla ActionString
	public void reset(String message) {
		this.message=message;
		timer =  new ObjectTimer(AliveObjectSettings.ACTION_STRING_DURATION);
	}
	
	//fa  avanzare il timer che determina la "vita" della ActionString
	//una volta che il tempo è passato, il messaggio viene impostato a null 
	public void tick() {
			timer.tick();
		if(timer.canDoAction()) {
			message=null;
		}
	}
}