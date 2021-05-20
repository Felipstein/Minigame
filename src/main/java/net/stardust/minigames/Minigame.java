package net.stardust.minigames;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.stardust.minigames.twb.NotAMapFolderException;

public abstract class Minigame implements Serializable {
	
	public static final String SERVER = "localhost";
	public static final int PORT = 10147;
	protected transient static final String mapsDir = "";
	protected transient static final int DELAY = 20;
	protected transient static final int PERIOD = 20;
	protected transient static final float PROPORTION = 0.75f;
	private static final long serialVersionUID = 4763429008896321664L;
	private transient static Random random = new Random();
	protected BukkitRunnable matchTimer;
	protected Map<String, BigDecimal> winners;
	protected int secondsToStart = 60;
	protected boolean matchRunning;
	private final BukkitRunnable preMatchTimer;
	private JavaPlugin plugin;
	private World world;
	private String name;
	private File mapFolder;
	private int maxPlayers;
	private int idAmount;
	private boolean matchStartable;
	
	{
		preMatchTimer = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(world.getPlayers().size() >= (int) (maxPlayers * PROPORTION)) {
					this.cancel();
					if(matchTimer != null) {
						matchTimer.runTaskTimer(plugin, DELAY, PERIOD);
					}
				} else {
					if(secondsToStart == 0) {
						secondsToStart = 60;
					} else {
						--secondsToStart;
					}
				}
			}
			
		};
	}
	
	protected Minigame(JavaPlugin plugin, String name, int maxPlayers, int idAmount) {
		Validate.notNull(name);
		Validate.notNull(plugin);
		if(maxPlayers < 2 || idAmount < 2) {
			throw new IllegalArgumentException("Máximo de jogadores ou quantidade de ids está nulo.");
		}
		this.name = name;
		this.maxPlayers = maxPlayers;
		this.idAmount = idAmount;
		this.plugin = plugin;
		mapFolder = new File(mapsDir + "\\" + name);
		if(mapFolder.isFile()) {
			throw new NotAMapFolderException("O arquivo dado não parece ser uma pasta.");
		}
		matchTimer = getMatchTimer();
		if(!mapFolder.exists()) {
			try {
				mapFolder.createNewFile();
			} catch(IOException e) {
				mapFolder = null;
				this.maxPlayers = 0;
				this.idAmount = 0;
			} finally {
				matchStartable = false;
			}
		}
	}
	
	protected final void tryPreMatch() {
		if(matchStartable) {
			preMatchTimer.runTaskTimer(plugin, DELAY, PERIOD);
		} else {
			die();
		}
	}
	
	protected final void die() {
		try {
			Socket socket = new Socket(SERVER, PORT);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			output.writeObject(this);
			boolean status = input.readBoolean();
			if(status) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			} else {
				System.exit(-1);
			}
		} catch(IOException e) {
			
		}
	}
	
	protected final File drawMap() {
		try {
			File[] maps = mapFolder.listFiles();
			File map = maps[random.nextInt(maps.length)];
			if(map.isFile()) {
				throw new NotAMapFolderException("A pasta de mapas contém um arquivo que não é uma pasta de "
						+ "mapa: " + map.getAbsolutePath());
			}
			File[] internalFiles = map.listFiles();
			boolean uidDatPresent = false;
			for(File internal : internalFiles) {
				if(internal.getName().equals("uid.dat")) {
					uidDatPresent = true;
					break;
				}
			}
			if(!uidDatPresent) {
				map.delete();
				map = drawMap();
			}
			return map;
		} catch(NullPointerException e) {
			return null;
		} catch(IllegalArgumentException e) {
			throw new NotAMapFolderException("A pasta de mapas não contém mapa algum.");
		}
	}
	
	protected abstract BukkitRunnable getMatchTimer();
	
	protected abstract void setWinnersMap();
	
	public abstract void communicate();
	
	public Map<String, BigDecimal> getWinnersMap() {
		return winners == null ? new LinkedHashMap<>() : winners;
	}
	
	public String getName() {
		return name;
	}
	
	protected File getMapFolder() {
		return mapFolder;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getIdAmount() {
		return idAmount;
	}
	
	public boolean isMatchStartable() {
		return matchStartable;
	}
}