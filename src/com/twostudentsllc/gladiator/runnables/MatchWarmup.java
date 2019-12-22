package com.twostudentsllc.gladiator.runnables;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.MapMatch;

public class MatchWarmup implements Runnable {

	private Main plugin;
	private MapMatch match;
	
	public MatchWarmup(Main plugin, MapMatch match)
	{
		this.plugin = plugin;
		this.match = match;
	}
	
	@Override
	public void run() {
		sendTimeLeft();

	}

	public void sendTimeLeft()
	{
		match.handleWarmupTimeRemaining(match.getWarmupCountdown().getSecondsLeft());
	}
}
