package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.Team;

public class Arena extends GameMap{
	
	//TODO: Figure out how to get spawnpoints for teams as well as lobbies and such
	
	
	public Arena(Main plugin, String minigameName, String mapName, String mapDisplayName, int minTeams, int maxTeams,
			int minPlayers, int maxPlayers, int warmupTimeLimit, int cooldownTimeLimit, int totalRounds) {
		super(plugin, minigameName, mapName, mapDisplayName, minTeams, maxTeams, minPlayers, maxPlayers, warmupTimeLimit, cooldownTimeLimit, totalRounds);
	}

	/**
	 * Creates a serialzed string version of this Arena
	 * @return Serialized string of this Arena
	 */
	@Override
	public String serialize() {
		String string = getMinigameName() + ":" + getMapName() + ":" + getMapDisplayName() + ":" + getMinTeams() + ":" + getMaxTeams() + ":" + getMinPlayers() + ":" + getMaxPlayers() + ":" + getWarmupTimeLimit() + ":" + getCooldownTimeLimit() + ":" + getTotalRounds();
		return string;
	}

	//TODO: Add startRound in MapMatch that instantiates a MatchRound object
	
	@Override
	public void startMatch(ArrayList<Team> teams) {
		if(!canStartMatch(teams))
			return;
		ArenaMatch match = new ArenaMatch(plugin, this, 100, warmupTimeLimit, cooldownTimeLimit, totalRounds, teams);
		currentMatch = match;
		hasRunningMatch = true;
	}
	
	@Override
	public void endMatch()
	{
		currentMatch = null;
		hasRunningMatch = false;
		unloadChunks();
	}

}
