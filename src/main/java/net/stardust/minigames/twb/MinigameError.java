package net.stardust.minigames.twb;

public class MinigameError extends Error {
	
	private static final long serialVersionUID = -2992948944391572870L;
	
	public MinigameError() {
		super();
	}
	
	public MinigameError(String message) {
		super(message);
	}
	
	public MinigameError(Throwable cause) {
		super(cause);
	}
	
	public MinigameError(String message, Throwable cause) {
		super(message, cause);
	}
}
