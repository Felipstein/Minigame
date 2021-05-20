package net.stardust.minigames.twb;

public class MinigameRTException extends RuntimeException {
	
	private static final long serialVersionUID = 1692996316989616389L;
	
	public MinigameRTException() {
		super();
	}
	
	public MinigameRTException(String message) {
		super(message);
	}
	
	public MinigameRTException(Throwable cause) {
		super(cause);
	}
	
	public MinigameRTException(String message, Throwable cause) {
		super(message, cause);
	}
}
