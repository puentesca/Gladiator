package com.twostudentsllc.gladiator.runnables;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.arenas.MapRound;

public class RoundTimelimitUpdater implements Runnable {

	/**
	 * The round that the time left should be sent to
	 */
	private MapRound round;
	
	private Main plugin;
	
	public RoundTimelimitUpdater(Main plugin, MapRound round)
	{
		this.plugin = plugin;
		this.round = round;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendTimeLeft();
	}
	
	/**
	 * Sends the time left to the current round
	 */
	public void sendTimeLeft()
	{
		int timeLeft = round.getTimelimitCountdown().getSecondsLeft();
		round.handleTimelimitRemaining(timeLeft);
	}

}