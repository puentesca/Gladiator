package com.twostudentsllc.gladiator.arenas;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.MapMatch;
import com.twostudentsllc.gladiator.generic_classes.MatchRound;
import com.twostudentsllc.gladiator.generic_classes.Team;

public class ArenaRound extends MatchRound{
	
	public ArenaRound(Main plugin, MapMatch match, ArrayList<Team> teams, int totalRoundTimelimit) {
		super(plugin, match, teams, totalRoundTimelimit);
		//Round is started in super class (MatchRound)
	}


	@Override
	public void startRound() {
		registerListeners();
		startTimelimitCountdown();
		
	}

	@Override
	public void endRound() {
		// TODO Auto-generated method stub
		roundCountdown.stopCountdown();
		unregisterListeners();
		match.endRound();
	}
	
	@Override
	public void registerListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterListeners() {
		// TODO Auto-generated method stub
		
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
}
