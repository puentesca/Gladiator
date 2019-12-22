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

public abstract class Match {


	//TODO: Implement Warmup/Cooldown functionality

	/**
	 * Contains the status of the current round
	 * @author Casey Puentes
	 *
	 */
	public static enum STATUS {
		WAITING, LOADING, IN_PROGRESS, COMPLETED, ERROR
	};
	
	protected Main plugin;
	
	/**
	 * The map that this current round is taking place on
	 */
	protected GameMap map;
	
	/**
	 * Holds the starting time limit of the round, in seconds
	 */
	private int timeLimit;
	
	/**
	 * Holds the time limit countdown for when a round is running.
	 */
	private Countdown timelimitCountdown;
	
	/**
	 * The teams currently participating in the round
	 */
	protected ArrayList<Team> teams;
	
	
	public Match(Main plugin, GameMap map, int timeLimit, ArrayList<Team> teams)
	{
		this.plugin = plugin;
		this.map = map;
		this.timeLimit = timeLimit;
		this.teams = teams;
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
	
	/**
	 * Receieves the round time limits time (in seconds) every second and handles a reaction
	 * @param time The amount of time left in the round
	 */
	public abstract void handleTimelimitRemaining(int time);
	
	/**
	 * Starts a round with the players and teams assigned
	 * @return True if the round was successfully started
	 */
	public abstract boolean startRound();
	
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
	
	/**
	 * Starts the time limit coundown for the round
	 */
	public void startTimelimitCountdown()
	{
		Countdown c;
		
		Runnable task = new RoundTimelimitUpdater(plugin, this);
		c = new Countdown(plugin, timeLimit, task, true);
		timelimitCountdown = c;
	}
	/**
	 * Stops the rounds timelimit countdown
	 */
	public void stopTimelimitCountdown()
	{
		timelimitCountdown.stopCountdown();
	}
	
	/**
	 * Ends a round
	 * @return True if the round was successfully ended
	 */
	public abstract boolean endRound();
	
	public Countdown getTimelimitCountdown()
	{
		return timelimitCountdown;
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
