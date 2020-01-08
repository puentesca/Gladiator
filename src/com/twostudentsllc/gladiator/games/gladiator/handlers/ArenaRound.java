package com.twostudentsllc.gladiator.games.gladiator.handlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.runnables.Countdown;
import com.twostudentsllc.gladiator.generic_game.handlers.MapMatch;
import com.twostudentsllc.gladiator.generic_game.handlers.MatchRound;
import com.twostudentsllc.gladiator.generic_game.PlayerStats;
import com.twostudentsllc.gladiator.generic_game.Team;
import com.twostudentsllc.gladiator.games.gladiator.listeners.DeathListener;
import com.twostudentsllc.gladiator.datastorage.mysql.MysqlCommunicator;
import com.twostudentsllc.gladiator.runnables.game.round.RoundRespawnTimer;

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
		//Stops all respawn countdowns
		for(Player p : respawnCountdowns.keySet())
		{
			respawnCountdowns.get(p).stopCountdown();
		}
		savePlayerStats();
		setWinner();
		roundCountdown.stopCountdown();
		unregisterRoundListeners();
		match.endRound();
	}
	
	@Override
	public void savePlayerStats()
	{
		for(Team t : teams)
		{
			for(Player p : t.getPlayers())
			{
				PlayerStats stats = t.getPlayerStats(p);
				int kills = stats.getKills();
				int deaths = stats.getDeaths();
				MysqlCommunicator comm = plugin.getMysqlManager().getCommunicator();
				comm.updateMinigameStat(p.getUniqueId(), match.getMap().getGame().getGameName(), "kills", kills);
				comm.updateMinigameStat(p.getUniqueId(), match.getMap().getGame().getGameName(), "deaths", deaths);
			}
		}
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
	public void handleRespawnTimeRemaining(int time, Player p)
	{
		if(time <= 0)
		{
			respawnPlayer(p);
			respawnCountdowns.remove(p);
		}
	}

	@Override
	public void handlePVPDeathEvent(Event e, Player killed, Player killer)
	{
		
		playerDied(killed);
		Bukkit.broadcastMessage(""
				+ "Player earned kill! Player: " + killer.getName());
		//FIXME: Registers the 'arrow' as a killer when shot, not the player
		playerEarnedKill(killer);
		
		//Cancels the event so they player doesnt actually die
		((EntityDamageEvent)e).setCancelled(true); 
	}
	
	@Override
	public void handleNaturalPlayerDeathEvent(Event e, Player killed, Entity killer)
	{
		playerDied(killed);
		Bukkit.broadcastMessage("Player was killed! Killer: " + killer.getName());
		
		//Cancels the event so they player doesnt actually die
		((EntityDamageEvent)e).setCancelled(true); 
	}
	
	@Override
	public void respawnPlayer(Player p)
	{
		match.sendPlayerToSpawn(p, true);
		p.setGameMode(GameMode.ADVENTURE);
	}
	

	@Override
	public void playerDied(Player p) {
		p.setHealth(20);
		p.setGameMode(GameMode.SPECTATOR);
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
		else
		{
			RoundRespawnTimer respawnTimer = new RoundRespawnTimer(plugin, this, p);
			
			Countdown c = new Countdown(plugin, respawnTime, respawnTimer, true);
			respawnCountdowns.put(p, c);
		}
	}
	
	//FIXME: Convert eventlistener for functionality to work
	@Override
	public void playerEliminated(Player p)
	{
		getTeam(p).getPlayerStats(p).eliminatedPlayer();
		Bukkit.broadcastMessage("Player '" + p.getName() + "' eliminated!");
		match.getMap().sendPlayerToSpectate(p);
		checkForWinner();
	}
	
	@Override
	public void checkForWinner()
	{
		//If all the teams have been eliminated
		if(hasSingleWinner())
		{
			endRound();
		}	
	}


	@Override
	public void playerEarnedKill(Player p) {
		//Increments the players kills
		Team playerTeam = getTeam(p);
		playerTeam.incrementPlayerKills(p);
	}


	@Override
	public void setWinner() {
		for(Team t : teams)
		{
			//If the team isnt eliminated, they get an extra point
			if(!t.isEliminated())
			{
				t.incrementWins();
			}
		}
	}
	
	@Override
	public boolean hasSingleWinner()
	{
		int alive = 0;
		for(Team t : teams)
		{
			if(!t.isEliminated())
				alive++;
		}
		return alive <= 1;
	}
	
}
