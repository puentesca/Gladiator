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
public class ArenaRound implements GladiatorRound {
	
	private STATUS state = STATUS.WAITING;
	
	HashMap<String, Location> spawnpoints;
	
	private Main plugin;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public STATUS getStatus()
	{
		return state;
	}
	

}
