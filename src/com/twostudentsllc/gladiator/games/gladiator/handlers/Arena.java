package com.twostudentsllc.gladiator.games.gladiator.handlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.datastorage.mysql.MysqlCommunicator;
import com.twostudentsllc.gladiator.generic_game.Team;
import com.twostudentsllc.gladiator.generic_game.handlers.GameMap;

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
		ArrayList<Player> winners = currentMatch.getWinningPlayers();
		
		for(Player p : winners)
		{
			MysqlCommunicator sql = plugin.getMysqlManager().getCommunicator();
			sql.updateMinigameStat(p.getUniqueId(), minigameName, "wins", 1);
		}
		//TODO: Send all players into another lobby
		String winnersMessage = "The winners are: ";
		for(Player p : winners)
		{
			winnersMessage += p.getDisplayName() + " ";
		}
		Bukkit.broadcastMessage(winnersMessage);
		currentMatch = null;
		hasRunningMatch = false;
		unloadChunks();	//TODO: Verify this works. Might want to add a 5 second timer until this calls to verify everyone has been teleported out
	}

}
