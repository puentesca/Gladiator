package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.global.Utils;
import com.twostudentsllc.gladiator.runnables.RoundTimelimitUpdater;

/**
 * An interface for each round in an arena.
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */

public abstract class MapMatch {


	//TODO: Implement Warmup/Cooldown functionality
	//TODO: Implement multiround functionality

	/**
	 * Contains the status of the current round
	 * @author Casey Puentes
	 *
	 */
	public static enum STATUS {
		WAITING, WARMUP, IN_PROGRESS, COOLDOWN, COMPLETED, ERROR
	};
	
	protected Main plugin;
	
	/**
	 * The map that this current round is taking place on
	 */
	protected GameMap map;
	
	/**
	 * Holds the starting time limit of the round, in seconds
	 */
	protected int timeLimit;
	
	/**
	 * Holds the countdown for a warmup. Null when there is no warmup countdown currently happening
	 */
	protected Countdown warmupTimelimitCountdown;
	/**
	 * Holds the countdown for a cooldown. Null when there is no cooldown countdown currently happening
	 */
	protected Countdown cooldownTimelimitCountdown;

	/**
	 * How long should the warm up last
	 */
	protected int warmupTimeLimit;

	/**
	 * How long should the cooldown period last
	 */
	protected int cooldownTimeLimit;


	/**
	 * Number of rounds this Match has
	 */
	protected int totalRounds;

	/**
	 * What round the Match is currently on
	 */
	protected int currentRoundCount;
	
	/**
	 * The current round that is happening on the match
	 */
	protected MatchRound currentRound;
	
	/**
	 * The teams currently participating in the round
	 */
	protected ArrayList<Team> teams;
	
	//TODO: Check for method that ends the match and timelimit countdown based on an event like team elim
	
	public MapMatch(Main plugin, GameMap map, int timeLimit, int warmupTimeLimit, int cooldownTimeLimit, int totalRounds, ArrayList<Team> teams)
	{
		setStatus(STATUS.WAITING);
		this.plugin = plugin;
		this.map = map;
		this.timeLimit = timeLimit;
		this.teams = teams;
		this.warmupTimeLimit = warmupTimeLimit;
		this.cooldownTimeLimit = cooldownTimeLimit;
		this.totalRounds = totalRounds;
		this.currentRoundCount = 0;
		assignPlayerSpawnpoints();
		doWarmup();
	}

	/**
	 * Tells the GameMap to teleport the player to the spectating area
	 * @param p
	 */
	public void sendPlayerToSpectate(Player p)
	{
		map.sendPlayerToSpectate(p);
	}
	
	//Method is called by Java when an object is going to be deleted
	@Override
	protected void finalize() {
		//Unloads loaded chunks in the world when the object is removed
		map.unloadChunks();
	}
	
	/**
	 * Gets if there is a winner
	 * @return True if the round has a winner
	 */
	public abstract boolean hasWinner();
	
	/**
	 * Gets the winner of the round
	 * @return
	 */
	public abstract Team getWinner();
	/**
	 * Gets the current status of the round
	 * @return The status of the round
	 */
	public abstract STATUS getStatus();
	
	public abstract void setStatus(STATUS status);
	
	/**
	 * In charge of handling the way the warmup time left is handled. Should instantiate a MatchRound when its zero.
	 * Should also nullify the instance of warmupTimelimitCounter when the countdown is finished.
	 * @param time
	 */
	public abstract void handleWarmupTimeRemaining(int time);
	
	/**
	 * In charge of handling the way the cooldown time left is handled.
	 * Should also nullify the instance of cooldownTimelimitCounter when the countdown is finished and stop warmup counter.
	 * @param time
	 */
	public abstract void handleCooldownTimeRemaining(int time);
	
	/**
	 * Starts a round with the players and teams assigned, also needs to increment round counter and stop cooldown counter
	 * @return True if the round was successfully started
	 */
	public abstract boolean startRound();
	
	/**
	 * Ends a round. Responsible for starting cooldown.
	 * @return True if the round was successfully ended
	 */
	public abstract boolean endRound();

	/**
	 * Handles the warmup period, also in charge of setting state to WARMUP
	 */
	public abstract void doWarmup();

	/**
	 * Handles the cooldown period, also in charge of setting state to COOLDOWN
	 */
	public abstract void doCooldown();
	
	/**
	 * Handles what happens after the cooldown is completed. If there is another round to be played, start the warmup for it
	 */
	public void cooldownCompleted()
	{
		cooldownTimelimitCountdown.stopCountdown();
		cooldownTimelimitCountdown = null;
		if(hasAnotherRound())
			doWarmup();
		else
		{
			map.endMatch();
			setStatus(STATUS.COMPLETED);
		}
	}

	/**
	 * Whether or not there are any rounds remaining
	 * @return true/false
	 */
	public boolean hasAnotherRound() {
		return currentRoundCount < totalRounds;
	}
	
	/**
	 * Teleports all players to the spawn
	 */
	public void sendAllPlayersToSpawn()
	{
		for(Team t : teams)
		{
			HashMap<Player, Location> playerSpawns = t.getPlayerSpawns();
			for(Player p : playerSpawns.keySet())
			{
				p.teleport(playerSpawns.get(p));
			}
		}
	}
	
	/**
	 * Sends a player to their assigned spawn
	 * @param p The player to send to spawn
	 */
	public void sendPlayerToSpawn(Player p)
	{
		for(Team t : teams)
		{
			if(t.containsPlayer(p))
			{
				p.teleport(t.getPlayerSpawns().get(p));
				return;
			}
		}
		throw new IllegalArgumentException("Player '" + p.getName() + "' is not part of a team in this round on map: '" + map.getMapDisplayName() + "'!");
	}
	
	public Countdown getWarmupCountdown()
	{
		return warmupTimelimitCountdown;
	}
	
	public Countdown getCooldownCountdown()
	{
		return cooldownTimelimitCountdown;
	}
	
	/**
	 * Resets all players stats to default state after a round has been completed
	 */
	public void resetPlayerStats()
	{
		for(Team t: teams)
		{
			t.resetAllPlayerStats();
		}
	}
	
	/**
	 * Maps every player a spawnpoint
	 */
	public void assignPlayerSpawnpoints()
	{
		HashMap<String, Location> locations = map.getLocations();
		//Loops through every team
		for(Team t : teams)
		{
			HashMap<Player, Location> playerSpawns = new HashMap<Player, Location>();
			int teamNum = t.getTeamID();
			ArrayList<Player> teamMembers = t.getPlayers();
			//Every player in every team
			for(int playerNum = 0; playerNum < t.getTotalPlayers(); playerNum++)
			{
				String spawnString = Utils.getPlayerSpawnLocationString(teamNum, playerNum);
				//If the spawn doesn't exist, there arent enough spawns set for players
				if(!locations.containsKey(spawnString))
					throw new IllegalArgumentException("This map has not been assigned enough player spawns! Confirmed missing spawn: '" + spawnString + "' and possibly others.");
				
				Location spawn = locations.get(spawnString);
				Player p = teamMembers.get(playerNum);
				
				playerSpawns.put(p, spawn);
			}
			t.setPlayerSpawns(playerSpawns);
		}
	}
}
