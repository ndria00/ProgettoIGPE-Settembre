package application.view;

import application.model.GameDirection;

//rappresenta lo stato dell'animazione di un mostro, è necessario
//avere questo tipo di dato per non duplicare le immagini
public class MonsterAnimationState {
	private int animationType;
	private int animationState;
	
	public MonsterAnimationState() {
		animationType = GameDirection.DIRECTION_NULL;
		animationState = 0;
	}
	
	public int getAnimationType() {
		return animationType;
	}
	
	public int getAnimationState() {
		return animationState;
	}
	
	public void update(int direction, int maxAnimationState) {
		//la tipologia di animazione non cambia e quindi bisogna solamente passare all'immagine successiva
		if(direction == animationType) {
			if(animationState == maxAnimationState)
				animationState = 0;
			else
				animationState++;
		
		}
		else {
			// cambia il tipo di animazione e quindi bisogna anche portare lo stato dell'animazione a zero
			animationType = direction;
			animationState = 0;
		}

	}
	
}