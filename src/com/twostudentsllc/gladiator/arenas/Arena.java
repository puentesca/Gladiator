package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.Team;

public class Arena extends GameMap{
	
	public Arena(Main plugin, String minigameName, String mapName, String mapDisplayName, int minTeams, int maxTeams,
			int teamSize, int warmupTimeLimit, int cooldownTimeLimit, int totalRounds) {
		super(plugin, minigameName, mapName, mapDisplayName, minTeams, maxTeams, teamSize, warmupTimeLimit, cooldownTimeLimit, totalRounds);
	}

	/**
	 * Creates a serialzed string version of this Arena
	 * @return Serialized string of this Arena
	 */
	@Override
	public String serialize() {
		String string = getMinigameName() + ":" + getMapName() + ":" + getMapDisplayName() + ":" + getMinTeams() + ":" + getMaxTeams() + ":" + getTeamSize() + ":" + getWarmupTimeLimit() + ":" + getCooldownTimeLimit() + ":" + getTotalRounds();
		return string;
	}
	
	@Override
	public void startMatch(ArrayList<Team> teams) {
		if(!canStartMatch())
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
		unloadChunks();	//TODO: Verify this works. Might want to add a 5 second timer until this calls to verify everyone has been teleported out
	}

}
