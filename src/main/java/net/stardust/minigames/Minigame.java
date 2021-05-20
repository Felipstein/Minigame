package net.stardust.minigames;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import net.stardust.client.Stardust;
import net.stardust.minigames.twb.MinigameIdInexistentException;

public abstract class Minigame {
	
	private String minigameName;
	private int maxPlayers;
	private int idAmount;
	private File mapFolder;
	private Map<Integer, Boolean> onlineIds;
	
	public Minigame(String minigameName, int maxPlayers, int idAmount, File mapFolder) {
		Stardust.validationNull(minigameName, mapFolder);
		if(maxPlayers <= 1 || idAmount < 1 || minigameName.isEmpty() || !mapFolder.exists() || mapFolder.isFile()) {
			throw new IllegalArgumentException();
		}
		this.minigameName = minigameName;
		this.maxPlayers = maxPlayers;
		this.idAmount = idAmount;
		this.mapFolder = mapFolder;
		onlineIds = new LinkedHashMap<>();
		for(int i = 1; i <= idAmount; i++) {
			onlineIds.put(i, true);
		}
	}
	
	public void createMatch(int id) {
		if(!onlineIds.containsKey(id)) {
			throw new MinigameIdInexistentException("Número máximo de ids: " + idAmount + "; id dado: " + id);
		}
		
	}
	
	public String getMinigameName() {
		return minigameName;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getIdAmount() {
		return idAmount;
	}
	
	public File getMapFolder() {
		return mapFolder;
	}
}