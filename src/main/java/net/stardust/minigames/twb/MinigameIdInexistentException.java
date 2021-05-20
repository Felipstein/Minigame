package net.stardust.minigames.twb;

public class MinigameIdInexistentException extends IndexOutOfBoundsException {
	
	private static final long serialVersionUID = 3441741474839867319L;
	
	public MinigameIdInexistentException() {
		super();
	}
	
	public MinigameIdInexistentException(String message) {
		super(message);
	}
}