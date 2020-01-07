package com.twostudentsllc.gladiator.runnables.game.match;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_game.handlers.MapMatch;

public class MatchCooldown implements Runnable {

	private Main plugin;
	private MapMatch match;
	
	public MatchCooldown(Main plugin, MapMatch match)
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
		match.handleCooldownTimeRemaining(match.getCooldownCountdown().getSecondsLeft());
	}
}
