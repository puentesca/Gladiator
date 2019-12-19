package com.twostudentsllc.gladiator.arenas;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
/**
 * An round for an arena
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class ArenaRound extends MapRound {
	
	private STATUS state = STATUS.WAITING;
	
	HashMap<String, Location> spawnpoints;
	
	private Main plugin;
	
	//TODO: Add player list and team list. Also make it so the constructor requires it.
	//TODO: Make Team class have and ID and require each team to have one spawnpoint
	
	public ArenaRound(Main plugin, HashMap<String, Location> spawnpoints)
	{
		this.plugin = plugin;
		this.spawnpoints = spawnpoints;
	}
	
	@Override
	public boolean startRound()
	{
		if(getStatus() != STATUS.WAITING)
		{
			System.out.println("Round not started as round did not have the status of: WAITING");
			return false;
		}
		//Add logic to start round
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
		return true;
	}
	
	@Override
	public Player getWinner() {
		// TODO: Return winner
		return null;
	}

	@Override
	public STATUS getStatus()
	{
		return state;
	}
	

}
