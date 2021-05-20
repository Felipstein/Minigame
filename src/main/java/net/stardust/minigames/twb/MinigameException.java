package net.stardust.minigames.twb;

import net.stardust.minigames.Minigame;

public class MinigameException extends Exception {
	
	private static final long serialVersionUID = -3283859338027285673L;
	private Minigame minigame;
	
	public MinigameException() {
		super();
	}
	
	public MinigameException(String message) {
		super(message);
	}
	
	public MinigameException(Throwable cause) {
		super(cause);
	}
	
	public MinigameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MinigameException(Minigame minigame) {
		super();
		this.minigame = minigame;
	}
	
	public MinigameException(String message, Minigame minigame) {
		super(message);
		this.minigame = minigame;
	}
	
	public MinigameException(Throwable cause, Minigame minigame) {
		super(cause);
		this.minigame = minigame;
	}
	
	public MinigameException(String message, Throwable cause, Minigame minigame) {
		super(message, cause);
		this.minigame = minigame;
	}
	
	public Minigame getMinigame() {
		return minigame;
	}
}
