package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.MapMatch;
import com.twostudentsllc.gladiator.generic_classes.MatchRound;
import com.twostudentsllc.gladiator.generic_classes.PlayerStats;
import com.twostudentsllc.gladiator.generic_classes.Team;
import com.twostudentsllc.gladiator.listeners.gladiator.DeathListener;

public class ArenaRound extends MatchRound{
	
	
	public ArenaRound(Main plugin, MapMatch match, ArrayList<Team> teams, int totalRoundTimelimit, int livesPerPlayer) {
		super(plugin, match, teams, totalRoundTimelimit, livesPerPlayer);
		//Round is started in super class (MatchRound)
	}


	@Override
	public void startRound() {
		registerRoundListeners();
		startTimelimitCountdown();
		
	}

	//TODO: Add call if a winner is found
	@Override
	public void endRound() {
		// TODO Auto-generated method stub
		roundCountdown.stopCountdown();
		unregisterRoundListeners();
		match.endRound();
	}
	
	@Override
	public void registerRoundListeners() {
		registeredListeners.add(new DeathListener(plugin,this));
		
	}
	
	@Override
	public void handleTimeRemaining(int time)
	{
		Bukkit.broadcastMessage("The round has " + time + " seconds remaining.");
		if(time == 0)
		{
			endRound();
		}
	}


	@Override
	public void playerDied(Player p) {
		//Increments the players deaths
		Team playerTeam = getTeam(p);
		playerTeam.incrementPlayerDeaths(p);
		PlayerStats playerStats = playerTeam.getPlayerStats(p);
		int playerDeaths = playerStats.getDeaths();
		//If the player has died enough, eliminate them
		if(playerDeaths >= livesPerPlayer)
		{
			playerEliminated(p);
		}
	}
	
	//FIXME: Convert eventlistener for functionality to work
	@Override
	public void playerEliminated(Player p)
	{
		getTeam(p).getPlayerStats(p).eliminatedPlayer();
		Bukkit.broadcastMessage("Player '" + p.getName() + "' eliminated!");
		match.sendPlayerToSpectate(p);
	}


	@Override
	public void playerEarnedKill(Player p) {
		//Increments the players kills
		Team playerTeam = getTeam(p);
		playerTeam.incrementPlayerKills(p);
	}
	
}
