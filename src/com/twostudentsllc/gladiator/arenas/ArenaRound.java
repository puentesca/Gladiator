package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.MapRound;
import com.twostudentsllc.gladiator.generic_classes.Team;
/**
 * An round for an arena
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class ArenaRound extends MapRound {
	
	private STATUS state = STATUS.WAITING;
	
	public ArenaRound(Main plugin, GameMap map, int timeLimit, ArrayList<Team> teams)
	{
		super(plugin, map, timeLimit, teams);
	}
	
	@Override
	public boolean startRound()
	{
		if(getStatus() != STATUS.WAITING)
		{
			System.out.println("Round not started as round did not have the status of: WAITING");
			return false;
		}
		sendAllPlayersToSpawn();
		//Starts the rounds time limit
		startTimelimitCountdown();
		//Add logic to start round
		state = STATUS.IN_PROGRESS;
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
		stopTimelimitCountdown();
		//Add logic to end round
		state = STATUS.COMPLETED;
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

	@Override
	public void handleTimelimitRemaining(int time) {
		
		Bukkit.broadcastMessage("Time left: " + time);
		//If the round is over
		if(time == 0)
		{
			endRound();
		}
	}
	

}
