package com.twostudentsllc.gladiator.arenas;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.supers.GameMap;

public class Arena extends GameMap {
	
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
	//TODO: Create round class and send the teams.
	public void startRound() {
		if(!canStartRound())
			return;
		
		//TODO: CHange to send tams to created round
		ArenaRound round = null;
		currentRound = round;
		hasRunningRound = true;
	}
	
	@Override
	public void endRound()
	{
		currentRound = null;
		hasRunningRound = false;
	}

}
