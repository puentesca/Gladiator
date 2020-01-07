package com.twostudentsllc.gladiator.runnables.game.round;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_game.handlers.MatchRound;

public class RoundRespawnTimer implements Runnable {

	private Main plugin;
	private MatchRound round;
	/**
	 * The player this respawn timer is for
	 */
	private Player player;
	
	public RoundRespawnTimer(Main plugin, MatchRound round, Player player)
	{
		this.plugin = plugin;
		this.round = round;
		this.player = player;
	}
	
	@Override
	public void run() {
		sendTimeLeft();
	}

	public void sendTimeLeft()
	{
		round.handleRespawnTimeRemaining(round.getPlayerRespawnCountdown(player).getSecondsLeft(), player);
	}
}
