package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Countdown;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.MapMatch;
import com.twostudentsllc.gladiator.generic_classes.Team;
import com.twostudentsllc.gladiator.listeners.gladiator.WarmupPlayerMoveListener;
import com.twostudentsllc.gladiator.runnables.MatchCooldown;
import com.twostudentsllc.gladiator.runnables.MatchWarmup;
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
		assignPlayerSpawnpoints();
		doWarmup();
	}
	
	@Override
	public void endMatch()
	{
		map.endMatch();
		for(Team team : teams)
		{
			for(Player p : team.getPlayers())
			{
				map.sendPlayerToLobby(p);
				p.setGameMode(GameMode.SURVIVAL);
			}
		}
		setStatus(STATUS.COMPLETED);
	}
	
	@Override
	public boolean startRound()
	{
		
		//TODO: Unfreeze all players
		if(getStatus() != STATUS.WARMUP)
		{
			System.out.println("Round not started as round did not have the status of: WARMUP. Status is instead: '" + getStatus() +"'");
			return false;
		}
		
		resetPlayerLives();
		
		//Add logic to unfreeze players
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
		//Add logic to end round
		resetPlayerStats();
		currentRound = null;
		doCooldown();
		return true;
	}
	
	@Override
	public boolean hasWinner()
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
	public Team getWinner() {
		// TODO: Return winner
		return null;
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
