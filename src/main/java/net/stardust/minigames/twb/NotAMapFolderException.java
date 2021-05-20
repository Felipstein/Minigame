package net.stardust.minigames.twb;

public class NotAMapFolderException extends MinigameRTException {
	
	private static final long serialVersionUID = -6153912919294220258L;
	
	public NotAMapFolderException() {
		super();
	}
	
	public NotAMapFolderException(String message) {
		super(message);
	}
	
	public NotAMapFolderException(Throwable cause) {
		super(cause);
	}
	
	public NotAMapFolderException(String message, Throwable cause) {
		super(message, cause);
	}
}
