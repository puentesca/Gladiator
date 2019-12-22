package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.Team;

public class Arena extends GameMap{
	
	//TODO: Figure out how to get spawnpoints for teams as well as lobbies and such
	
	
	public Arena(Main plugin, String minigameName, String mapName, String mapDisplayName, int minTeams, int maxTeams,
			int minPlayers, int maxPlayers) {
		super(plugin, minigameName, mapName, mapDisplayName, minTeams, maxTeams, minPlayers, maxPlayers);
	}

	/**
	 * Creates a serialzed string version of this Arena
	 * @return Serialized string of this Arena
	 */
	@Override
	public String serialize() {
		String string = getMinigameName() + ":" + getMapName() + ":" + getMapDisplayName() + ":" + getMinTeams() + ":" + getMaxTeams() + ":" + getMinPlayers() + ":" + getMaxPlayers();
		return string;
	}

	@Override
	public void startRound(ArrayList<Team> teams) {
		if(!canStartRound(teams))
			return;
		ArenaMatch round = new ArenaMatch(plugin, this, 100, teams);
		currentRound = round;
		hasRunningRound = true;
	}
	
	@Override
	public void endRound()
	{
		currentRound = null;
		hasRunningRound = false;
		unloadChunks();
	}

}
