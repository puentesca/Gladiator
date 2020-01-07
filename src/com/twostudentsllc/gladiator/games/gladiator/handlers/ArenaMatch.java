package com.twostudentsllc.gladiator.games.gladiator.handlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.datastorage.mysql.MysqlCommunicator;
import com.twostudentsllc.gladiator.runnables.Countdown;
import com.twostudentsllc.gladiator.generic_game.handlers.GameMap;
import com.twostudentsllc.gladiator.generic_game.handlers.MapMatch;
import com.twostudentsllc.gladiator.generic_game.Team;
import com.twostudentsllc.gladiator.games.gladiator.listeners.WarmupPlayerMoveListener;
import com.twostudentsllc.gladiator.runnables.game.match.MatchCooldown;
import com.twostudentsllc.gladiator.runnables.game.match.MatchWarmup;
/**
 * An round for an arena
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class ArenaMatch extends MapMatch {

	private STATUS state;
	
	public ArenaMatch(Main plugin, GameMap map, int timeLimit, int warmupTimeLimit, int cooldownTimeLimit, int totalRounds, ArrayList<Team> teams)
	{
		super(plugin, map, timeLimit, warmupTimeLimit, cooldownTimeLimit, totalRounds, teams);
	}
	
	
	@Override
	public void startMatch()
	{
		//Updates the players matches played total
		for(Team t : teams)
		{
			for(Player p: t.getPlayers())
			{
				MysqlCommunicator sql = plugin.getMysqlManager().getCommunicator();
				sql.updateMinigameStat(p.getUniqueId(), map.getMinigameName(), "plays", 1);
			}
		}
		
		assignPlayerSpawnpoints();
		giveAllPlayersKits();
		doWarmup();
	}
	
	@Override
	public void endMatch()
	{
		//TODO: Update win and loss stats for all players in the MysqlCommunicator
		map.endMatch();
		for(Team team : teams)
		{
			for(Player p : team.getPlayers())
			{
				map.getGame().sendPlayerToHubWorld(p);
			}
		}
		setStatus(STATUS.COMPLETED);
	}
	
	@Override
	public boolean startRound()
	{
		
		if(getStatus() != STATUS.WARMUP)
		{
			System.out.println("Round not started as round did not have the status of: WARMUP. Status is instead: '" + getStatus() +"'");
			return false;
		}
		
		resetPlayerLives();
		currentRoundCount++;
		//TODO: Make timeLimit and lives be changeable dynamically
		currentRound = new ArenaRound(plugin, this, teams, timeLimit, playerLives);
		setStatus(STATUS.IN_PROGRESS);
		return true;
	}
	
	@Override
	public boolean endRound()
	{
		if(getStatus() != STATUS.IN_PROGRESS)
		{
			System.out.println("Round not started as round did not have the status of: IN_PROGRESS");
			return false;
		}
		
		resetPlayerStats();
		currentRound = null;
		doCooldown();
		return true;
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
		return alive == 1;
	}
	
	@Override
	public ArrayList<Player> getWinningPlayers() {
		ArrayList<Player> winners = new ArrayList<Player>();;
		int maxWins = 0;
		for(Team t : teams)
		{
			//If there is a new winner
			if(t.getWins() > maxWins)
			{
				winners = (ArrayList<Player>)t.getPlayers().clone();
			}
			//If there is a tie, add the other team to the winners
			else if(t.getWins() == maxWins)
			{
				for(Player p : t.getPlayers())
				{
					winners.add(p);
				}
			}
		}
		return winners;
	}

	@Override
	public STATUS getStatus()
	{
		return state;
	}
	
	public void setStatus(STATUS status)
	{
		state = status;
	}

	@Override
	public void handleWarmupTimeRemaining(int time) {
		
		Bukkit.broadcastMessage("Warmup Time left: " + time);
		//If the round is over
		if(time == 0)
		{
			unregisterWarmupListeners();
			startRound();
			warmupTimelimitCountdown = null;
		}
	}
	
	@Override
	public void handleCooldownTimeRemaining(int time) {
		
		Bukkit.broadcastMessage("Cooldown Time left: " + time);
		//If the round is over
		if(time == 0)
		{
			cooldownCompleted();
		}
	}
	
	@Override
	public void doWarmup() {
		setStatus(STATUS.WARMUP);
		sendAllPlayersToSpawn();
		
		Runnable task = new MatchWarmup(plugin, this);
		//Starts the warmup countdown
		warmupTimelimitCountdown = new Countdown(plugin, warmupTimeLimit, task, true);
		registerWarmupListeners();
		//TODO: Add player freezing until the startRound() is called
	}
	
	public void registerWarmupListeners()
	{
		warmupListeners.add(new WarmupPlayerMoveListener(plugin, this));
	}

	@Override
	public void doCooldown() {
		if(getStatus() != STATUS.IN_PROGRESS)
		{
			throw new IllegalArgumentException("Tried to start cooldown period when the state of the game was not IN_PROGRESS!");
		}
		
		Runnable task = new MatchCooldown(plugin, this);
		//Starts the warmup countdown
		cooldownTimelimitCountdown = new Countdown(plugin, cooldownTimeLimit, task, true);
		
		setStatus(STATUS.COOLDOWN);
	}
	

}
