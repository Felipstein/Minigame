package net.stardust.minigames;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.stardust.minigames.twb.NotAMapFolderException;

public abstract class Minigame implements Serializable {
	
	protected transient static final String mapsDir = "";
	protected transient static final int DELAY = 20;
	protected transient static final int PERIOD = 20;
	protected transient static final float PROPORTION = 0.75f;
	private static final long serialVersionUID = 4763429008896321664L;
	private transient static Random random = new Random();
	protected final BukkitRunnable preMatchTimer;
	protected BukkitRunnable matchTimer;
	protected Set<String> winners;
	protected int secondsToStart = 60;
	protected boolean matchRunning;
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
	
	public Minigame(JavaPlugin plugin, String name, int maxPlayers, int idAmount) {
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
		winners = new HashSet<>();
		matchTimer = getMatchTimer();
		if(!mapFolder.exists()) {
			try {
				mapFolder.createNewFile();
			} catch(IOException e) {
				this.name = null;
				mapFolder = null;
				this.maxPlayers = 0;
				this.idAmount = 0;
			} finally {
				matchStartable = false;
			}
		}
	}
	
	protected final File drawMap() {
		try {
			File[] maps = mapFolder.listFiles();
			File map = maps[random.nextInt(maps.length)];
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
			throw new NotAMapFolderException("A pasta de mapas não contém nenhum mapa válido.");
		}
	}
	
	protected abstract BukkitRunnable getMatchTimer();
	
	public abstract void communicate();
	
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