package com.twostudentsllc.gladiator.games.gladiator;

import java.util.HashMap;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.games.gladiator.handlers.Arena;
import com.twostudentsllc.gladiator.generic_game.handlers.Game;
import com.twostudentsllc.gladiator.generic_game.handlers.GameMap;
import com.twostudentsllc.gladiator.datastorage.DatabaseUtils;

public class Gladiator extends Game{
	
	public Gladiator(Main plugin, String game, String displayName) {
		super(plugin, game, displayName);
	}

	//TODO: Add round creation from GameManager
	
	@Override
	public void createGameMap(String[] args) {

		//TODO: I think there might be a better way to do this
		String minigameName = args[1];
		String mapName = args[2];
		if(hasGameMap(mapName))
			throw new IllegalArgumentException("The minigame '" + minigameName + "' already has a map named '" + mapName + "'!");
		String mapDisplayName = args[3];
		int minTeams = Integer.parseInt(args[4]);
		int maxTeams = Integer.parseInt(args[5]);
		int teamSize = Integer.parseInt(args[6]);
		int warmupTimeLimit = Integer.parseInt(args[7]);
		int cooldownTimeLimit = Integer.parseInt(args[8]);
		int totalRounds = Integer.parseInt(args[9]);
		
		Arena map = new Arena(plugin, minigameName, mapName, mapDisplayName, minTeams, maxTeams, teamSize, warmupTimeLimit, cooldownTimeLimit, totalRounds);
		maps.put(mapName, map);
	}
	
	@Override
	public boolean saveAllData()
	{
		HashMap<String, String> serializedMaps = new HashMap<String, String>();
		for(String s : maps.keySet())
		{
			GameMap map = maps.get(s);
			map.saveLocations();
			String serializedMap = map.serialize();
			serializedMaps.put(map.getMapName(), serializedMap);
		}
		
		DatabaseUtils.saveMaps(serializedMaps, minigameName, "maps");
		kits.forceSaveToFile();
		return true;
	}
	
	@Override
	public void loadAllMaps()
	{
		HashMap<String, String> serializedMaps = DatabaseUtils.loadMap(minigameName, "maps");
		for(String s : serializedMaps.keySet())
		{
			String mapName = s;
			String[] parts = serializedMaps.get(s).split(":");
			String minigameName = parts[0];
			//mapName is parts 1 but is the key
			String mapDisplayName = parts[2];
			int minTeams = Integer.parseInt(parts[3]);
			int maxTeams = Integer.parseInt(parts[4]);
			int teamSize = Integer.parseInt(parts[5]);
			int warmupTimeLimit = Integer.parseInt(parts[6]);
			int cooldownTimeLimit = Integer.parseInt(parts[7]);
			int totalRounds = Integer.parseInt(parts[8]);
			
			Arena map = new Arena(plugin, minigameName, mapName, mapDisplayName, minTeams, maxTeams, teamSize, warmupTimeLimit, cooldownTimeLimit, totalRounds);
			
			maps.put(mapName, map);
			
		}
	}

}
